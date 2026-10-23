import { test, expect } from '@playwright/test';
import { STATUS_REQUESTED } from '../src/constants';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';
import { generateTwoMonthsFutureDate } from '../src/utils/test-data';

test.describe('Update Appointment Date', () => {
  test('update appointment date for an existing booking', async ({ page }) => {
    const appointmentDate = generateTwoMonthsFutureDate();

    await page.goto(env.appUrl);

    const loginPage = new LoginPage(page);
    const dashboardPage = await loginPage.loginWithDefaultCredentials();

    await dashboardPage.prepareClientBookings();
    await dashboardPage.selectBookingStatus(STATUS_REQUESTED);
    await dashboardPage.sortBookingsByDescendingOrder();

    const firstBookingId = (await dashboardPage.getFirstBookingId()).trim();
    const bookingDetailsPage = await dashboardPage.selectFirstBooking();

    await bookingDetailsPage.clickBookingDetails();
    await bookingDetailsPage.updateAppointmentDate(appointmentDate);
    await bookingDetailsPage.navigateToFinishPage();

    const bookingNumber = (await bookingDetailsPage.getJobNumber()).trim();
    // Job number cell can include link id suffix; compare primary id token
    expect(bookingNumber).toContain(firstBookingId.split(/\s+/)[0]);

    await page.waitForTimeout(3000);
    const extractedDateText = await bookingDetailsPage.getDate();
    expect(extractedDateText.length).toBeGreaterThan(0);

    const cleanedDate = extractedDateText.replace(/(\d+)(st|nd|rd|th)/, '$1').trim();
    const match = cleanedDate.match(/\w+\s+(\w+)\s+(\d+)\s+(\d+)/);
    expect(match).toBeTruthy();

    const months: Record<string, number> = {
      Jan: 0, Feb: 1, Mar: 2, Apr: 3, May: 4, Jun: 5,
      Jul: 6, Aug: 7, Sep: 8, Oct: 9, Nov: 10, Dec: 11,
    };
    const month = months[match![1]];
    const day = Number(match![2]);
    let year = Number(match![3]);
    if (year < 100) year += 2000;

    const pad = (n: number) => String(n).padStart(2, '0');
    const formatted = `${pad(day)}/${pad(month + 1)}/${year}`;
    expect(formatted).toBe(appointmentDate);
  });
});
