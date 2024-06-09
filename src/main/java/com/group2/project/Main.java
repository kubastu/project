package com.group2.project;

import com.group2.project.weather.Weather;
import com.group2.project.weather.WeatherData;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.group2.project.ContactsRepository","com.group2.project.ContactsService"})
public class Main
{

    public static void main(String[] args) {
        //SpringApplication.run(Main.class, args);
        ProjectApplication.main(args);

    }


}

