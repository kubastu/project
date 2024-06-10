package com.group2.project;

import com.group2.project.calendarobjects.CalendarObject;
import com.group2.project.email.Email;
import com.group2.project.headless.CalendarManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;


@ComponentScan(basePackages = {"com.group2.project.ContactsRepository","com.group2.project.ContactsService"})
@SpringBootApplication
public class ProjectApplication extends Application {

	private static boolean shouldRun = true;

	private ConfigurableApplicationContext springContext;
	private Parent rootNode;

	// ! WARNING: DO NOT USE THIS MAIN, PLEASE USE MAIN::MAIN() TO CORRECTLY LOAD WITH FXML !
	public static void main(String[] args) {
		//SpringApplication.run(ProjectApplication.class, args);
		//runREPL();
		Application.launch(args);
	}

	// setting up fxml: https://gist.github.com/Da9el00/f4340927b8ba6941eb7562a3306e93b6
	@Override
	public void init() throws Exception
	{
		springContext = SpringApplication.run(ProjectApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/calendarActivity.fxml"));
		rootNode = fxmlLoader.load();

	}

	// Launches the application in GUI mode:
	@Override
	public void start(Stage stage) throws IOException
	{
		stage.setScene(new Scene(rootNode));
		stage.setTitle("Calendar App");
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		springContext.close();
		super.stop();
	}

}
