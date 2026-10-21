import { Locator, Page } from '@playwright/test';
import {
  BOOKING_CREATED_MSG,
  MAX_APPOINTMENT_PAGE_ATTEMPTS,
  MAX_NEXT_ATTEMPTS,
  SHORT_WAIT,
} from '../constants';
import { BasePage } from './base.page';

export class BookingPage extends BasePage {
  private static readonly CLIENT_NAME = "input[placeholder='Start typing...']";
  private static readonly YES_ACCOUNT_HOLDER = "label[for='same_yes']";
  private static readonly YES_BEST_CONTACT = "label[for='is_same_yes']";
  private static readonly NO_OF_INTERPRETER = "input[name='AuslanField']";
  private static readonly DATE_OF_APPOINTMENT = "input[name='dpDate']";
  private static readonly START_TIME = "input[name='dpEventDate']";
  private static readonly END_TIME = "input[name='dpEventEndTime']";
  private static readonly NATURE_OF_APPOINTMENT = '#natureOfAppointment';
  private static readonly WHAT_WILL_BE_DISCUSSED = '#specificAppointmentTypesNew';
  private static readonly NOTES_FOR_INTERPRETER = '#specialInstructions';
  private static readonly TERMS_AND_CONDITION = "Label[name='tnc']";
  private static readonly SUCCESS_MSG = 'div.notification__text';
  private static readonly RECURRING = 'span.slider.round';
  private static readonly RECURRING_END_DATE = "input[placeholder='End date']";
  private static readonly AUSLAN_USER = "label[for='currentUserIsClientYes']";

  constructor(page: Page) {
    super(page);
  }

  async selectClient(searchText: string, suggestion: string): Promise<this> {
    const clientName = this.page.locator(BookingPage.CLIENT_NAME);
    await clientName.click();
    await this.page.keyboard.type(searchText);
    await this.page
      .locator('.ui-autocomplete-items span', { hasText: suggestion })
      .click();
    await clientName.press('Tab');
    return this;
  }

  async selectOrganisation(): Promise<this> {
    const selectOrg = this.page.getByText('Organisation').last();
    await selectOrg.waitFor();
    await selectOrg.click();
    return this;
  }

  async selectAccountHolderOptions(): Promise<this> {
    await this.clickElement(BookingPage.YES_ACCOUNT_HOLDER);
    await this.clickElement(BookingPage.YES_BEST_CONTACT);
    return this;
  }

  async selectBookingType(bookingType: string): Promise<this> {
    await this.page.locator(`button[name='${bookingType}']`).click();
    return this;
  }

  async enterNumberOfInterpreters(count: number): Promise<void> {
    await this.fillElement(BookingPage.NO_OF_INTERPRETER, String(count));
  }

  /**
   * Navigate open datepicker to target month/year and click the day.
   * Skips adjacent-month (.old / .new) cells that cause strict-mode duplicates.
   */
  private async pickDateFromOpenCalendar(date: string): Promise<void> {
    const [dayStr, monthStr, yearStr] = date.split('/');
    const day = String(parseInt(dayStr, 10));
    const monthIndex = parseInt(monthStr, 10) - 1;
    const year = parseInt(yearStr, 10);
    const monthNames = [
      'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December',
    ];
    const targetHeader = `${monthNames[monthIndex]} ${year}`;

    const calendar = this.page.locator('table').filter({
      has: this.page.getByRole('columnheader', { name: 'Su' }),
    });
    if (!(await calendar.isVisible().catch(() => false))) {
      return;
    }

    // Header + nav live as siblings above the table in bootstrap-datepicker
    const picker = calendar.locator('xpath=ancestor::*[contains(@class,"datepicker")][1]');
    const root = (await picker.count()) ? picker : calendar.locator('xpath=..');

    for (let i = 0; i < 24; i++) {
      const headerText = ((await root.textContent()) ?? '').replace(/\s+/g, ' ');
      if (headerText.includes(targetHeader)) {
        break;
      }
      // Prefer next; if we've gone past, still use next (future dates)
      const next = root.locator('th.next, .next, a').filter({ hasText: /.*/ }).nth(1);
      const nextAlt = root.getByRole('link').nth(1);
      if (await nextAlt.isVisible().catch(() => false)) {
        await nextAlt.click();
      } else if (await next.isVisible().catch(() => false)) {
        await next.click();
      } else {
        break;
      }
      await this.waitForTimeout(200);
    }

    const inMonthDay = calendar
      .locator('td.day:not(.old):not(.new), td:not(.old):not(.new)')
      .getByRole('link', { name: day, exact: true });
    if (await inMonthDay.count()) {
      await inMonthDay.first().click();
    } else {
      await calendar.getByRole('link', { name: day, exact: true }).last().click();
    }
  }

