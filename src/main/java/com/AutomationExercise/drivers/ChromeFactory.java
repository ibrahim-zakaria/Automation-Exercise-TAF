package com.AutomationExercise.drivers;

import com.AutomationExercise.utils.dataReader.PropertyReader;
import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChromeFactory extends AbstractDriver {
    private ChromeOptions getOptions(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-infobars");
        options.setAcceptInsecureCerts( true);
        options.setPageLoadStrategy( PageLoadStrategy.EAGER);

        ChromeDriver chrome = new ChromeDriver(options);
        chrome.executeCdpCommand("Network.enable", new HashMap<>());
        chrome.executeCdpCommand("Network.setBlockedURLs", Map.of("urls", List.of(
                "*googlesyndication.com*", "*doubleclick.net*",
                "*googleadservices.com*", "*google-analytics.com*")));
        if(PropertyReader.getProperty("executionType").equalsIgnoreCase("localHeadless")||PropertyReader.getProperty("executionType").equalsIgnoreCase("remote")){
            options.addArguments("--headless");
        }
        return options;
    }
    @Override
    public WebDriver createDriver()  {
        if(PropertyReader.getProperty("executionType").equalsIgnoreCase("localHeadless") ||
                PropertyReader.getProperty("executionType").equalsIgnoreCase("local")){
            return new ChromeDriver(getOptions());
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
