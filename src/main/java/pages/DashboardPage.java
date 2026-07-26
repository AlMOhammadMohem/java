package pages;

import helpers.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class DashboardPage extends BasePage {

    private final ElementActions actions;
      private final By payerMgmtLink = By.cssSelector("a[href='/payer-management']");

    public DashboardPage(WebDriver driver) {
              super(driver);
              this.actions = new ElementActions(driver);
    }

    public void navigateToPayerManagement() {
              actions.click(payerMgmtLink);
              WaitUtils.waitForVisible(driver, By.cssSelector("button.pbm-btn-add"));
    }

    public boolean isLoaded() {
              try {
                            WaitUtils.waitForVisible(driver, payerMgmtLink);
                            return driver.findElement(payerMgmtLink).isDisplayed();
              } catch (Exception e) {
                            return false;
              }
    }
}
