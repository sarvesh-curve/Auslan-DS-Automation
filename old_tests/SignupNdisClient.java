import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import org.testng.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignupNdisClient {

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
    static Locator ndisClientSignupButton;
    static Locator firstName;
    static Locator lastName;
    static Locator clientPassword;
    static Locator clientConfirmPassword;
    static Locator phoneNumber;
    static Locator email;
    static Locator commPreference;
    static Locator locationPreference;
    static Locator findAddress;
    static Locator planManaged;
    static Locator sameBillingDetailsCheckbox;
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
        ndisClientSignupButton = page.locator("a[name='btnClient']");
        firstName = page.locator("input[name='first_name']");
        lastName = page.locator("input[name='last_name']");
        clientPassword = page.locator("input[name='password']");
        clientConfirmPassword = page.locator("input[name='certainPassword']");
        phoneNumber = page.locator("input[name='mobile']");
        email = page.locator("input[name='email']");
        commPreference = page.locator("select[name='comm_pref']");
        locationPreference = page.locator("select[name='location_pref']");
        findAddress = page.locator("input[name='search_address']");
        planManaged = page.locator("select[name='how_would_you_like_to_receive_notes']");
        sameBillingDetailsCheckbox = page.locator("div.mat-checkbox-inner-container").first();
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

    public static void selectNdisClientSignup() {
        ndisClientSignupButton.click();
    }

    public static void fillPersonalDetails() {
        firstName.waitFor();
        firstName.click();
        firstName.fill("AutoNDIS" + generateDate());

        lastName.waitFor();
        lastName.click();
        lastName.fill("AuttLastName");

        clientPassword.waitFor();
        clientPassword.click();
        clientPassword.fill("Curve@2025");

        clientConfirmPassword.waitFor();
        clientConfirmPassword.click();
        clientConfirmPassword.fill("Curve@2025");

        phoneNumber.waitFor();
        phoneNumber.click();
        phoneNumber.fill("0488883690");

        email.waitFor();
        email.click();
        email.fill("sarvesh" + generateDate() + "@curvetomorrow.com.au");
    }

    public static void selectPreferences() {
        commPreference.selectOption(new SelectOption().setIndex(2));
        locationPreference.selectOption(new SelectOption().setValue("VIC"));
    }

    public static void fillAddress() {
        findAddress.waitFor();
        findAddress.click();
        findAddress.page().keyboard().type("2 Victory Road, Clarinda VIC, Australia", new Keyboard.TypeOptions().setDelay(100));

        page.locator("text=Clarinda VIC").first().click();
    }

    public static void selectPlanAndBilling() {
        planManaged.selectOption(new SelectOption().setValue("self_managed"));
        sameBillingDetailsCheckbox.click();
    }

    public static void acceptTermsAndRegister() {
        termsConditions.click();
        registerButton.click();
    }

    public static void verifyRegistration() {
        confirmMsg.waitFor();
        String verificationText = "Congratulations. Your account has been created.";
        System.out.println(confirmMsg.textContent().trim());
        Assert.assertTrue(confirmMsg.textContent().contains(verificationText));
        System.out.println("Verified the message");
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
        selectNdisClientSignup();

        // Fill Registration Form
        fillPersonalDetails();
        selectPreferences();
        fillAddress();
        selectPlanAndBilling();

        // Submit Registration
        acceptTermsAndRegister();

        // Verification
        verifyRegistration();
    }
}
