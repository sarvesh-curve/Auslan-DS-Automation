import { expect, Page } from '@playwright/test';
import { LONG_WAIT, MEDIUM_WAIT, SHORT_WAIT } from '../constants';
import { BasePage } from './base.page';
import { BookingPage } from './booking.page';

export class BookingDetailsPage extends BasePage {
  private static readonly CANCEL_REASONS = 'select#cancel-reasons';
  private static readonly ACTIVE_STEP = '#steps nav a.active';
  private static readonly DATE_OF_APPOINTMENT = "input[name='dpDate']";
  private static readonly TERMS_AND_CONDITION = "Label[name='tnc']";
  private static readonly JOB_NUMBER = "td[data-title='JOB NUMBER']";
  private static readonly DATE = "td[data-title='DATE']";
  private static readonly INTERPRETER_SEARCH = '#search';
  private static readonly FIRST_INTERPRETER = "td:has-text('Sarvesh Interpreter')";
  private static readonly ASSIGN_BUTTON = "button[name='assignBtn'], button.assign-btn";
  private static readonly SAVE_BUTTON = "button[name='saveBtn']";

  constructor(page: Page) {
    super(page);
  }

  async clickBookingDetails(): Promise<this> {
    const bookingDetailsLink = this.page.getByText('Booking details');
    await bookingDetailsLink.waitFor();
    await bookingDetailsLink.click();
    return this;
  }

  async searchInterpreter(): Promise<this> {
    await this.waitForTimeout(SHORT_WAIT);
    await this.clickElement(BookingDetailsPage.INTERPRETER_SEARCH);
    await this.fillElement(BookingDetailsPage.INTERPRETER_SEARCH, 'Sarvesh');
    await this.pressKey('Enter');
    return this;
  }

  async selectInterpreter(): Promise<this> {
    await this.waitForTimeout(MEDIUM_WAIT);
    await this.waitForElement(BookingDetailsPage.FIRST_INTERPRETER);
    const row = this.page
      .locator('table tbody tr', { hasText: 'Sarvesh Interpreter' })
      .first();
    const cb = row.locator('md-checkbox').first();
    await cb.scrollIntoViewIfNeeded();
    await cb.click();
    return this;
  }

  async assignInterpreter(): Promise<this> {
    await this.waitForTimeout(MEDIUM_WAIT);

    const labelledAssign = this.page.locator('button').filter({ hasText: /^Assign$/ });
    const inviteAssign = this.page
      .locator('h2, h3, [role="heading"]')
      .filter({ hasText: 'Invite Interpreters' })
      .locator('button')
      .first();

    const clickAssign = async (locator: ReturnType<Page['locator']>) => {
      await locator.scrollIntoViewIfNeeded().catch(() => undefined);
      await expect(locator).toBeEnabled();
      await locator.click();
    };

    if (await labelledAssign.count()) {
      await clickAssign(labelledAssign.first());
    } else if (await inviteAssign.count()) {
      await clickAssign(inviteAssign);
    } else {
      await clickAssign(
        this.page.locator('button').filter({ hasText: /Assign/i }).first(),
      );
    }

    await this.waitForTimeout(MEDIUM_WAIT);
    const saveBtn = this.page.locator(BookingDetailsPage.SAVE_BUTTON);
    if (await saveBtn.isEnabled().catch(() => false)) {
      await saveBtn.scrollIntoViewIfNeeded();
      await this.waitForTimeout(MEDIUM_WAIT);
      await saveBtn.click();
      await this.waitForTimeout(MEDIUM_WAIT);
    }

    const assignedInterpreter = this.page.locator("a:has-text('Sarvesh Interpreter')");
    await assignedInterpreter.first().waitFor();
    await expect(assignedInterpreter.first()).toBeVisible();
    return this;
  }

  async clickCancelBooking(): Promise<this> {
    const cancelBtn = this.page.getByRole('button', { name: 'Cancel Booking' });
    await cancelBtn.waitFor();
    await cancelBtn.click();
    await this.waitForTimeout(MEDIUM_WAIT);

    // Linked-booking modal (newer UI) or legacy "Cancel this job" prompts
    const linkedOnly = this.page.getByRole('button', {
      name: 'Cancel only this booking',
    });
    const cancelThisJob = this.page.getByRole('button', { name: 'Cancel this job' });
    const cancelOnlyThisJob = this.page.getByRole('button', {
      name: 'Cancel only this job',
    });

    if (await linkedOnly.isVisible().catch(() => false)) {
      await linkedOnly.click();
    } else if (await cancelThisJob.isVisible().catch(() => false)) {
      await cancelThisJob.click();
    } else if (await cancelOnlyThisJob.isVisible().catch(() => false)) {
      await cancelOnlyThisJob.click();
    }

    const reasons = this.page.locator(BookingDetailsPage.CANCEL_REASONS);
    await reasons.waitFor({ state: 'visible', timeout: 30_000 });
    await reasons.selectOption('no reason given');

    const noChargeBtn = this.page.getByRole('button', {
      name: 'Cancelled No Charge',
    });
    await noChargeBtn.waitFor();
    await noChargeBtn.click();
    await this.waitForTimeout(MEDIUM_WAIT);
    return this;
  }

