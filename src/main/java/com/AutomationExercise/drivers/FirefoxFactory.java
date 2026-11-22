package com.AutomationExercise.drivers;


import com.AutomationExercise.utils.dataReader.PropertyReader;
import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

public class FirefoxFactory extends AbstractDriver {
        FirefoxOptions options = new FirefoxOptions();
    private FirefoxOptions getOptions() {
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if(PropertyReader.getProperty("executionType").equalsIgnoreCase("localHeadless")||PropertyReader.getProperty("executionType").equalsIgnoreCase("remote")){
            options.addArguments("--headless");
        }
                return options;
    }

    @Override
    public WebDriver createDriver()  {
        if(PropertyReader.getProperty("executionType").equalsIgnoreCase("localHeadless") ||
                PropertyReader.getProperty("executionType").equalsIgnoreCase("local")){
            return new FirefoxDriver(getOptions());
        } else if (PropertyReader.getProperty("executionType").equalsIgnoreCase("remote")) {
            try{
                return new RemoteWebDriver(
                        new URI("http://" + host  + ":" + port + "/wd/hub" ).toURL(), getOptions());
            } catch (Exception e) {
                LogsManager.error("Error creating RemoteWebDriver", e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            LogsManager.error("Invalid execution type.", PropertyReader.getProperty("executionType"));
            throw new IllegalArgumentException("Invalid execution type: " + PropertyReader.getProperty("executionType"));
        }
    }
}
