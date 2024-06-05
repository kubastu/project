package com.group2.project;

import com.group2.project.calendarobjects.CalendarObject;
import com.group2.project.calendarobjects.Event;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Scanner;

@SpringBootApplication
public class ProjectApplication extends Application {

	private static boolean shouldRun = true;

	private ConfigurableApplicationContext springContext;
	private Parent rootNode;

	public static void main(String[] args) {
		//SpringApplication.run(ProjectApplication.class, args);
		//runREPL();
		//Application.launch(args);
	}

	// setting up fxml: https://gist.github.com/Da9el00/f4340927b8ba6941eb7562a3306e93b6
	@Override
	public void init() throws Exception
	{
		springContext = SpringApplication.run(ProjectApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/calendarActivity.fxml"));
		rootNode = fxmlLoader.load();
	}

	// JZ: Launches the application in GUI mode:
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

	// JZ: this function defines a simple Read-Eval-Print-Loop for a basic calendar I/O.
	public static void runREPL() {

		// Insight from : https://www.tutorialspoint.com/how-can-we-read-from-standard-input-in-java

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("REPL Options : \n (A)dd \n (R)emove \n (L)ist [All] \n (E)xit \n");
			String option = br.readLine();

			while(!option.equals("E")) {
				if(option.equals("A")) {
					System.out.println("Title ?");
					String tempTitle = br.readLine();
					System.out.println("Description ?");
					String tempDescription = br.readLine();

					CalendarObject temp = new CalendarObject(new Date(), tempTitle, tempDescription);
					CalendarManager.addCalendarObject(temp);
				} else if(option.equals("R")) {
					// TODO: Implement date + id for this to work
				} else if(option.equals("L")) {
					System.out.println(CalendarManager.listCalendarObjects());
				}

				System.out.println("#########################");
				System.out.print("REPL Options : \n (A)dd \n (R)emove \n (L)ist [All] \n (E)xit \n");
				option = br.readLine();

			}

		} catch(IOException e) {
			e.printStackTrace();
		}

	}

}
