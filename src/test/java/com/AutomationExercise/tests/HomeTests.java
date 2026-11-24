package com.AutomationExercise.tests;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.pages.components.NavigationComponent;
import com.AutomationExercise.pages.components.ProductsActions;
import com.AutomationExercise.utils.dataReader.TestDataGenerator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomeTests extends BaseTest{

    @Test(description = "Test Case 10: Verify Subscription in home page", groups={"sanity"})
    public void verifySubscriptionInHome(){
        new NavigationComponent(driver)
                .navigateToHomePage()
                .verifyNavigationToHomePage()
                .scrollDownToEndOfPage()
                .verifySubscriptionIsVisible()
                .enterSubscriptionEmail(TestDataGenerator.randEmailAddress())
                .clickOnSubscriptionSubmitButton()
                .verifySubsSuccessMsgIsVisible();
    }

    @BeforeMethod
    public void setUp() {
        driver = new GUIDriver();
    }

    @AfterMethod
    public void tearDown() {
        ProductsActions.removeAllSelectedProducts();
        GUIDriver.quitDriver();
    }

}
