import { test, expect, Page } from '@playwright/test';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';

/**
 * Negative auth and navigation cases (product pack: 02-auth-and-access, 03-navigation).
 *
 * Lockout safety: 5 consecutive wrong passwords lock the account for 30 minutes and login is
 * limited to 5 POSTs per 20 s. Only ONE test uses a wrong password on the real TEST_EMAIL, and it
 * logs in correctly straight afterwards. Everything else uses an unknown email or never submits.
 * The lockout and rate-limit paths themselves are NOT automated here (need a throwaway account).
 */

const EMAIL = '#email';
const PASSWORD = '#pass';
const LOGIN_BTN = "button[name='login_user']";
const LOGOUT = '#lnkLogout';
const PROFILE_DROPDOWN = '#lnkProfile';
const NEW_BOOKING = '#lnkNewBooking';

const BAD_CREDENTIALS_MSG = /Email or Password not found/i;
const UNKNOWN_EMAIL = `no-such-user-${Date.now()}@example.com`;
const BASE = env.appUrl.split('#')[0];

async function expectOnLoginScreen(page: Page): Promise<void> {
  await expect(page.locator(EMAIL)).toBeVisible();
  await expect(page.locator(PASSWORD)).toBeVisible();
}

async function login(page: Page): Promise<void> {
  await page.goto(env.appUrl);
  await new LoginPage(page).loginWithDefaultCredentials();
  await expect(page.locator(NEW_BOOKING)).toBeVisible({ timeout: 30_000 });
}

test.describe('Auth: negative and navigation', () => {
  test.describe('login form validation', () => {
    test.beforeEach(async ({ page }) => {
      await page.goto(env.appUrl);
      await expectOnLoginScreen(page);
    });

    test('Log In is disabled when both fields are empty', async ({ page }) => {
      await expect(page.locator(LOGIN_BTN)).toBeDisabled();
    });

    test('empty password keeps Log In disabled', async ({ page }) => {
      await page.locator(EMAIL).fill(env.testEmail);
      await expect(page.locator(LOGIN_BTN)).toBeDisabled();
    });

    test('empty email keeps Log In disabled', async ({ page }) => {
      await page.locator(PASSWORD).fill('Whatever1!');
      await expect(page.locator(LOGIN_BTN)).toBeDisabled();
    });

    test('malformed email shows "This field is invalid."', async ({ page }) => {
      await page.locator(EMAIL).fill('not-an-email');
      await page.locator(PASSWORD).click();
      await expect(page.getByText('This field is invalid.')).toBeVisible();
      await expect(page.locator(LOGIN_BTN)).toBeDisabled();
    });

    test('password under 6 characters shows the minimum-length message', async ({ page }) => {
      await page.locator(EMAIL).fill(env.testEmail);
      await page.locator(PASSWORD).fill('abc');
      await page.locator(EMAIL).click();
      await expect(page.getByText('Minimum 6 characters required.')).toBeVisible();
      await expect(page.locator(LOGIN_BTN)).toBeDisabled();
    });
  });

  test.describe('rejected credentials', () => {
    test('unknown email is rejected and the user stays on the login screen', async ({ page }) => {
      await page.goto(env.appUrl);
      await page.locator(EMAIL).fill(UNKNOWN_EMAIL);
      await page.locator(PASSWORD).fill('Wrong-Pass1!');
      await page.locator(LOGIN_BTN).click();

      await expect(page.getByText(BAD_CREDENTIALS_MSG).first()).toBeVisible();
      await expectOnLoginScreen(page);
      await expect(page.locator(NEW_BOOKING)).toHaveCount(0);
    });

    test('wrong password for a real user is rejected, then a correct login still works', async ({
      page,
    }) => {
      await page.goto(env.appUrl);
      await page.locator(EMAIL).fill(env.testEmail);
      await page.locator(PASSWORD).fill(`${env.testPassword}-wrong`);
      await page.locator(LOGIN_BTN).click();

      // Either the toast, or the inline "incorrect password" error with attempts left.
      await expect(
        page.getByText(/Email or Password not found|incorrect password|attempt\(s\) left/i).first(),
      ).toBeVisible();
      await expectOnLoginScreen(page);
      await expect(page.locator(NEW_BOOKING)).toHaveCount(0);

      // One failure must not lock the account: a correct login succeeds immediately.
      await page.locator(EMAIL).fill(env.testEmail);
      await page.locator(PASSWORD).fill(env.testPassword);
      await page.locator(LOGIN_BTN).click();
      await expect(page.locator(NEW_BOOKING)).toBeVisible({ timeout: 30_000 });
    });
  });

  test.describe('unauthenticated navigation', () => {
    test('unknown URL goes to the login screen, not the 404 page', async ({ page }) => {
      await page.goto(`${BASE}#/this-route-does-not-exist`);
      await expect(page).toHaveURL(/#\/authenticate/);
      await expectOnLoginScreen(page);
      await expect(page.getByText('Page not found')).toHaveCount(0);
    });

    for (const route of ['booking-management', 'user-management', 'reports']) {
      test(`protected route #/${route} redirects to login when signed out`, async ({ page }) => {
        await page.goto(`${BASE}#/${route}`);
        await expect(page).toHaveURL(/#\/authenticate/);
        await expectOnLoginScreen(page);
      });
    }

    test('"Forgot your password?" opens the reset form', async ({ page }) => {
      await page.goto(env.appUrl);
      await page.locator("[name='lnkResetPass']").click();
      await expect(page).toHaveURL(/#\/reset/);
      await expect(page.getByText(/RESET PASSWORD FORM/i)).toBeVisible();
    });
  });

  test.describe('session', () => {
    test('after Log Out, Back does not expose the bookings list', async ({ page }) => {
      await login(page);

      await page.locator(PROFILE_DROPDOWN).click();
      await page.locator(LOGOUT).click();
      await expect(page).toHaveURL(/#\/authenticate/);
      await expectOnLoginScreen(page);

      await page.goBack();
      await expect(page.locator(NEW_BOOKING)).toHaveCount(0);
      await expect(page.locator(EMAIL)).toBeVisible();
    });

    test('a signed-in user opening the login URL lands on the bookings list', async ({ page }) => {
      await login(page);

      await page.goto(`${BASE}#/authenticate`);
      await expect(page).toHaveURL(/#\/booking-management/);
      await expect(page.locator(NEW_BOOKING)).toBeVisible();
    });
  });
});
