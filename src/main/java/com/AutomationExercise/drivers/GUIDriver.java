package com.AutomationExercise.drivers;

import com.AutomationExercise.utils.actions.AlertActions;
import com.AutomationExercise.utils.actions.BrowserActions;
import com.AutomationExercise.utils.actions.ElementActions;
import com.AutomationExercise.utils.actions.FrameActions;
import com.AutomationExercise.utils.dataReader.PropertyReader;
import com.AutomationExercise.utils.logs.LogsManager;
import com.AutomationExercise.validations.Validation;
import com.AutomationExercise.validations.Verification;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;

import java.net.URISyntaxException;


public class GUIDriver {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    public GUIDriver() {
        String browser = System.getProperty("browser") != null ? System.getProperty("browser") : "Edge";
        Browser browserType = Browser.valueOf(browser.toUpperCase());
        LogsManager.info("Browser value is : " + browser);
        LogsManager.info("Browser Type: " + browserType);
        AbstractDriver abstractDriver = browserType.getBrowserFactory();
        WebDriver driver = null;
        try {
            driver = ThreadGuard.protect( abstractDriver.createDriver());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        driverThreadLocal.set(driver);
    }

    public WebDriver get() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        try {
            WebDriver driver = driverThreadLocal.get();
            if (driver != null) {
                driver.quit();
            }
        } finally {
            driverThreadLocal.remove();
        }
    }


    public ElementActions elementActions() {
        return new ElementActions(get());
    }
    public BrowserActions browserActions() {
        return new BrowserActions(get());
    }
    public AlertActions alertActions() {
        return new AlertActions(get());
    }
    public FrameActions frameActions() {
        return new FrameActions(get());
    }
    public Validation  validation() {
        return new Validation(get());
    }
    public Verification verification() {
        return new Verification(get());
    }

}
