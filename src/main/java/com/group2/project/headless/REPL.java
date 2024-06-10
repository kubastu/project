package com.group2.project.headless;

import com.group2.project.calendarobjects.CalendarObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;

public class REPL
{

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

                    CalendarObject temp = new CalendarObject(ZonedDateTime.now(), tempTitle, tempDescription);
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
