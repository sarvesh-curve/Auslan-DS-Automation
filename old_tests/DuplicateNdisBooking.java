import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DuplicateNdisBooking {

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
    static Locator email;
    static Locator password;
    static Locator loginBtn;
    static Locator dsQServiceDropdown;
    static Locator dsqGlobal;
    static Locator clientFilter;
    static Locator getClientName;
    static Locator suburbFilter;
    static Locator waitForOrgName;
    static Locator firstBooking;
    static Locator descOrder;
    static Locator duplicateButton;
    static Locator termsAndCondition;
    static Locator finishBtn;
    static Locator successMsg;

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
        email = page.locator("#email");
        password = page.locator("#pass");
        loginBtn = page.locator("button[name='login_user']");
        dsQServiceDropdown = page.locator("ul.dsq-service-dropdown");
        dsqGlobal = page.locator(".deaf-global");
        clientFilter = page.locator("input[name='client_name']");
        getClientName = page.locator("tbody tr").first().locator("td").nth(7);
        suburbFilter = page.locator("input[name='suburb']");
        waitForOrgName = page.locator("td[title='Sarvesh newndis']").first();
        firstBooking = page.locator("tbody tr").first().locator("td").nth(1);
        descOrder = page.getByText("Job");
        duplicateButton = page.getByText("Duplicate");
        termsAndCondition = page.locator("Label[name='tnc']");
        successMsg = page.locator("div.notification__text");
    }

    public static void navigateToApplication() {
        page.navigate("http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout");
    }

    public static void login() {
        email.fill("sarvesh@curvetomorrow.com.au");
        password.fill("Curve@2025");
        loginBtn.click();
    }

    public static void selectGlobalDSQService() {
        dsQServiceDropdown.click();
        dsqGlobal.click();
    }

    public static void filterByClientName() {
        clientFilter.click();
        clientFilter.fill("Sarvesh newndis");
        String clientName = getClientName.textContent().trim();
    }

    public static void clearSuburbFilter() {
        suburbFilter.click();
    }

    public static void waitForClientToLoad() {
        waitForOrgName.waitFor();
    }

    public static void sortBookingsByDescendingOrder() {
        System.out.println(firstBooking.innerText());
        descOrder.click();

        waitForOrgName.waitFor();
        page.waitForResponse(
                response -> response.url().contains("/api/v1/bookings") &&
                        response.url().contains("sort=job") &&
                        response.url().contains("direction=desc") &&
                        response.status() == 200,
                () -> {
                    descOrder.click();
                }
        );

        Locator afterSync = page.locator("tbody tr").first().locator("td").nth(1);
        System.out.println(afterSync.innerText());
        firstBooking.click();
        System.out.println("tested");

        page.waitForTimeout(1000);
    }

    public static void clickDuplicateBooking() {
        assertThat(duplicateButton).isEnabled();
        duplicateButton.click();
    }

    public static void acceptTermsAndFinish() {
        termsAndCondition.waitFor();
        termsAndCondition.click();
        assertThat(termsAndCondition).isChecked();

        finishBtn = page.getByRole(AriaRole.BUTTON).getByText("FINISH");
        finishBtn.click();

        System.out.println("Clicked on finish button");
    }

    public static void verifyBookingCreation() {
        successMsg.waitFor();
        System.out.println(successMsg.textContent());
        String alertSuccessMsg = "The Booking has been created.";
        Assert.assertEquals(successMsg.textContent(), alertSuccessMsg);
    }

    public static void clickNextBtn() {
        Locator nextBtn = page.getByRole(AriaRole.BUTTON).getByText("Next");
        nextBtn.waitFor();
        page.waitForTimeout(1000);
        nextBtn.click();
    }

    public static void repeatNextAction() {
        int i;
        for (i = 0; i < 6; i++) {
            clickNextBtn();
        }
    }

    // ============================================================
    // MAIN - METHOD CALLS
    // ============================================================
    public static void main(String[] args) throws InterruptedException {

        // Setup
        setupBrowser();
        navigateToApplication();
        initializeLocators();

        // Login
        login();

        // Dashboard Operations
        selectGlobalDSQService();
        filterByClientName();
        clearSuburbFilter();
        waitForClientToLoad();

        // Sort and Select Booking
        sortBookingsByDescendingOrder();

        // Duplicate Booking
        clickDuplicateBooking();

        // Navigate through pages
        repeatNextAction();

        // Accept Terms and Finish
        acceptTermsAndFinish();

        // Verification
        verifyBookingCreation();
    }
}
