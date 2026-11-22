package com.AutomationExercise.utils.actions;

import com.AutomationExercise.utils.wait.WaitManager;
import com.AutomationExercise.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;

public class AlertActions {
    private final WebDriver driver;
    private final WaitManager waitManager;

    public AlertActions(WebDriver driver){
        this.driver = driver;
        waitManager = new WaitManager(driver);
    }

     public void acceptAlert(){
          waitManager.fluentWait().until(d->{
             try{
                 d.switchTo().alert().accept();
                 return true;
             }
             catch(Exception e){
                 LogsManager.error("Alert Accept Failed", e.getMessage());
                 return false;
             }
         });
     }
     /*
     *
      */
     public void dismissAlert(){
         waitManager.fluentWait().until(d->{
             try{
                 d.switchTo().alert().dismiss();
                 return true;
             }catch(Exception e){
                 LogsManager.error("Alert Dismiss Failed", e.getMessage());
                 return false;
             }
         });
     }
     /**
     * Gets text form alert
     * @return the text of the Alert
     * */
     public String getAlertText(){
         return waitManager.fluentWait().until(d->{
             try{
                 String text =  d.switchTo().alert().getText();
                 return !text.isEmpty()? text : null;
             }catch(Exception e){
                 LogsManager.error("Alert Get Text Failed", e.getMessage());
                 return null;
             }
         });
     }
    /**
     * Sets text form alert
     * @param text the text to set in the Alert
     * */
    public void setAlertText(String text){
        waitManager.fluentWait().until(d->{
            try{
                d.switchTo().alert().sendKeys(text);
                return true;
            }catch(Exception e){
                LogsManager.error("Alert Set Text Failed", e.getMessage());
                return false;
            }
        });
    }
}
