package com.group2.project.Weather;

public class WeatherData {
  private String time;
  private double temperature;
  private double maxTemperature;
  private double minTemperature;
  private String sunrise;
  private String sunset;

  public WeatherData(String time, double temperature, double maxTemperature, double minTemperature, String sunrise, String sunset) {
    this.time = time;
    this.temperature = temperature;
    this.maxTemperature = maxTemperature;
    this.minTemperature = minTemperature;
    this.sunrise = sunrise;
    this.sunset = sunset;
  }

  public String getTime() {
    return time;
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

  public String getSunrise() {
    return sunrise;
  }

  public String getSunset() {
    return sunset;
  }

  @Override
  public String toString() {
    return "Current Time: " + time + "\n" +
        "Current Temperature (C): " + temperature + "\n" +
        "Max Temperature (C): " + maxTemperature + "\n" +
        "Min Temperature (C): " + minTemperature + "\n" +
        "Sunrise: " + sunrise + "\n" +
        "Sunset: " + sunset;
  }
}
