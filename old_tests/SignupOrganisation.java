import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class SignupOrganisation {

    // ============================================================
    // VARIABLES
    // ============================================================
    static Playwright playwright;
    static Browser browser;
    static BrowserContext browserContext;
    static Page page;

    // ============================================================
    // LOCATORS
    // ============================================================
    static Locator bookWithUsButton;
    static Locator orgSignupButton;
    static Locator orgName;
    static Locator locationPreference;
    static Locator findAddress;
    static Locator sameBillingDetailsCheckbox;
    static Locator contactFirstName;
    static Locator contactLastName;
    static Locator contactEmail;
    static Locator contactPhone;
    static Locator firstName;
    static Locator lastName;
    static Locator clientPassword;
    static Locator clientConfirmPassword;
    static Locator commPreference;
    static Locator email;
    static Locator phoneNumber;
    static Locator termsConditions;
    static Locator registerButton;
    static Locator confirmMsg;

    // ============================================================
    // METHODS
    // ============================================================

    public static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    public static void initializeLocators() {
        bookWithUsButton = page.locator("a[href='#/register/pre']");
        orgSignupButton = page.locator("a[name='btnOrganization']");
        orgName = page.locator("input[name='business_name']");
        locationPreference = page.locator("select[name='location_pref']");
        findAddress = page.locator("input[name='search_address']").first();
        sameBillingDetailsCheckbox = page.locator("div.mat-checkbox-inner-container").first();
        contactFirstName = page.locator("input[name='cn_first_name']");
        contactLastName = page.locator("input[name='cn_last_name']");
        contactEmail = page.locator("input[name='cn_email']");
        contactPhone = page.locator("input[name='cn_phone']");
        firstName = page.locator("input[name='first_name']");
        lastName = page.locator("input[name='last_name']");
        clientPassword = page.locator("input[name='password']");
        clientConfirmPassword = page.locator("input[name='certainPassword']");
        commPreference = page.locator("select[name='comm_pref']");
        email = page.locator("input[name='email']");
        phoneNumber = page.locator("input[name='mobile']");
        termsConditions = page.locator("div.mat-checkbox-inner-container").nth(1);
        registerButton = page.locator("button[name='register_user']");
        confirmMsg = page.locator("div.notification__text");
    }

    public static void navigateToApplication() {
        page.navigate("http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout");
    }

    public static void clickBookWithUs() {
        bookWithUsButton.waitFor();
        bookWithUsButton.click();
    }

    public static void selectOrganisationSignup() {
        orgSignupButton.click();
    }

    public static void fillOrganisationDetails() {
        orgName.waitFor();
        orgName.click();
        orgName.fill("Autoorg" + generateDate());

        locationPreference.selectOption(new SelectOption().setValue("VIC"));
    }

    public static void fillAddress() {
        findAddress.waitFor();
        findAddress.click();
        findAddress.page().keyboard().type("2 Victory Road, Clarinda VIC, Australia", new Keyboard.TypeOptions().setDelay(100));

        page.locator("text=Clarinda VIC").first().click();

        sameBillingDetailsCheckbox.click();
    }

    public static void fillContactDetails() {
        contactFirstName.waitFor();
        contactFirstName.click();
        contactFirstName.fill("autoorg" + generateDate());

        contactLastName.waitFor();
        contactLastName.click();
        contactLastName.fill("autoorg");

        contactEmail.waitFor();
        contactEmail.click();
        contactEmail.fill("sarvesh" + generateDate() + "@curvetomorrow.com.au");

        contactPhone.waitFor();
        contactPhone.click();
        contactPhone.fill("0488883690");
    }

    public static void fillAccountUserDetails() {
        firstName.waitFor();
        firstName.click();
        firstName.fill("Org+" + generateDate());

        lastName.waitFor();
        lastName.click();
        lastName.fill("Aut0LastName");

        clientPassword.waitFor();
        clientPassword.click();
        clientPassword.fill("Curve@2025");

        clientConfirmPassword.waitFor();
        clientConfirmPassword.click();
        clientConfirmPassword.fill("Curve@2025");

        commPreference.selectOption(new SelectOption().setIndex(2));

        email.waitFor();
        email.click();
        email.fill("sarvesh" + generateDate() + "@curvetomorrow.com.au");

        phoneNumber.waitFor();
        phoneNumber.click();
        phoneNumber.fill("0488883690");
    }

    public static void acceptTermsAndRegister() {
        termsConditions.click();
        registerButton.click();
    }

    public static void verifyRegistration() {
        confirmMsg.waitFor();

        String verificationText = "Thank you for registering with Deaf Connect. We value your business. We will verify your details and be in contact shortly.";
        System.out.println(confirmMsg.textContent().trim());
        Assert.assertTrue(confirmMsg.textContent().contains(verificationText));
        System.out.println("Verified the message");
    }

    public static void tearDownBrowser() {
        page.close();
        browserContext.close();
        playwright.close();
    }

    public static String generateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    // ============================================================
    // MAIN - METHOD CALLS
    // ============================================================
    public static void main(String[] args) throws InterruptedException {

        // Setup
        setupBrowser();
        navigateToApplication();
        initializeLocators();

        // Navigate to Signup
        clickBookWithUs();
        selectOrganisationSignup();

        // Fill Registration Form
        fillOrganisationDetails();
        fillAddress();
        fillContactDetails();
        fillAccountUserDetails();

        // Submit Registration
        acceptTermsAndRegister();

        // Verification
        verifyRegistration();

        // Teardown
        tearDownBrowser();
    }
}
