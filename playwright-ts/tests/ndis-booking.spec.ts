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

    await bookingPage.completeInterpreterBookingFlow(generateTwoMonthsFutureDate());

    // Auslan User page
    await bookingPage.clickNext();

    // Preferences page
    await bookingPage.clickNext();

    await bookingPage.acceptTermsAndConditions();
    await bookingPage.clickFinish();

    expect(await bookingPage.isBookingCreatedSuccessfully()).toBe(true);
  });
});
