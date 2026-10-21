import { Page } from '@playwright/test';

export class BasePage {
  constructor(protected readonly page: Page) {}

  async navigateTo(url: string): Promise<void> {
    await this.page.goto(url);
  }

  async waitForTimeout(milliseconds: number): Promise<void> {
    await this.page.waitForTimeout(milliseconds);
  }

  async clickElement(selector: string): Promise<void> {
    await this.page.locator(selector).click();
  }

  async fillElement(selector: string, text: string): Promise<void> {
    await this.page.locator(selector).fill(text);
  }

  async getTextContent(selector: string): Promise<string | null> {
    return this.page.locator(selector).textContent();
  }

  async waitForElement(selector: string): Promise<void> {
    await this.page.locator(selector).waitFor();
  }

  async isElementVisible(selector: string): Promise<boolean> {
    return this.page.locator(selector).isVisible();
  }

  async isElementEnabled(selector: string): Promise<boolean> {
    return this.page.locator(selector).isEnabled();
  }

  async pressKey(key: string): Promise<void> {
    await this.page.keyboard.press(key);
  }

  async clickButtonByRole(text: string): Promise<void> {
    await this.page.getByRole('button', { name: text }).click();
  }

  async getPageTitle(): Promise<string> {
    return this.page.title();
  }
}
