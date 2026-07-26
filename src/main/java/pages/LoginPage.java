package pages;

import helpers.ElementActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * NOTE: Locators below are best-effort, based on direct visual observation of the
   * rendered login form rather than live DOM inspection - the application auto-redirects
   * an authenticated session away from /login, and logging out to inspect the raw HTML
   * was intentionally skipped per project decision. Verify with DevTools on first failure.
   */
public class LoginPage extends BasePage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);
      private final ElementActions actions;

    private final By emailInput = By.cssSelector("input[type='email'], input[placeholder*='mail']");
      private final By passwordInput = By.cssSelector("input[type='password']");
      private final By loginButton = By.xpath("//button[normalize-space()='Login']");

    public LoginPage(WebDriver driver) {
              super(driver);
              this.actions = new ElementActions(driver);
    }

    public void login(String username, String password) {
              log.info("Logging in with username: {}", username);
              actions.type(emailInput, username);
              actions.type(passwordInput, password);
              actions.click(loginButton);
    }
}
