package com.group2.project;

import com.group2.project.Weather.Weather;
import com.group2.project.Weather.WeatherData;
import javafx.application.Application;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan(basePackages = {"com.group2.project.ContactsRepository","com.group2.project.ContactsService"})
@SpringBootApplication
public class Main
{

    public static void main(String[] args) {
        //SpringApplication.run(Main.class, args);
        ProjectApplication.main(args);

        Weather weather = new Weather();
        WeatherData weatherData = weather.getWeatherData();
        if (weatherData != null) {
            System.out.println(weatherData);
        } else {
            System.out.println("Weather data could not be retrieved.");
        }
    }



}

