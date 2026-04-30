package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import constants.AppConstants;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingDetailsPage extends BasePage {

    // Locators
    private static final String BOOKING_DETAILS_LINK_TEXT = "Booking details";
    private static final String CANCEL_BOOKING_BTN_TEXT = "Cancel Booking";
    private static final String CANCEL_THIS_JOB_BTN_TEXT = "Cancel this job";
    private static final String CANCEL_REASONS_SELECTOR = "select#cancel-reasons";
    private static final String CANCELLED_NO_CHARGE_BTN_TEXT = "Cancelled No Charge";
    private static final String ACTIVE_STEP_SELECTOR = "#steps nav a.active";
    private static final String DUPLICATE_BUTTON_TEXT = "Duplicate";
    private static final String DATE_OF_APPOINTMENT_SELECTOR = "input[name='dpDate']";
    private static final String TERMS_AND_CONDITION_SELECTOR = "Label[name='tnc']";
    private static final String JOB_NUMBER_SELECTOR = "td[data-title='JOB NUMBER']";
    private static final String DATE_SELECTOR = "td[data-title='DATE']";

    public BookingDetailsPage(Page page) {
        super(page);
    }

    public BookingDetailsPage clickBookingDetails() {
        Locator bookingDetailsLink = page.getByText(BOOKING_DETAILS_LINK_TEXT);
        bookingDetailsLink.waitFor();
        bookingDetailsLink.click();
        System.out.println("Clicked Booking Details link");
        return this;
    }

    public BookingDetailsPage clickCancelBooking() {
        Locator cancelBtn = page.locator("button:has-text('" + CANCEL_BOOKING_BTN_TEXT + "')");
        cancelBtn.waitFor();
        cancelBtn.click();
        System.out.println("Clicked Cancel Booking button");

        Locator cancelJobBtn = page.locator("button:has-text('" + CANCEL_THIS_JOB_BTN_TEXT + "')");
        cancelJobBtn.waitFor();
        cancelJobBtn.click();
        System.out.println("Clicked Cancel This Job button");

        page.locator(CANCEL_REASONS_SELECTOR).selectOption("no reason given");
        System.out.println("Selected cancel reason");

        Locator noChargeBtn = page.locator("button:has-text('" + CANCELLED_NO_CHARGE_BTN_TEXT + "')");
        noChargeBtn.waitFor();
        noChargeBtn.click();
        System.out.println("Clicked Cancelled No Charge button");

        waitForTimeout(AppConstants.MEDIUM_WAIT);
        return this;
    }

    public void verifyBookingStatus(String expectedStatus) {
        Locator activeStep = page.locator(ACTIVE_STEP_SELECTOR);
        assertThat(activeStep).isVisible();
        assertThat(activeStep).hasText(expectedStatus);
        System.out.println("Successfully verified booking status: " + expectedStatus);
    }

    public BookingPage clickDuplicate() {
        Locator duplicateBtn = page.getByText(DUPLICATE_BUTTON_TEXT);
        assertThat(duplicateBtn).isEnabled();
        duplicateBtn.click();
        System.out.println("Clicked Duplicate button");
        return new BookingPage(page);
    }

    public BookingDetailsPage updateAppointmentDate(String date) {
        clickNextTillAppointmentDetailsPage();

        Locator dateField = page.locator(DATE_OF_APPOINTMENT_SELECTOR);
        dateField.click();
        dateField.clear();
        dateField.page().keyboard().type(date);
        System.out.println("Updated appointment date to: " + date);

        page.getByRole(AriaRole.LINK).getByText("Use address on my profile").click();
        return this;
    }

    public BookingDetailsPage navigateToFinishPage() {
        Locator finishBtn = page.getByRole(AriaRole.BUTTON).getByText("FINISH");
        Locator termsAndCondition = page.locator(TERMS_AND_CONDITION_SELECTOR);

        repeatNextAction(termsAndCondition);

        termsAndCondition.waitFor();

        if (!termsAndCondition.isChecked()) {
            termsAndCondition.click();
            assertThat(termsAndCondition).isChecked();
        }

        finishBtn.click();
        waitForTimeout(AppConstants.LONG_WAIT);
        System.out.println("Clicked Finish button");

        return this;
    }

    public String getJobNumber() {
        Locator jobID = page.locator(JOB_NUMBER_SELECTOR);
        jobID.waitFor();
        String bookingNumber = jobID.innerText().trim();
        System.out.println("Booking Number: " + bookingNumber);
        return bookingNumber;
    }

    public String getDate() {
        String date = page.locator(DATE_SELECTOR).innerText().trim();
        System.out.println("Date: " + date);
        return date;
    }

    private void clickNextBtn() {
        Locator nextBtn = page.getByRole(AriaRole.BUTTON).getByText("Next");
        nextBtn.waitFor();
        waitForTimeout(AppConstants.SHORT_WAIT);

        if (!nextBtn.isEnabled()) {
            throw new RuntimeException("Next button is disabled");
        }

        nextBtn.click();
        System.out.println("Clicked Next button");
    }

    private void clickNextTillAppointmentDetailsPage() {
        Locator dateOfAppointment = page.locator(DATE_OF_APPOINTMENT_SELECTOR);
        int maxAttempts = AppConstants.MAX_APPOINTMENT_PAGE_ATTEMPTS;

        for (int i = 0; i < maxAttempts; i++) {
            if (dateOfAppointment.isVisible()) {
                return;
            }
            clickNextBtn();
        }
    }

    private void repeatNextAction(Locator targetElement) {
        for (int i = 0; i < AppConstants.MAX_NEXT_ATTEMPTS; i++) {
            if (targetElement.isVisible()) {
                System.out.println("Target element is visible");
                return;
            }
            clickNextBtn();
        }
    }
}
