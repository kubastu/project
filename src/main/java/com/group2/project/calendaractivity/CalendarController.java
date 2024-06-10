package com.group2.project.calendaractivity;

import com.group2.project.calendarobjects.CalendarObject;
import com.group2.project.calendarobjects.CalendarType;
import com.group2.project.calendarobjects.Event;
import com.group2.project.calendarobjects.Meeting;
import com.group2.project.jsonmanipulator.RawJSONParser;
import com.group2.project.jsonmanipulator.RawJSONWriter;
import com.group2.project.weather.Weather;
import com.group2.project.weather.WeatherData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class CalendarController implements Initializable
{

    // This class defines a 'main' running class that handles CRUD operations of Calendar Object representations, as well as front-end execution.
    // -- -- This class handles ALL front-end interactions as well.

    // JZ: basic elements derived from: https://gist.github.com/Da9el00/f4340927b8ba6941eb7562a3306e93b6

    private ZonedDateTime focusedDate, today, selected;

    private int selectedDay = 0;

    // Core TextView's
    @FXML
    private Text yearText, monthText, selectedText, eventsText;

    // Supplemental TextView's
    @FXML
    private Text todayText, locationText, tempText, timeCheckedText, minTempText, maxTempText, sunriseText, sunsetText;

    @FXML
    private VBox todayInfo;

    @FXML
    private TextField titleField, descField, locField, timeField;

    @FXML
    private FlowPane calendar;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private ListView<String> eventsListView;

    private boolean editing = false;
    private int editingIndex = -1;

    // holds mappings for each month to
    //private Map<Integer, List<CalendarActivity>> calendarEventMap = new HashMap<>();

    // map <year, <month, <day, activityOBJ>>>
    // holds a mapping for persistent data structure
    // motivation: hold year(s) --> month(s) --> day(s) --> events for that specific date (easy storing in JSON).
    private final Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> calendarEventMap = new HashMap<>();

    private double calendarWidth;
    private double calendarHeight;
    private double strokeWidth = 1;
    private double spacingH;
    private double spacingV;
    private double rectangleWidth;
    private double rectangleHeight;

    // Function that initializes most of the core functionalities (i.e. reading from persistence layer, starting up calendar, as well as graphical elements)
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        focusedDate = ZonedDateTime.now();
        today = ZonedDateTime.now();

        // Gets the weather in a separate thread. Since calling an external API is asynchronous and takes time... this is efficient!
        Thread weatherCaller = new Thread(() -> getWeather());
        weatherCaller.start();

        calendarWidth = calendar.getPrefWidth();
        calendarHeight = calendar.getPrefHeight();
        spacingH = calendar.getHgap();
        spacingV = calendar.getVgap();
        rectangleWidth = (calendarWidth/7) - strokeWidth - spacingH;
        rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;

        // sets the ComboBox for different types of our CalendarObject representations:
        comboBox.setItems(FXCollections.observableArrayList("Calendar Object", "Event", "Meeting"));
        comboBox.getSelectionModel().selectFirst();

        comboBox.getSelectionModel().selectedItemProperty().addListener((opts, oldVal, newVal) -> {
            System.out.println(oldVal + " -> " + newVal);
            switch(newVal)
            {
                case "Calendar Object":
                    locField.setVisible(false);
                    timeField.setVisible(false);
                    break;
                case "Event":
                    locField.setVisible(false);
                    timeField.setVisible(true);
                    break;
                case "Meeting":
                    locField.setVisible(true);
                    timeField.setVisible(true);
                    break;
            }
        });

        locField.setVisible(false);
        timeField.setVisible(false);

        drawCalendar();

        // Calls the JSON reader in a separate thread as to make more efficient the graphical start-up.
        // -- -- Put threaded-jsonreader BELOW drawCalendar because it is too fast...
        Thread jsonCaller = new Thread(() -> RawJSONParser.readJSON(this));
        jsonCaller.start();

        // from: https://www.youtube.com/watch?v=Pqfd4hoi5cc&ab_channel=BroCode
        eventsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                // will handle whenever user clicks on a different element in list view
                String current = eventsListView.getSelectionModel().getSelectedItem();
                //System.out.println(current);

                // method disables editing toggle when user clicks away from current date which the user is editing
                onUnFocusEditing();
            }
        });
    }

    // Function that is called from a threaded-instance: gets the weather data for today and displays it.
    private void getWeather()
    {
        WeatherData weatherData = Weather.getWeatherData();
        if (weatherData != null) {
            System.out.println("##########################");
            System.out.println(weatherData);
            System.out.println("##########################");

            todayText.setText("Today's Date: " + today.getMonthValue() + "/" + today.getDayOfMonth() + "/" + today.getYear());
            locationText.setText("Location: Chicago");
            timeCheckedText.setText("Time Checked: " + weatherData.getFormattedTime());
            sunriseText.setText("Sunrise: " + weatherData.getSunrise());
            sunsetText.setText("Sunset: " + weatherData.getSunset());
            // special cases: convert to fahrenheit:
            tempText.setText("Temperature: " + weatherData.convertTempToFahrenheit(weatherData.getTemperature()) + "°F");
            minTempText.setText("Min Temp: " + weatherData.convertTempToFahrenheit(weatherData.getMinTemperature()) + "°F");
            maxTempText.setText("Max Temp: " + weatherData.convertTempToFahrenheit(weatherData.getMaxTemperature()) + "°F");

        } else {
            System.out.println("Weather data could not be retrieved.");
            locationText.setText("Unable to retrieve Weather.");
        }
    }

    // Function that is called when reading from the JSON has finished. If the list has something then it is inserted into our Map, and redraws the calendar.
    public void acceptMap(Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> resultMap)
    {
        if(resultMap == null || resultMap.isEmpty())
        {
            System.out.println("Accepted Results are null");
            return;
        }

        //System.out.println("Accepted: " + resultMap.size() + " years");

        calendarEventMap.clear();
        calendarEventMap.putAll(resultMap);

        calendar.getChildren().clear();
        drawCalendar();

    }

    // Function that softly 'redraws' the elements on the calendar. (Mainly to run when a user has selected a different day square).
    private void respringCalendar()
    {
        int dateOffset = ZonedDateTime.of(focusedDate.getYear(), focusedDate.getMonthValue(), 1,0,0,0,0,focusedDate.getZone()).getDayOfWeek().getValue();

        int monthMaxDate = focusedDate.getMonth().maxLength();
        //Check for leap year
        if(focusedDate.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }

        for(int i = 0; i < calendar.getChildren().size(); i++)
        {
            StackPane current = (StackPane) calendar.getChildren().get(i);
            //System.out.println(i);
            Rectangle currentRect = (Rectangle) current.getChildren().get(0);

            if(i + 1 > dateOffset)
            {
                int currentDay = i - dateOffset + 1;
                //System.out.println("OFFSET: " + dateOffset + "\nCURRENTDAY : " + currentDay + "\nToday: " + today.getDayOfMonth());
                if(currentDay <= monthMaxDate)
                {
                    // If the square is the one the user has selected:
                    if(currentDay == selectedDay)
                    {
                        currentRect.setStroke(Color.RED);
                        currentRect.setStrokeWidth(1 + 0.5); // todo : can change this for differences in focused date square.
                        selectedText.setText("Selected Day: " + selected.getMonthValue() + "/" + selected.getDayOfMonth() + "/" + selected.getYear());
                        // get events
                        eventsText.setText("Events For: " + selected.getMonthValue() + "/" + selected.getDayOfMonth() + "/" + selected.getYear());
                        respringListView();
                    }
                    // If the square is the one that represents today:
                    else if(today.getDayOfMonth() == currentDay && today.getYear() == focusedDate.getYear() && today.getMonth() == focusedDate.getMonth()){
                        currentRect.setStroke(Color.BLUE);
                        currentRect.setStrokeWidth(strokeWidth + 0.5);
                    }
                    // any other normal square:
                    else
                    {
                        currentRect.setStroke(Color.BLACK);
                        currentRect.setStrokeWidth(strokeWidth);
                    }

                }
            }

        }
    }

    // Draws the calendar as well as all the components that supplement it.
    private void drawCalendar(){
        yearText.setText(String.valueOf(focusedDate.getYear()));
        monthText.setText(String.valueOf(focusedDate.getMonth()));

        // Get List of activities for a given month using the ternary operator to make sure it is not null.
        Map<Integer, List<CalendarActivity>> calendarActivityMap = calendarEventMap.get(focusedDate.getYear()) == null ? null : calendarEventMap.get(focusedDate.getYear()).get(focusedDate.getMonthValue());

        int monthMaxDate = focusedDate.getMonth().maxLength();
        //Check for leap year
        if(focusedDate.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(focusedDate.getYear(), focusedDate.getMonthValue(), 1,0,0,0,0,focusedDate.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                rectangle.setWidth(rectangleWidth);
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        if(calendarActivityMap != null)
                        {
                            List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                            // If there is an event on the current square:
                            if(calendarActivities != null && !calendarActivities.isEmpty()){
                                createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                            }
                        }

                        // On click function for selecting a 'square' day of the calendar.
                        stackPane.setOnMouseClicked(mouseEvent -> {
                            //System.out.println("Clicked Date: " + currentDate);
                            selectedDay = currentDate;
                            selected = ZonedDateTime.of(focusedDate.getYear(), focusedDate.getMonthValue(), selectedDay, 0, 0, 0, 0, focusedDate.getZone());
                            respringCalendar();
                            // do listview
                            respringListView();
                            onUnFocusEditing();
//                            rectangle.setStroke(Color.RED);
//                            rectangle.setStrokeWidth(strokeWidth + 0.5);
                        });
                    }
                    // For selected date:
                    if(selected != null && selected.getDayOfMonth() == currentDate && selected.getYear() == focusedDate.getYear() && selected.getMonth() == focusedDate.getMonth())
                    {
                        rectangle.setStroke(Color.RED);
                        rectangle.setStrokeWidth(strokeWidth + 0.5);
                    }
                    // For today's date:
                    if(today.getYear() == focusedDate.getYear() && today.getMonth() == focusedDate.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                        rectangle.setStrokeWidth(strokeWidth + 0.5);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }

        // updates list view to show current objects for the specific date:
        respringListView();
    }

    // creates a highlighted entry in the calendar visually to depict an event:
    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("..."); //maybe do   + X more
                calendarActivityBox.getChildren().add(moreActivities);
                break;
            }

            String currTitle = calendarActivities.get(k).getCalendarObject().getTitle();
            String usedTitle = "- " + currTitle;
            if(currTitle.length() > 12)
            {
                usedTitle = "- " + currTitle.substring(0, 8) + "..";
            }

            Text text = new Text(usedTitle);
            calendarActivityBox.getChildren().add(text);

            // can we use this for anything?
