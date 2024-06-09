package com.group2.project.jsonparser;

import org.bson.json.JsonObject;
import java.io.*;

public class JSONReader
{

    public JSONReader()
    {
        // todo add CalendarActivity
    }

    public static void openJSON()
    {
        try {
            //InputStream is = JSONReader.class.getResourceAsStream("src/main/docker/data-postgres/data-postgres.json");
            File jsonFile = new File("src/main/docker/data-postgres/data-postgres.json");

            BufferedReader reader = new BufferedReader(new FileReader(jsonFile));

            StringBuilder result = new StringBuilder();
            for(String line; (line = reader.readLine()) != null;)
            {
                result.append(line);
            }

            System.out.println(result.toString());

        } catch(Exception e) {
            e.printStackTrace();
        }


    }

}
