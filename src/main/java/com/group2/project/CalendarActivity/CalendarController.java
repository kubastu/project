//package com.group2.project.CalendarActivity;
//
//import com.group2.project.calendarobjects.CalendarObject;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//
//import javafx.event.ActionEvent;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.FlowPane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.text.Text;
//
//import java.net.URL;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.ResourceBundle;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Rectangle;
//
//import java.util.*;
//
//public class CalendarController implements Initializable
//{
//
//    // JZ: basic elements derived from: https://gist.github.com/Da9el00/f4340927b8ba6941eb7562a3306e93b6
//
//    private ZonedDateTime focusedDate, today, selected;
//
//    private int selectedDay = 0;
//
//    @FXML
//    private Text yearText, monthText, selectedText, eventsText;
//
//    @FXML
//    private TextField titleField, descField;
//
//    @FXML
//    private FlowPane calendar;
//
//    @FXML
//    private ListView<String> eventsListView;
//
//    // holds mappings for each month to
//    //private Map<Integer, List<CalendarActivity>> calendarEventMap = new HashMap<>();
//
//    // map <year, <month, <day, activityOBJ>>>
//    private final Map<Integer, Map<Integer, Map<Integer, List<CalendarActivity>>>> calendarEventMap = new HashMap<>();
//
//    private double calendarWidth;
//    private double calendarHeight;
//    private double strokeWidth = 1;
//    private double spacingH;
//    private double spacingV;
//    private double rectangleWidth;
//    private double rectangleHeight;
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources)
//    {
//        focusedDate = ZonedDateTime.now();
//        today = ZonedDateTime.now();
//
//        calendarWidth = calendar.getPrefWidth();
//        calendarHeight = calendar.getPrefHeight();
//        spacingH = calendar.getHgap();
//        spacingV = calendar.getVgap();
//        rectangleWidth = (calendarWidth/7) - strokeWidth - spacingH;
//        rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
//
//        drawCalendar();
//
//        // from: https://www.youtube.com/watch?v=Pqfd4hoi5cc&ab_channel=BroCode
//        eventsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//                // will handle whenever user clicks on a different element in list view
//                String current = eventsListView.getSelectionModel().getSelectedItem();
//                System.out.println(current);
//            }
//        });
//    }
//
//    private void respringCalendar()
//    {
//        // todo : put this in a redundant function
//        int dateOffset = ZonedDateTime.of(focusedDate.getYear(), focusedDate.getMonthValue(), 1,0,0,0,0,focusedDate.getZone()).getDayOfWeek().getValue();
//
//        int monthMaxDate = focusedDate.getMonth().maxLength();
//        //Check for leap year
//        if(focusedDate.getYear() % 4 != 0 && monthMaxDate == 29){
//            monthMaxDate = 28;
//        }
//
//        for(int i = 0; i < calendar.getChildren().size(); i++)
//        {
//            StackPane current = (StackPane) calendar.getChildren().get(i);
//            //System.out.println(i);
//            Rectangle currentRect = (Rectangle) current.getChildren().get(0);
//
//            if(i + 1 > dateOffset)
//            {
//                int currentDay = i - dateOffset + 1;
//                //System.out.println("OFFSET: " + dateOffset + "\nCURRENTDAY : " + currentDay + "\nToday: " + today.getDayOfMonth());
//                if(currentDay <= monthMaxDate)
//                {
//                    if(currentDay == selectedDay)
//                    {
//                        currentRect.setStroke(Color.RED);
//                        currentRect.setStrokeWidth(1 + 0.5); // change this
//                        selectedText.setText("Selected Day: " + selected.getMonthValue() + "/" + selected.getDayOfMonth() + "/" + selected.getYear());
//                        // get events
//                        eventsText.setText("Events For: " + selected.getMonthValue() + "/" + selected.getDayOfMonth() + "/" + selected.getYear());
//                        respringListView();
//                    }
//                    else if(today.getDayOfMonth() == currentDay && today.getYear() == focusedDate.getYear() && today.getMonth() == focusedDate.getMonth()){
//                        currentRect.setStroke(Color.BLUE);
//                        currentRect.setStrokeWidth(strokeWidth + 0.5);
//                    }
//                    else
//                    {
//                        currentRect.setStroke(Color.BLACK);
//                        currentRect.setStrokeWidth(strokeWidth);
//                    }
//
//                }
//            }
//
//        }
//    }
//
//    private void drawCalendar(){
//        yearText.setText(String.valueOf(focusedDate.getYear()));
//        monthText.setText(String.valueOf(focusedDate.getMonth()));
//
//        //List of activities for a given month
//        Map<Integer, List<CalendarActivity>> calendarActivityMap = calendarEventMap.get(focusedDate.getYear()) == null ? null : calendarEventMap.get(focusedDate.getYear()).get(focusedDate.getMonthValue());
//
//        int monthMaxDate = focusedDate.getMonth().maxLength();
//        //Check for leap year
//        if(focusedDate.getYear() % 4 != 0 && monthMaxDate == 29){
//            monthMaxDate = 28;
//        }
//        int dateOffset = ZonedDateTime.of(focusedDate.getYear(), focusedDate.getMonthValue(), 1,0,0,0,0,focusedDate.getZone()).getDayOfWeek().getValue();
//
//        for (int i = 0; i < 6; i++) {
//            for (int j = 0; j < 7; j++) {
//                StackPane stackPane = new StackPane();
//
//                Rectangle rectangle = new Rectangle();
//                rectangle.setFill(Color.TRANSPARENT);
//                rectangle.setStroke(Color.BLACK);
//                rectangle.setStrokeWidth(strokeWidth);
//                rectangle.setWidth(rectangleWidth);
//                rectangle.setHeight(rectangleHeight);
//                stackPane.getChildren().add(rectangle);
//
//                int calculatedDate = (j+1)+(7*i);
//                if(calculatedDate > dateOffset){
//                    int currentDate = calculatedDate - dateOffset;
//                    if(currentDate <= monthMaxDate){
//                        Text date = new Text(String.valueOf(currentDate));
//                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
//                        date.setTranslateY(textTranslationY);
//                        stackPane.getChildren().add(date);
//
//                        if(calendarActivityMap != null)
//                        {
//                            List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
//                            if(calendarActivities != null){
//                                createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
//                            }
//                        }
//
//                        stackPane.setOnMouseClicked(mouseEvent -> {
//                            // todo fix this
//                            //selectedDate = focusedDate;
//                            //System.out.println("CLICKED" + focusedDate.toString());
//                            System.out.println("Clicked Date: " + currentDate);
//                            selectedDay = currentDate;
//                            selected = ZonedDateTime.of(focusedDate.getYear(), focusedDate.getMonthValue(), selectedDay, 0, 0, 0, 0, focusedDate.getZone());
//                            respringCalendar();
//                            // do listview
//                            respringListView();
////                            rectangle.setStroke(Color.RED);
////                            rectangle.setStrokeWidth(strokeWidth + 0.5);
//                        });
//                    }
//                    if(selectedDay == currentDate){
//                        rectangle.setStroke(Color.RED);
//                        rectangle.setStrokeWidth(strokeWidth + 0.5);
//                    }
//                    if(today.getYear() == focusedDate.getYear() && today.getMonth() == focusedDate.getMonth() && today.getDayOfMonth() == currentDate){
//                        rectangle.setStroke(Color.BLUE);
//                        rectangle.setStrokeWidth(strokeWidth + 0.5);
//                    }
//                }
//                calendar.getChildren().add(stackPane);
//            }
//        }
//
//        respringListView();
//    }
//
//    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
//        VBox calendarActivityBox = new VBox();
//        for (int k = 0; k < calendarActivities.size(); k++) {
//            if(k >= 2) {
//                Text moreActivities = new Text("..."); //maybe do   + X more
//                calendarActivityBox.getChildren().add(moreActivities);
//                //todo : change this into listview
////                moreActivities.setOnMouseClicked(mouseEvent -> {
////                    //On ... click print all activities for given date
////                    System.out.println("events: " + calendarActivities.size());
////                    System.out.println(calendarActivities);
////                });
//                break;
//            }
//            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
//            calendarActivityBox.getChildren().add(text);
////            text.setOnMouseClicked(mouseEvent -> {
////                //On Text clicked
////                System.out.println(text.getText());
////            });
//        }
//        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
//        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
//        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
//        calendarActivityBox.setStyle("-fx-background-color:GRAY"); // todo can change this to be unique
//        stackPane.getChildren().add(calendarActivityBox);
//    }
//
//    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
//        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();
//
//        for (CalendarActivity activity: calendarActivities) {
//            int activityDate = activity.getDate().getDayOfMonth();
//            if(!calendarActivityMap.containsKey(activityDate)){
//                calendarActivityMap.put(activityDate, List.of(activity));
//            } else {
//                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);
//
//                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
//                newList.add(activity);
//                calendarActivityMap.put(activityDate, newList);
//            }
//        }
//        return  calendarActivityMap;
//    }
//
//    // JZ: FXML onclick functions below:
//
//    @FXML
//    private void backOneMonth(ActionEvent event)
//    {
//        focusedDate = focusedDate.minusMonths(1);
//        calendar.getChildren().clear();
//        drawCalendar();
//    }
//
//    @FXML
//    private void forwardOneMonth(ActionEvent event)
//    {
//        focusedDate = focusedDate.plusMonths(1);
//        calendar.getChildren().clear();
//        drawCalendar();
//    }
//
//    @FXML
//    private void submitCalendarObject(ActionEvent event)
//    {
//        String title = titleField.getText();
//        String desc = descField.getText();
//        titleField.setText("");
//        descField.setText("");
//
//        // change time depending on calendarobject type
//        ZonedDateTime time = ZonedDateTime.of(selected.getYear(), selected.getMonthValue(), selected.getDayOfMonth(), 0,0,0,0, focusedDate.getZone());
//
////        calendarActivities.add(new CalendarActivity(time, title, 111111));
//        addToMap(new CalendarActivity(time, title, 111111));
//    }
//
//    private void addToMap(CalendarActivity toAdd)
//    {
//        // year
//        if(calendarEventMap.get(selected.getYear()) == null)
//        {
//            calendarEventMap.put(selected.getYear(),
//                    new HashMap<>());
//            calendarEventMap.get(selected.getYear()).put(selected.getMonthValue(), new HashMap<>());
//            calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).put(selected.getDayOfMonth(), new ArrayList<>());
//            calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).add(toAdd);
//        }
//        else
//        {
//            if(calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()) == null)
//            {
//                calendarEventMap.get(selected.getYear()).put(selected.getMonthValue(), new HashMap<>());
//                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).put(selected.getDayOfMonth(), new ArrayList<>());
//                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).add(toAdd);
//            }
//            else
//            {
//                if(calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()) == null)
//                {
//                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).put(selected.getDayOfMonth(), new ArrayList<>());
//                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()).add(toAdd);
//                }
//                else
//                {
//                    calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth())
//                            .add(toAdd);
//                }
//            }
//        }
//
//        // todo : can we respring ui instead of completely redraw?
//        // todo : implement respring... causes not clean ui action.
//        calendar.getChildren().clear();
//        drawCalendar();
//        //respringListView();
//    }
//
//    private List<CalendarActivity> getEventsForDate()
//    {
//        if(selected == null)
//        {
//            return null;
//        }
//
//        if(     calendarEventMap.get(selected.getYear()) != null &&
//                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()) != null &&
//                calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth()) != null
//        )
//        {
//            return calendarEventMap.get(selected.getYear()).get(selected.getMonthValue()).get(selected.getDayOfMonth());
//        }
//        return null;
//    }
//
//    // update ui functions below:
//    private void respringListView()
//    {
//        List<CalendarActivity> forSelected = getEventsForDate();
//        eventsListView.getItems().clear();
//        if(forSelected != null && !forSelected.isEmpty())
//        {
//            for(CalendarActivity activity : forSelected)
//            {
//                eventsListView.getItems().add(activity.toString());
//            }
//        }
//    }
//
//
//}
