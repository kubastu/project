package com.group2.project;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@Component
@SpringBootApplication
public class Main
{

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        //ProjectApplication.main(args);
    }

}
