import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CancelBooking {

    // ============================================================
    // VARIABLES
    // ============================================================
    static Playwright playwright;
    static Browser browser;
    static BrowserContext browserContext;
    static Page page;
    static String firstBookingId;
    static String status;
    static String expectedStatus;

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
    static Locator dateOfAppointment;
    static Locator cancelBookingButton;
    static Locator cancelThisJobButton;
    static Locator cancelReasonsSelect;
    static Locator cancelledNoChargeButton;
    static Locator termsAndCondition;
    static Locator finishBtn;
    static Locator nextBtn;
    static Locator activeStep;

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
        cancelReasonsSelect = page.locator("select#cancel-reasons");
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
        page.waitForTimeout(1000);
    }

    public static void selectBookingStatus(Page page, String status) {
        Locator dropdownTrigger = page.locator("ul#booking-state > li > a[href='javascript:void(0)']:not([ng-reflect-ng-class])");
        dropdownTrigger.click();

        Locator optionsList = page.locator("ul#booking-state ul.menu");
        optionsList.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        page.locator("ul#booking-state ul.menu li a:has-text('" + status + "')").click();
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

        page.waitForTimeout(1000);
    }

    public static void selectFirstBooking() {
        Locator afterSync = page.locator("tbody tr").first().locator("td").nth(1);
        System.out.println("first booking id: " + afterSync.innerText());
        firstBookingId = afterSync.innerText();
        firstBooking.click();
        System.out.println("tested");

        page.waitForTimeout(1000);
    }

    public static void cancelBooking() {
        cancelBookingButton = page.locator("button:has-text('Cancel Booking')");
        cancelBookingButton.waitFor();
        cancelBookingButton.click();

        cancelThisJobButton = page.locator("button:has-text('Cancel this job')");
        cancelThisJobButton.waitFor();
        cancelThisJobButton.click();

        cancelReasonsSelect.selectOption("no reason given");

        cancelledNoChargeButton = page.locator("button:has-text('Cancelled No Charge')");
        cancelledNoChargeButton.waitFor();
        cancelledNoChargeButton.click();

        page.waitForTimeout(2000);
    }

    public static void verifyBookingStatus(Page page, String expectedStatus) {
        activeStep = page.locator("#steps nav a.active");
        assertThat(activeStep).isVisible();
        assertThat(activeStep).hasText(expectedStatus);
        System.out.println("Successfully verified the status");
    }

    public static void clickNextBtn() {
        nextBtn = page.getByRole(AriaRole.BUTTON).getByText("Next");
        nextBtn.waitFor();
        page.waitForTimeout(1000);

        if (!nextBtn.isEnabled()) {
            throw new RuntimeException("Next button is disabled before finding an element");
        }

        nextBtn.click();
        System.out.println("Clicked on next button");
    }

    public static void clickNextTillAppointmentDetailsPage() {
        int maxattempts = 8;

        for (int i = 0; i < maxattempts; i++) {
            if (dateOfAppointment.isVisible()) {
                return;
            }
            clickNextBtn();
        }
    }

    public static void repeatNextAction() {
        int i;
        for (i = 0; i < 6; i++) {
            if (termsAndCondition.isVisible()) {
                System.out.println("Terms and condition is visible");
                return;
            }
            clickNextBtn();
        }
    }

    public static void tearDownBrowser() {
        page.close();
        browserContext.close();
        playwright.close();
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

        // Select and Filter Bookings
        status = "Requested";
        selectBookingStatus(page, status);
        sortBookingsByDescendingOrder();
        selectFirstBooking();

        // Cancel Booking
        cancelBooking();

        // Verify Cancellation
        expectedStatus = "Cancelled No Charge";
        verifyBookingStatus(page, expectedStatus);

        // Teardown
        tearDownBrowser();
    }
}
