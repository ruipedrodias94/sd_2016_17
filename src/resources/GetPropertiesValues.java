package resources;

import org.omg.CORBA.portable.InputStream;

import java.io.*;
import java.util.Properties;

/**
 * Created by jorgearaujo on 25/10/16.
 */
public class GetPropertiesValues {

    FileInputStream inputStream;

    public Properties getProperties() {
        Properties prop = new Properties();

        try {

            inputStream = new FileInputStream("config.properties");
            prop.load(inputStream);
            inputStream.close();

            return prop;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
