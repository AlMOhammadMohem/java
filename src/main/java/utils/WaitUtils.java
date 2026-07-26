package utils;

import config.ConfigReader;
import constants.AppConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

public class WaitUtils {

    private WaitUtils() {
    }

    public static WebElement waitForVisible(WebDriver driver, By locator) {
              return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()))
                                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
              return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()))
                                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForInvisible(WebDriver driver, By locator) {
              return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()))
                                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Toast notifications in this app are transient and can disappear from the DOM
       * within ~1-2 seconds, faster than the default explicit wait polling interval
       * would reliably catch. A short-interval FluentWait is used instead so we don't
       * miss a toast that appears and disappears between polls.
       */
    public static boolean waitForToastContaining(WebDriver driver, String expectedTextFragment) {
              Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                                .withTimeout(Duration.ofSeconds(AppConstants.TOAST_TIMEOUT_SECONDS))
                                .pollingEvery(Duration.ofMillis(AppConstants.TOAST_POLL_MILLIS))
                                .ignoring(NoSuchElementException.class)
                                .ignoring(StaleElementReferenceException.class);

          try {
                        return fluentWait.until((Function<WebDriver, Boolean>) drv -> {
                                          WebElement toast = drv.findElement(By.cssSelector("p-toast"));
                                          String text = toast.getText();
                                          return text != null && text.toLowerCase().contains(expectedTextFragment.toLowerCase());
                        });
          } catch (Exception e) {
                        return false;
          }
    }
}
