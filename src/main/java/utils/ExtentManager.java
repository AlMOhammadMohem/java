package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import config.ConfigReader;

import java.time.format.DateTimeFormatter;

public class ExtentManager {

    private static ExtentReports extentReports;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
              if (extentReports == null) {
                            String timestamp = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                            String reportPath = ConfigReader.getReportPath() + "ExtentReport_" + timestamp + ".html";

                  ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
                            sparkReporter.config().setDocumentTitle("NeoRx Payer Management - Automation Report");
                            sparkReporter.config().setReportName("Selenium/TestNG Execution Report");

                  extentReports = new ExtentReports();
                            extentReports.attachReporter(sparkReporter);
                            extentReports.setSystemInfo("Application", "NeoRx Payer Management");
                            extentReports.setSystemInfo("Browser", ConfigReader.getBrowser());
              }
              return extentReports;
    }
}
