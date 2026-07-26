package pages.components;

import helpers.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

/**
 * Wraps the generic confirmation dialog used for destructive actions (e.g. Delete Payer).
   */
public class ConfirmDialog {

    private final WebDriver driver;
      private final ElementActions actions;

    private final By title = By.cssSelector(".pbm-dialog__title");
      private final By confirmButton = By.cssSelector(".pbm-dialog__btn:not(.pbm-dialog__btn--secondary)");
      private final By cancelButton = By.cssSelector(".pbm-dialog__btn--secondary");

    public ConfirmDialog(WebDriver driver) {
              this.driver = driver;
              this.actions = new ElementActions(driver);
    }

    public String getTitleText() {
              return WaitUtils.waitForVisible(driver, title).getText();
    }

    public void confirmDelete() {
              actions.click(confirmButton);
    }

    public void cancelDelete() {
              actions.click(cancelButton);
    }
}
