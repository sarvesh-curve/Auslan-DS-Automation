import { test, expect } from '@playwright/test';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';
import {
  generateTwoMonthsFifteenDaysFutureDate,
  generateTwoMonthsFutureDate,
} from '../src/utils/test-data';

test.describe('Recurring Booking', () => {
  test('create a recurring NDIS booking and verify linked bookings', async ({
    page,
  }) => {
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
    await bookingPage.selectRecurringBtn();
    await bookingPage.enterRecurringEndDate(generateTwoMonthsFifteenDaysFutureDate());
    await bookingPage.clickNext();

    await bookingPage.selectNatureOfAppointment(0);
    await bookingPage.selectWhatWillBeDiscussed(1);
    await bookingPage.fillNotesForInterpreter('No notes for interpreter');
    await bookingPage.clickNext();
    await bookingPage.clickNext();
    await bookingPage.clickNext();

    await bookingPage.acceptTermsAndConditions();
    await bookingPage.clickFinish();

    expect(await bookingPage.isBookingCreatedSuccessfully()).toBe(true);

    await dashboardPage.prepareClientBookings();
    await dashboardPage.sortLinkBookingsByDescendingOrder();
    await dashboardPage.getFirstBookingLinkId();
    await dashboardPage.clickFirstBookingLinkId();
  });
});
