import { test, expect } from '@playwright/test';
import { env } from '../src/config/env';
import { LoginPage } from '../src/pages/login.page';

test.describe('Login', () => {
  test('user can login successfully with valid credentials', async ({ page }) => {
    await page.goto(env.appUrl);

    const loginPage = new LoginPage(page);
    const dashboardPage = await loginPage.loginWithDefaultCredentials();

    const pageTitle = await dashboardPage.getPageTitle();
    expect(pageTitle).toBeTruthy();
  });
});
