import { Page } from '@playwright/test';
import { env } from '../config/env';
import { BasePage } from './base.page';
import { DashboardPage } from './dashboard.page';

export class LoginPage extends BasePage {
  private static readonly EMAIL = '#email';
  private static readonly PASSWORD = '#pass';
  private static readonly LOGIN_BTN = "button[name='login_user']";

  constructor(page: Page) {
    super(page);
  }

  async enterEmail(email: string): Promise<this> {
    await this.fillElement(LoginPage.EMAIL, email);
    return this;
  }

  async enterPassword(password: string): Promise<this> {
    await this.fillElement(LoginPage.PASSWORD, password);
    return this;
  }

  async clickLoginButton(): Promise<DashboardPage> {
    await this.clickElement(LoginPage.LOGIN_BTN);
    return new DashboardPage(this.page);
  }

  async login(email: string, password: string): Promise<DashboardPage> {
    await this.enterEmail(email);
    await this.enterPassword(password);
    return this.clickLoginButton();
  }

  async loginWithDefaultCredentials(): Promise<DashboardPage> {
    return this.login(env.testEmail, env.testPassword);
  }
}
