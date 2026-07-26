package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentManager;
import utils.ScreenshotUtils;

/**
 * Bridges TestNG lifecycle events to Extent Reports and Log4j2. Registered via
   * testng.xml <listeners> so it applies to every test class without per-class boilerplate.
   */
public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    // ThreadLocal so parallel test execution (if enabled later) doesn't cross-contaminate report nodes
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentTest getTest() {
              return extentTest.get();
    }

    @Override
      public void onTestStart(ITestResult result) {
                String description = result.getMethod().getDescription() != null
                                  ? result.getMethod().getDescription()
                                  : result.getMethod().getMethodName();
                ExtentTest test = ExtentManager.getInstance().createTest(result.getMethod().getMethodName(), description);
                extentTest.set(test);
                log.info("Test started: {}", result.getMethod().getMethodName());
      }

    @Override
      public void onTestSuccess(ITestResult result) {
                extentTest.get().log(Status.PASS, "Test passed");
                log.info("Test passed: {}", result.getMethod().getMethodName());
      }

    @Override
      public void onTestFailure(ITestResult result) {
                String testName = result.getMethod().getMethodName();
                log.error("Test failed: {}", testName, result.getThrowable());

          WebDriver driver = DriverManager.getDriver();
                ExtentTest node = extentTest.get();

          if (driver != null) {
                        String screenshotPath = ScreenshotUtils.captureScreenshot(driver, testName);
                        node.fail(result.getThrowable());
                        try {
                                          node.addScreenCaptureFromPath(screenshotPath, testName);
                        } catch (Exception e) {
                                          log.warn("Could not attach screenshot to report for test: {}", testName, e);
                        }
          } else {
                        node.fail(result.getThrowable());
          }
      }

    @Override
      public void onTestSkipped(ITestResult result) {
                String reason = result.getThrowable() != null ? result.getThrowable().getMessage() : "dependency failure";
                extentTest.get().log(Status.SKIP, "Test skipped: " + reason);
                log.warn("Test skipped: {} - {}", result.getMethod().getMethodName(), reason);
      }

    @Override
      public void onFinish(ITestContext context) {
                ExtentManager.getInstance().flush();
                log.info("Suite finished. Passed={}, Failed={}, Skipped={}",
                                         context.getPassedTests().size(),
                                         context.getFailedTests().size(),
                                         context.getSkippedTests().size());
      }
}
