package com.group2.project.calendaractivity;

import com.group2.project.calendarobjects.CalendarObject;
import com.group2.project.calendarobjects.CalendarType;
import com.group2.project.calendarobjects.Event;
import com.group2.project.calendarobjects.Meeting;
import com.group2.project.jsonparser.RawJSONParser;
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

    // JZ: basic elements derived from: https://gist.github.com/Da9el00/f4340927b8ba6941eb7562a3306e93b6

    private ZonedDateTime focusedDate, today, selected;

    private int selectedDay = 0;

    @FXML
    private Text yearText, monthText, selectedText, eventsText;

    @FXML
    private TextField titleField, descField, locText, timeText;

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
    private final Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> calendarEventMap = new HashMap<>();

    private double calendarWidth;
    private double calendarHeight;
    private double strokeWidth = 1;
    private double spacingH;
    private double spacingV;
    private double rectangleWidth;
    private double rectangleHeight;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        focusedDate = ZonedDateTime.now();
        today = ZonedDateTime.now();

        Thread weatherCaller = new Thread(() -> getWeather());
        weatherCaller.start();

        calendarWidth = calendar.getPrefWidth();
        calendarHeight = calendar.getPrefHeight();
        spacingH = calendar.getHgap();
        spacingV = calendar.getVgap();
        rectangleWidth = (calendarWidth/7) - strokeWidth - spacingH;
        rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;

        comboBox.setItems(FXCollections.observableArrayList("Calendar Object", "Event", "Meeting"));
        comboBox.getSelectionModel().selectFirst();

        comboBox.getSelectionModel().selectedItemProperty().addListener((opts, oldVal, newVal) -> {
            System.out.println(oldVal + " -> " + newVal);
            switch(newVal)
            {
                case "Calendar Object":
                    locText.setVisible(false);
                    timeText.setVisible(false);
                    break;
                case "Event":
                    locText.setVisible(false);
                    timeText.setVisible(true);
                    break;
                case "Meeting":
                    locText.setVisible(true);
                    timeText.setVisible(true);
                    break;
            }
        });

        locText.setVisible(false);
        timeText.setVisible(false);

        drawCalendar();

        // put threaded-jsonreader BELOW calendar because it is too fast...
        Thread jsonCaller = new Thread(() -> RawJSONParser.readJSON(this));
        jsonCaller.start();

        // from: https://www.youtube.com/watch?v=Pqfd4hoi5cc&ab_channel=BroCode
        eventsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                // will handle whenever user clicks on a different element in list view
                String current = eventsListView.getSelectionModel().getSelectedItem();
                //System.out.println(current);
                onUnFocusEditing();
            }
        });
    }

    private void getWeather()
    {
        WeatherData weatherData = Weather.getWeatherData();
        if (weatherData != null) {
            System.out.println(weatherData);
        } else {
            System.out.println("Weather data could not be retrieved.");
        }
    }

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

    private void respringCalendar()
    {
        // todo : put this in a redundant function
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
                    if(currentDay == selectedDay)
                    {
                        currentRect.setStroke(Color.RED);
                        currentRect.setStrokeWidth(1 + 0.5); // change this
                        selectedText.setText("Selected Day: " + selected.getMonthValue() + "/" + selected.getDayOfMonth() + "/" + selected.getYear());
                        // get events
                        eventsText.setText("Events For: " + selected.getMonthValue() + "/" + selected.getDayOfMonth() + "/" + selected.getYear());
                        respringListView();
                    }
                    else if(today.getDayOfMonth() == currentDay && today.getYear() == focusedDate.getYear() && today.getMonth() == focusedDate.getMonth()){
                        currentRect.setStroke(Color.BLUE);
                        currentRect.setStrokeWidth(strokeWidth + 0.5);
                    }
                    else
                    {
                        currentRect.setStroke(Color.BLACK);
                        currentRect.setStrokeWidth(strokeWidth);
                    }

                }
            }

        }
    }

    private void drawCalendar(){
        yearText.setText(String.valueOf(focusedDate.getYear()));
        monthText.setText(String.valueOf(focusedDate.getMonth()));

        //List of activities for a given month
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
                            if(calendarActivities != null && calendarActivities.size() > 0){
                                createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                            }
                        }

                        stackPane.setOnMouseClicked(mouseEvent -> {
                            System.out.println("Clicked Date: " + currentDate);
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
                    if(selected != null && selected.getDayOfMonth() == currentDate && selected.getYear() == focusedDate.getYear() && selected.getMonth() == focusedDate.getMonth())
                    {
                        rectangle.setStroke(Color.RED);
                        rectangle.setStrokeWidth(strokeWidth + 0.5);
                    }
                    if(today.getYear() == focusedDate.getYear() && today.getMonth() == focusedDate.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                        rectangle.setStrokeWidth(strokeWidth + 0.5);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }

        respringListView();
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("..."); //maybe do   + X more
                calendarActivityBox.getChildren().add(moreActivities);
                //todo : change this into listview
//                moreActivities.setOnMouseClicked(mouseEvent -> {
//                    //On ... click print all activities for given date
//                    System.out.println("events: " + calendarActivities.size());
//                    System.out.println(calendarActivities);
//                });
                break;
            }
            // todo : change time with this + make sure title cannot be too long
            //Text text = new Text(calendarActivities.get(k).getCalendarObject().getTitle() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            String currTitle = calendarActivities.get(k).getCalendarObject().getTitle();
            String usedTitle = currTitle;
            if(currTitle.length() > 12)
            {
                usedTitle = currTitle.substring(0, 9) + "...";
            }

            Text text = new Text(usedTitle);
            calendarActivityBox.getChildren().add(text);
//            text.setOnMouseClicked(mouseEvent -> {
//                //On Text clicked
//                System.out.println(text.getText());
//            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY"); // todo can change this to be unique
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
        return  calendarActivityMap;
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

    @FXML
    private void submitCalendarObject(ActionEvent event)
    {
        if(!isValidCalendarSubmission())
        {
            return;
        }

        if(selected == null)
        {
            System.out.println("Must select a date to mark for calendar object");
            return;
        }

        String title = titleField.getText();
        String desc = descField.getText();
        String timeString = timeText.getText();
        String location = locText.getText();
        CalendarType type = getTypeFromCombo();

        titleField.setText("");
        descField.setText("");
        locText.setText("");
        timeText.setText("");

        // change time depending on calendarobject type
        ZonedDateTime time = ZonedDateTime.of(selected.getYear(), selected.getMonthValue(), selected.getDayOfMonth(), 0,0,0,0, focusedDate.getZone());

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


        // todo check for editing

    }

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
                    timeText.setText(currentEvent.getTime());
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
                    timeText.setText(currentMeeting.getTime());
                    locText.setText(currentMeeting.getLocation());
                }
            }

        }



