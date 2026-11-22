package com.AutomationExercise.pages.components;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class HomePage {
    private GUIDriver driver;
    ProductsActions productsActions;

    public HomePage(GUIDriver driver) {
        this.driver = driver;
        productsActions = new ProductsActions(driver);
    }

    private final By homeSlider = By.id("slider");
    private final By userNameLabelLoc = By.tagName("b");
    private final By firstProductViewBtnLoc = By.cssSelector("a[href='/product_details/1']");
    private final By scrollUpBtnLoc = By.cssSelector("a#scrollUp");
    private final By websiteLogo = By.cssSelector("div.logo img");
    private final By subscribeTxtLocator = By.cssSelector("div.single-widget h2");
    private final By subscribeEmailLocator = By.id("susbscribe_email");
    private final By submitSubscribe = By.id("subscribe");
    private final By successSubsMsgLocator = By.cssSelector("div.alert-success.alert");
//#recommended-item-carousel a[data-product-id] *****************
    @Step("Click on View Product Button for first Product}")
    public ProductDetailsPage clickOnViewProductOnFirst() {
        driver.elementActions().click(firstProductViewBtnLoc);
        return new ProductDetailsPage(driver);
    }
    @Step("Scroll over all products")
    public HomePage scrollOverAllProducts() {
        driver.elementActions().scrollTBottomOfPage();
        return this;
    }

    @Step("Hover Over Product")
    public HomePage hoverOverProduct(int order) {
        productsActions.hoverOverProduct(order);
        return this;
    }

    @Step("Click Add to Cart Button on Hovered Product")
    public HomePage clickOnHoveredAddToCartButton() {
        productsActions.clickOnHoveredAddToCartButton();
        return this;
    }

    @Step("Click View Product Button for Product #{order}")
    public ProductDetailsPage clickOnViewProductBtn(int order) {
        productsActions.clickOnViewProductBtn(order);
        return new ProductDetailsPage(driver);
    }

    @Step("Click on Continue Shopping Button")
    public HomePage clickOnContinueShoppingButton() {
        productsActions.clickOnContinueShoppingBtn();
        return this;
    }

    @Step("Click on view cart Button")
    public CartPage clickOnViewCartButton() {
        productsActions.clickOnViewCartBtn();
        return new CartPage(driver);
    }

    @Step("Click On Scroll-up button")
    public HomePage clickOnScrollUpButton() {
        driver.elementActions().click(scrollUpBtnLoc);
        return this;
    }
    @Step("Enter Subscription Email address")
    public HomePage enterSubscriptionEmail(String email) {
        driver.elementActions().enterText(subscribeEmailLocator,email);
        return this;
    }

    @Step("Click on submit of Subscription")
    public HomePage clickOnSubscriptionSubmitButton() {
        driver.elementActions().click(submitSubscribe);
        return this;
    }


    @Step("Scroll up to the Top of Home page")
    public HomePage scrollUpToTopOfPage() {
        driver.elementActions().scrollToTop();
        return this;
    }

    @Step("Verify all product are displayed")
    public HomePage verifyAllProductsPageIsDisplayed() {
        driver.verification().assertTrue(
                productsActions.areAllProductsDisplayed()
                , "Products are not displayed"
        );
        return this;
    }

    @Step("Verify All Products Container Is Visible")
    public HomePage verifyProductsContainerIsDisplayed() {
        driver.verification().assertTrue(
                productsActions.isProductsContainerVisible()
                , "Products container is not displayed");
        return this;
    }
    @Step("Scroll to bottom of home page")
    public HomePage scrollDownToEndOfPage() {
        //driver.elementActions().scrollTBottomOfPage();
        //driver.elementActions().scrollTBottomOfPage();
        driver.elementActions().scrollUntilProductsLoad2();
        return new HomePage(driver);
    }

    /**********************************************************************************
     ***************************** Validation & Verification **************************
     **********************************************************************************/

    @Step("Verify Home Page by Home-Slider. is it Visible")
    public HomePage verifyNavigationToHomePage() {
        driver.verification().isElementIsVisible(homeSlider);
        return new HomePage(driver);
    }
    @Step("Verify Website logo is Visible")
    public HomePage verifyLogoIsVisible() {
        driver.verification().isElementIsVisible(websiteLogo);
        return this;
    }

    @Step("Verify that 'Logged in as username' is visible #{expectedUserName}")
    public NavigationComponent verifyUserIsLoggedIn(String expectedUserName) {
        String actualUserName = driver.elementActions().getText(userNameLabelLoc);
        LogsManager.info("Verify user logged: " + actualUserName);
        driver.verification().equals(expectedUserName, actualUserName, "User label does not match. Expected: " + expectedUserName + ", Actual: " + actualUserName);
        return new NavigationComponent(driver);
    }
    @Step("Verify SUBSCRIPTION is visible")
    public HomePage verifySubscriptionIsVisible() {
        driver.verification().isElementIsVisible(subscribeTxtLocator);
        return new HomePage(driver);
    }

    @Step("Verify Subscription success Massage is visible")
    public HomePage verifySubsSuccessMsgIsVisible() {
        driver.verification().isElementIsVisible(subscribeEmailLocator);
        return this;
    }

    public NavigationComponent getNavigationComponent() {
        return new NavigationComponent(driver);
    }
    public ProductFilterSidebar getProductFilterSidebar() {
        return new ProductFilterSidebar(driver);
    }



}
