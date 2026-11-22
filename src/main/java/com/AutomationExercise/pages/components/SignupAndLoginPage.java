package com.AutomationExercise.pages.components;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.utils.dataReader.AccountData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class SignupAndLoginPage {
    private final GUIDriver driver;

    public SignupAndLoginPage(GUIDriver driver) {
        this.driver = driver;
    }

    private final By signupLoginPageLoc = By.cssSelector("a[href='/login']");
    private final By loginEmailField = By.cssSelector("[data-qa='login-email']");
    private final By passwordField = By.cssSelector("[data-qa='login-password']");
    private final By userName = By.cssSelector("[data-qa='signup-name']");
    private final By signupEmailField = By.cssSelector("[data-qa='signup-email']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");
    private final By signupButton = By.cssSelector("[data-qa='signup-button']");

    //Account Created
    private final By accountCreatedLabel =
            By.xpath("//b[normalize-space()='Account Created!']");
    private final By continueBtn = By.cssSelector("[data-qa='continue-button']");
    private final By loginErrorMessage =
            By.xpath("//p[normalize-space()='Your email or password is incorrect!']");
    private final By signupErrorMessage =
            By.xpath("//p[normalize-space()='Email Address already exist!']");
    private final By userLabel = By.tagName("b");

    //Actions Section
    @Step("Enter Email Address for Login")
    public SignupAndLoginPage inputEmail(String email) {
        driver.elementActions().enterText(loginEmailField, email);
        return this;
    }

    @Step("Enter Password for Login")
    public SignupAndLoginPage inputPassword(String password) {
        driver.elementActions().enterMaskedText(passwordField, password);
        return this;
    }

    @Step("Click on the Login button")
    public SignupAndLoginPage clickLoginButton() {
        driver.elementActions().click(loginButton);
        return this;
    }

    @Step("Login with Valid Credentials")
    public HomePage login(String email, String password) {
        inputEmail(email);
        inputPassword(password);
        clickLoginButton();

        // Wait for successful navigation or verify Home Page
        return new HomePage(driver);
    }

    @Step("Enter User Name for Signup")
    public SignupAndLoginPage inputSignupName(String name) {
        driver.elementActions().enterText(userName, name);
        return this;
    }

    @Step("Enter Email Address for Signup")
    public SignupAndLoginPage inputSignupEmail(String email) {
        driver.elementActions().enterText(signupEmailField, email);
        return this;
    }

    @Step("Click on the Signup button")
    public SignupPage clickSignupButton() {
        driver.elementActions().click(signupButton);
        return new SignupPage(driver);
    }

    @Step("Enter Email, Password and click Signup button")
    public SignupAndLoginPage signup(String userName, String email) {
        inputSignupName(userName);
        inputSignupEmail(email);
        clickSignupButton();
        return this;
    }

    //Validation Section
    @Step("Verify User logged out")
    public NavigationComponent verifyUserLoggedOut() {
        driver.verification().isElementIsVisible(signupLoginPageLoc);
        return new NavigationComponent(driver);
    }

    @Step("Verify Login Error Message")
    public SignupAndLoginPage verifyLoginErrorMessage(String expectedMessage) {
        String actualMessage = driver.elementActions().getText(loginErrorMessage);
        expectedMessage = "Your email or password is incorrect!";
        driver.verification().equals(
                actualMessage,
                expectedMessage,
                "Login error message does not match"
        );

        return this;
    }

    @Step("Verify Signup Error Message")
    public NavigationComponent verifySignupErrorMessage(String expectedMessage) {
        String actualMessage = driver.elementActions().getText(signupErrorMessage);
        expectedMessage = "Email Address already exist!";
        driver.verification().equals(
                actualMessage,
                expectedMessage,
                "Signup error message does not match"
        );
        return new NavigationComponent(driver);
    }

    @Step("Verify that user is navigated to login page")
    public NavigationComponent verifyUserIsNavigatedToLoginPage() {
        String actualUrl = driver.browserActions().getCurrentUrl();
        String expectedUrl = "https://automationexercise.com/login";
        driver.verification().equals(actualUrl, expectedUrl,
                "Expected to be navigated to the Login/Signup page, but the actual URL did not match. ");
        return  new NavigationComponent(driver);
    }

}
