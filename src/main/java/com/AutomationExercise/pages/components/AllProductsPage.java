package com.AutomationExercise.pages.components;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AllProductsPage {
    public AllProductsPage(GUIDriver driver) {
        this.driver = driver;
        this.productsActions = new ProductsActions(driver);
    }

    GUIDriver driver;
    public WebElement product;
    ProductsActions productsActions;
    CartItemsComponent cartItems;

    private final By searchField = By.id("search_product");
    private final By submitSearch = By.id("submit_search");
    private final By cartModal = By.id("cartModal");

    @Step("Enter a search name")
    public AllProductsPage enterSearchName(String searchName) {
        driver.elementActions().enterText(searchField, searchName);
        return this;
    }

    @Step("Submit search")
    public AllProductsPage submitSearch() {
        driver.elementActions().click(submitSearch);
        return this;
    }

    /* @Step("Hover Over Product")
     public AllProductsPage hoverOverProduct(int order) {
         product = getProductFrame(order);
         //WebElement addToCart = product.findElement(addToCartBtnLoc);
         driver.elementActions().scrollToElementStable(product);
         driver.elementActions().hoverOver(product);
         WebElement overlay = product.findElement(
                 By.cssSelector(".product-overlay")
         );
         driver.elementActions().waitUntilVisible(overlay);
         ///driver.elementActions().waitUntilVisible(product);
         driver.elementActions().waitUntilClickable(product.findElement(addToCartBtnLoc));
         driver.elementActions().waitUntilVisible(product.findElement(productPriceLoc));
         //driver.elementActions().click(addToCart);
         return this;
     }*/
    @Step("Hover Over Product")
    public AllProductsPage hoverOverProduct(int order) {
        productsActions.hoverOverProduct(order);
        return this;
    }

    @Step("Click Add to Cart Button on Hovered Product")
    public AllProductsPage clickOnHoveredAddToCartButton() {
        productsActions.clickOnHoveredAddToCartButton();
        return this;
    }

    @Step("Click View Product Button for Product #{order}")
    public ProductDetailsPage clickOnViewProductBtn(int order) {
        productsActions.clickOnViewProductBtn(order);
        return new ProductDetailsPage(driver);
    }

    @Step("Click on Continue Shopping Button")
    public AllProductsPage clickOnContinueShoppingButton() {
        productsActions.clickOnContinueShoppingBtn();
        return this;
    }

    @Step("Click on view cart Button")
    public CartPage clickOnViewCartButton() {
        productsActions.clickOnViewCartBtn();
        return new CartPage(driver);
    }

    @Step("Verify all product are displayed")
    public AllProductsPage verifyAllProductsPageIsDisplayed() {
        driver.verification().assertTrue(
                productsActions.areAllProductsDisplayed()
                ,"Products are not displayed"
        );
        return this;
    }

    @Step("Verify All Products Container Is Visible")
    public AllProductsPage verifyProductsContainerIsDisplayed() {
        driver.verification().assertTrue(
                productsActions.isProductsContainerVisible()
                ,"Products container is not displayed");
        return this;
    }

    /* ************************************************************************************ *
     * ********************************* Helpers Components ******************************  *
     * ************************************************************************************ */

    @Step("Click on Add to Cart Button for Product ID: {productId}")
    public AllProductsPage clickOnHoveredAddToCartButton(String productId) {
        driver.elementActions()
                .click(addToCartBtn(productId));
        return this;
    }

    private By addToCartBtn(String productId) {
        return By.cssSelector(
                "[data-product-id='" + productId + "']"
        );
    }
 public ProductFilterSidebar getProductFilterSidebar() {
        return new ProductFilterSidebar(driver);
 }


 public AllProductsPage verifyAllProductNamesMatchSearchKey(String searchText) {
        driver.verification().assertTrue(
                productsActions
                        .AreAllProductNamesMatchingSearchKey(searchText)
                ,"Not all product names match the search term"
        );
        return this;
 }

    /*
    * private final Map<String, SelectedProducts> productsData = new LinkedHashMap<>();

private void captureProductData(WebElement product) {
    String id = getProductId(product);
    SelectedProducts fresh = new SelectedProducts(id, getProductName(product), getProductPrice(product), 1);
    productsData.merge(id, fresh, (old, n) ->
            new SelectedProducts(old.id(), old.name(), old.price(), old.qty() + 1));
}

private String getProductName(WebElement product) {
    return driver.elementActions()
            .getText(driver.elementActions().findElement(product, infoNameLoc));
}

private BigDecimal getProductPrice(WebElement product) {
    String value = driver.elementActions()
            .getText(driver.elementActions().findElement(product, infoPriceLoc))
            .replace("Rs.", "").trim();
    return new BigDecimal(value);
}

private void printProductsData() {
    productsData.values().forEach(System.out::println);
}
    * */


}
