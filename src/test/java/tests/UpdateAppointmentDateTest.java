package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BookingDetailsPage;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.TestDataGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateAppointmentDateTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Update appointment date for an existing booking and verify the change")
    public void testUpdateAppointmentDate() {
        String appointmentDate = TestDataGenerator.generateTwoMonthsFutureDate();
        System.out.println("Using appointment date: " + appointmentDate);

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
        String firstBookingId = dashboardPage.getFirstBookingId();
        BookingDetailsPage bookingDetailsPage = dashboardPage.selectFirstBooking();

        // Navigate to Booking Details
        bookingDetailsPage.clickBookingDetails();

        // Update Appointment Date
        bookingDetailsPage.updateAppointmentDate(appointmentDate);

        // Navigate and Submit
        bookingDetailsPage.navigateToFinishPage();

        // Verify Booking Update
        String bookingNumber = bookingDetailsPage.getJobNumber();
        Assert.assertEquals(bookingNumber, firstBookingId, "Booking number should match");

        // Verify Updated Date
        page.waitForTimeout(3000);
        String extractedDateText = bookingDetailsPage.getDate();
        System.out.println("Extracted Date: " + extractedDateText);

        String cleanedDate = extractedDateText.replaceAll("(\\d+)(st|nd|rd|th)", "$1");
        System.out.println("Cleaned Date: " + cleanedDate);

        DateTimeFormatter extractedFormatter = DateTimeFormatter.ofPattern("EEE MMM d yy");
        LocalDate extracted = LocalDate.parse(cleanedDate.trim(), extractedFormatter);

        Assert.assertEquals(extracted.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
                appointmentDate, "Date should match the expected date");
        
        System.out.println("✅ Date verification passed!");
        System.out.println("✅ Update appointment date test completed successfully");
    }
}
