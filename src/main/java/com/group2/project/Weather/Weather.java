package com.group2.project.Weather;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class Weather {
  private static final double CHICAGO_LATITUDE = 41.85;
  private static final double CHICAGO_LONGITUDE = -87.65;

  public WeatherData getWeatherData() {
    try {
      return fetchWeatherData(CHICAGO_LATITUDE, CHICAGO_LONGITUDE);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private WeatherData fetchWeatherData(double latitude, double longitude) {
    try {
      String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
          "&longitude=" + longitude +
          "&current=temperature_2m&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset&timezone=America%2FChicago&forecast_days=1";
      HttpURLConnection apiConnection = fetchApiResponse(url);

      if (apiConnection.getResponseCode() != 200) {
        System.out.println("Error: Could not connect to API");
        return null;
      }

      String jsonResponse = readApiResponse(apiConnection);
      JSONParser parser = new JSONParser();
      JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
      JSONObject currentWeatherJson = (JSONObject) jsonObject.get("current");
      JSONObject dailyWeatherJson = (JSONObject) jsonObject.get("daily");

      String time = (String) currentWeatherJson.get("time");
      double temperature = (double) currentWeatherJson.get("temperature_2m");
      double maxTemperature = (double) ((JSONArray) dailyWeatherJson.get("temperature_2m_max")).get(0);
      double minTemperature = (double) ((JSONArray) dailyWeatherJson.get("temperature_2m_min")).get(0);
      String sunrise = (String) ((JSONArray) dailyWeatherJson.get("sunrise")).get(0);
      String sunset = (String) ((JSONArray) dailyWeatherJson.get("sunset")).get(0);

      return new WeatherData(time, temperature, maxTemperature, minTemperature, sunrise, sunset);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String readApiResponse(HttpURLConnection apiConnection) {
    try {
      StringBuilder resultJson = new StringBuilder();
      Scanner scanner = new Scanner(apiConnection.getInputStream());
      while (scanner.hasNext()) {
        resultJson.append(scanner.nextLine());
      }
      scanner.close();
      return resultJson.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static HttpURLConnection fetchApiResponse(String urlString) {
    try {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      return conn;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}

class WeatherData {
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