  async verifyBookingStatus(expectedStatus: string): Promise<void> {
    const activeStep = this.page.locator(BookingDetailsPage.ACTIVE_STEP);
    await expect(activeStep).toBeVisible();
    await expect(activeStep).toHaveText(expectedStatus);
  }

  async clickDuplicate(): Promise<BookingPage> {
    await this.waitForTimeout(SHORT_WAIT);
    const duplicateBtn = this.page.getByText('Duplicate');
    await expect(duplicateBtn).toBeEnabled();
    await duplicateBtn.click();
    await this.waitForTimeout(MEDIUM_WAIT);
    return new BookingPage(this.page);
  }

  async updateAppointmentDate(date: string): Promise<this> {
    const bookingPage = new BookingPage(this.page);
    await bookingPage.clickNextTillAppointmentDetailsPage();
    // Reuse create-booking date picker handling (navigates to correct month)
    await bookingPage.fillAppointmentDate(date);
    // Address is usually already populated on edit — only fill if profile link is needed
    const profileLink = this.page.getByRole('link', {
      name: 'Use address on my profile',
    });
    if (await profileLink.isVisible().catch(() => false)) {
      await profileLink.click({ force: true });
      await this.waitForTimeout(SHORT_WAIT);
    }
    return this;
  }

  async navigateToFinishPage(): Promise<this> {
    const bookingPage = new BookingPage(this.page);
    const finishBtn = this.page.getByRole('button', { name: /FINISH/i });

    // Advance wizard until FINISH is enabled (tnc label often stays attached-but-hidden)
    for (let i = 0; i < 10; i++) {
      if (await finishBtn.isVisible().catch(() => false)) {
        break;
      }
      await bookingPage.clickNext();
    }

    await finishBtn.waitFor({ state: 'visible', timeout: 30_000 });

    // Accept T&Cs via the checkbox input the label points at
    const policyCheckbox = this.page.locator('#policy, input[name="policy"], input[type="checkbox"]').last();
    if (await policyCheckbox.count()) {
      await policyCheckbox.check({ force: true }).catch(async () => {
        await policyCheckbox.evaluate((el: HTMLInputElement) => {
          el.checked = true;
          el.dispatchEvent(new Event('change', { bubbles: true }));
        });
      });
    } else {
      await this.page
        .locator(BookingDetailsPage.TERMS_AND_CONDITION)
        .evaluate((el: HTMLElement) => el.click());
    }

    await finishBtn.click();
    await this.waitForTimeout(LONG_WAIT);

    const updateBookingBtn = this.page.getByRole('button', {
      name: /Update this booking/i,
    });
    await this.waitForTimeout(MEDIUM_WAIT);
    if (await updateBookingBtn.isVisible().catch(() => false)) {
      await updateBookingBtn.click();
      await this.waitForTimeout(MEDIUM_WAIT);
    }
    return this;
  }

  async getJobNumber(): Promise<string> {
    // Land on Booking Info if the wizard left us elsewhere
    const bookingInfo = this.page.getByText('Booking Info', { exact: true });
    if (await bookingInfo.isVisible().catch(() => false)) {
      await bookingInfo.click();
      await this.waitForTimeout(MEDIUM_WAIT);
    }

    const selectors = [
      BookingDetailsPage.JOB_NUMBER,
      "td[data-title='Job']",
      "td[data-title='JOB']",
    ];
    for (const selector of selectors) {
      const cell = this.page.locator(selector).first();
      if (await cell.isVisible().catch(() => false)) {
        const text = (await cell.innerText()).trim();
        if (text) return text;
      }
    }

    // Last resort: first body cell in Job Details table
    const fallback = this.page.locator('table tbody tr').first().locator('td').first();
    await fallback.waitFor({ timeout: 15_000 });
    return (await fallback.innerText()).trim();
  }

  async getDate(): Promise<string> {
    const selectors = [BookingDetailsPage.DATE, "td[data-title='Date']", "td[data-title='DATE']"];
    for (const selector of selectors) {
      const cell = this.page.locator(selector).first();
      if (await cell.isVisible().catch(() => false)) {
        const text = (await cell.innerText()).trim();
        if (text) return text;
      }
    }
    return (await this.page.locator('table tbody tr').first().locator('td').nth(1).innerText()).trim();
  }
}
