package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
              try (FileInputStream fis = new FileInputStream("config.properties")) {
                            properties.load(fis);
              } catch (IOException e) {
                            throw new RuntimeException("Unable to load config.properties from project root", e);
              }
    }

    private ConfigReader() {
    }

    public static String getBaseUrl() {
              return properties.getProperty("base.url");
    }

    public static String getUsername() {
              return properties.getProperty("app.username");
    }

    public static String getPassword() {
              return properties.getProperty("app.password");
    }

    public static String getBrowser() {
              return properties.getProperty("browser", "chrome");
    }

    public static int getExplicitWaitSeconds() {
              return Integer.parseInt(properties.getProperty("explicit.wait.seconds", "15"));
    }

    public static int getImplicitWaitSeconds() {
              return Integer.parseInt(properties.getProperty("implicit.wait.seconds", "0"));
    }

    public static int getPageLoadTimeoutSeconds() {
              return Integer.parseInt(properties.getProperty("page.load.timeout.seconds", "30"));
    }

    public static long getSlowMoDelayMs() {
              return Long.parseLong(properties.getProperty("app.slowmo.ms", "0"));
    }

    public static String getReportPath() {
              return properties.getProperty("report.path", "reports/");
    }

    public static String getScreenshotPath() {
              return properties.getProperty("screenshot.path", "screenshots/");
    }
}
