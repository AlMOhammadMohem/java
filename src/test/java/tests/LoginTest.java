package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenshotUtils;

public class LoginTest extends BaseTest {

    @Test(description = "Verify user can log in with valid credentials and lands on the dashboard")
      public void testValidLogin() {
                log.info("Executing testValidLogin");

          LoginPage loginPage = new LoginPage(getDriver());
                loginPage.login(ConfigReader.getUsername(), ConfigReader.getPassword());

          DashboardPage dashboardPage = new DashboardPage(getDriver());
                boolean isLoggedIn = dashboardPage.isLoaded();

          if (!isLoggedIn) {
                        ScreenshotUtils.captureScreenshot(getDriver(), "testValidLogin_failure");
                        log.error("Login verification failed - dashboard did not load as expected");
          }

          Assert.assertTrue(isLoggedIn, "Login was not successful - dashboard did not load");
                log.info("Login verified successfully");
      }
}
