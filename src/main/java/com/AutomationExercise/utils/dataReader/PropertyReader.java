package com.AutomationExercise.utils.dataReader;

import com.AutomationExercise.utils.logs.LogsManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

public class PropertyReader {
    public static Properties loadProperties() {
        try{
            Properties properties = new Properties();
            Collection<File> propertiesFile;
            propertiesFile = FileUtils.listFiles(
                    new File("src/main/resources"),
                    new String[]{"properties"}, true);
            propertiesFile.forEach(file -> {
                try{
                    properties.load(FileUtils.openInputStream(file));
                }catch (Exception e){
                    LogsManager.error("Error loading properties file: " + e.getMessage());
                }

            });
            properties.putAll(System.getProperties());
            System.getProperties().putAll(properties);
            return properties;
        } catch (Exception e) {
            LogsManager.error("Error loading properties file: " + e.getMessage());
            return null;
        }
    }
    public static String getProperty(String key){
        try{
            return loadProperties().getProperty(key);
        }catch (Exception e){
            LogsManager.error("Error loading properties file: " + e.getMessage());
            return null;
        }
    }
}
