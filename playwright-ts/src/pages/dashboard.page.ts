import { expect, Page } from '@playwright/test';
import { CLIENT_NAME, MEDIUM_WAIT, SHORT_WAIT } from '../constants';
import { BasePage } from './base.page';
import { BookingDetailsPage } from './booking-details.page';
import { BookingPage } from './booking.page';

export class DashboardPage extends BasePage {
  private static readonly NEW_BOOKING_BTN = "a[id='lnkNewBooking']";
  private static readonly DSQ_SERVICE_DROPDOWN = 'ul.dsq-service-dropdown';
  private static readonly DSQ_GLOBAL = '.deaf-global';
  private static readonly CLIENT_FILTER = "input[name='client_name']";
  private static readonly SUBURB_FILTER = "input[name='suburb']";
  private static readonly WAIT_FOR_ORG_NAME = "td[title='Sarvesh newndis']";
  private static readonly BOOKING_STATE_TRIGGER =
    "ul#booking-state > li > a[href='javascript:void(0)']:not([ng-reflect-ng-class])";
  private static readonly BOOKING_STATE_MENU = 'ul#booking-state ul.menu';

  constructor(page: Page) {
    super(page);
  }

  async clickNewBooking(): Promise<BookingPage> {
    await this.clickElement(DashboardPage.NEW_BOOKING_BTN);
    return new BookingPage(this.page);
  }

  async selectGlobalDSQService(): Promise<this> {
    await this.clickElement(DashboardPage.DSQ_SERVICE_DROPDOWN);
    await this.clickElement(DashboardPage.DSQ_GLOBAL);
    return this;
  }

  async filterByClientName(clientName: string = CLIENT_NAME): Promise<this> {
    const clientFilter = this.page.locator(DashboardPage.CLIENT_FILTER);
    await clientFilter.click();
    await clientFilter.fill(clientName);
    return this;
  }

  async clearSuburbFilter(): Promise<this> {
    await this.clickElement(DashboardPage.SUBURB_FILTER);
    return this;
  }

  async waitForClientToLoad(): Promise<this> {
    await this.page.locator(DashboardPage.WAIT_FOR_ORG_NAME).first().waitFor();
    await this.waitForTimeout(SHORT_WAIT);
    return this;
  }

  async selectBookingStatus(status: string): Promise<this> {
    await this.clickElement(DashboardPage.BOOKING_STATE_TRIGGER);
    const optionsList = this.page.locator(DashboardPage.BOOKING_STATE_MENU);
    await optionsList.waitFor({ state: 'visible' });
    await this.page
      .locator(`ul#booking-state ul.menu li a:has-text('${status}')`)
      .click();
    return this;
  }

  async sortBookingsByDescendingOrder(): Promise<this> {
    const descOrder = this.page.getByText('Job', { exact: true });
    await descOrder.click();
    await this.page.locator(DashboardPage.WAIT_FOR_ORG_NAME).first().waitFor();

    await Promise.all([
      this.page.waitForResponse(
        (response) =>
          response.url().includes('/api/v1/bookings') &&
          response.url().includes('sort=job') &&
          response.url().includes('direction=desc') &&
          response.status() === 200,
      ),
      descOrder.click(),
    ]);

    await this.waitForTimeout(MEDIUM_WAIT);
    return this;
  }

  async sortLinkBookingsByDescendingOrder(): Promise<void> {
    await this.waitForTimeout(SHORT_WAIT);
    const descOrder = this.page.getByText('Job', { exact: true });
    await descOrder.dblclick();
    await this.page.locator(DashboardPage.WAIT_FOR_ORG_NAME).first().waitFor();

    await Promise.all([
      this.page.waitForResponse(
        (response) =>
          response.url().includes('/api/v1/bookings') &&
          response.url().includes('sort=job') &&
          response.url().includes('direction=desc') &&
          response.status() === 200,
      ),
      descOrder.click(),
    ]);

    await this.waitForTimeout(SHORT_WAIT);
  }

  async selectFirstBooking(): Promise<BookingDetailsPage> {
    const firstBookingCell = this.page.locator('tbody tr').first().locator('td').nth(3);
    await firstBookingCell.click();
    await this.waitForTimeout(MEDIUM_WAIT);
    return new BookingDetailsPage(this.page);
  }

  async getFirstBookingId(): Promise<string> {
    return this.page.locator('tbody tr').first().locator('td').nth(1).innerText();
  }

  async getFirstBookingLinkId(): Promise<string> {
    const firstBookingLink = this.page.locator('td.bookingID span.linkId a').first();
    return (await firstBookingLink.innerText()).trim();
  }

  async clickFirstBookingLinkId(): Promise<string> {
    const firstBookingLink = this.page.locator('td.bookingID span.linkId a').first();
    const bookingNo = (await firstBookingLink.innerText()).trim();
    await firstBookingLink.click();
    await this.waitForTimeout(SHORT_WAIT);
    await expect(this.page.getByText(/Displaying .* Bookings/i)).toBeVisible();
    return bookingNo;
  }

  /** Shared dashboard prep used by cancel / duplicate / assign / update flows. */
  async prepareClientBookings(): Promise<this> {
    await this.selectGlobalDSQService();
    await this.filterByClientName();
    await this.clearSuburbFilter();
    await this.waitForClientToLoad();
    return this;
  }
}
