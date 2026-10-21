import { test, expect } from '@playwright/test';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';
import { generateTwoMonthsFutureDate } from '../src/utils/test-data';

test.describe('Over 65 Booking', () => {
  test('create a new Over 65 booking successfully', async ({ page }) => {
    await page.goto(env.appUrl);

    const loginPage = new LoginPage(page);
    const dashboardPage = await loginPage.loginWithDefaultCredentials();
    const bookingPage = await dashboardPage.clickNewBooking();

    await bookingPage.selectClient('Sarvesh', 'Sarvesh Chaudhari 1 - OVER65');
    await bookingPage.selectAccountHolderOptions();
    await bookingPage.clickNext();

    await bookingPage.completeInterpreterBookingFlow(generateTwoMonthsFutureDate());

    // Auslan User + Preferences
    await bookingPage.clickNext();
    await bookingPage.clickNext();

    await bookingPage.acceptTermsAndConditions();
    await bookingPage.clickFinish();

    expect(await bookingPage.isBookingCreatedSuccessfully()).toBe(true);
  });
});
