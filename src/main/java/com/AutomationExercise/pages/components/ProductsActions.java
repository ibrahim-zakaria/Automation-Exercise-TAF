package com.AutomationExercise.pages.components;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductsActions {
    static GUIDriver driver;
    public WebElement product;
    ProductsActions(GUIDriver driver) {
        this.driver = driver;
    }

    private final By productsPageHeadingLabel = By.cssSelector("h2.title");
    private final By allProductCardsLoc = By.cssSelector("div.features_items > div:has(> div.product-image-wrapper)");
    private final By productPriceLoc = By.cssSelector(".product-overlay .overlay-content h2");
    private final By productNameLoc = By.cssSelector(".product-overlay p");
    private final By productCard = By.className("single-products");
    private final By viewProductBtnLoc = By.cssSelector("a[href*='/product_details']");

    //Buttons
    private final By continueShoppingBtnLoc = By.cssSelector("#cartModal button");
    private final By viewCartBtnLoc = By.cssSelector("#cartModal a");
    private final By addToCartBtnLoc = By.cssSelector(".product-overlay a");
    private final By visibleProductNameLocator = By.cssSelector("div.productinfo > p");


    private static List<SelectedProducts> selectedProducts = new ArrayList<>();
    private int duplicateProductIndex = 0;

    public static void waitProductsLoad(){
        driver.elementActions().areAllElementsDisplayed(By.cssSelector(".productinfo.text-center img"));
    }

    public static void removeAllSelectedProducts() {
        selectedProducts.clear();
    }

    public void clickOnContinueShoppingBtn() {
        WebElement modal = driver.elementActions().find(By.id("cartModal"));

        System.out.println("Modal displayed: " + modal.isDisplayed());
        System.out.println("Modal size: " + modal.getSize());

        WebElement button = driver.elementActions().find(
                By.cssSelector("#cartModal button")
        );

        System.out.println("Button displayed: " + button.isDisplayed());
        System.out.println("Button size: " + button.getSize());
        System.out.println("Button enabled: " + button.isEnabled());
        driver.elementActions().
                clickOnContinue(continueShoppingBtnLoc);
    }

    public void clickOnViewCartBtn() {
        driver.elementActions().click(viewCartBtnLoc);
    }

    public void clickOnAddToCartBtn() {
        driver.elementActions().click(addToCartBtnLoc);
    }

    public void hoverOverProduct(int order) {
        product = getProductCard(order);

        try {
            WebElement wrapper = product.findElement(By.xpath(".."));
            WebElement overlay = wrapper.findElement(
                    By.cssSelector(".product-overlay"));

            driver.elementActions().hoverUntilVisible(
                    wrapper, By.cssSelector(".product-overlay"));

            driver.elementActions().waitUntilVisible(overlay);

            driver.elementActions().waitUntilClickable(
                    wrapper.findElement(addToCartBtnLoc));

            driver.elementActions().waitUntilVisible(
                    wrapper.findElement(productPriceLoc));

        } catch (RuntimeException e) {
            logHoverDiagnostics(order);
            throw e;
        }
    }

    public void clickOnHoveredAddToCartButton(){
        WebElement btn = driver.elementActions()
                .findElement(product, addToCartBtnLoc);
        driver.elementActions().waitUntilClickable(btn);
        try {
            //driver.elementActions().hoverOver(product);
            driver.elementActions().click(btn);
        } catch (RuntimeException e) {
            // driver.elementActions().diagnoseClickTarget(btn);
            throw e;
        }
        captureProductData(product);

    }

    public void clickOnViewProductBtn(int order) {
        driver.elementActions().diagnoseClickTarget(getViewProductBtn(order));
        driver.elementActions().click(getViewProductBtn(order));

    }

    public static List<SelectedProducts>  getSelectedProducts() {
       return
               selectedProducts;
    }

    public static void  removeFromSelectedProducts(int index) {
         selectedProducts
                 .remove(index);
   }

    public static void addNewSelectedProduct(SelectedProducts newSelectedProduct) {
       selectedProducts
               .add(newSelectedProduct);
    }

    public boolean isProductsContainerVisible() {
        return driver.elementActions()
                .isDisplayed(productsPageHeadingLabel);
    }

    public boolean areAllProductsDisplayed() {
        return driver.elementActions()
                .areAllElementsDisplayed(allProductCardsLoc);
    }

    public void captureProductData(WebElement product) {
        //System.out.println(getProductPrice(product));
        BigDecimal price = getProductPrice(product);//BigDecimal.ZERO;
        String name = getProductName(product);
        String id = getProductId(product);
        int qty = 1;
        if (!getSelectedProducts().isEmpty()) {
            if (isProductAlreadyCaptured()) {
                SelectedProducts tempProductData = getSelectedProducts().get(duplicateProductIndex);
                qty = tempProductData.qty() + 1;
                removeFromSelectedProducts(duplicateProductIndex);
            }
        }
        SelectedProducts tempProData = new SelectedProducts(
                id,
                name,
                price,
                qty,
                price.multiply(BigDecimal.valueOf(qty))
        );
        addNewSelectedProduct(tempProData);
    }

    private void logHoverDiagnostics(int order) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver.get(); // عدّل حسب GUIDriver
            LogsManager.error("---- Hover diagnostics for product #" + order + " ----");
            LogsManager.error("scrollY=" + js.executeScript("return window.scrollY"));
            LogsManager.error("hovered=" + js.executeScript("return arguments[0].matches(':hover')", product));
            LogsManager.error("ads=" + driver.elementActions().findElements(
                    By.cssSelector("iframe[id^='aswift'], ins.adsbygoogle, #google_vignette")).size());
            driver.elementActions().diagnoseClickTarget(product);
        } catch (Exception diagEx) {
            LogsManager.error("Diagnostics failed: " + diagEx.getMessage());
        }
    }

    private WebElement getProductCard(int order) {
        return driver.elementActions()
                .findElement(
                        getProductByOrder(order)
                        , productCard
                );

    }

    private List<WebElement> getAllProducts() {
        return driver.elementActions()
                .findElements(allProductCardsLoc);
    }

    private WebElement getProductByOrder(int order) {
        List<WebElement> allProducts = getAllProducts();
        if (order <= 0 || order > allProducts.size()) {
            throw new IllegalArgumentException("Order out of bounds!");
        }
        return allProducts.get(order - 1);
    }

    private String getProductId(WebElement product) {
        WebElement element = driver.elementActions().findElement(product, addToCartBtnLoc);
        return driver.elementActions().getAttributeValue(element, "data-product-id");
    }

    private String getProductName(WebElement product) {
        WebElement element = driver.elementActions().findElement(
                product
                , productNameLoc
        );
        return driver.elementActions().getText(element);
    }

    private WebElement getViewProductBtn(int order) {
        WebElement viewProductLink = getProductByOrder(order);
        return driver.elementActions()
                .findElement(
                        viewProductLink
                        , viewProductBtnLoc
                );
    }

    private BigDecimal getProductPrice(WebElement product) {
        WebElement element =
                driver.elementActions().findElement(product, productPriceLoc);
        LogsManager.info("Price element HTML: " + element.getAttribute("outerHTML"));

        String value = driver.elementActions().getText(element)
                .replace("Rs.", "").trim();

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            LogsManager.error("Failed to convert product price: [" + value + "]");
            throw e;
        }
    }

    private boolean isProductAlreadyCaptured() {
        String id = getProductId(product);
        duplicateProductIndex = 0;
        for (SelectedProducts productData : getSelectedProducts()) {
            if (productData.id().equals(id)) {
                return true;
            }
            duplicateProductIndex++;
        }
        return false;
    }

    private void printProductsData() {
        getSelectedProducts()
                .forEach(System.out::println);
    }

    public boolean AreAllProductNamesMatchingSearchKey(String searchText) {
        List<WebElement> productsNames =
                driver.elementActions().findElements(visibleProductNameLocator);
        if (productsNames.isEmpty()) {
            LogsManager.warn("No products found for search key: " + searchText);
            return false;
        }
        for (WebElement productName : productsNames) {
            if (!productName.getText().toLowerCase().contains(searchText.toLowerCase())) {
                LogsManager.error("Product name does not match search key: [" + productName.getText() + "]");
                return false;
            }
        }
        return true;
    }



}
