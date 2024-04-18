package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetPropertiesData {

    public static Properties getPropertiesData() {
        Properties prop = new Properties();
        InputStream input;
        {
            try {
                String env = System.getProperty("Environment") == null ? "sandbox" : System.getProperty("Environment");
                input = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/" + env + ".properties");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }
}
