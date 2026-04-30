import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class SignupOver65Client {

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
    static Locator over65ClientSignupButton;
    static Locator firstName;
    static Locator lastName;
    static Locator clientPassword;
    static Locator clientConfirmPassword;
    static Locator phoneNumber;
    static Locator dateOfBirth;
    static Locator email;
    static Locator commPreference;
    static Locator locationPreference;
    static Locator findAddress;
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
        over65ClientSignupButton = page.locator("a[name='btnInterpreter']");
        firstName = page.locator("input[name='first_name']");
        lastName = page.locator("input[name='last_name']");
        clientPassword = page.locator("input[name='password']");
        clientConfirmPassword = page.locator("input[name='certainPassword']");
        phoneNumber = page.locator("input[name='mobile']");
        dateOfBirth = page.locator("input[name='date_of_birth']");
        email = page.locator("input[name='email']");
        commPreference = page.locator("select[name='comm_pref']");
        locationPreference = page.locator("select[name='location_pref']");
        findAddress = page.locator("input[name='search_address']");
        termsConditions = page.locator("div.mat-checkbox-inner-container").last();
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

    public static void selectOver65ClientSignup() {
        over65ClientSignupButton.click();
    }

    public static void fillPersonalDetails() {
        firstName.waitFor();
        firstName.click();
        firstName.fill("Auto65+" + generateDate());

        lastName.waitFor();
        lastName.click();
        lastName.fill("Aut0LastName");

        clientPassword.waitFor();
        clientPassword.click();
        clientPassword.fill("Curve@2025");

        clientConfirmPassword.waitFor();
        clientConfirmPassword.click();
        clientConfirmPassword.fill("Curve@2025");

        phoneNumber.waitFor();
        phoneNumber.click();
        phoneNumber.fill("0488883690");

        dateOfBirth.waitFor();
        dateOfBirth.click();
        dateOfBirth.page().keyboard().type(generateDOBForOver65());

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

    public static void tearDownBrowser() {
        page.close();
        browserContext.close();
        playwright.close();
    }

    public static String generateDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    public static String generateDOBForOver65() {
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.minusYears(65);   // youngest allowed
        LocalDate minDate = today.minusYears(100);  // oldest allowed

        long randomDay = ThreadLocalRandom.current()
                .nextLong(minDate.toEpochDay(), maxDate.toEpochDay());

        LocalDate randomDOB = LocalDate.ofEpochDay(randomDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return randomDOB.format(formatter);
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
        selectOver65ClientSignup();

        // Fill Registration Form
        fillPersonalDetails();
        selectPreferences();
        fillAddress();

        // Submit Registration
        acceptTermsAndRegister();

        // Verification
        verifyRegistration();

        // Teardown
        tearDownBrowser();
    }
}
