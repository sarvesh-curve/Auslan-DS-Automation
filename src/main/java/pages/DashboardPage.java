package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import constants.AppConstants;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPage extends BasePage {

    // Locators
    private static final String NEW_BOOKING_BTN_SELECTOR = "a[id='lnkNewBooking']";
    private static final String DSQ_SERVICE_DROPDOWN_SELECTOR = "ul.dsq-service-dropdown";
    private static final String DSQ_GLOBAL_SELECTOR = ".deaf-global";
    private static final String CLIENT_FILTER_SELECTOR = "input[name='client_name']";
    private static final String SUBURB_FILTER_SELECTOR = "input[name='suburb']";
    private static final String WAIT_FOR_ORG_NAME_SELECTOR = "td[title='Sarvesh newndis']";
    private static final String JOB_COLUMN_TEXT = "Job";
    private static final String BOOKING_STATE_DROPDOWN_TRIGGER = "ul#booking-state > li > a[href='javascript:void(0)']:not([ng-reflect-ng-class])";
    private static final String BOOKING_STATE_MENU = "ul#booking-state ul.menu";

    public DashboardPage(Page page) {
        super(page);
    }

    public BookingPage clickNewBooking() {
        clickElement(NEW_BOOKING_BTN_SELECTOR);
        System.out.println("Clicked New Booking button");
        return new BookingPage(page);
    }

    public DashboardPage selectGlobalDSQService() {
        clickElement(DSQ_SERVICE_DROPDOWN_SELECTOR);
        System.out.println("Clicked DSQ service dropdown");
        
        clickElement(DSQ_GLOBAL_SELECTOR);
        System.out.println("Selected Global DSQ");
        return this;
    }

    public DashboardPage filterByClientName(String clientName) {
        Locator clientFilter = page.locator(CLIENT_FILTER_SELECTOR);
        clientFilter.click();
        clientFilter.fill(clientName);
        System.out.println("Filtered by client name: " + clientName);
        return this;
    }

    public DashboardPage filterByClientName() {
        return filterByClientName(AppConstants.CLIENT_NAME);
    }

    public DashboardPage clearSuburbFilter() {
        clickElement(SUBURB_FILTER_SELECTOR);
        System.out.println("Cleared suburb filter");
        return this;
    }

    public DashboardPage waitForClientToLoad() {
        page.locator(WAIT_FOR_ORG_NAME_SELECTOR).first().waitFor();
        waitForTimeout(AppConstants.SHORT_WAIT);
        System.out.println("Client data loaded");
        return this;
    }

    public DashboardPage selectBookingStatus(String status) {
        clickElement(BOOKING_STATE_DROPDOWN_TRIGGER);
        System.out.println("Clicked booking status dropdown");

        Locator optionsList = page.locator(BOOKING_STATE_MENU);
        optionsList.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        page.locator("ul#booking-state ul.menu li a:has-text('" + status + "')").click();
        System.out.println("Selected booking status: " + status);
        return this;
    }

    public DashboardPage sortBookingsByDescendingOrder() {
        Locator firstBooking = page.locator("tbody tr").first().locator("td").nth(1);
        System.out.println("Initial first booking ID: " + firstBooking.innerText());

        Locator descOrder = page.getByText(JOB_COLUMN_TEXT);
        descOrder.click();

        Locator waitForOrgName = page.locator(WAIT_FOR_ORG_NAME_SELECTOR).first();
        waitForOrgName.waitFor();

        page.waitForResponse(
                response -> response.url().contains("/api/v1/bookings") &&
                        response.url().contains("sort=job") &&
                        response.url().contains("direction=desc") &&
                        response.status() == 200,
                () -> descOrder.click()
        );

        waitForTimeout(AppConstants.SHORT_WAIT);
        System.out.println("Sorted bookings by descending order");
        return this;
    }

    public void sortLinkBookingsByDescendingOrder() {
        Locator firstBooking = page.locator("tbody tr").first().locator("td").nth(1);
        System.out.println("Initial first booking ID: " + firstBooking.innerText());

        waitForTimeout(AppConstants.SHORT_WAIT);

        Locator descOrder = page.getByText(JOB_COLUMN_TEXT);
        descOrder.dblclick();
        System.out.println("Double clicked on Job Column to get bookings in desc order (Latest bookings)");

        Locator waitForOrgName = page.locator(WAIT_FOR_ORG_NAME_SELECTOR).first();
        waitForOrgName.waitFor();

        System.out.println("waiting for org name");

        page.waitForResponse(
                response -> response.url().contains("/api/v1/bookings") &&
                        response.url().contains("sort=job") &&
                        response.url().contains("direction=desc") &&
                        response.status() == 200,
                () -> descOrder.click()
        );

        System.out.println("waiting for response");


        waitForTimeout(AppConstants.SHORT_WAIT);
        System.out.println("Sorted bookings by descending order");
    }

    public BookingDetailsPage selectFirstBooking() {
        Locator firstBookingCell = page.locator("tbody tr").first().locator("td").nth(1);
        String bookingId = firstBookingCell.innerText();
        System.out.println("First booking ID: " + bookingId);

        firstBookingCell.click();
        waitForTimeout(AppConstants.SHORT_WAIT);
        System.out.println("Clicked on first booking");

        if (firstBookingCell.isVisible()){
            Locator jobIdSpan = page.locator("td.bookingID .actions-container > span")
                    .filter(new Locator.FilterOptions().setHasText(java.util.regex.Pattern.compile("^\\d+$")))
                    .first();
            jobIdSpan.click();
        }

        return new BookingDetailsPage(page);
    }

    public String getFirstBookingId() {
        Locator firstBookingCell = page.locator("tbody tr").first().locator("td").nth(1);
        return firstBookingCell.innerText();
    }

    public String getFirstBookingLinkId() {
        Locator firstBookingLink = page.locator("td.bookingID span.linkId a").first();
        String bookingNo = firstBookingLink.innerText().trim(); // "#10015";
        System.out.println("Initial first booking link ID: " + bookingNo);
        return bookingNo;
    }

    public String clickFirstBookingLinkId() {
        Locator firstBookingLink = page.locator("td.bookingID span.linkId a").first();
        String bookingNo = firstBookingLink.innerText().trim();
        firstBookingLink.click();
        waitForTimeout(AppConstants.SHORT_WAIT);
        Locator totalBookings = page.getByText("Displaying 1 - 3 of 3 Bookings");
        assertThat(totalBookings).isVisible();

        System.out.println("Bookings are verified");
        return bookingNo;
    }


}
