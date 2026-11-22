package com.AutomationExercise.utils.dataReader;

public record PaymentCardData(
        String nameOnCard,
        String cardNumber,
        String cvc,
        String expirationMonth,
        String expirationYear
) {
}
