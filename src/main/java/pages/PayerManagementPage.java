package pages;

import helpers.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.components.ConfirmDialog;
import pages.components.PayerFormDialog;
import utils.WaitUtils;

import java.util.List;

public class PayerManagementPage extends BasePage {

    private final ElementActions actions;

    private final By searchInput = By.cssSelector("input.pbm-search__input");
      private final By addPayerButton = By.cssSelector("button.pbm-btn-add");
      private final By payerCards = By.tagName("pbm-payer-card");
      private final By cardTitle = By.cssSelector("h3.pbm-card-viewer__title");
      private final By editButton = By.cssSelector("button.pbm-btn-neutral");
      private final By deleteButton = By.cssSelector("button.pbm-btn-danger-outline");

    public PayerManagementPage(WebDriver driver) {
              super(driver);
              this.actions = new ElementActions(driver);
    }

    public PayerFormDialog openAddPayerDialog() {
              actions.click(addPayerButton);
              return new PayerFormDialog(driver);
    }

    public void searchPayer(String name) {
              actions.type(searchInput, name);
    }

    public boolean isPayerDisplayed(String name) {
              return !findCardsByName(name).isEmpty();
    }

    public PayerFormDialog openEditPayerDialog(String name) {
              WebElement card = findCardsByName(name).get(0);
              card.findElement(editButton).click();
              return new PayerFormDialog(driver);
    }

    public ConfirmDialog clickDeletePayer(String name) {
              WebElement card = findCardsByName(name).get(0);
              card.findElement(deleteButton).click();
              return new ConfirmDialog(driver);
    }

    private List<WebElement> findCardsByName(String name) {
              WaitUtils.waitForVisible(driver, searchInput);
              List<WebElement> cards = driver.findElements(payerCards);
              return cards.stream()
                                .filter(card -> {
                                                      List<WebElement> titles = card.findElements(cardTitle);
                                                      return !titles.isEmpty() && titles.get(0).getText().trim().equalsIgnoreCase(name.trim());
                                })
                                .toList();
    }
}
