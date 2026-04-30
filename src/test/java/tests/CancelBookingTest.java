package tests;

import base.BaseTest;
import constants.AppConstants;
import org.testng.annotations.Test;
import pages.BookingDetailsPage;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ConfigReader;

public class CancelBookingTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Cancel a booking and verify status is updated to Cancelled No Charge")
    public void testCancelBooking() {
        // Navigate to application
        page.navigate(ConfigReader.getAppUrl());

        // Login
        LoginPage loginPage = new LoginPage(page);
        DashboardPage dashboardPage = loginPage.loginWithDefaultCredentials();

        // Dashboard Operations - prepare to find booking
        dashboardPage.selectGlobalDSQService()
                .filterByClientName()
                .clearSuburbFilter()
                .waitForClientToLoad();

        // Select and Filter Bookings
        dashboardPage.selectBookingStatus(AppConstants.STATUS_REQUESTED)
                .sortBookingsByDescendingOrder();

        // Select first booking and navigate to details
        BookingDetailsPage bookingDetailsPage = dashboardPage.selectFirstBooking();

        // Cancel the booking
        bookingDetailsPage.clickCancelBooking();

        // Verify cancellation status
        bookingDetailsPage.verifyBookingStatus(AppConstants.STATUS_CANCELLED_NO_CHARGE);

        System.out.println("✅ Cancel booking test completed successfully");
    }
}
