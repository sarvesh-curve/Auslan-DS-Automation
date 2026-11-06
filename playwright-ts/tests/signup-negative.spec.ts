import { test, expect, Page } from '@playwright/test';
import { ADDRESS, ADDRESS_SUGGESTION } from '../src/constants';
import { env } from '../src/config/env';
import { SignupPage } from '../src/pages/signup.page';

/**
 * Negative NDIS-client signup cases (product pack: 02-auth-and-access, 07-user-management-ui).
 * Validation cases never submit. The duplicate-email case submits with an email that already
 * exists (TEST_EMAIL), so no account is created.
 */

const REGISTER_BTN = "button[name='register_user']";

async function openNdisSignup(page: Page): Promise<SignupPage> {
  await page.goto(env.appUrl);
  const signup = new SignupPage(page);
  await signup.clickBookWithUs();
  await signup.selectNdisClientSignup();
  await expect(page.locator(REGISTER_BTN)).toBeVisible();
  return signup;
}

test.describe('NDIS signup: negative', () => {
  test('Register is disabled while the form is empty', async ({ page }) => {
    await openNdisSignup(page);
    await expect(page.locator(REGISTER_BTN)).toBeDisabled();
  });

  test('mismatched confirm password shows "Password does not match"', async ({ page }) => {
    await openNdisSignup(page);
    await page.locator("input[name='password']").fill('Zq9!kLm2x');
    await page.locator("input[name='certainPassword']").fill('Zq9!kLm2y');
    await page.locator("input[name='first_name']").click();
    await expect(page.getByText('Password does not match')).toBeVisible();
    await expect(page.locator(REGISTER_BTN)).toBeDisabled();
  });

  test('mobile under 10 digits shows "Minimum 10 digits required."', async ({ page }) => {
    await openNdisSignup(page);
    await page.locator("input[name='mobile']").fill('0400123');
    await page.locator("input[name='first_name']").click();
    await expect(page.getByText('Minimum 10 digits required.')).toBeVisible();
    await expect(page.locator(REGISTER_BTN)).toBeDisabled();
  });

  test('malformed email is flagged and keeps Register disabled', async ({ page }) => {
    await openNdisSignup(page);
    await page.locator("input[name='email']").fill('not-an-email');
    await page.locator("input[name='first_name']").click();
    await expect(page.getByText('Email is required')).toBeVisible();
    await expect(page.locator(REGISTER_BTN)).toBeDisabled();
  });

  test('a weak password leaves Register disabled even with every other field filled', async ({
    page,
  }) => {
    const signup = await openNdisSignup(page);
    await signup.fillPersonalDetails('AutoNeg', 'AuttLastName', 'abcdefgh');
    await signup.fillEmail();
    await signup.selectCommunicationPreference(2);
    await signup.selectLocationPreference('VIC');
    await signup.fillAddress(ADDRESS, ADDRESS_SUGGESTION);
    await signup.selectPlanManaged('self_managed');
    await signup.clickSameBillingDetails();
    await signup.acceptTermsAndConditions(1);
    await expect(page.locator(REGISTER_BTN)).toBeDisabled();
  });

  test('an already registered email is rejected and no account is created', async ({ page }) => {
    const signup = await openNdisSignup(page);
    await signup.fillPersonalDetails('AutoDup', 'AuttLastName', env.signupPassword);
    await signup.fillEmail(env.testEmail);
    await signup.selectCommunicationPreference(2);
    await signup.selectLocationPreference('VIC');
    await signup.fillAddress(ADDRESS, ADDRESS_SUGGESTION);
    await signup.selectPlanManaged('self_managed');
    await signup.clickSameBillingDetails();
    await signup.acceptTermsAndConditions(1);
    await signup.clickRegister();

    expect(await signup.getConfirmationMessage()).toContain('Email has already been taken');
    await expect(page.locator(REGISTER_BTN)).toBeVisible();
  });
});
