import { test, expect } from '@playwright/test';
import {
  ACCOUNT_CREATED_MSG,
  ADDRESS,
  ADDRESS_SUGGESTION,
} from '../src/constants';
import { env } from '../src/config/env';
import { SignupPage } from '../src/pages/signup.page';

test.describe('NDIS Client Signup', () => {
  test('register a new NDIS client successfully', async ({ page }) => {
    await page.goto(env.appUrl);

    const signupPage = new SignupPage(page);

    await signupPage.clickBookWithUs();
    await signupPage.selectNdisClientSignup();

    await signupPage.fillPersonalDetails('AutoNDIS', 'AuttLastName', env.signupPassword);
    await signupPage.fillEmail();

    await signupPage.selectCommunicationPreference(2);
    await signupPage.selectLocationPreference('VIC');

    await signupPage.fillAddress(ADDRESS, ADDRESS_SUGGESTION);

    await signupPage.selectPlanManaged('self_managed');
    await signupPage.clickSameBillingDetails();

    await signupPage.acceptTermsAndConditions(1);
    await signupPage.clickRegister();

    expect(await signupPage.isRegistrationSuccessful(ACCOUNT_CREATED_MSG)).toBe(true);
  });
});
