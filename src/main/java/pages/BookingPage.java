package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import constants.AppConstants;

public class BookingPage extends BasePage {

    // Locators
    private static final String CLIENT_NAME_SELECTOR = "input[placeholder='Start typing...']";
    private static final String YES_ACCOUNT_HOLDER_SELECTOR = "label[for='same_yes']";
    private static final String YES_BEST_CONTACT_SELECTOR = "label[for='is_same_yes']";
    private static final String INTERPRETER_BTN_SELECTOR = "button[name='btnInterpreter']";
    private static final String NO_OF_INTERPRETER_SELECTOR = "input[name='AuslanField']";
    private static final String DATE_OF_APPOINTMENT_SELECTOR = "input[name='dpDate']";
    private static final String START_TIME_SELECTOR = "input[name='dpEventDate']";
    private static final String END_TIME_SELECTOR = "input[name='dpEventEndTime']";
    private static final String NATURE_OF_APPOINTMENT_SELECTOR = "id=natureOfAppointment";
    private static final String WHAT_WILL_BE_DISCUSSED_SELECTOR = "id=specificAppointmentTypesNew";
    private static final String NOTES_FOR_INTERPRETER_SELECTOR = "id=specialInstructions";
    private static final String TERMS_AND_CONDITION_SELECTOR = "Label[name='tnc']";
    private static final String SUCCESS_MSG_SELECTOR = "div.notification__text";
    private static final String ORGANIZATION_RADIO_SELECTOR = "label:has-text('Organisation')";
    private static final String AUSLAN_USER_SELECTOR = "label[for='currentUserIsClientYes']";
    private static final String RECURRING_SELECTOR = "span.slider.round";
    private static final String RECURRING_END_DATE_SELECTOR = "input[placeholder='End date']";

    public BookingPage(Page page) {
        super(page);
    }

    public BookingPage selectClient(String searchText, String suggestion) {
        Locator clientName = page.locator(CLIENT_NAME_SELECTOR);
        clientName.click();
        clientName.page().keyboard().type(searchText);

        page.locator(".ui-autocomplete-items span",
                new Page.LocatorOptions().setHasText(suggestion)).click();

        clientName.press("Tab");
        System.out.println("Selected client: " + suggestion);
        return this;
    }

    public BookingPage selectOrganisation() {
        Locator selectOrg = page.getByText("Organisation").last();
        selectOrg.waitFor();
        selectOrg.click();
        return this;
    }

    public BookingPage selectAccountHolderOptions() {
        clickElement(YES_ACCOUNT_HOLDER_SELECTOR);
        clickElement(YES_BEST_CONTACT_SELECTOR);
        System.out.println("Selected account holder options");
        return this;
    }

    public BookingPage selectBookingType(String bookingType) {
        page.locator(String.format("button[name='%s']", bookingType)).click();
        System.out.println("Selected booking type: " + bookingType);
        return this;
    }

    public void enterNumberOfInterpreters(int count) {
        fillElement(NO_OF_INTERPRETER_SELECTOR, String.valueOf(count));
        System.out.println("Entered number of interpreters: " + count);
    }

    public BookingPage fillAppointmentDate(String date) {
        Locator dateField = page.locator(DATE_OF_APPOINTMENT_SELECTOR);
        dateField.click();
        dateField.page().keyboard().type(date);
        System.out.println("Entered appointment date: " + date);
        return this;
    }

    public void selectRecurringBtn() {
        clickElement(RECURRING_SELECTOR);
    }

    public void enterRecurringEndDate(String date) {
        Locator enterEndDate = page.locator(RECURRING_END_DATE_SELECTOR);
        enterEndDate.click();
        enterEndDate.page().keyboard().type(date);
        System.out.println("Entered recurring end date: " + date);
    }

    public BookingPage useProfileAddress() {
        page.getByRole(AriaRole.LINK).getByText("Use address on my profile").click();
        System.out.println("Used profile address");
        return this;
    }