//            text.setOnMouseClicked(mouseEvent -> {
//                //On Text clicked
//                System.out.println(text.getText());
//            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY"); // todo: can we change this to be unique?
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }

    // JZ: FXML onclick functions below:

    @FXML
    private void backOneMonth(ActionEvent event)
    {
        focusedDate = focusedDate.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    private void forwardOneMonth(ActionEvent event)
    {
        focusedDate = focusedDate.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    // Function that checks if inputs are correct, for creating / editing a CalendarObject
    @FXML
    private void submitCalendarObject(ActionEvent event)
    {
        if(!isValidCalendarSubmission())
        {
            return;
        }

        if(selected == null)
        {
            System.out.println("Must select a date to mark for calendar object.");
            return;
        }

        // Gets info from ALL fields, even if they are invisible:
        String title = titleField.getText();
        String desc = descField.getText();
        String timeString = timeField.getText();
        String location = locField.getText();
        CalendarType type = getTypeFromCombo();

        // reset the fields:
        titleField.setText("");
        descField.setText("");
        locField.setText("");
        timeField.setText("");

        // change time depending on calendarobject type
        ZonedDateTime time = ZonedDateTime.of(selected.getYear(), selected.getMonthValue(), selected.getDayOfMonth(), 0,0,0,0, focusedDate.getZone());

        // if we are adding a new Calendar Object:
        if(!editing)
        {
            switch (type)
            {
                case CALENDAR_OBJECT -> addToMap(new CalendarActivity(CalendarType.CALENDAR_OBJECT, time, new CalendarObject(time, title, desc)));
                case EVENT -> addToMap(new CalendarActivity(CalendarType.EVENT, time, new Event(time, title, desc, timeString)));
                case MEETING -> addToMap(new CalendarActivity(CalendarType.MEETING, time, new Meeting(time, title, desc, timeString, location)));

            }
        }
        else //if editing then it already exists in the map...
        {
            switch (type)
            {
                case CALENDAR_OBJECT -> editInMap(new CalendarActivity(CalendarType.CALENDAR_OBJECT, time, new CalendarObject(time, title, desc)));
                case EVENT -> editInMap(new CalendarActivity(CalendarType.EVENT, time, new Event(time, title, desc, timeString)));
                case MEETING -> editInMap(new CalendarActivity(CalendarType.MEETING, time, new Meeting(time, title, desc, timeString, location)));

            }
        }

    }

    // Function that gets selected item from list view and fills in appropriate fields for sake of editing:
    @FXML
    private void editSelectedObject(ActionEvent event)
    {
        if(selected == null || eventsListView.getSelectionModel().getSelectedIndex() == -1)
        {
            return;
        }

        List<CalendarActivity> forSelected = getEventsForDate();
        int index = eventsListView.getSelectionModel().getSelectedIndex();
        editingIndex = index;
        CalendarActivity selected = forSelected.get(index);
        editing = true;

        // further usage of instanceof to be able to downcast, since our high-level CalendarActivity stores only a CalendarObject. To downcast we much check runtime instance type!
        switch(selected.getType())
        {
            case CALENDAR_OBJECT -> {
                comboBox.getSelectionModel().select("Calendar Object");
                titleField.setText(selected.getCalendarObject().getTitle());
                descField.setText(selected.getCalendarObject().getDescription());
            }
            case EVENT -> {
                if(selected.getCalendarObject() instanceof Event)
                {
                    Event currentEvent;
                    currentEvent = (Event) selected.getCalendarObject();
                    comboBox.getSelectionModel().select("Event");
                    titleField.setText(currentEvent.getTitle());
                    descField.setText(currentEvent.getDescription());
                    timeField.setText(currentEvent.getTime());
                }
            }
            case MEETING -> {
                if(selected.getCalendarObject() instanceof Meeting)
                {
                    Meeting currentMeeting;
                    currentMeeting = (Meeting) selected.getCalendarObject();
                    comboBox.getSelectionModel().select("Meeting");
                    titleField.setText(currentMeeting.getTitle());
                    descField.setText(currentMeeting.getDescription());
                    timeField.setText(currentMeeting.getTime());
                    locField.setText(currentMeeting.getLocation());
                }
            }

        }

    }

    // function that defines functionality for deleting an object; removes it from the persistent representation map, then calls the write function, while updating the UI.
    @FXML
    private void deleteSelectedObject(ActionEvent event)
    {
        if(selected == null || eventsListView.getSelectionModel().getSelectedIndex() == -1)
        {
            return;
        }

        List<CalendarActivity> forSelected = getEventsForDate();
        int index = eventsListView.getSelectionModel().getSelectedIndex();

        calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).remove(index);

        //System.out.println(calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).remove(index));
        //System.out.println(calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()));

        calendar.getChildren().clear();
        drawCalendar();

        writeToJSON();

    }

    // when user clicks away while editing, resets all the values to visually indicate change in behavior.
    private void onUnFocusEditing()
    {
        if(editing)
        {
            editing = false;
            editingIndex = -1;
            titleField.setText("");
            descField.setText("");
            locField.setText("");
            timeField.setText("");
        }

    }

    // checks to see if user's input is valid for sake of creating/editing a CalendarObject:
    private boolean isValidCalendarSubmission()
    {
        if(titleField.getText().isEmpty())
        {
            System.out.println("Not valid title text.");
            return false;
        }
        else if(descField.getText().isEmpty())
        {
            System.out.println("Not valid description text.");
            return false;
        }
        else if(comboBox.getValue() == null)
        {
            System.out.println("Not valid calendar object type.");
            return false;
        }
        else if((comboBox.getValue().equals("Event") || comboBox.getValue().equals("Meeting")) && !isValidTimeSubmission())
        {
            System.out.println("Not valid time submission.");
            return false;
        }
        else if(comboBox.getValue().equals("Meeting") && locField.getText().isEmpty())
        {
            System.out.println("Not valid location text.");
            return false;
        }

        return true;
    }

    // Separate function for checking if time formatting is appropriate:
    private boolean isValidTimeSubmission()
    {
        String timeStamp = timeField.getText();

        if(timeStamp == null || timeStamp.length() != 5)
        {
            return false;
        }
        else if(!timeStamp.substring(2, 3).equals(":"))
        {
            return false;
        }
        String hours = timeStamp.substring(0, 2);
        String mins = timeStamp.substring(3, 5);
        try {
            int parsedH = Integer.parseInt(hours);
            int parsedM = Integer.parseInt(mins);

            if(parsedH < 0 || parsedH > 23)
            {
                return false;
            }
            else if(parsedM < 0 || parsedM > 59)
            {
                return false;
            }

        } catch(NumberFormatException e) {
            return false;
        }

        return true;
    }

    // function that gets the type from the ComboBox:
    private CalendarType getTypeFromCombo()
    {
        switch(comboBox.getValue())
        {
            case "Calendar Object": return CalendarType.CALENDAR_OBJECT;
            case "Event": return CalendarType.EVENT;
            case "Meeting": return CalendarType.MEETING;
            case null, default: return CalendarType.CALENDAR_OBJECT;
        }
    }

    // Function that 'overwrites' (edits) a specific object in our persistence map, then calls the json writer.
    private void editInMap(CalendarActivity toEdit)
    {
        if(editingIndex == -1)
        {
            System.out.println("Editing index -1");
            return;
        }

        calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).set(editingIndex, toEdit);

        calendar.getChildren().clear();
        drawCalendar();

        writeToJSON();

    }

    // function that adds a new high-level CalendarActivity to our map. However, MUST check if all appropriate keys exist before insertion.
    private void addToMap(CalendarActivity toAdd)
    {
        // Check year key:
        if(calendarEventMap.get(selected.getYear()) == null)
        {
            calendarEventMap.put(selected.getYear(),
                    new HashMap<>());
            calendarEventMap.get(selected.getYear()).put(selected.getMonthValue(), new HashMap<>());
            calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).put(selected.getDayOfMonth(), new ArrayList<>());
            calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).add(toAdd);
        }
        else
        {
            // Check month key:
            if(calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()) == null)
            {
                calendarEventMap.get(selected.getYear()).put(selected.getMonthValue(), new HashMap<>());
                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).put(selected.getDayOfMonth(), new ArrayList<>());
                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).add(toAdd);
            }
            else
            {
                // check day key:
                if(calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()) == null)
                {
                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).put(selected.getDayOfMonth(), new ArrayList<>());
                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).add(toAdd);
                }
                else
                {
                    // ready to add:
                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth())
                            .add(toAdd);
                }
            }
        }

        // Note: is it possible to respring calendar? As opposed to re-drawing it after any change?
        calendar.getChildren().clear();
        drawCalendar();

        writeToJSON();

    }

    // function that returns a list of CalendarActivity objects for whatever date the user has selected:
    private List<CalendarActivity> getEventsForDate()
    {
        if(selected == null)
        {
            return null;
        }

        if(     calendarEventMap.get(selected.getYear()) != null &&
                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()) != null &&
                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()) != null
        )
        {
            return calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth());
        }
        return null;
    }

    // function that calls a threaded instance of our json writer. Since this action takes time, efficient to do it in a separate thread!
    private void writeToJSON()
    {
        Thread writeThread = new Thread(() -> RawJSONWriter.writeJSON(calendarEventMap));
        writeThread.start();
    }

    // Function updates the listview for specific day:
    private void respringListView()
    {
        List<CalendarActivity> forSelected = getEventsForDate();
        eventsListView.getItems().clear();
        if(forSelected != null && !forSelected.isEmpty())
        {
            for(CalendarActivity activity : forSelected)
            {
                eventsListView.getItems().add(activity.toString());
            }
        }
    }


}

