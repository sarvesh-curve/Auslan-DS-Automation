import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UpdateAppointmentDate {

    // ============================================================
    // VARIABLES
    // ============================================================
    static Playwright playwright;
    static Browser browser;
    static BrowserContext browserContext;
    static Page page;
    static String firstBookingId;
    static String appointmentDate = "28/05/2026";

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
    static Locator bookingDetailsLink;
    static Locator dateOfAppointment;
    static Locator useProfileAddress;
    static Locator finishBtn;
    static Locator termsAndCondition;
    static Locator jobID;
    static Locator dateLocator;

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
        dateOfAppointment = page.locator("input[name='dpDate']");
        jobID = page.locator("td[data-title='JOB NUMBER']");
        dateLocator = page.locator("td[data-title='DATE']");
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

    public static void navigateToBookingDetails() {
        bookingDetailsLink = page.getByText("Booking details");
        bookingDetailsLink.waitFor();
        bookingDetailsLink.click();
    }

    public static void updateAppointmentDate() {
        clickNextTillAppointmentDetailsPage();

        dateOfAppointment.click();
        dateOfAppointment.clear();
        dateOfAppointment.page().keyboard().type(appointmentDate);

        useProfileAddress = page.getByRole(AriaRole.LINK).getByText("Use address on my profile");
        useProfileAddress.click();
    }

    public static void navigateToFinishPage() {
        finishBtn = page.getByRole(AriaRole.BUTTON).getByText("FINISH");
        termsAndCondition = page.locator("Label[name='tnc']");

        repeatNextAction();

        termsAndCondition.waitFor();

        if (!termsAndCondition.isChecked()) {
            termsAndCondition.click();
            assertThat(termsAndCondition).isChecked();
        }

        finishBtn.click();
        page.waitForTimeout(5000);

        System.out.println("Clicked on finish button");
    }

    public static void verifyBookingUpdate() {
        jobID.waitFor();

        String bookingNumber = jobID.innerText().trim();
        System.out.println("BookingNumber: " + bookingNumber);
        Assert.assertEquals(bookingNumber, firstBookingId);

        String date = dateLocator.innerText().trim();
        System.out.println("Date: " + date);
    }

    public static void verifyUpdatedDate() {
        page.waitForTimeout(3000);
        String extractedDateText = dateLocator.innerText().trim();
        System.out.println("Extracted Date: " + extractedDateText);

        String cleanedDate = extractedDateText.replaceAll("(\\d+)(st|nd|rd|th)", "$1");
        System.out.println("Cleaned Date: " + cleanedDate);

        DateTimeFormatter extractedFormatter = DateTimeFormatter.ofPattern("EEE MMM d yy");
        LocalDate extracted = LocalDate.parse(cleanedDate.trim(), extractedFormatter);

        Assert.assertEquals(extracted.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), appointmentDate, "Date should match the expected date");
        System.out.println("✅ Date verification passed!");
    }

    public static void tearDownBrowser() {
        page.close();
        browserContext.close();
        playwright.close();
    }

    public static void clickNextBtn() {
        Locator nextBtn = page.getByRole(AriaRole.BUTTON).getByText("Next");
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
        selectFirstBooking();

        // Navigate to Booking Details
        navigateToBookingDetails();

        // Update Appointment Date
        updateAppointmentDate();

        // Navigate and Submit
        navigateToFinishPage();

        // Verification
        verifyBookingUpdate();
        verifyUpdatedDate();

        // Teardown
        tearDownBrowser();
    }
}
