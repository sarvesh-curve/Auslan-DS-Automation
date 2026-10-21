import { test, expect } from '@playwright/test';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';
import { generateTwoMonthsFutureDate } from '../src/utils/test-data';

test.describe('NDIS Booking', () => {
  test('create a new NDIS booking successfully', async ({ page }) => {
    await page.goto(env.appUrl);

    const loginPage = new LoginPage(page);
    const dashboardPage = await loginPage.loginWithDefaultCredentials();
    const bookingPage = await dashboardPage.clickNewBooking();

    await bookingPage.selectClient('Sarvesh', 'Sarvesh newndis - NDIS');
    await bookingPage.selectAccountHolderOptions();
    await bookingPage.clickNext();

    await bookingPage.selectBookingType('btnInterpreter');
    await bookingPage.enterNumberOfInterpreters(1);
    await bookingPage.clickNext();

    const appointmentDate = generateTwoMonthsFutureDate();
    await bookingPage.fillAppointmentDate(appointmentDate);
    await bookingPage.useProfileAddress();
    await bookingPage.fillStartTime('01:30 PM');
    await bookingPage.fillEndTime('02:10 PM');
    await bookingPage.clickNext();

    await bookingPage.selectNatureOfAppointment(0);
    await bookingPage.selectWhatWillBeDiscussed(1);
    await bookingPage.fillNotesForInterpreter('No notes for interpreter');
    await bookingPage.clickNext();

    // Auslan User page
    await bookingPage.clickNext();

    // Preferences page
    await bookingPage.clickNext();

    await bookingPage.acceptTermsAndConditions();
    await bookingPage.clickFinish();

    expect(await bookingPage.isBookingCreatedSuccessfully()).toBe(true);
  });
});
