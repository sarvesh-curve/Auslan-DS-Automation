import { defineConfig, devices } from '@playwright/test';
import dotenv from 'dotenv';
import path from 'path';

dotenv.config({ path: path.resolve(__dirname, '.env') });

const headless = process.env.HEADLESS !== 'false';

export default defineConfig({
  testDir: './tests',
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 1,
  workers: 1,
  reporter: [
    ['html', { open: 'never' }],
    ['list'],
    ['junit', { outputFile: 'test-results/junit.xml' }],
  ],
  timeout: 180_000,
  expect: { timeout: 15_000 },
  use: {
    baseURL:
      process.env.APP_URL ??
      'http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout',
    headless,
    screenshot: 'only-on-failure',
    trace: 'retain-on-failure',
    video: 'retain-on-failure',
    actionTimeout: 30_000,
    navigationTimeout: 60_000,
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
});
