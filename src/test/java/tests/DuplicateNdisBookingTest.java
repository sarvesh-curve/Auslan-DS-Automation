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

public class DuplicateNdisBookingTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Duplicate an existing NDIS booking and verify it is created successfully")
    public void testDuplicateNdisBooking() {
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

        // Sort and Select Booking
        dashboardPage.sortBookingsByDescendingOrder();
        BookingDetailsPage bookingDetailsPage = dashboardPage.selectFirstBooking();

        // Duplicate the booking
        BookingPage bookingPage = bookingDetailsPage.clickDuplicate();

        // Generate and log dynamic appointment date (2 months and 15 days in future)
        // Note: In duplicate flow, we click through pages without modifying the date
        String generatedDate = TestDataGenerator.generateTwoMonthsFifteenDaysFutureDate();
        System.out.println("Generated date (2 months + 15 days): " + generatedDate);

        // Navigate through pages (6 Next button clicks as per original test)
        bookingPage.clickNextMultipleTimes(6);

        // Accept Terms and Finish
        bookingPage.acceptTermsAndConditions()
                .clickFinish();

        // Verify booking creation
        boolean isCreated = bookingPage.isBookingCreatedSuccessfully();
        Assert.assertTrue(isCreated, "Duplicated booking should be created successfully");

        System.out.println("✅ Duplicate NDIS booking test completed successfully");
    }
}
