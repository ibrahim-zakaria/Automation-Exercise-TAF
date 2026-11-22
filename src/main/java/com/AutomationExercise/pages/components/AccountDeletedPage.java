package com.AutomationExercise.pages.components;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.validations.Validation;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class AccountDeletedPage {
    GUIDriver driver;
    public AccountDeletedPage(GUIDriver driver) {
        this.driver = driver;
    }
    private final By accountDeletedLabel = By.cssSelector("h2[data-qa='account-deleted'] b");
    private final By continueBtnLoc = By.cssSelector("a[data-qa='continue-button']");

    @Step("Verify Account is deleted")
    public AccountDeletedPage verifyAccountDeleted(){
        String actualDeleteMessage = getDeletionStatus();
        String expectedDeleteMessage = "ACCOUNT DELETED!";
        driver.validation().isElementIsVisible(accountDeletedLabel);
        driver.validation().assertEquals(actualDeleteMessage,expectedDeleteMessage,"Account Deleted!");
        Validation.assertAll();
        return this;
    }

    @Step("Click on continue button")
    public NavigationComponent clickOnContinueBtn(){
        driver.elementActions().click(continueBtnLoc);
        return new NavigationComponent(driver);
    }

    private String getDeletionStatus(){
        return driver.elementActions().getText(accountDeletedLabel);
    }
}
