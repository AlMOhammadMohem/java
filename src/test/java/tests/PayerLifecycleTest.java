package tests;

import base.BaseTest;
import config.ConfigReader;
import models.Payer;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.PayerManagementPage;
import pages.components.PayerFormDialog;
import utils.RandomDataGenerator;
import utils.WaitUtils;

public class PayerLifecycleTest extends BaseTest {

    // Instance field: shared across @Test methods in this class because TestNG reuses
    // a single instance of the class for all its @Test methods.
    private Payer firstPayer;

    // Static: read by PayerDeleteTest (a different test class) in the same JVM/suite run.
    public static Payer secondPayer;

    private void loginAndOpenPayerManagement() {
              new LoginPage(getDriver()).login(ConfigReader.getUsername(), ConfigReader.getPassword());
              new DashboardPage(getDriver()).navigateToPayerManagement();
    }

    @Test(priority = 1, description = "Create the first payer using randomly generated data")
      public void testCreateFirstPayer() {
                log.info("Executing testCreateFirstPayer");
                loginAndOpenPayerManagement();

          PayerManagementPage payerManagementPage = new PayerManagementPage(getDriver());
                firstPayer = RandomDataGenerator.generatePayer();
                log.info("Generated first payer: {}", firstPayer);

          PayerFormDialog formDialog = payerManagementPage.openAddPayerDialog();
                formDialog.createPayer(firstPayer);

          Assert.assertTrue(WaitUtils.waitForToastContaining(getDriver(), "success"),
                                            "Success toast was not displayed after creating the first payer");

          payerManagementPage.searchPayer(firstPayer.getEnglishName());
                Assert.assertTrue(payerManagementPage.isPayerDisplayed(firstPayer.getEnglishName()),
                                                  "First payer was not found in the list after creation");

          log.info("First payer created successfully: {}", firstPayer.getEnglishName());
      }

    @Test(priority = 2, dependsOnMethods = "testCreateFirstPayer",
                      description = "Edit the first payer's English and Arabic names")
      public void testEditFirstPayer() {
                log.info("Executing testEditFirstPayer");

          PayerManagementPage payerManagementPage = new PayerManagementPage(getDriver());
                payerManagementPage.searchPayer(firstPayer.getEnglishName());

          String updatedEnglishName = firstPayer.getEnglishName() + " Updated";
                String updatedArabicName = firstPayer.getArabicName() + " \u0645\u062D\u062F\u062B";

          PayerFormDialog formDialog = payerManagementPage.openEditPayerDialog(firstPayer.getEnglishName());
                formDialog.editNamesOnly(updatedEnglishName, updatedArabicName);

          Assert.assertTrue(WaitUtils.waitForToastContaining(getDriver(), "success"),
                                            "Success toast was not displayed after editing the first payer");

          payerManagementPage.searchPayer(updatedEnglishName);
                Assert.assertTrue(payerManagementPage.isPayerDisplayed(updatedEnglishName),
                                                  "Updated payer name was not found in the list after editing");

          firstPayer.setEnglishName(updatedEnglishName);
                firstPayer.setArabicName(updatedArabicName);
                log.info("First payer updated successfully: {}", updatedEnglishName);
      }

    @Test(priority = 3, dependsOnMethods = "testEditFirstPayer",
                      description = "Create a second payer using different randomly generated data")
      public void testCreateSecondPayer() {
                log.info("Executing testCreateSecondPayer");

          PayerManagementPage payerManagementPage = new PayerManagementPage(getDriver());
                secondPayer = RandomDataGenerator.generatePayer();
                log.info("Generated second payer: {}", secondPayer);

          PayerFormDialog formDialog = payerManagementPage.openAddPayerDialog();
                formDialog.createPayer(secondPayer);

          Assert.assertTrue(WaitUtils.waitForToastContaining(getDriver(), "success"),
                                            "Success toast was not displayed after creating the second payer");

          payerManagementPage.searchPayer(secondPayer.getEnglishName());
                Assert.assertTrue(payerManagementPage.isPayerDisplayed(secondPayer.getEnglishName()),
                                                  "Second payer was not found in the list after creation");

          log.info("Second payer created and stored for PayerDeleteTest: {}", secondPayer.getEnglishName());
      }
}
