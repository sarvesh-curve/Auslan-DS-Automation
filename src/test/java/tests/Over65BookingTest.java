package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BookingPage;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.TestDataGenerator;

public class Over65BookingTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Create a new NDIS booking and verify it is created successfully")
    public void testCreateOver65Booking() {
        // Navigate to application
        page.navigate(ConfigReader.getAppUrl());

        // Login
        LoginPage loginPage = new LoginPage(page);
        DashboardPage dashboardPage = loginPage.loginWithDefaultCredentials();

        // Click New Booking
        BookingPage bookingPage = dashboardPage.clickNewBooking();

        // Fill Client Information
        bookingPage.selectClient("Sarvesh", "Sarvesh Chaudhari 1 - OVER65")
                .selectAccountHolderOptions();
        bookingPage.clickNext();

        // Select Booking Type
        bookingPage.selectBookingType("btnInterpreter")
                .enterNumberOfInterpreters(1);
        bookingPage.clickNext();

        // Fill Appointment Details
        String appointmentDate = TestDataGenerator.generateTwoMonthsFutureDate();
        System.out.println("Using appointment date: " + appointmentDate);
        bookingPage.fillAppointmentDate(appointmentDate)
                .useProfileAddress()
                .fillStartTime("01:30 PM")
                .fillEndTime("02:10 PM");
        bookingPage.clickNext();

        // Fill Details Page
        bookingPage.selectNatureOfAppointment(0)
                .selectWhatWillBeDiscussed(1)
                .fillNotesForInterpreter("No notes for interpreter");
        bookingPage.clickNext();

        // Auslan User Page
        bookingPage.clickNext();

        // Preferences Page
        bookingPage.clickNext();

        // Billing Details
        bookingPage.acceptTermsAndConditions()
                .clickFinish();

        // Verify booking creation
        boolean isCreated = bookingPage.isBookingCreatedSuccessfully();
        Assert.assertTrue(isCreated, "Booking should be created successfully");

        System.out.println("✅ Over 65 booking test completed successfully");
    }
}
