package com.AutomationExercise.pages.components;

import com.AutomationExercise.drivers.GUIDriver;
import com.AutomationExercise.utils.dataReader.PaymentCardData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class PaymentPage {
    GUIDriver driver;

    public PaymentPage(GUIDriver driver) {
        this.driver = driver;
    }


    private final By NameOnCardLoc = By.cssSelector("input[data-qa='name-on-card']");
    private final By cardNumberLoc = By.cssSelector("input[data-qa='card-number']");
    private final By cardCvcLoc = By.cssSelector("input[data-qa='cvc']");
    private final By cardExpiryMonthLoc = By.cssSelector("input[data-qa='expiry-month']");
    private final By cardExpiryYearLoc = By.cssSelector("input[data-qa='expiry-year']");
    private final By payAndConfirmOrderBtnLoc = By.cssSelector("button[data-qa='pay-button']");
    private final By paymentConfirmMsgLoc = By.cssSelector("#success_message .alert-success");

    @Step("Fill Payment Details")
    public PaymentPage fillPaymentDetails(PaymentCardData cardData) {
        driver.elementActions().enterText(NameOnCardLoc, cardData.nameOnCard());
        driver.elementActions().enterText(cardNumberLoc, cardData.cardNumber());
        driver.elementActions().enterText(cardCvcLoc, cardData.cvc());
        driver.elementActions().enterText(cardExpiryMonthLoc, cardData.expirationMonth());
        driver.elementActions().enterText(cardExpiryYearLoc, cardData.expirationYear());
        return this;
    }

    @Step("Verify payment success")
    public OrderConfirmationPage verifyPaymentSuccess() {

        driver.verification().assertTrue(
                isPaymentSuccessful()
                , "Unsuccessful payment process"
        );
        return new OrderConfirmationPage(driver);
    }

    String actualText;

   /* @Step("Click on pay and confirm Order and Submit Payment")
    public PaymentPage clickOnPayAndConfirmOrder() {
        driver.elementActions().click(payAndConfirmOrderBtnLoc);
        //driver.elementActions().waitUntilVisible(paymentConfirmMsgLoc);
        actualText = driver.elementActions().captureTransientMessage(paymentConfirmMsgLoc);
       /* actualText = driver.elementActions()
                .waitUntilVisible(paymentConfirmMsgLoc)
                .getText();
        System.out.println("Payment message: " + actualText);
        return this;
    }*/

    @Step("Click on Pay and Confirm Order")
    public PaymentPage clickOnPayAndConfirmOrder() {
        driver.elementActions()
                .startTransientMessageObserver(paymentConfirmMsgLoc);
        driver.elementActions().click(payAndConfirmOrderBtnLoc);
        actualText = driver.elementActions()
                .getCapturedPaymentMessage();

        return this;
    }
    @Step("Verify payment success message")
    public boolean isPaymentSuccessful() {
        /*try {
            actualText = driver.elementActions()
                    .waitUntilVisible(paymentConfirmMsgLoc)
                    .getText();*/

        return actualText.equals("Your order has been placed successfully!");
        /*} catch (TimeoutException e) {
            return false;
        }*/
    }
}
