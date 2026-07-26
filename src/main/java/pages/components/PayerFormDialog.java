package pages.components;

import helpers.ElementActions;
import models.Payer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

/**
 * Wraps the 3-step Add/Edit Payer wizard. The DOM is identical for both create and
   * edit modes, so this single component serves both flows: createPayer() drives all
   * three steps, while editNamesOnly() only touches the name fields on step 1 and then
   * clicks through steps 2-3 unchanged, since editing only requires updating names.
   */
public class PayerFormDialog {

  private final WebDriver driver;
    private final ElementActions actions;

  private final By englishNameInput = By.cssSelector("input.payer-dialog__input[dir='ltr']");
    private final By arabicNameInput = By.cssSelector("input.payer-dialog__input[dir='rtl']");
    private final By payerTypeDropdown = By.cssSelector("p-select.payer-dialog__input");
    private final By emailInput = By.cssSelector("input.payer-dialog__input[placeholder='e.g. email@mail.com']");
    private final By phoneInput = By.cssSelector("input.payer-dialog__input[placeholder='+9665XXXXXXX']");
    // Country, City, Preferred Language and Preferred Contact Method are all rendered as
  // <p-select class="payer-dialog__input"> on step 2, exactly like payerTypeDropdown on
  // step 1, so a plain class-based CSS selector cannot tell them apart (there would be
  // several matches on step 2 alone). Each field on this form is wrapped in a
  // <div class="payer-dialog__field"> together with its <label>, so we scope an XPath by
  // that label text instead. Verified against the live DOM on 27/07/2026.
  private final By licenseNumberInput = By.xpath(
          "//div[contains(@class,'payer-dialog__field')][.//label[normalize-space()='License Number']]//input");
    private final By countryDropdown = By.xpath(
            "//div[contains(@class,'payer-dialog__field')][.//label[normalize-space()='Country']]//p-select");
    private final By cityDropdown = By.xpath(
            "//div[contains(@class,'payer-dialog__field')][.//label[normalize-space()='City']]//p-select");
    private final By preferredLanguageDropdown = By.xpath(
            "//div[contains(@class,'payer-dialog__field')][.//label[normalize-space()='Preferred Language']]//p-select");
    private final By preferredContactMethodDropdown = By.xpath(
            "//div[contains(@class,'payer-dialog__field')][.//label[normalize-space()='Preferred Contact Method']]//p-select");
    private final By dateInputs = By.cssSelector("input.p-datepicker-input");
    private final By todayCell = By.cssSelector("td.p-datepicker-today span.p-datepicker-day");
    private final By primaryButton = By.cssSelector("button.pbm-btn-primary");
    private final By cancelButton = By.xpath("//button[contains(@class,'pbm-btn-neutral') and normalize-space()='Cancel']");
    private final By fieldError = By.cssSelector(".pbm-field-error");

  public PayerFormDialog(WebDriver driver) {
        this.driver = driver;
        this.actions = new ElementActions(driver);
  }

  /**
   * Drives the full 3-step wizard to create a brand new payer.
     */
  public void createPayer(Payer payer) {
        // Step 1: Basic Information
      actions.type(englishNameInput, payer.getEnglishName());
        actions.type(arabicNameInput, payer.getArabicName());
        actions.selectPrimeNgOption(payerTypeDropdown, payer.getPayerType());
        actions.click(primaryButton);

      // Step 2: Contact Information
      // NOTE: License Number, Country, City, Preferred Language and Preferred Contact
      // Method were added here after live QA on 27/07/2026 confirmed the wizard cannot
      // advance past step 2 without them (they are required fields in the live app but
      // were previously missing from this method). During that same QA pass, the
      // Country/City lookup data was observed to be intermittently empty in this
      // environment - if that recurs, selectPrimeNgOption() below will time out waiting
      // for a matching option, which should be reported to the backend team as an
      // environment/data issue rather than a locator problem.
      actions.type(emailInput, buildEmail(payer));
        actions.type(phoneInput, "+966500000000");
        actions.type(licenseNumberInput, "LIC-" + System.currentTimeMillis());
        actions.selectPrimeNgOption(countryDropdown, payer.getCountry());
        actions.selectPrimeNgOption(cityDropdown, payer.getCity());
        actions.selectPrimeNgOption(preferredLanguageDropdown, payer.getPreferredLanguage());
        actions.selectPrimeNgOption(preferredContactMethodDropdown, payer.getPreferredContactMethod());
        actions.click(primaryButton);

      // Step 3: Effective Period - select today's date for both Effective and Expiry
      selectTodayForAllDatePickers();
        actions.click(primaryButton);
  }

  /**
   * Edit flow only needs to update the English/Arabic names on step 1, then click
     * through the remaining steps unchanged (the wizard requires visiting every step
                                                 * before Save is enabled, even if no other fields are modified).
     *
     * Known issue (found in live QA on 27/07/2026): the app has been observed to
     * intermittently fail to preload step 2's Country/City values when opening Edit,
     * which then blocks Save with "required field" errors, and separately to reject a
     * valid Save with "This payer has been modified since it was loaded" even on a
     * freshly reloaded record. Both are application-side bugs, not issues with this
     * method or its locators - call hasValidationErrors() after this method returns
     * and report failures to the dev team rather than treating them as a test defect.
     */
    public void editNamesOnly(String updatedEnglishName, String updatedArabicName) {
      actions.type(englishNameInput, updatedEnglishName);
      actions.type(arabicNameInput, updatedArabicName);
      actions.click(primaryButton);
      actions.click(primaryButton);
      actions.click(primaryButton);
}

  public boolean hasValidationErrors() {
        return !driver.findElements(fieldError).isEmpty();
  }

  public void cancel() {
        actions.click(cancelButton);
  }

  private void selectTodayForAllDatePickers() {
        for (int i = 0; i < driver.findElements(dateInputs).size(); i++) {
                driver.findElements(dateInputs).get(i).click();
                WaitUtils.waitForClickable(driver, todayCell).click();
        }
  }

  private String buildEmail(Payer payer) {
        String base = payer.getEnglishName().toLowerCase().replaceAll("[^a-z0-9]", "");
    return base + "@example.com";
}
}
