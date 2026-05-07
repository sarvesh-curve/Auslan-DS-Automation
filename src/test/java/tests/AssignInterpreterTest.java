package tests;

import base.BaseTest;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BookingDetailsPage;
import pages.BookingPage;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.TestDataGenerator;

public class AssignInterpreterTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Duplicate an existing NDIS booking and verify it is created successfully")
    public void testAssignInterpreter() {
        // Navigate to application
        page.navigate(ConfigReader.getAppUrl());

        // Login
        LoginPage loginPage = new LoginPage(page);
        DashboardPage dashboardPage = loginPage.loginWithDefaultCredentials();

        // Dashboard Operations
        dashboardPage.selectGlobalDSQService()
                .filterByClientName()
                .clearSuburbFilter()
                .waitForClientToLoad();

        // Select and Filter Bookings
        dashboardPage.selectBookingStatus(AppConstants.STATUS_REQUESTED);

        // Sort and Select Booking
        dashboardPage.sortBookingsByDescendingOrder();
        BookingDetailsPage bookingDetailsPage = dashboardPage.selectFirstBooking();

        bookingDetailsPage.searchInterpreter();
        bookingDetailsPage.selectInterpreter();
        bookingDetailsPage.assignInterpreter();

    }
}
