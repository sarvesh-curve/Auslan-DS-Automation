import { test, expect } from '@playwright/test';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';
import { generateTwoMonthsFutureDate } from '../src/utils/test-data';

test.describe('Organisation Representative Booking', () => {
  test('create a new org representative booking successfully', async ({ page }) => {
    await page.goto(env.appUrl);

    const loginPage = new LoginPage(page);
    const dashboardPage = await loginPage.loginWithDefaultCredentials();
    const bookingPage = await dashboardPage.clickNewBooking();

    await bookingPage.selectOrganisation();
    await bookingPage.selectClient(
      'Sarvesh',
      'SARVESH - sarvesh org - ORGANISATIONALREPRESENTATIVE',
    );
    await bookingPage.selectAccountHolderOptions();
    await bookingPage.clickNext();

    await bookingPage.completeInterpreterBookingFlow(generateTwoMonthsFutureDate());

    await bookingPage.selectAuslanUser();
    await bookingPage.clickNext();
    await bookingPage.clickNext();

    await bookingPage.acceptTermsAndConditions();
    await bookingPage.clickFinish();

    expect(await bookingPage.isBookingCreatedSuccessfully()).toBe(true);
  });
});
