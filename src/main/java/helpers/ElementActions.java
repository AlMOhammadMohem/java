package helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;
import java.util.function.Supplier;

/**
 * Low-level reusable interactions used by every Page Object. Centralizing click/type
   * here (instead of calling WebElement methods directly from pages) gives us a single
   * place to add StaleElementReferenceException retries and consistent logging.
   */
public class ElementActions {

    private static final Logger log = LogManager.getLogger(ElementActions.class);
      private final WebDriver driver;

    public ElementActions(WebDriver driver) {
              this.driver = driver;
    }

    public void click(By locator) {
              retryOnStale(() -> {
                            WaitUtils.waitForClickable(driver, locator).click();
                            return null;
              });
              log.debug("Clicked element: {}", locator);
    }

      public void type(By locator, String text) {
                retryOnStale(() -> {
                              WebElement element = WaitUtils.waitForVisible(driver, locator);
                              element.clear();
                              element.sendKeys(text);
                              return null;
                });
                log.debug("Typed '{}' into element: {}", text, locator);
      }

    /**
     * Selects an option from a PrimeNG p-select dropdown: opens it, optionally types
       * into the overlay's filter box to narrow results (some overlays have one, some
                                                               * don't), then clicks the matching li.p-select-option by visible text.
       */
    public void selectPrimeNgOption(By dropdownLocator, String optionText) {
              click(dropdownLocator);

          List<WebElement> filterBoxes = driver.findElements(By.cssSelector("input.p-select-filter"));
              if (!filterBoxes.isEmpty() && filterBoxes.get(0).isDisplayed()) {
                            filterBoxes.get(0).sendKeys(optionText);
              }

          By optionLocator = By.xpath("//li[contains(@class,'p-select-option') and normalize-space()='" + optionText + "']");
              WaitUtils.waitForClickable(driver, optionLocator).click();
              log.debug("Selected PrimeNG option '{}' from dropdown: {}", optionText, dropdownLocator);
    }

    public WebElement findButtonByText(String buttonText) {
              By locator = By.xpath("//button[normalize-space()='" + buttonText + "']");
              return WaitUtils.waitForClickable(driver, locator);
    }

    private void retryOnStale(Supplier<Void> action) {
              int attempts = 0;
              while (attempts < 2) {
                            try {
                                              action.get();
                                              return;
                            } catch (StaleElementReferenceException e) {
                                              attempts++;
                                              log.warn("StaleElementReferenceException caught, retrying ({}/2)", attempts);
                            }
              }
    }
}
