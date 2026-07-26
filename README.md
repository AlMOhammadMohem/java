# NeoRx Payer Management - Selenium Automation Framework

Enterprise-style Selenium WebDriver + TestNG + Maven framework automating the Payer Management module of the NeoRx application (login, create, edit, delete).

## Tech Stack

Java 17 | Selenium WebDriver | TestNG | Maven | WebDriverManager | Extent Reports | Log4j2 | DataFaker + Commons Lang3

## Project Structure

- `src/main/java/config` - ConfigReader: loads config.properties once, exposes typed getters.
- - `src/main/java/constants` - AppConstants: static values shared across the framework (timeouts, keywords).
  - - `src/main/java/drivers` - DriverFactory (creates Chrome/Edge/Firefox instances via WebDriverManager) and DriverManager (ThreadLocal WebDriver so parallel runs never share a driver).
    - - `src/main/java/utils` - WaitUtils (explicit/fluent waits), ScreenshotUtils, RandomDataGenerator, ExtentManager.
      - - `src/main/java/helpers` - ElementActions: reusable low-level interactions (click, type, PrimeNG dropdown selection) with built-in StaleElementReferenceException retry.
        - - `src/main/java/models` - Payer POJO used to pass generated test data between pages and tests.
          - - `src/main/java/pages` - Page Objects (LoginPage, DashboardPage, PayerManagementPage) and pages/components for reusable dialog components (PayerFormDialog, ConfirmDialog) shared between create/edit flows.
            - - `src/test/java/base` - BaseTest: WebDriver and ExtentReports lifecycle.
              - - `src/test/java/listeners` - TestListener: TestNG-to-Extent/Log4j2 bridge, screenshot-on-failure.
                - - `src/test/java/tests` - LoginTest, PayerLifecycleTest, PayerDeleteTest.
                  - - `reports/`, `screenshots/`, `logs/` - generated output (git-ignored).
                    - - `test-data/reference-data.properties` - static reference values (randomly generated data lives only in memory per run).
                     
                      - ## Configuration (config.properties)
                     
                      - All environment-specific values live here: base.url, app.username, app.password, browser, explicit.wait.seconds, app.slowmo.ms, report.path, screenshot.path. Override the browser per run without touching code: mvn test -Dbrowser=firefox
                     
                      - ## Running the Framework
                     
                      - Install dependencies:
                     
                      - ```
                        mvn clean install
                        ```

                        Run the full suite (executes testng.xml: Login, Create Payer 1, Edit Payer 1, Create Payer 2, Delete Payer 2):

                        ```
                        mvn clean test
                        ```

                        Run a single test class:

                        ```
                        mvn test -Dtest=LoginTest
                        ```

                        Run a single test method:

                        ```
                        mvn test -Dtest=PayerLifecycleTest#testCreateFirstPayer
                        ```

                        Run with a specific browser:

                        ```
                        mvn test -Dbrowser=edge
                        ```

                        Generate and open the Extent report: the report is generated automatically at the end of every run at reports/ExtentReport_TIMESTAMP.html. Open it directly in any browser.

                        Run inside IntelliJ IDEA: right-click testng.xml and choose Run, or right-click any test class/method and choose Run. Make sure the Maven project has been imported/reloaded first so dependencies resolve.

                        Run from the terminal:

                        ```
                        cd selenium-framework
                        mvn clean test
                        ```

                        Logs stream live to the console and are also written to logs/automation.log.

                        ## Key Design Decisions

                        Locator strategy: CSS class selectors are used almost everywhere instead of XPath, since the application exposes no usable id attributes. Where two fields share an identical class (English/Arabic name inputs), the dir=ltr/dir=rtl attribute disambiguates them instead of a fragile text-based XPath.

                        No hardcoded waits: every wait is an explicit WebDriverWait or a short-interval FluentWait, used specifically for toast notifications which can disappear from the DOM within 1-2 seconds. The single exception is the intentional, config-driven demonstration delay in PayerDeleteTest (Scenario 5 only), explicitly requested for visual observation and not used for synchronization.

                        ThreadLocal WebDriver: DriverManager isolates the driver per thread so the suite is safe to parallelize later without code changes.

                        Shared dialog components: PayerFormDialog is used by both the create and edit flows since the underlying wizard DOM is identical in both modes; only the data entered differs.

                        Login locators are best-effort: the app auto-redirects an authenticated session away from /login, so LoginPage locators were built from direct visual observation rather than live DOM inspection. If LoginTest fails on first run, inspect the live login form via DevTools and adjust LoginPage accordingly.

                        Cross-class data sharing: PayerLifecycleTest.secondPayer is a public static field read by PayerDeleteTest in the same suite run, avoiding an external file or database just to pass test data between two classes in a single JVM execution.

                        ## Extending the Framework

                        Add new Page Objects under pages/, new reusable components under pages/components/, and new test classes under tests/, then register them in testng.xml. Keep all locators inside Page Objects/components only - test classes should never contain a locator.