//        System.out.println(forSelected.get(index));
//        System.out.println(eventsListView.getSelectionModel().getSelectedItem());
    }

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

    }

    private void onUnFocusEditing()
    {
        if(editing)
        {
            editing = false;
            editingIndex = -1;
            titleField.setText("");
            descField.setText("");
            locText.setText("");
            timeText.setText("");
        }

    }

    private boolean isValidCalendarSubmission()
    {
        if(titleField.getText().isEmpty())
        {
            System.out.println("Not valid title text");
            return false;
        }
        else if(descField.getText().isEmpty())
        {
            System.out.println("Not valid description text");
            return false;
        }
        else if(comboBox.getValue() == null)
        {
            System.out.println("Not valid calendar object type");
            return false;
        }
        else if((comboBox.getValue().equals("Event") || comboBox.getValue().equals("Meeting")) && !isValidTimeSubmission())
        {
            System.out.println("Not valid time submission");
            return false;
        }
        else if(comboBox.getValue().equals("Meeting") && locText.getText().isEmpty())
        {
            System.out.println("Not valid location text");
            return false;
        }

        return true;
    }

    private boolean isValidTimeSubmission()
    {
        String timeStamp = timeText.getText();

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

    }

    private void addToMap(CalendarActivity toAdd)
    {
        // year
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
            if(calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()) == null)
            {
                calendarEventMap.get(selected.getYear()).put(selected.getMonthValue(), new HashMap<>());
                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).put(selected.getDayOfMonth(), new ArrayList<>());
                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).add(toAdd);
            }
            else
            {
                if(calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()) == null)
                {
                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).put(selected.getDayOfMonth(), new ArrayList<>());
                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).add(toAdd);
                }
                else
                {
                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth())
                            .add(toAdd);
                }
            }
        }

        // todo : can we respring ui instead of completely redraw?
        // todo : implement respring... causes not clean ui action.
        calendar.getChildren().clear();
        drawCalendar();
        //respringListView();
    }

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

    // update ui functions below:
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

