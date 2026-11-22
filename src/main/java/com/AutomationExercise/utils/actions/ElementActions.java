package com.AutomationExercise.utils.actions;

import com.AutomationExercise.utils.wait.WaitManager;
import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ElementActions {
    WebDriver driver;
    WaitManager wait;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitManager(driver);
    }

    public void enterText(By locator, String text) {
        wait.fluentWait().until(d ->
        {
            WebElement element = driver.findElement(locator);
            new Actions(d).moveToElement(element).perform();
            element.sendKeys(text);
            LogsManager.info("Entered tex: \"" + text + "\" ** " + "info element: (" + locator + ")");
            return true;
        });
    }

    public void enterMaskedText(By locator, String value) {
        driver.findElement(locator).sendKeys(value);
        LogsManager.info("Typed value: " + maskValue(value) + " into element: " + locator);
    }

    private String maskValue(String value) {
        if (value == null || value.isEmpty()) {
            return "****";
        }
        return "*".repeat(value.length() - Math.min(4, value.length()))
                + value.substring(Math.max(0, value.length() - 4));
    }

    public String getText(By locator) {
        return wait.fluentWait().until(d ->
        {
            try {
                WebElement element = find(locator);
                String text = element.getText();
                LogsManager.info("************Text found: " + text + " info element " + locator);
                return !text.isEmpty() ? text : null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    public String getText(WebElement element) {
        String text = element.getText();
        LogsManager.info("Text found: " + text + " info element " + element);
        return text;
    }

    public String getAttributeValue(WebElement element, String attributeName) {
        LogsManager.info("Attribute found: " + attributeName + " for element " + element);
        return element.getAttribute(attributeName);
    }

    public String getAttributeValue(By locator, String attributeName) {
        wait.fluentWait().until(d -> {
            WebElement element = find(locator);
            scrollToElementJS(locator);
            return element.getAttribute(attributeName) != null;
        });
        WebElement element = find(locator);
        String value = element.getAttribute(attributeName);
        LogsManager.info("Attribute found: " + attributeName + " for element " + locator);
        return value;
    }

    public void click(By locator) {
        wait.fluentWait().until(d -> {
            WebElement element = find(locator);
            scrollToElementJS(locator);
            element.click();
            LogsManager.info("Clicked element " + locator);
            return true;
        });
    }

    public void clickOnContinue(By locator) {

        scrollToElementJS(locator);

        wait.fluentWait().until(d -> {
            WebElement element = d.findElement(locator);

            if (!element.isDisplayed() || !element.isEnabled()) {
                return false;
            }

            String diagnostic = (String) ((JavascriptExecutor) d)
                    .executeScript("""
                            const protector = document.querySelector('#click-protector');
                            const button = arguments[0];
                            
                            return JSON.stringify({
                                protectorExists: !!protector,
                                protectorParent: protector?.parentElement?.outerHTML,
                                buttonParent: button.parentElement?.outerHTML,
                                protectorInsideButton: button.contains(protector),
                                buttonInsideProtector: protector?.contains(button)
                            });
                            """, element);
            LogsManager.info("Click protector diagnostics: " + diagnostic);

            element.click();
            LogsManager.info("Clicked element " + locator);
            return true;
        });
    }


    /*public void click(WebElement element) {
        wait.fluentWait().until(
                ExpectedConditions.elementToBeClickable(element)
        ).click();

        LogsManager.info("Clicked element: " + element);
    }*/

    public void click(WebElement element) {
        try {
            wait.fluentWait().until(d -> {
                element.click();
                LogsManager.info("Clicked element: " + element);
                return true;
            });
        } /*catch (ElementClickInterceptedException e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            LogsManager.error("Target element outerHTML: "
                    + element.getAttribute("outerHTML"));

            WebElement blocker = (WebElement) js.executeScript(
                    "const r = arguments[0].getBoundingClientRect();" +
                            "return document.elementFromPoint(r.left + r.width / 2, r.top + r.height / 2);",
                    element
            );

            LogsManager.error("************ Intercepting element outerHTML: "
                    + (blocker != null ? blocker.getAttribute("outerHTML") : "Not found"));

            throw e;
        }*/ catch (ElementClickInterceptedException e) {
            LogsManager.error("Click intercepted: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            /*LogsManager.error("Click failed with: "
                    + e.getClass().getName() + " | " + e.getMessage());
            throw e;*/
            e.printStackTrace();
        }
    }


   /* public void click(WebElement element) {
        element.click();
        LogsManager.info("Clicked on element: " + element);
    }*/

    //Function that override fineElement function
    public WebElement find(By locator) {
        return driver.findElement(locator);
    }

    public boolean isDisplayed(By locator) {
        try {
            wait.fluentWait().until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean areAllElementsDisplayed(By locator) {
        try {
            wait.fluentWait().until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(locator)
            );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /*
     *Function to scroll to the Element using JS
     * @param locator of the Element
     */
    public void scrollToElementJS(By locator) {
        WebElement element = driver.findElement(locator);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior:'auto', block:'center', inline:'center'});",
                element
        );
    }

    public void scrollToElementStable(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].scrollIntoView({behavior:'instant', block:'center', inline:'nearest'});", element);

        // ننتظر حتى يثبت scrollY في قراءتين متتاليتين
        final double[] last = {-1};
        wait.fluentWait().until(d -> {
            double y = ((Number) js.executeScript("return window.scrollY;")).doubleValue();
            boolean stable = (y == last[0]);
            last[0] = y;
            return stable;
        });
    }

    public boolean isElementCovered(WebElement element) {
        return (Boolean) ((JavascriptExecutor) driver).executeScript("""
                const rect = arguments[0].getBoundingClientRect();
                const x = rect.left + rect.width / 2;
                const y = rect.top + rect.height / 2;
                const topElement = document.elementFromPoint(x, y);
                
                return topElement == null
                    || !(topElement == arguments[0]
                    || arguments[0].contains(topElement));
                """, element);
    }


    public void hoverUntilVisible(WebElement container, By childLocator) {
        wait.fluentWait().until(d -> {
            scrollToElementStable(container);
            if (isElementCovered(container)) {
                return false; // انتظر حتى يزول العنصر الذي يغطيه
            }
            new Actions(driver)
                    .moveToElement(container, -10, -10)
                    .moveToElement(container)
                    .perform();
            return container.findElement(childLocator).isDisplayed();
        });
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public void uploadFile(By locator, String fileName) {
        String fileAbsolute = System.getProperty("user.dir") + File.separator + fileName;
        System.out.println("************  Uploading file: " + fileAbsolute);
        wait.fluentWait().until(d -> {
            try {
                WebElement element = find(locator);
                scrollToElementJS(locator);
                element.sendKeys(fileAbsolute);
                LogsManager.info("Uploaded file: " + fileAbsolute + " info element " + locator);
                return true;
            } catch (Exception e) {
                System.out.println("Upload file error" + e.getMessage());
                return false;
            }
        });
    }

    public WebElement findElement(WebElement parent, By childLocator) {
        try {
            WebElement element = parent.findElement(childLocator);
            LogsManager.info("Found element: " + childLocator);
            return element;
        } catch (NoSuchElementException e) {
            LogsManager.error("Failed to find element: " + childLocator, e.getMessage()
            );
            throw e;
        }
    }

    public void selectByVisibleText(By locator, String text) {
        new Select(driver.findElement(locator))
                .selectByVisibleText(text);

        LogsManager.info("Selected option '" + text
                + "' by visible text from: " + locator);
    }

    public void selectByValue(By locator, String value) {
        new Select(driver.findElement(locator))
                .selectByValue(value);

        LogsManager.info("Selected option with value '" + value
                + "' from: " + locator);
    }

    public void selectByIndex(By locator, int index) {
        new Select(driver.findElement(locator))
                .selectByIndex(index);

        LogsManager.info("Selected option at index " + index
                + " from: " + locator);
    }

    public void setCheckbox(By locator, boolean shouldBeChecked) {
        WebElement checkbox = driver.findElement(locator);

        if (checkbox.isSelected() != shouldBeChecked) {
            checkbox.click();
        }

        LogsManager.info("Checkbox state set to " + shouldBeChecked
                + " for: " + locator);
    }

    public void waitUntilInvisible(WebElement element) {
        wait.fluentWait().until(
                ExpectedConditions.invisibilityOf(element)
        );
        LogsManager.info("Element is no longer visible");
    }

    public void hoverOver(WebElement element) {
        new Actions(driver)
                .moveToElement(element)
                .perform();

        LogsManager.info("Hovered over element: " + element);
    }

    public WebElement waitUntilClickable(WebElement element) {
        return wait.fluentWait().until(
                //ExpectedConditions.elementToBeClickable(locator)
                ExpectedConditions.elementToBeClickable(element)
        );
    }

    public WebElement waitUntilVisible(By locator) {
        return wait.fluentWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    public String waitAndGetTextContent(By locator) {
        return wait.fluentWait().until(d -> {
            List<WebElement> els = d.findElements(locator);
            if (els.isEmpty()) return null;
            String t = els.get(0).getAttribute("textContent");
            return (t != null && !t.isBlank()) ? t.trim() : null;
        });
    }

    public WebElement waitUntilVisible(WebElement element) {
        return wait.fluentWait().until(
                ExpectedConditions.visibilityOf(element)
        );
    }

    public void scrollTBottomOfPage() {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void diagnoseClickTarget(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Map<String, Object> result =
                (Map<String, Object>) js.executeScript(
                        """
                                const r = arguments[0].getBoundingClientRect();
                                const x = r.left + r.width / 2;
                                const y = r.top + r.height / 2;
                                const top = document.elementFromPoint(x, y);
                                
                                return {
                                    targetTag: arguments[0].tagName,
                                    targetClass: arguments[0].className,
                                    displayed: arguments[0].getBoundingClientRect().width > 0,
                                    topTag: top ? top.tagName : null,
                                    topId: top ? top.id : null,
                                    topClass: top ? top.className : null,
                                    topHtml: top ? top.outerHTML : null
                                };
                                """,
                        element
                );

        result.forEach((key, value) ->
                System.out.println(key + ": " + value)
        );
    }

    public void scrollUntilProductsLoad() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        long previousHeight = 0;
        int stableCount = 0;

        while (stableCount < 3) {
            js.executeScript(
                    "window.scrollTo(0, document.body.scrollHeight)"
            );

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            long currentHeight = ((Number) js.executeScript(
                    "return document.body.scrollHeight"
            )).longValue();

            if (currentHeight == previousHeight) {
                stableCount++;
            } else {
                stableCount = 0;
            }

            previousHeight = currentHeight;
        }

        js.executeScript("window.scrollTo(0, 0)");
    }

    public void scrollUntilProductsLoad2() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        long previousHeight = -1;
        int stableCount = 0;

        while (stableCount < 3) {
            js.executeScript("window.scrollBy(0, 600)");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }

            long currentHeight = ((Number) js.executeScript(
                    "return document.documentElement.scrollHeight"
            )).longValue();

            if (currentHeight == previousHeight
                    && ((Number) js.executeScript(
                    "return window.scrollY + window.innerHeight"
            )).longValue() >= currentHeight - 5) {
                stableCount++;
            } else {
                stableCount = 0;
            }

            previousHeight = currentHeight;
        }
    }

    public void scrollToTop() {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, 0)");
    }


    public String waitForPaymentSuccessMessage(By locator) {
        return wait.fluentWait()
                .pollingEvery(Duration.ofMillis(100))
                .until(d -> {
                    List<WebElement> elements =
                            d.findElements(locator);

                    for (WebElement element : elements) {
                        if (element.isDisplayed()) {
                            String text = element.getText().trim();

                            if (!text.isEmpty()) {
                                return text;
                            }
                        }
                    }
                    return null;
                });
    }

    /********* Start mentoring *********************************************************/
    public void startTransientMessageObserver(By locator) {
        String selector = ((By.ByCssSelector) locator).toString()
                .replace("By.cssSelector: ", "");

        ((JavascriptExecutor) driver).executeScript("""
                const selector = arguments[0];
                const storageKey = "paymentSuccessMessage";
                const expectedText =
                    "Your order has been placed successfully!";
                
                sessionStorage.removeItem(storageKey);
                
                const checkMessage = () => {
                    const element = document.querySelector(selector);
                
                    if (!element || element.getClientRects().length === 0) {
                        return;
                    }
                
                    const text = element.textContent.trim();
                
                    if (text.includes(expectedText)) {
                        sessionStorage.setItem(storageKey, text);
                        observer.disconnect();
                    }
                };
                
                const observer = new MutationObserver(checkMessage);
                
                observer.observe(document.documentElement, {
                    subtree: true,
                    childList: true,
                    attributes: true,
                    characterData: true
                });
                
                checkMessage();
                """, selector);
    }

    /***********************Wait For Transient Message ********************************************************/

    public String getCapturedPaymentMessage() {
        return (String) ((JavascriptExecutor) driver)
                .executeScript("""
                        return sessionStorage.getItem(
                            "paymentSuccessMessage"
                        );
                        """);
    }

    /*******************************************/
    public String captureTransientMessage(By locator) {
        String selector = ((By.ByCssSelector) locator).toString()
                .replace("By.cssSelector: ", "");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        return (String) js.executeAsyncScript("""
                const selector = arguments[0];
                const callback = arguments[arguments.length - 1];
                const expectedText = "Your order has been placed successfully!";
                let finished = false;
                
                const finish = (text) => {
                    if (finished) return;
                    finished = true;
                    observer.disconnect();
                    clearTimeout(timer);
                    callback(text);
                };
                
                const checkMessage = () => {
                    const element = document.querySelector(selector);
                
                    if (element &&
                        element.offsetParent !== null &&
                        element.textContent.trim().includes(expectedText)) {
                        finish(element.textContent.trim());
                    }
                };
                
                const observer = new MutationObserver(checkMessage);
                
                observer.observe(document.documentElement, {
                    subtree: true,
                    childList: true,
                    attributes: true,
                    characterData: true
                });
                
                const timer = setTimeout(() => finish(null), 10000);
                
                checkMessage();
                """, selector);
    }

    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }
}

