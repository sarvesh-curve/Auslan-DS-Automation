import { expect, Page } from '@playwright/test';
import { MEDIUM_WAIT } from '../constants';
import { env } from '../config/env';
import { DashboardPage } from '../pages/dashboard.page';
import { InterpreterInvitePage } from '../pages/interpreter-invite.page';
import { LoginPage } from '../pages/login.page';
import { generateTwoMonthsFutureDate } from './test-data';

/** Logs in and creates a fresh NDIS interpreter booking; returns the dashboard. */
export async function loginAndCreateBooking(page: Page): Promise<DashboardPage> {
  await page.goto(env.appUrl);
  const dashboardPage = await new LoginPage(page).loginWithDefaultCredentials();
  const bookingPage = await dashboardPage.clickNewBooking();

  await bookingPage.selectClient('Sarvesh', 'Sarvesh newndis - NDIS');
  await bookingPage.selectAccountHolderOptions();
  await bookingPage.clickNext();
  await bookingPage.completeInterpreterBookingFlow(generateTwoMonthsFutureDate());
  // Auslan User page, then Preferences page
  await bookingPage.clickNext();
  await bookingPage.clickNext();
  await bookingPage.acceptTermsAndConditions();
  await bookingPage.clickFinish();
  expect(await bookingPage.isBookingCreatedSuccessfully()).toBe(true);
  return dashboardPage;
}

async function jobNumbers(page: Page): Promise<number[]> {
  const cells = await page.locator('tbody tr td:nth-child(2)').allInnerTexts();
  return cells.map((c) => parseInt(c.trim(), 10)).filter((n) => !Number.isNaN(n));
}

/** The list header toggles asc/desc, so click until the newest job is on top. */
async function sortJobsDescending(page: Page): Promise<void> {
  const header = page.getByText('Job', { exact: true });
  for (let attempt = 0; attempt < 4; attempt++) {
    const ids = await jobNumbers(page);
    if (ids.length > 1 && ids[0] > ids[1]) return;
    const refreshed = page.waitForResponse(
      (r) => r.url().includes('/api/v1/bookings') && r.url().includes('sort=job') && r.ok(),
    );
    await header.click();
    await refreshed;
    await page.waitForLoadState('networkidle');
    await page.waitForTimeout(MEDIUM_WAIT);
  }
  throw new Error('Could not sort bookings by newest job first');
}

/** Opens the newest booking for the suite client (global DSQ so all interpreters list). */
export async function openNewestBooking(page: Page): Promise<InterpreterInvitePage> {
  const dashboardPage = new DashboardPage(page);
  await dashboardPage.prepareClientBookings();
  await sortJobsDescending(page);
  await dashboardPage.selectFirstBooking();
  return new InterpreterInvitePage(page).waitForList();
}
