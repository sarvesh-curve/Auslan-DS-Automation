import { test, expect, Page } from '@playwright/test';
import { env } from '../src/config/env';
import { BookingPage } from '../src/pages/booking.page';
import { LoginPage } from '../src/pages/login.page';
import { generateFutureDate, generateTwoMonthsFutureDate } from '../src/utils/test-data';

/**
 * Create-booking wizard validation (product pack: 04-bookings, 05-booking-management-ui).
 * Nothing here is submitted, so no bookings are created. Each test stops at the step under test.
 */

async function startBooking(page: Page): Promise<BookingPage> {
  await page.goto(env.appUrl);
  const dashboard = await new LoginPage(page).loginWithDefaultCredentials();
  const booking = await dashboard.clickNewBooking();
  await booking.selectClient('Sarvesh', 'Sarvesh newndis - NDIS');
  await booking.selectAccountHolderOptions();
  await booking.clickNext();
  return booking;
}

async function toAppointmentStep(page: Page): Promise<BookingPage> {
  const booking = await startBooking(page);
  await booking.selectBookingType('btnInterpreter');
  await booking.enterNumberOfInterpreters(1);
  await booking.clickNext();
  await expect(page.locator("input[name='dpDate']")).toBeVisible();
  return booking;
}

test.describe('Create booking: validation', () => {
  test.describe('step 2 type', () => {
    for (const [count, message] of [
      ['0', 'Minimum value should be 1.'],
      ['101', 'Max value should be 100.'],
    ]) {
      test(`${count} interpreters shows "${message}"`, async ({ page }) => {
        const booking = await startBooking(page);
        await booking.selectBookingType('btnInterpreter');
        await page.locator("input[name='AuslanField']").fill(count);
        await page.locator("input[name='AuslanField']").blur();
        await expect(page.getByText(message)).toBeVisible();
      });
    }
  });

  test.describe('step 3 appointment', () => {
    test('a past date is rejected: the field stays empty and Start Date is required', async ({
      page,
    }) => {
      await toAppointmentStep(page);
      const date = page.locator("input[name='dpDate']");
      await date.click();
      await date.fill('01/01/2020');
      await date.press('Tab');
      // Canary rejects the typed past date instead of showing "Date is in the past".
      await expect(date).toHaveValue('');
      await page.getByRole('button', { name: /Next/i }).click();
      await expect(page.getByText('Start Date is required')).toBeVisible();
    });

    test('a date beyond 5 years shows "Please change the date to within 5 years"', async ({
      page,
    }) => {
      const booking = await toAppointmentStep(page);
      await booking.fillAppointmentDate(generateFutureDate(72));
      await expect(page.getByText('Please change the date to within 5 years')).toBeVisible();
    });

    test('end time before start time shows "End time can\'t be before start time"', async ({
      page,
    }) => {
      const booking = await toAppointmentStep(page);
      await booking.fillAppointmentDate(generateTwoMonthsFutureDate());
      await booking.fillStartTime('02:00 PM');
      await booking.fillEndTime('01:00 PM');
      await expect(page.getByText("End time can't be before start time")).toBeVisible();
    });

    test('Next on an empty appointment shows the toast and required-field errors', async ({
      page,
    }) => {
      await toAppointmentStep(page);
      await page.getByRole('button', { name: /Next/i }).click();
      await expect(page.getByText('Please complete all sections on the page.')).toBeVisible();
      await expect(page.getByText('Start Date is required')).toBeVisible();
      await expect(page.getByText('Start Time is required')).toBeVisible();
      await expect(page.getByText('End Time is required')).toBeVisible();
      await expect(page.locator("input[name='dpDate']")).toBeVisible(); // still on step 3
    });
  });

  test.describe('step 4 details', () => {
    test('a one-character note shows "Please specify in Note for the Interpreter"', async ({
      page,
    }) => {
      const booking = await toAppointmentStep(page);
      await booking.fillAppointmentDate(generateTwoMonthsFutureDate());
      await booking.useProfileAddress();
      await booking.fillStartTime('01:30 PM');
      await booking.fillEndTime('02:10 PM');
      await booking.clickNext();
      await booking.fillNotesForInterpreter('x');
      await page.locator('#specialInstructions').blur();
      await expect(page.getByText('Please specify in Note for the Interpreter')).toBeVisible();
    });
  });

  test.describe('step 7 billing', () => {
    test('FINISH without accepting Terms shows "Kindly accept Terms and Conditions"', async ({
      page,
    }) => {
      const booking = await startBooking(page);
      await booking.completeInterpreterBookingFlow(generateTwoMonthsFutureDate());
      await booking.clickNext(); // Auslan User
      await booking.clickNext(); // Preferences
      await booking.clickFinish();
      await expect(page.getByText('Kindly accept Terms and Conditions')).toBeVisible();
      await expect(page.getByText('The Booking has been created.')).toHaveCount(0);
    });
  });
});
