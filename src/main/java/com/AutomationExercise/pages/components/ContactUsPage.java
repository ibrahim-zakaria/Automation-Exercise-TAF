package com.AutomationExercise.pages.components;

import com.AutomationExercise.drivers.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ContactUsPage {
    public ContactUsPage(GUIDriver driver) {
        this.driver = driver;
    }
    GUIDriver driver;
    private final By nameLocator = By.cssSelector("input[data-qa='name']");
    private final By emailLocator = By.cssSelector("input[data-qa='email']");
    private final By subjectLocator = By.cssSelector("input[data-qa='subject']");
    private final By messageLocator = By.cssSelector("textarea[data-qa='message']");
    private final By uploadFileLocator = By.cssSelector("input[type='file']");
    private final By submitLocator = By.cssSelector("input[type='submit']");
    private final By GetInTouchLocator = By.xpath("//h2[normalize-space()='Get In Touch']");
    private final By successMsgLocator = By.xpath("//div[@id='contact-page'] //div[normalize-space()='Success! Your details have been submitted successfully.']");
    private final By homeBtnLocator = By.cssSelector("a.btn[href='/']");

    @Step("Enter  name")
    public ContactUsPage enterName(String name) {
        driver.elementActions().enterText(nameLocator, name);
        return this;
    }

    @Step("Enter Email Address")
    public ContactUsPage enterEmail(String email) {
        driver.elementActions().enterText(emailLocator, email);
        return this;
    }

    @Step("Enter Subject")
    public ContactUsPage enterEmailSubject(String subject) {
        driver.elementActions().enterText(subjectLocator, subject);
        return this;
    }

    @Step("Enter Message")
    public ContactUsPage enterMessageText(String message) {
        driver.elementActions().enterText(messageLocator, message);
        return this;
    }

    @Step("Upload File ")
    public ContactUsPage uploadFile(String fileName) {
        driver.elementActions().uploadFile(uploadFileLocator, fileName);
        return this;
    }

    @Step("Click on Submit")
    public ContactUsPage submit() {
        driver.elementActions().click(submitLocator);
        return this;
    }

    @Step("Click on Home button")
    public HomePage clickOnHomeButton() {
        driver.elementActions().click(homeBtnLocator);
        return new HomePage(driver);
    }

    @Step("Verify 'GET IN TOUCH' is visible")
    public ContactUsPage verifyGetInTouchIsVisible() {
        driver.verification().isElementIsVisible(GetInTouchLocator);
        return this;
    }
    @Step("Accept alert Message")
    public ContactUsPage acceptAlertMessage() {
        driver.elementActions().acceptAlert();
        return this;
    }

    @Step("Verify successful message")
    public ContactUsPage verifySuccessfulMessage() {
        String actualTxt = driver.elementActions().getText(successMsgLocator);
        String expectedTxt = "Success! Your details have been submitted successfully.";
        driver.verification().equals(
                expectedTxt
                ,actualTxt
                ,"Actual message doesn't match expected message"
        );
        return this;
    }
}

