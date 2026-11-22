package com.AutomationExercise.utils.file;

import com.AutomationExercise.utils.logs.LogsManager;

import java.io.File;

/**
 * Override Class
 */
public class FileUtils {
    private static final String USER_DIR = System.getProperty("user.dir") + File.separator;
    public static void createDir(String path) {
        File dir = new File(USER_DIR + path);
        if (!dir.exists()) {
            dir.mkdirs();
            LogsManager.info(String.format("Created directory: %s", dir.getAbsolutePath()));
        }
    }
    public static void renameFile(String oldPath, String newPath) {
        try {
            File oldFile = new File(USER_DIR + oldPath);
            File newFile = new File(USER_DIR + newPath);
            if(oldFile.renameTo(newFile)){
                LogsManager.info(oldPath + " renamed successfully to " + newPath);
            }else {
                LogsManager.info(oldPath + " failed to rename to " + newPath);
            }
        }catch (Exception e){
            LogsManager.info("Error renaming file: ", e.getMessage());
        }
    }

    public static void cleanDir(File file) {
        try {
            org.apache.commons.io.FileUtils.deleteQuietly(file);
            LogsManager.info(file.getAbsolutePath() + " deleted successfully");
            }catch (Exception e){
            LogsManager.info(file.getAbsolutePath() + " failed to delete file");
        }
    }
}
