import { expect, Locator, Page } from '@playwright/test';
import { MEDIUM_WAIT } from '../constants';
import { BasePage } from './base.page';

/** "Invite Interpreters" panel on the booking job screen (Recommended list, search, invite). */
export class InterpreterInvitePage extends BasePage {
  private static readonly ROWS = 'tbody tr:has(md-checkbox)';
  private static readonly RECOMMENDED = '#lnk-recommended-interpreters';
  private static readonly SEARCH = '#search';
  private static readonly INVITE_BTN = "button[name='inviteBtn']";
  private static readonly SAVE_BTN = "button[name='saveBtn']";
  private static readonly NEARBY_API = '/nearby_interpreters';
  private static readonly INVITE_API = '/invite_interpreters';

  constructor(page: Page) {
    super(page);
  }

  private rows(): Locator {
    return this.page.locator(InterpreterInvitePage.ROWS);
  }

  /** Rows whose name cell matches exactly (names are unique within the list). */
  private row(name: string): Locator {
    return this.rows().filter({ hasText: name });
  }

  private isNearbyResponse(recommended?: boolean) {
    return (response: { url(): string; status(): number }) => {
      const url = response.url();
      if (!url.includes(InterpreterInvitePage.NEARBY_API) || response.status() !== 200) {
        return false;
      }
      return recommended === undefined || url.includes('recommended=true') === recommended;
    };
  }

  async waitForList(): Promise<this> {
    await this.rows().first().waitFor({ timeout: 60_000 });
    return this;
  }

  async isRecommendedOn(): Promise<boolean> {
    return this.page.locator(`${InterpreterInvitePage.RECOMMENDED} input`).isChecked();
  }

  /** Flips the toggle (if needed) and waits for the refetched list. */
  async setRecommended(on: boolean): Promise<this> {
    if ((await this.isRecommendedOn()) === on) {
      return this;
    }
    const refetched = this.page.waitForResponse(this.isNearbyResponse(on));
    await this.page.locator(`${InterpreterInvitePage.RECOMMENDED} .toggle-slider`).click();
    await refetched;
    await this.waitForList();
    return this;
  }

  /** Clicks the toggle without waiting on each refetch (stress case). */
  async clickRecommendedToggle(times: number): Promise<void> {
    const slider = this.page.locator(`${InterpreterInvitePage.RECOMMENDED} .toggle-slider`);
    for (let i = 0; i < times; i++) {
      await slider.click();
    }
  }

  async names(): Promise<string[]> {
    return this.rows().evaluateAll((rs) =>
      rs.map((r) => (r.querySelectorAll('td')[2]?.textContent ?? '').trim()),
    );
  }

  async count(name: string): Promise<number> {
    return this.row(name).count();
  }

  async isVisibleInList(name: string): Promise<boolean> {
    return (await this.count(name)) > 0;
  }

  /** The status cell shows an icon (img with a src) once someone is invited/assigned. */
  async hasStatusIcon(name: string): Promise<boolean> {
    const img = this.row(name).first().locator('td').nth(1).locator('img');
    if (!(await img.count())) return false;
    const src = await img.first().getAttribute('src');
    return !!src && src.trim().length > 0;
  }

  async search(text: string): Promise<this> {
    const refetched = this.page.waitForResponse(this.isNearbyResponse());
    await this.page.locator(InterpreterInvitePage.SEARCH).fill(text);
    await this.pressKey('Enter');
    await refetched.catch(() => undefined);
    await this.waitForTimeout(MEDIUM_WAIT);
    return this;
  }

  async clearSearch(): Promise<this> {
    await this.page.locator(InterpreterInvitePage.SEARCH).fill('');
    await this.pressKey('Enter');
    await this.waitForTimeout(MEDIUM_WAIT);
    return this;
  }

  async selectInterpreters(names: string[]): Promise<this> {
    for (const name of names) {
      const checkbox = this.row(name).first().locator('md-checkbox').first();
      await checkbox.scrollIntoViewIfNeeded();
      await checkbox.click();
    }
    return this;
  }

  /** Select the given interpreters, send the invite and wait for the list to refresh. */
  async invite(names: string[]): Promise<this> {
    await this.selectInterpreters(names);
    const inviteBtn = this.page.locator(InterpreterInvitePage.INVITE_BTN);
    await expect(inviteBtn).not.toHaveClass(/half-opacity/);
    await inviteBtn.click();
    // Invite is staged; the invitation is only sent when the booking is saved.
    const saveBtn = this.page.locator(InterpreterInvitePage.SAVE_BTN);
    await expect(saveBtn).toBeEnabled();
    const invited = this.page.waitForResponse(
      (r) => r.url().includes(InterpreterInvitePage.INVITE_API) && r.ok(),
    );
    await saveBtn.click();
    await invited;
    await this.waitForList();
    await this.waitForTimeout(MEDIUM_WAIT);
    return this;
  }

  async isSaveEnabled(): Promise<boolean> {
    return this.page.locator(InterpreterInvitePage.SAVE_BTN).isEnabled();
  }
}
