package com.group2.project.CalendarActivity;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

import java.util.*;

public class CalendarController implements Initializable
{

    // JZ: basic elements derived from: https://gist.github.com/Da9el00/f4340927b8ba6941eb7562a3306e93b6

    ZonedDateTime focusedDate, today;

    int selectedDay = 0;

    @FXML
    private Text year, month;

    @FXML
    private FlowPane calendar;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        focusedDate = ZonedDateTime.now();
        today = ZonedDateTime.now();
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
                    }
                    else if(today.getDayOfMonth() == currentDay){
                        currentRect.setStroke(Color.BLUE);
                        currentRect.setStrokeWidth(1 + 0.5);
                    }
                    else
                    {
                        currentRect.setStroke(Color.BLACK);
                        currentRect.setStrokeWidth(1);
                    }

                }
            }

        }
    }

    private void drawCalendar(){
        year.setText(String.valueOf(focusedDate.getYear()));
        month.setText(String.valueOf(focusedDate.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(focusedDate);

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
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
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

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if(calendarActivities != null){
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                        stackPane.setOnMouseClicked(mouseEvent -> {
                            // todo fix this
                            //selectedDate = focusedDate;
                            //System.out.println("CLICKED" + focusedDate.toString());
                            System.out.println("Clicked Date: " + currentDate);
                            selectedDay = currentDate;
                            respringCalendar();
//                            rectangle.setStroke(Color.RED);
//                            rectangle.setStrokeWidth(strokeWidth + 0.5);
                        });
                    }
//                    if(selectedDay == currentDate){
//                        rectangle.setStroke(Color.RED);
//                        rectangle.setStrokeWidth(strokeWidth + 0.5);
//                    }
                    if(today.getYear() == focusedDate.getYear() && today.getMonth() == focusedDate.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                        rectangle.setStrokeWidth(strokeWidth + 0.5);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                //todo : change this into listview
//                moreActivities.setOnMouseClicked(mouseEvent -> {
//                    //On ... click print all activities for given date
//                    System.out.println("events: " + calendarActivities.size());
//                    System.out.println(calendarActivities);
//                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
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

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();

        // insert randomization here. example usecase below.
//        Random random = new Random();
//        for (int i = 0; i < 50; i++) {
//            ZonedDateTime time = ZonedDateTime.of(year, month, random.nextInt(27)+1, 16,0,0,0,dateFocus.getZone());
//            calendarActivities.add(new CalendarActivity(time, "Hans", 111111));
//        }

        return createCalendarMap(calendarActivities);
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

    }

}
