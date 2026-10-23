import { test } from '@playwright/test';
import {
  STATUS_CANCELLED_NO_CHARGE,
  STATUS_REQUESTED,
} from '../src/constants';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';

test.describe('Cancel Booking', () => {
  test('cancel a booking and verify Cancelled No Charge status', async ({ page }) => {
    await page.goto(env.appUrl);

    const loginPage = new LoginPage(page);
    const dashboardPage = await loginPage.loginWithDefaultCredentials();

    await dashboardPage.prepareClientBookings();
    await dashboardPage.selectBookingStatus(STATUS_REQUESTED);
    await dashboardPage.sortBookingsByDescendingOrder();

    const bookingDetailsPage = await dashboardPage.selectFirstBooking();
    await bookingDetailsPage.clickCancelBooking();
    await bookingDetailsPage.verifyBookingStatus(STATUS_CANCELLED_NO_CHARGE);
  });
});
