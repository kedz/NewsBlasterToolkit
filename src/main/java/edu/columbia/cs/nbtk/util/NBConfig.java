package edu.columbia.cs.nbtk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 6/12/13
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class NBConfig {

    private Properties properties;

    private static NBConfig NB_CONFIGURATION;

    public static NBConfig getInstance() {
        if(NB_CONFIGURATION == null)
            NB_CONFIGURATION = new NBConfig();
        return NB_CONFIGURATION;
    }


    private NBConfig() {

        Properties prop = new Properties();
        System.out.println("[ LOADING CONFIGURATION ]");

        try {
            prop.load(NBConfig.class.getClassLoader().getResourceAsStream("nbtk.properties"));
            this.properties = prop;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static NBConfig useLocalFile(String propsFilename) {
        return useLocalFile(new File(propsFilename));
    }



    public static NBConfig useLocalFile(File propsFile) {
        try {
            NB_CONFIGURATION.properties.load(new FileInputStream(propsFile));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
        return NB_CONFIGURATION;
    }

    public String getProperty(String property) {return properties.getProperty(property);}


}
