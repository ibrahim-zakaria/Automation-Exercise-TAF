package com.AutomationExercise.utils.actions;

import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

public class BrowserActions {
    private final WebDriver driver;
    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }
    /**
     * Maximize window
     */
    public void maximizeWindow(){
        driver.manage().window().maximize();
    }
    /**
     * Function to close current window
     */
    public void close(){
        driver.close();
    }
    /**
     * My Function to navigate to specific URL
     * @param url String of URL
     *
     */
    public void navigateTo(String url){
        driver.get(url);
        LogsManager.info("Navigate to URL:" + url);
    }
    /**
     * Function returns current page URL
     * @return The current page URL
     */
    public String getCurrentUrl(){
        String currentUrl = driver.getCurrentUrl();
        LogsManager.info("Current URL:" + currentUrl);
        return currentUrl;
    }

    /**
     * Function to refresh page
     */
    public void refreshPage(){
        driver.navigate().refresh();
    }
    /**
     * Function to open new window
     */
    public void openNewWindow(){
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

}
