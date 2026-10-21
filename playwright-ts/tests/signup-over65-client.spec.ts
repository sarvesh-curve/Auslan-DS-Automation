import { test, expect } from '@playwright/test';
import {
  ACCOUNT_CREATED_MSG,
  ADDRESS,
  ADDRESS_SUGGESTION,
  TEST_PASSWORD,
} from '../src/constants';
import { env } from '../src/config/env';
import { SignupPage } from '../src/pages/signup.page';

test.describe('Over 65 Client Signup', () => {
  test('register a new Over 65 client successfully', async ({ page }) => {
    await page.goto(env.appUrl);

    const signupPage = new SignupPage(page);

    await signupPage.clickBookWithUs();
    await signupPage.selectOver65ClientSignup();

    await signupPage.fillPersonalDetails('Auto65+', 'Aut0LastName', TEST_PASSWORD);
    await signupPage.fillDateOfBirth();
    await signupPage.fillEmail();

    await signupPage.selectCommunicationPreference(2);
    await signupPage.selectLocationPreference('VIC');
    await signupPage.fillAddress(ADDRESS, ADDRESS_SUGGESTION);

    await signupPage.acceptTermsAndConditionsLast();
    await signupPage.clickRegister();

    expect(await signupPage.isRegistrationSuccessful(ACCOUNT_CREATED_MSG)).toBe(true);
  });
});
