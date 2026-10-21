import { test, expect } from '@playwright/test';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';
import { generateTwoMonthsFifteenDaysFutureDate } from '../src/utils/test-data';

test.describe('Duplicate NDIS Booking', () => {
  test('duplicate an existing NDIS booking successfully', async ({ page }) => {
    await page.goto(env.appUrl);

    const loginPage = new LoginPage(page);
    const dashboardPage = await loginPage.loginWithDefaultCredentials();

    await dashboardPage.prepareClientBookings();
    await dashboardPage.sortBookingsByDescendingOrder();
    const bookingDetailsPage = await dashboardPage.selectFirstBooking();

    const bookingPage = await bookingDetailsPage.clickDuplicate();

    // Date is generated for parity with Java; duplicate flow clicks through without editing it
    generateTwoMonthsFifteenDaysFutureDate();

    await bookingPage.clickNextMultipleTimes(6);
    await bookingPage.acceptTermsAndConditions();
    await bookingPage.clickFinish();

    expect(await bookingPage.isBookingCreatedSuccessfully()).toBe(true);
  });
});
