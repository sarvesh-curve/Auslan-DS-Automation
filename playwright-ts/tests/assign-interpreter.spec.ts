import { test } from '@playwright/test';
import { STATUS_REQUESTED } from '../src/constants';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';

test.describe('Assign Interpreter', () => {
  test('assign an interpreter to a requested booking', async ({ page }) => {
    await page.goto(env.appUrl);

    const loginPage = new LoginPage(page);
    const dashboardPage = await loginPage.loginWithDefaultCredentials();

    await dashboardPage.prepareClientBookings();
    await dashboardPage.selectBookingStatus(STATUS_REQUESTED);
    await dashboardPage.sortBookingsByDescendingOrder();

    const bookingDetailsPage = await dashboardPage.selectFirstBooking();
    await bookingDetailsPage.searchInterpreter();
    await bookingDetailsPage.selectInterpreter();
    await bookingDetailsPage.assignInterpreter();
  });
});