    public BookingPage fillStartTime(String time) {
        Locator startTime = page.locator(START_TIME_SELECTOR);
        startTime.waitFor();
        startTime.click();
        startTime.fill(time);
        System.out.println("Entered start time: " + time);
        return this;
    }

    public void fillEndTime(String time) {
        Locator endTime = page.locator(END_TIME_SELECTOR);
        endTime.waitFor();
        endTime.click();
        endTime.fill(time);
        endTime.press("Tab");
        System.out.println("Entered end time: " + time);
    }

    public BookingPage selectNatureOfAppointment(int index) {
        page.locator(NATURE_OF_APPOINTMENT_SELECTOR)
                .selectOption(new SelectOption().setIndex(index));
        System.out.println("Selected nature of appointment");
        return this;
    }

    public BookingPage selectWhatWillBeDiscussed(int index) {
        page.locator(WHAT_WILL_BE_DISCUSSED_SELECTOR)
                .selectOption(new SelectOption().setIndex(index));
        System.out.println("Selected what will be discussed");
        return this;
    }

    public BookingPage fillNotesForInterpreter(String notes) {
        fillElement(NOTES_FOR_INTERPRETER_SELECTOR, notes);
        System.out.println("Entered notes for interpreter");
        return this;
    }

    public BookingPage selectAuslanUser() {
        Locator selectAuslanRadioBtn = page.locator(AUSLAN_USER_SELECTOR);
        if (!selectAuslanRadioBtn.isChecked()){
            selectAuslanRadioBtn.waitFor();
            selectAuslanRadioBtn.click();
        }
        System.out.println("Selected the Auslan user");
        return this;
    }

    public BookingPage acceptTermsAndConditions() {
        clickElement(TERMS_AND_CONDITION_SELECTOR);
        System.out.println("Accepted terms and conditions");
        return this;
    }

    public BookingPage clickFinish() {
        page.getByRole(AriaRole.BUTTON).getByText("FINISH").click();
        System.out.println("Clicked Finish button");
        return this;
    }

    /** Clicks the wizard Next button when enabled (shared with BookingDetailsPage flows). */
    public void clickNext() {
        Locator nextBtn = page.getByRole(AriaRole.BUTTON).getByText("Next");
        nextBtn.waitFor();
        waitForTimeout(AppConstants.SHORT_WAIT);

        if (!nextBtn.isEnabled()) {
            throw new RuntimeException("Next button is disabled");
        }

        nextBtn.click();
        System.out.println("Clicked Next button");
    }

    public void clickNextMultipleTimes(int times) {
        for (int i = 0; i < times; i++) {
            clickNext();
        }
    }

    /** Clicks Next until the appointment date field is visible (duplicate / edit-booking wizard). */
    public void clickNextTillAppointmentDetailsPage() {
        Locator dateOfAppointment = page.locator(DATE_OF_APPOINTMENT_SELECTOR);
        int maxAttempts = AppConstants.MAX_APPOINTMENT_PAGE_ATTEMPTS;

        for (int i = 0; i < maxAttempts; i++) {
            if (dateOfAppointment.isVisible()) {
                return;
            }
            clickNext();
        }
    }

    /** Clicks Next until the given locator is visible (e.g. terms checkbox before FINISH). */
    public void repeatNextUntilVisible(Locator targetElement) {
        for (int i = 0; i < AppConstants.MAX_NEXT_ATTEMPTS; i++) {
            if (targetElement.isVisible()) {
                System.out.println("Target element is visible");
                return;
            }
            clickNext();
        }
    }

    public String getSuccessMessage() {
        Locator successMsg = page.locator(SUCCESS_MSG_SELECTOR);
        successMsg.waitFor();
        String message = successMsg.textContent();
        System.out.println("Success message: " + message);
        return message;
    }

    public boolean isBookingCreatedSuccessfully() {
        String message = getSuccessMessage();
        return message.contains(AppConstants.BOOKING_CREATED_MSG);
    }
}
