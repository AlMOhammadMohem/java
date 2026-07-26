package tests;

import base.BaseTest;
import config.ConfigReader;
import models.Payer;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.PayerManagementPage;
import pages.components.ConfirmDialog;
import utils.WaitUtils;

public class PayerDeleteTest extends BaseTest {

    @Test(description = "Delete the second payer created in PayerLifecycleTest")
      public void testDeleteSecondPayer() throws InterruptedException {
                log.info("Executing testDeleteSecondPayer");

          Payer secondPayer = PayerLifecycleTest.secondPayer;
                Assert.assertNotNull(secondPayer,
                                                     "Second payer data unavailable - PayerLifecycleTest.testCreateSecondPayer must run first and pass");

          new LoginPage(getDriver()).login(ConfigReader.getUsername(), ConfigReader.getPassword());
                new DashboardPage(getDriver()).navigateToPayerManagement();

          PayerManagementPage payerManagementPage = new PayerManagementPage(getDriver());

          // Intentional demonstration delay, requested for this scenario only, so the
          // deletion flow can be watched step by step. Sourced from config.properties
          // (app.slowmo.ms) rather than hardcoded, per the "no hardcoded values" rule -
          // this is a deliberate visual pacing feature, not a synchronization mechanism.
          long slowMoMs = ConfigReader.getSlowMoDelayMs();
                log.info("Scenario 5 demonstration mode: pausing {} ms between actions", slowMoMs);

          payerManagementPage.searchPayer(secondPayer.getEnglishName());
                Thread.sleep(slowMoMs);

          ConfirmDialog confirmDialog = payerManagementPage.clickDeletePayer(secondPayer.getEnglishName());
                Thread.sleep(slowMoMs);

          confirmDialog.confirmDelete();
                Thread.sleep(slowMoMs);

          Assert.assertTrue(WaitUtils.waitForToastContaining(getDriver(), "success"),
                                            "Success toast was not displayed after deleting the second payer");

          payerManagementPage.searchPayer(secondPayer.getEnglishName());
                Assert.assertFalse(payerManagementPage.isPayerDisplayed(secondPayer.getEnglishName()),
                                                   "Second payer still exists in the grid after deletion");

          log.info("Second payer deleted and verified successfully: {}", secondPayer.getEnglishName());
      }
}
