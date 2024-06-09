package com.group2.project.jsonparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JSONParser
{

    public static void readJSON()
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
