package drivers;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
              WebDriver driver;

          switch (browser.trim().toLowerCase()) {
            case "firefox":
                              WebDriverManager.firefoxdriver().setup();
                              driver = new FirefoxDriver(new FirefoxOptions());
                              break;
            case "edge":
                              WebDriverManager.edgedriver().setup();
                              driver = new EdgeDriver(new EdgeOptions());
                              break;
            case "chrome":
            default:
                              WebDriverManager.chromedriver().setup();
                              ChromeOptions options = new ChromeOptions();
                              options.addArguments("--start-maximized");
                              driver = new ChromeDriver(options);
                              break;
          }

          // Implicit wait intentionally left at 0 - mixing implicit and explicit waits
          // causes unpredictable effective timeouts; this framework relies exclusively
          // on explicit/fluent waits (see utils.WaitUtils).
          driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWaitSeconds()));
              driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeoutSeconds()));
              driver.manage().window().maximize();

          return driver;
    }
}
