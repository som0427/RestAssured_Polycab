package utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GetProperty {

    private static final String ENVIRONMENT = "environment";
    private static String value = null;
   String envValue;

    public static String value(String key) {
        Properties prop = new Properties();
        GetProperty gp = new GetProperty();

        try {
            FileReader reader = new FileReader("application.properties");
            prop.load(reader);
            value = prop.getProperty(gp.getValueForEnv(key));
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }   catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private String getValueForEnv(String key){

        if(System.getProperty(ENVIRONMENT).equals("QA")){
            envValue = "qa." + key;
        }
        if(System.getProperty(ENVIRONMENT).equals("preProd")){
            envValue = "preProd." + key;
        }
        if(System.getProperty(ENVIRONMENT).equals("Production")){
            envValue = "prod." + key;
        }

        return envValue;
    }
}