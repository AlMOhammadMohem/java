package utils;

import config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    public static String captureScreenshot(WebDriver driver, String testName) {
              String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
              String fileName = testName + "_" + timestamp + ".png";
              String destinationPath = ConfigReader.getScreenshotPath() + fileName;

          try {
                        File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                        File destinationFile = new File(destinationPath);
                        FileUtils.copyFile(sourceFile, destinationFile);
                        log.info("Screenshot captured: {}", destinationPath);
          } catch (IOException e) {
                        log.error("Failed to capture screenshot for test: {}", testName, e);
          }

          return destinationPath;
    }
}
