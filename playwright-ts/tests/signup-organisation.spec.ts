import { test, expect } from '@playwright/test';
import {
  ADDRESS,
  ADDRESS_SUGGESTION,
  ORG_REGISTERED_MSG,
  TEST_PASSWORD,
} from '../src/constants';
import { env } from '../src/config/env';
import { SignupPage } from '../src/pages/signup.page';

test.describe('Organisation Signup', () => {
  test('register a new organisation successfully', async ({ page }) => {
    await page.goto(env.appUrl);

    const signupPage = new SignupPage(page);

    await signupPage.clickBookWithUs();
    await signupPage.selectOrganisationSignup();

    await signupPage.fillOrganisationDetails('Autoorg');
    await signupPage.selectLocationPreference('VIC');
    await signupPage.fillAddress(ADDRESS, ADDRESS_SUGGESTION);
    await signupPage.clickSameBillingDetails();

    await signupPage.fillContactDetails('autoorg', 'autoorg');
    await signupPage.fillAccountUserDetails('Org+', 'Aut0LastName', TEST_PASSWORD);

    await signupPage.acceptTermsAndConditions(1);
    await signupPage.clickRegister();

    expect(await signupPage.isRegistrationSuccessful(ORG_REGISTERED_MSG)).toBe(true);
  });
});
