package com.AutomationExercise.tests;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.drivers.WebDriverProvider;
import com.AutomationExercise.pages.components.NavigationComponent;
import com.AutomationExercise.utils.dataReader.AccountData;
import com.AutomationExercise.utils.dataReader.JsonReader;
import com.AutomationExercise.utils.dataReader.TestDataGenerator;
import org.openqa.selenium.WebDriver;

public class BaseTest implements WebDriverProvider {
    protected GUIDriver driver;
    protected JsonReader testData;

    @Override
    public WebDriver getWebDriver() {
        return driver.get();
    }

    public void beforeMethod() {
        testData = new JsonReader("testData.json");
    }

    protected static AccountData randomAccountData() {
        TestDataGenerator.NameParts name = TestDataGenerator.randomNameParts();
        TestDataGenerator.CompanyData company = TestDataGenerator.randomCompany();
        TestDataGenerator.Birthday birthday = TestDataGenerator.randomBirthday();
        return new AccountData(
                TestDataGenerator.randEmailAddress(),
                TestDataGenerator.randFullName(),
                TestDataGenerator.randGender(),
                TestDataGenerator.randPassword(),
                birthday.day(),
                birthday.month(),
                birthday.year(),
                name.first(),
                name.last(),
                company.name(),
                company.address(),
                TestDataGenerator.address(),
                "India",
                TestDataGenerator.state(),
                TestDataGenerator.city(),
                TestDataGenerator.zipcode(),
                TestDataGenerator.mobileNumber(),
                true,
                true
        );
    }

    protected AccountData createTestUser() {
        AccountData accountData = randomAccountData();
        new NavigationComponent(driver)
                .navigateToHomePage()
                .getNavigationComponent()
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
                .clickOnLogoutLink()
                .verifyUserLoggedOut();
        return accountData;
    }

    protected void login(String email, String password, String userName) {
        new NavigationComponent(driver)
                .clickOnSignupAndLogin()
                .login(email, password)
                .verifyUserIsLoggedIn(userName);
    }

    protected void login() {
        JsonReader jsonReader = new JsonReader("users");
        String email = jsonReader.getJsonValue("$ValidUser.email");
        String password = jsonReader.getJsonValue("$ValidUser.password");
        String userName = jsonReader.getJsonValue("$ValidUser.userName");
        new NavigationComponent(driver)
                .clickOnSignupAndLogin()
                .login(email, password)
                .verifyUserIsLoggedIn(userName);
    }

    protected JsonReader getSearchTestData() {
        return new JsonReader("search-data");
    }

    protected void deleteAccount(String email, String password, String userName) {
        new NavigationComponent(driver)
                .clickOnSignupAndLogin()
                .login(email, password)
                .verifyUserIsLoggedIn(userName)
                .clickOnDeleteAccountLink()
                .verifyAccountDeleted();
    }


}

