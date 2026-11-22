package com.AutomationExercise.listeners;

import com.AutomationExercise.drivers.WebDriverProvider;
import com.AutomationExercise.media.ScreenRecordManager;
import com.AutomationExercise.media.ScreenshotsManager;
import com.AutomationExercise.utils.file.FileUtils;
import com.AutomationExercise.utils.dataReader.PropertyReader;
import com.AutomationExercise.utils.logs.LogsManager;
import com.AutomationExercise.utils.report.AllureAttachmentManager;
import com.AutomationExercise.utils.report.AllureConstants;
import com.AutomationExercise.utils.report.AllureEnvironmentManager;
import com.AutomationExercise.utils.report.AllureReportGenerator;
import com.AutomationExercise.validations.Validation;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;

public class TestNGListeners implements IExecutionListener, IInvokedMethodListener, ITestListener {
    public void onExecutionStart(){
        LogsManager.info("                     -----------------------------------------------                          ");
        LogsManager.info("Test Execution Started");
        cleanTestOutputDirectories();
        LogsManager.info("Directories cleared");
        createTestOutputDirectories();
        LogsManager.info("Directories created");
        PropertyReader.loadProperties();
        LogsManager.info("Properties file loaded");
        AllureEnvironmentManager.setupAllureEnvironmentManager();
        LogsManager.info("Allure Environment Manager setup");
    }
    public void onExecutionFinish(){
        AllureReportGenerator.copyHistory();
        AllureReportGenerator.generateReports(false);
        AllureReportGenerator.generateReports(true);
        //AllureReportGenerator.openReport(AllureReportGenerator.renameReport());


    }
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if(method.isTestMethod()){
            ScreenRecordManager.stopRecording(testResult.getName());
            LogsManager.info("Test Case: "+ testResult.getName() + "Recording Started");
        }
    }
    public void afterInvocation (IInvokedMethod method, ITestResult testResult) {
        WebDriver driver = null;
        if(method.isTestMethod()){
            ScreenRecordManager.setRecording();

            Validation.assertAll();
            if(testResult.getInstance() instanceof WebDriverProvider provider){
                System.out.println( testResult.getInstance() instanceof WebDriverProvider);
                driver = provider.getWebDriver();
            }
            switch(testResult.getStatus()){
                 case ITestResult.SUCCESS -> ScreenshotsManager.takeFullPageScreenshot(driver, "passed-" + testResult.getName());
                case ITestResult.FAILURE -> ScreenshotsManager.takeFullPageScreenshot(driver, "failed-" + testResult.getName());
                case ITestResult.SKIP -> ScreenshotsManager.takeFullPageScreenshot(driver, "skipped-" + testResult.getName());
            }
            AllureAttachmentManager.attachLogs();
            AllureAttachmentManager.attachRecords(testResult.getName());
        }
    }

    public void onTestSuccess (ITestResult result) {
        LogsManager.info("Test Case: "+ result.getName() + "Passed");
    }

    public void onTestFailure (ITestResult result) {
        LogsManager.info("Test Case: "+ result.getName() + "Failed");
    }

    public void onTestSkipped (ITestResult result) {
        LogsManager.info("Test Case: "+ result.getName() + "Skipped");
    }

    private void cleanTestOutputDirectories() {
        FileUtils.cleanDir(AllureConstants.RESULTS_FOLDER.toFile());
        FileUtils.cleanDir(new File(ScreenshotsManager.SCREENSHOTS_PATH));
        FileUtils.cleanDir(new File(ScreenRecordManager.RECORDINGS_PATH));
        FileUtils.cleanDir(new File(LogsManager.LOGS_PATH));
    }
    private void createTestOutputDirectories() {
        //Implement logic to create test output directories
        FileUtils.createDir(ScreenshotsManager.SCREENSHOTS_PATH);
        FileUtils.createDir(ScreenRecordManager.RECORDINGS_PATH);
    }

}
