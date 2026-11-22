package com.AutomationExercise.validations;

import com.AutomationExercise.utils.actions.ElementActions;
import com.AutomationExercise.utils.wait.WaitManager;
import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertTrue;

public abstract class BaseAssertion {
    protected final WebDriver driver;
    protected final WaitManager waitManager;
    protected final ElementActions elementActions;
    public BaseAssertion(WebDriver driver){
        this.driver = driver;
        this.waitManager = new WaitManager(driver);
        this.elementActions = new ElementActions(driver);
    }

    public abstract void assertTrue(boolean condition, String message);

    protected abstract void assertFalse(boolean condition, String message);
    protected abstract void assertEquals(String actual, String expected,String message);
    public void equals(String actual, String expected, String message) {
        assertEquals(actual, expected, message);
    }
    public void isElementIsVisible(By locator){
            boolean flag = waitManager.fluentWait().until(d->{
        try{
            return d.findElement(locator).isDisplayed();

        }catch (Exception e){
            LogsManager.error("Element is not visible", e.getMessage());
            return false;
        }
            });
            assertTrue(flag, "Element is not visible: " + locator);
    }

    public void assertPageUrl(String expectedUrl){
        String actualUrl = driver.getCurrentUrl();
        assertEquals(actualUrl, expectedUrl, "Page URL not matched . Expected: " + expectedUrl );
    }

}
