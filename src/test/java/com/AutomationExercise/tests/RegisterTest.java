package com.AutomationExercise.tests;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.pages.components.NavigationComponent;
import com.AutomationExercise.utils.dataReader.AccountData;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {

    //Test Scripts
    @Test(description = "Test Case 1: Register User", groups = {"smoke"})
    public void signupTest() {
        AccountData accountData = randomAccountData();
        new NavigationComponent(driver)
                .clickOnSignupAndLogin()
                .inputSignupName(accountData.username())
                .inputSignupEmail(accountData.email())
                .clickSignupButton()
                .verifyFormPageDisplayed()
                .fillAccountInformation(accountData)
                .clickCreateAccountButton()
                .verifyAccountCreation()
                .clickContinueButton()
                .verifyUserIsLoggedIn(accountData.username())
                .clickOnDeleteAccountLink()
                .verifyAccountDeleted();
        System.out.println(accountData.password());
    }

    @Test(description = "Test Case 2: Login User with correct email and password", groups = {"smoke", "regression", "sanity"})
    public void validLoginTest() {
        AccountData accountData = createTestUser();
        new NavigationComponent(driver)
                .navigateToHomePage()
                .getNavigationComponent()
                .clickOnSignupAndLogin()
                .login(accountData.email(), accountData.password())
                .verifyUserIsLoggedIn(accountData.username())
                .clickOnDeleteAccountLink()
                .verifyAccountDeleted();
    }

    @Test(description = "Test Case 3: Login User with incorrect email and password")
    public void loginWithInvalidCredentialsTest() {
        AccountData accountData = randomAccountData();
        new NavigationComponent(driver)
                .clickOnSignupAndLogin()
                .inputEmail(accountData.email())
                .inputPassword(accountData.password())
                .clickLoginButton()
                .verifyLoginErrorMessage("Invalid email or password");
    }

    @Test(description = "Test Case 4: Logout User", groups = {"smoke", "regression"})
    public void logoutTest() {
        AccountData  accountData = createTestUser();
        String email = accountData.email();
        String password = accountData.password();
        String username = accountData.username();
        new NavigationComponent(driver)
                .clickOnSignupAndLogin()
                .login(email, password)
                .verifyUserIsLoggedIn(username)
                .clickOnLogoutLink()
                .verifyUserIsNavigatedToLoginPage()
                .clickOnSignupAndLogin()
                .login(email,password)
                .getNavigationComponent()
                .clickOnDeleteAccountLink()
                .verifyAccountDeleted();
    }

    @Test(description = "Test Case 5: Register User with existing email")
    public void registerTest() {
        AccountData accountData = createTestUser();
        new NavigationComponent(driver)
                .clickOnSignupAndLogin()
                .signup(accountData.username(), accountData.email())
                .verifySignupErrorMessage("Invalid email or password");

        deleteAccount(accountData.email(), accountData.password(), accountData.username());
    }


    //Configurations
    @BeforeMethod
    public void setUp() {
        driver = new GUIDriver();
        new NavigationComponent(driver).navigateToHomePage();
    }

    @AfterMethod
    public void tearDown() {
        GUIDriver.quitDriver();
    }

}
