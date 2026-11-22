package com.AutomationExercise.media;

import com.AutomationExercise.utils.logs.LogsManager;
import com.AutomationExercise.utils.time.TimeManager;
import com.AutomationExercise.utils.report.AllureAttachmentManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;

public class ScreenshotsManager {
public static final String SCREENSHOTS_PATH = "target/test-output/screenshots";
public static void takeFullPageScreenshot(WebDriver driver, String screenshotName) {
    try{
        File screenshotSrc = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        //Save Screenshot
        File screenshotDst = new File(SCREENSHOTS_PATH +  screenshotName + "-" + TimeManager.getTimestamp() + ".png");
        FileUtils.copyFile(screenshotSrc, screenshotDst);
        //throw log INFO
        LogsManager.info("Taking screenshot of: " + screenshotDst.getAbsolutePath());
        AllureAttachmentManager.attachScreenshot(screenshotName,  screenshotDst.getAbsolutePath());

    }catch(Exception e){
        LogsManager.error("Failed to Capture Screen ", e.getMessage());
    }
}

    public static void takeElementScreenshot(WebDriver driver, By locator) {
        try{
            WebElement element = driver.findElement(locator);
            File screenshotSrc = element.getScreenshotAs(OutputType.FILE);
            //get the visible name/text of an element
            String accessibleName = element.getAccessibleName();
            //Save Screenshot

            File screenshotDst = new File(SCREENSHOTS_PATH +  accessibleName + "-" + TimeManager.getTimestamp() + ".png");
            FileUtils.copyFile(screenshotSrc, screenshotDst);
            //throw log INFO
            LogsManager.info("Taking screenshot of: " + screenshotDst.getAbsolutePath());

            //TODO: Attach Screenshot to allure report

        }catch(Exception e){
            LogsManager.error("Failed to Capture Screen ", e.getMessage());
        }
    }

}
