package com.group2.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
		randomFunction();

	}
	public static void randomFunction(){
		System.out.println("Hello");
	}

	public static void anotherFunction(){
		System.out.println("another function");
	}

}
