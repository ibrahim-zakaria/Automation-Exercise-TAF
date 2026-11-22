package com.AutomationExercise.utils.report;

import com.AutomationExercise.utils.system.OSUtils;
import com.AutomationExercise.utils.system.TerminalUtils;
import com.AutomationExercise.utils.logs.LogsManager;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AllureBinaryManager {
        private static final String STATIC_VERSION = "2.34.1";
  /*  private static class LazyHolder{
       static final String VERSION = resolveVersion();
        private static String resolveVersion(){
            try{
                String url = Jsoup.connect("https://github.com/allure-framework/allure2/releases/latest").followRedirects(true).execute().url().toString();
                return url.split("/tag/")[1];
            } catch (Exception e) {
                LogsManager.error("Failed to resolve Allure version", e.getMessage());
                throw new IllegalStateException("Can't resolve Allure version", e);
            }
        }
    }*/

    private static Path downloadZip (String version) {
        try {
            String url = AllureConstants.ALLURE_ZIP_BASE_URL + version + "/allure-commandline-" + version + ".zip";
            System.out.println("****************************************** \n url:  "+ url +"\nversion: " + version + "\n***********************************************");
            Path zipFile = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version + ".zip");
            if (!Files.exists(zipFile)) {
                Files.createDirectories(AllureConstants.EXTRACTION_DIR);
                try (BufferedInputStream in = new BufferedInputStream(new URI(url).toURL().openStream());
                     OutputStream out = Files.newOutputStream(zipFile)) {
                    in.transferTo(out);
                } catch (Exception e) {
                    LogsManager.error("Invalid URL for Allure download: ", e.getMessage());
                }
            }
            return zipFile;
        } catch (Exception e) {
            LogsManager.error("Error downloading Allure zip file", e.getMessage());
            return Paths.get("");
        }

    }



//Extract ZIP file for Allure
    private static void extractZip(Path zipPath) {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path filePath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), File.separator, entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories (filePath);
                } else {
                    Files.createDirectories (filePath.getParent());
                    Files.copy(zipInputStream, filePath);
                }
            }
        } catch (Exception e) {
            LogsManager.error("Error extracting Allure zip file", e.getMessage());
        }

    }

    public static void downloadAndExtract() {
        try {
            //String version = LazyHolder.VERSION;
            String version = STATIC_VERSION;
            Path extractionDir = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version);
                //c:\Users\hussi\.m2\repository\allure\allure-2.34.1
            if (Files.exists(extractionDir)) {
                LogsManager.info("Allure binaries already exist.");
                return;
            }

            // Give execute permissions to the binary if not on Windows
            if (!OSUtils.getCurrentOS().equals(OSUtils.OSType.WINDOWS)) {
                 TerminalUtils.executeTerminalCommand("chmod", "u+x", AllureConstants.USER_DIR.toString());
            }
            Path zipPath = downloadZip(version);
            extractZip(zipPath);
            LogsManager.info("Allure binaries downloaded and extracted.");

            // Give execute permissions to the binary if not on Windows
            if (!OSUtils.getCurrentOS().equals(OSUtils.OSType.WINDOWS)) {
                TerminalUtils.executeTerminalCommand("chmod", "u+x", getExecutable().toString());
            }

            // clean up the zip file after extraction
            Files.deleteIfExists(
                    Files.list(AllureConstants.EXTRACTION_DIR)
                            .filter(p -> p.toString().endsWith(".zip"))
                            .findFirst()
                            .orElse(null)
            );
            
            }catch (Exception e){
                LogsManager.error("Error downloading or extraction binaries", e.getMessage());
        }
    }



    public static Path getExecutable() {
        //String version = LazyHolder.VERSION;
        String version = STATIC_VERSION;
        Path binaryPath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version, "bin", "allure");
        return OSUtils.getCurrentOS() == OSUtils.OSType.WINDOWS
                ? binaryPath.resolveSibling(binaryPath.getFileName() + ".bat")
                : binaryPath;
    }
}
