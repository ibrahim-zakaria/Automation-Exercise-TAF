package com.AutomationExercise.utils.dataReader;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class AllureUtils {
    public static void clearAllureResources()
    {
        FileUtils.deleteQuietly(new File("test-outputs/allure-results"));
    }
}
