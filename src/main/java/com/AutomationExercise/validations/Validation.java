package com.AutomationExercise.validations;

import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

/**
 * Provides soft validation methods for test assertions.
 *
 * <p>
 * This class extends {@link BaseAssertion} and uses TestNG's
 * {@link SoftAssert} to allow multiple validations to be performed
 * before reporting the collected failures.
 * </p>
 *
 * <p>
 * The {@code used} flag keeps track of whether any soft assertion
 * has been executed. This prevents calling {@link #assertAll()}
 * when no validations were performed.
 * </p>
 */
public class Validation extends BaseAssertion {

    /*
     * Each Validation object owns its own SoftAssert instance.
     * This prevents assertion states from being shared between
     * different Validation objects.
     */
    private static final SoftAssert softAssert = new SoftAssert();

    /*
     * Indicates whether at least one soft assertion has been used.
     */
    private static boolean used = false;

    /**
     * Creates a Validation object for the given WebDriver.
     *
     * @param driver the WebDriver instance used by the validation layer
     */
    public Validation(WebDriver driver) {
        super(driver);
    }
    /**
     * Performs a soft assertion that the given condition is true.
     *
     * @param condition the condition to verify
     * @param message   message displayed if the assertion fails
     */
    @Override
    public void assertTrue(boolean condition, String message) {

        // Execute the soft assertion and store its result.
        softAssert.assertTrue(condition, message);

        // Mark that at least one soft assertion has been used.
        used = true;
    }

    /**
     * Performs a soft assertion that the given condition is false.
     *
     * @param condition the condition to verify
     * @param message   message displayed if the assertion fails
     */
    @Override
    public void assertFalse(boolean condition, String message) {
        softAssert.assertFalse(condition, message);
        // assertFalse is also a valid use of SoftAssert.
        used = true;
    }

    /**
     * Performs a soft assertion comparing the actual and expected values.
     *
     * @param actual   the actual value
     * @param expected the expected value
     * @param message  message displayed if the assertion fails
     */
    @Override
    public void assertEquals(String actual, String expected, String message) {
        softAssert.assertEquals(actual, expected, message);
        used = true;
    }

    public void assertEquals(int actual, int expected, String message) {
        softAssert.assertEquals(actual, expected, message);
        used = true;
    }

    /**
     * Reports all collected soft assertion failures.
     *
     * <p>
     * SoftAssert does not immediately fail the test when an assertion
     * fails. The collected failures are reported when this method is
     * called.
     * </p>
     *
     * <p>
     * If no soft assertion was used, the method returns without doing
     * anything.
     * </p>
     *
     * @throws AssertionError if one or more soft assertions failed
     */
    public static void assertAll() {
        if (!used) { // Nothing to report if no soft assertion was executed.
            return;
        }
        try {
            softAssert.assertAll();// Report all collected assertion failures.
        } catch (AssertionError e) {
            LogsManager.error(// Log the combined assertion failure before failing the test.
                    "Assert All Failed",
                    e.getMessage()
            );
            // Re-throw the error so TestNG marks the test as failed.
            throw e;
        } finally {
            // Reset the usage state for the next validation cycle.
            used = false;
        }
    }
}