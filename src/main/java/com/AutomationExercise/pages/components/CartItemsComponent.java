package com.AutomationExercise.pages.components;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CartItemsComponent {
    GUIDriver driver;

    public CartItemsComponent(GUIDriver driver) {
        this.driver = driver;
    }

    private final By cartProductRowsLoc = By.cssSelector("tr[id^='product-']");
    private final By productPriceLoc = By.cssSelector("td.cart_price p");
    private final By productQuantityLoc = By.cssSelector("td.cart_quantity");
    private final By placeOrderLinkLoc = By.cssSelector("a[href='/payment']");
    private final By productTotalPriceLoc = By.cssSelector("td.cart_total p");
    //private final By productsIdLoc = By.cssSelector("tr[id^='product-']");
    private final By productsLinkLoc = By.cssSelector("td.cart_description h4 a");
    private final By productCategoryLoc = By.cssSelector("td.cart_description p");
    private final By productsImgLoc = By.cssSelector("td.cart_product a img");

    private final String productDetailsBaseUrl = "https://automationexercise.com/product_details/";
    private final String productImageBaseUrl = "https://automationexercise.com/get_product_picture/";


    /*************************************************************************/

    public boolean areAllProductsMatched(List<SelectedProducts> selectedProducts) {
        for (SelectedProducts product : selectedProducts) {
            CartProducts productInCart = getProductFromCart(product.id());
            if (!verifyProductDataMatches(productInCart, product)) {
                LogsManager.error("Product In cart :" + productInCart.toString() + " doesn't match expected selected user product :" + product.toString());
                return false;
            }

        }
        return true;
    }

    private boolean verifyProductDataMatches(CartProducts productInCart, SelectedProducts userProduct) {
        return productInCart.quantity() == userProduct.qty() &&
                productInCart.unitPrice().compareTo(userProduct.price()) == 0 &&
                productInCart.totalPrice().compareTo(userProduct.total()) == 0;
    }

    private CartProducts getProductFromCart(String selectedProductId) {

        String actualProductId = "product-" + selectedProductId;
        return getCartProductsSnapshot()
                .stream()
                .filter(product -> product.productId().equals(actualProductId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product not found" + actualProductId));
    }

    /*
    **********************{ Linked Hash Map } ***************
    * private CartProducts getProductFromCart(String selectedProductId) {
        String actualProductId = "product-" + selectedProductId;
        return getCartProductsSnapshot()
                .getOrDefault(actualProductId, null) != null
                ? getCartProductsSnapshot().get(actualProductId)
                : throw new NoSuchElementException(
                        "Product not found: " + actualProductId);
    }
    ************************************{End}***********************
    * */


    public BigDecimal getExpectedCartTotalPrice() {
        return ProductsActions.getSelectedProducts()
                .stream()
                .map(SelectedProducts::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //.map(product -> product.total())
        //.reduce(BigDecimal.ZERO, (total, price) -> total.add(price));
    }


    public List<CartProducts> getCartProductsSnapshot() {
        List<WebElement> ProductsRows = driver.elementActions().findElements(cartProductRowsLoc);
        List<CartProducts> cartSnapshot = new ArrayList<>();
        for (WebElement productRow : ProductsRows) {
            cartSnapshot.add(getProductData(productRow));
        }
        return cartSnapshot;
    }

    /**************************************************************************/

    //To get cart's Count of items
    public int getCartItemsCount() {
        return driver.elementActions()
                .findElements(cartProductRowsLoc)
                .size();
    }

    public List<WebElement> getCartProducts() {
        return driver.elementActions()
                .findElements(cartProductRowsLoc);
    }

    public BigDecimal getBigDecimal(String value) {
        return new BigDecimal(value);
    }


    public BigDecimal getTotalUnitPrice(WebElement product) {
        return new BigDecimal(product.findElement(productTotalPriceLoc)
                .getText()
                .replace("Rs.", "")
                .trim()
        );
    }

    public boolean areProductPricesCorrect() {
        List<WebElement> cartProducts = getCartProducts();
        if (cartProducts.isEmpty()) {
            return false;
        }
        for (WebElement cartProduct : cartProducts) {
            BigDecimal price = BigDecimal.valueOf(getQuantity(cartProduct));
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }

        }
        return true;
    }

    public boolean areProductQuantitiesCorrect() {
        List<WebElement> cartProducts = getCartProducts();
        if (cartProducts.isEmpty()) {
            return false;
        }
        for (WebElement cartProduct : cartProducts) {
            int qty = getQuantity(cartProduct);
            if (qty <= 0) {
                return false;
            }

        }
        return true;
    }

    public boolean areProductTotalPricesCorrect() {
        List<WebElement> productsRows = getCartProducts();
        if (productsRows.isEmpty()) {
            return false;
        }
        for (WebElement productRow : productsRows) {
            BigDecimal unitPrice = getProductPrice(productRow);
            int qty = getQuantity(productRow);
            BigDecimal expectedTotalUnitPrice = unitPrice.multiply(BigDecimal.valueOf(qty));
            BigDecimal actualTotalUnitPrice = getTotalUnitPrice(productRow);
            if (expectedTotalUnitPrice.compareTo(actualTotalUnitPrice) != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean areProductImagesCorrect() {
        List<WebElement> productsRows = getCartProducts();
        if (productsRows.isEmpty()) {
            return false;
        }
        for (WebElement productRow : productsRows) {
            String expectedImageUrl = buildExpectedProductUrl(productRow, productImageBaseUrl);
            String actualImageUrl = getProductDetailsLink(productRow);
            if (!expectedImageUrl.equals(actualImageUrl)) {
                return false;
            }
        }
        return true;
    }

    public boolean areProductCategoriesNonEmpty() {
        List<WebElement> productsRows = getCartProducts();
        if (productsRows.isEmpty()) {
            return false;
        }
        // Check that every category contains non-whitespace text.
        for (WebElement productRow : productsRows) {
            if (getProductCategory(productRow)
                    .trim()
                    .isEmpty()) {
                return false;
            }
        }
        // All categories contain text.
        return true;
    }

    public boolean areProductDetailsLinksCorrect() {
        List<WebElement> productsRows = getCartProducts();
        if (productsRows.isEmpty()) {
            return false;
        }
        for (WebElement productRow : productsRows) {
            String expectedUrl = buildExpectedProductUrl(productRow, productDetailsBaseUrl);
            String actualUrl = getProductDetailsLink(productRow);
            if (!expectedUrl.equals(actualUrl)) {
                return false;
            }
        }
        return true;
    }

    public boolean areProductTitlesNotEmpty() {
        List<WebElement> productsRows = getCartProducts();
        for (WebElement productRow : productsRows) {
            if (getProductTitle(productRow).trim().isEmpty()) {
                return false;
            }
        }
        return !productsRows.isEmpty();
    }

    public String buildExpectedProductUrl(WebElement productRow, String urlPrefix) {
        String productId = getProductId(productRow).split("-")[1];
        return urlPrefix + productId;
    }




    /*******************************************************************
     * *************************** INNER TOOLS *********************** *
     ********************************************************************/

    private CartProducts getProductData(WebElement product) {
        return new CartProducts(
                getProductId(product),
                getProductImageUrl(product),
                getProductTitle(product),
                getProductCategory(product),
                getProductPrice(product),
                getQuantity(product),
                getTotalUnitPrice(product)
        );
    }

    public String getProductId(WebElement product) {
        return product
                .getAttribute("id");
    }

    private String getProductImageUrl(WebElement product) {
        return product.findElement(productsImgLoc)
                .getAttribute("src");
    }

    public String getProductTitle(WebElement product) {
        return product.findElement(productsLinkLoc)
                .getText();
    }

    private String getProductCategory(WebElement product) {
        return product.findElement(productCategoryLoc)
                .getText();
    }

    public int getQuantity(WebElement product) {
        return Integer.parseInt(product.findElement(productQuantityLoc)
                .getText()
        );
    }

    public BigDecimal getProductPrice(WebElement product) {
        return new BigDecimal(
                product.findElement(productPriceLoc)
                        .getText()
                        .replace("Rs.", "")
                        .trim()
        );
    }

    private String getProductDetailsLink(WebElement productRow) {
        return productRow.findElement(productsLinkLoc)
                .getAttribute("href");
    }


}
