package base;

import com.aventstack.extentreports.ExtentReports;
import config.ConfigReader;
import drivers.DriverFactory;
import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.ExtentManager;

import java.lang.reflect.Method;

/**
 * Base class for all test classes. Owns the WebDriver lifecycle (create/quit) and the
   * ExtentReports lifecycle. Test classes only call getDriver() - they never touch
   * DriverFactory/DriverManager directly.
   */
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);
      protected static ExtentReports extentReports;

    @BeforeSuite(alwaysRun = true)
      public void setUpSuite() {
                log.info("========== TEST SUITE STARTED ==========");
                extentReports = ExtentManager.getInstance();
      }

    @BeforeMethod(alwaysRun = true)
      public void setUp(Method method) {
                log.info("---------- Starting test: {} ----------", method.getName());

          // -Dbrowser system property (e.g. mvn test -Dbrowser=firefox) overrides config.properties
          String browser = System.getProperty("browser", ConfigReader.getBrowser());
                WebDriver driver = DriverFactory.createDriver(browser);
                DriverManager.setDriver(driver);

          driver.get(ConfigReader.getBaseUrl());
                log.info("Navigated to base URL: {}", ConfigReader.getBaseUrl());
      }

    @AfterMethod(alwaysRun = true)
      public void tearDown(ITestResult result) {
                String testName = result.getMethod().getMethodName();

          if (result.getStatus() == ITestResult.SUCCESS) {
                        log.info("Test PASSED: {}", testName);
          } else if (result.getStatus() == ITestResult.FAILURE) {
                        log.error("Test FAILED: {}", testName, result.getThrowable());
          } else if (result.getStatus() == ITestResult.SKIP) {
                        log.warn("Test SKIPPED: {}", testName);
          }

          // Screenshot-on-failure is handled centrally in listeners.TestListener.onTestFailure,
          // since ITestListener still has access to the driver at the exact moment of
          // failure, before this method tears it down.
          DriverManager.quitDriver();
                log.info("---------- Finished test: {} ----------", testName);
      }

    @AfterSuite(alwaysRun = true)
      public void tearDownSuite() {
                if (extentReports != null) {
                              extentReports.flush();
                }
                log.info("========== TEST SUITE FINISHED ==========");
      }

    protected WebDriver getDriver() {
              return DriverManager.getDriver();
    }
}
