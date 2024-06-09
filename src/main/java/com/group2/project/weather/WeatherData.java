package com.group2.project.weather;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherData {
  private String time;
  private double temperature;
  private double maxTemperature;
  private double minTemperature;
  private String sunrise;
  private String sunset;

  private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_DATE_TIME;
  private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a");

  public WeatherData(String time, double temperature, double maxTemperature, double minTemperature, String sunrise, String sunset) {
    this.time = time;
    this.temperature = temperature;
    this.maxTemperature = maxTemperature;
    this.minTemperature = minTemperature;
    this.sunrise = sunrise;
    this.sunset = sunset;
  }

  private String formatTime(String dateTime) {
    LocalDateTime localDateTime = LocalDateTime.parse(dateTime, inputFormatter);
    return localDateTime.format(outputFormatter);
  }

  public String getTime() {
    return time;
  }

  public String getFormattedTime()
  {
    return formatTime(getTime());
  }

  public double getTemperature() {
    return temperature;
  }

  public double getMaxTemperature() {
    return maxTemperature;
  }

  public double getMinTemperature() {
    return minTemperature;
  }

  public double convertTempToFahrenheit(double celsius)
  {
      double fahrenheit = (celsius * 9 / 5) + 32;
      return(Math.round(fahrenheit * 100.0) / 100.0);
  }

  public String getSunrise() {
    return formatTime(sunrise);
  }

  public String getSunset() {
    return formatTime(sunset);
  }

  @Override
  public String toString() {
    return "Current Time: " + time + "\n" +
            "Current Parsed Time: " + formatTime(time) + "\n" +
        "Current Temperature (C): " + temperature + "\n" +
        "Max Temperature (C): " + maxTemperature + "\n" +
        "Min Temperature (C): " + minTemperature + "\n" +
        "Sunrise: " + getSunrise() + "\n" +
        "Sunset: " + getSunset();
  }
}