  async fillAppointmentDate(date: string): Promise<this> {
    const dateField = this.page.locator(BookingPage.DATE_OF_APPOINTMENT);
    await dateField.click();
    await dateField.fill('');
    await dateField.fill(date);
    // Prefer keeping the typed value; only use calendar if fill did not stick
    await this.page.keyboard.press('Escape');
    await dateField.press('Tab');
    const value = (await dateField.inputValue()).trim();
    if (value !== date) {
      await dateField.click();
      await this.pickDateFromOpenCalendar(date);
    }
    return this;
  }

  async selectRecurringBtn(): Promise<void> {
    await this.clickElement(BookingPage.RECURRING);
  }

  async enterRecurringEndDate(date: string): Promise<void> {
    const enterEndDate = this.page.locator(BookingPage.RECURRING_END_DATE);
    await enterEndDate.click();
    await enterEndDate.fill(date);
    await this.pickDateFromOpenCalendar(date);
  }

  async useProfileAddress(): Promise<this> {
    await this.page
      .getByRole('link', { name: 'Use address on my profile' })
      .click({ force: true });
    return this;
  }

  async fillStartTime(time: string): Promise<this> {
    const startTime = this.page.locator(BookingPage.START_TIME);
    await startTime.waitFor();
    await startTime.click();
    await startTime.fill(time);
    return this;
  }

  async fillEndTime(time: string): Promise<void> {
    const endTime = this.page.locator(BookingPage.END_TIME);
    await endTime.waitFor();
    await endTime.click();
    await endTime.fill(time);
    await endTime.press('Tab');
  }

  async selectNatureOfAppointment(index: number): Promise<this> {
    await this.page.locator(BookingPage.NATURE_OF_APPOINTMENT).selectOption({ index });
    return this;
  }

  async selectWhatWillBeDiscussed(index: number): Promise<this> {
    await this.page.locator(BookingPage.WHAT_WILL_BE_DISCUSSED).selectOption({ index });
    return this;
  }

  async fillNotesForInterpreter(notes: string): Promise<this> {
    await this.fillElement(BookingPage.NOTES_FOR_INTERPRETER, notes);
    return this;
  }

  async selectAuslanUser(): Promise<this> {
    const radio = this.page.locator(BookingPage.AUSLAN_USER);
    if (!(await radio.isChecked())) {
      await radio.waitFor();
      await radio.click();
    }
    return this;
  }

  async acceptTermsAndConditions(): Promise<this> {
    await this.clickElement(BookingPage.TERMS_AND_CONDITION);
    return this;
  }

  async clickFinish(): Promise<this> {
    await this.page.getByRole('button', { name: 'FINISH' }).click();
    return this;
  }

  async clickNext(): Promise<void> {
    const nextBtn = this.page.getByRole('button', { name: /Next/i });
    await nextBtn.waitFor();
    await this.waitForTimeout(SHORT_WAIT);

    if (!(await nextBtn.isEnabled())) {
      throw new Error('Next button is disabled');
    }

    await nextBtn.click();
  }

  async clickNextMultipleTimes(times: number): Promise<void> {
    for (let i = 0; i < times; i++) {
      await this.clickNext();
    }
  }

  async clickNextTillAppointmentDetailsPage(): Promise<void> {
    const dateOfAppointment = this.page.locator(BookingPage.DATE_OF_APPOINTMENT);
    for (let i = 0; i < MAX_APPOINTMENT_PAGE_ATTEMPTS; i++) {
      if (await dateOfAppointment.isVisible()) {
        return;
      }
      await this.clickNext();
    }
  }

  async repeatNextUntilVisible(targetElement: Locator): Promise<void> {
    for (let i = 0; i < MAX_NEXT_ATTEMPTS; i++) {
      if (await targetElement.isVisible()) {
        return;
      }
      await this.clickNext();
    }
  }

  async getSuccessMessage(): Promise<string> {
    const successMsg = this.page.locator(BookingPage.SUCCESS_MSG);
    await successMsg.waitFor();
    return (await successMsg.textContent()) ?? '';
  }

  async isBookingCreatedSuccessfully(): Promise<boolean> {
    const message = await this.getSuccessMessage();
    return message.includes(BOOKING_CREATED_MSG);
  }

  /** Shared interpreter booking wizard steps after client selection. */
  async completeInterpreterBookingFlow(appointmentDate: string): Promise<void> {
    await this.selectBookingType('btnInterpreter');
    await this.enterNumberOfInterpreters(1);
    await this.clickNext();

    await this.fillAppointmentDate(appointmentDate);
    await this.useProfileAddress();
    await this.fillStartTime('01:30 PM');
    await this.fillEndTime('02:10 PM');
    await this.clickNext();

    await this.selectNatureOfAppointment(0);
    await this.selectWhatWillBeDiscussed(1);
    await this.fillNotesForInterpreter('No notes for interpreter');
    await this.clickNext();
  }
}
