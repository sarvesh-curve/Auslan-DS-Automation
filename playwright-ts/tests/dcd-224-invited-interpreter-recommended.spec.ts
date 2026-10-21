import { test, expect, Page } from '@playwright/test';
import { InterpreterInvitePage } from '../src/pages/interpreter-invite.page';
import { loginAndCreateBooking, openNewestBooking } from '../src/utils/booking-flow';

// DCD-224: invited interpreters must stay visible in the list while "Recommended only" is ON.
// Interpreters seeded on canary for the suite client.
const ALI = 'Ali Interpreter';
const NIKHIL = 'Nikhil Interpreter';
const CHAUDHARI = 'Interpreter Chaudhari';

async function expectInvitedOnce(invitePage: InterpreterInvitePage, name: string) {
  await expect.poll(() => invitePage.count(name)).toBe(1);
  expect(await invitePage.hasStatusIcon(name)).toBe(true);
}

test.describe.serial('DCD-224 invited interpreter with Recommended ON', () => {
  let page: Page;
  let invitePage: InterpreterInvitePage;

  test.beforeAll(async ({ browser }) => {
    page = await browser.newPage();
    await loginAndCreateBooking(page);
    invitePage = await openNewestBooking(page);
    await invitePage.setRecommended(true);
  });

  test.afterAll(async () => {
    await page.close();
  });

  test('TC01 invited interpreter remains visible when Recommended is ON', async () => {
    expect(await invitePage.isRecommendedOn()).toBe(true);
    expect(await invitePage.hasStatusIcon(ALI)).toBe(false);

    await invitePage.invite([ALI]);

    expect(await invitePage.isRecommendedOn()).toBe(true);
    await expectInvitedOnce(invitePage, ALI);
  });

  test('TC02 multiple invited interpreters all remain visible', async () => {
    await invitePage.invite([NIKHIL]);

    await expectInvitedOnce(invitePage, ALI);
    await expectInvitedOnce(invitePage, NIKHIL);
  });

  test('TC03 invited interpreters visible after navigating away and back', async () => {
    await page.locator('#lnkBooking').click();
    invitePage = await openNewestBooking(page);

    expect(await invitePage.isRecommendedOn()).toBe(true);
    await expectInvitedOnce(invitePage, ALI);
    await expectInvitedOnce(invitePage, NIKHIL);
  });

  test('TC04 Recommended OFF still shows the invited interpreters', async () => {
    await invitePage.setRecommended(false);

    await expectInvitedOnce(invitePage, ALI);
    await expectInvitedOnce(invitePage, NIKHIL);
  });

  test('TC05 toggling OFF then ON keeps invited interpreters listed', async () => {
    await invitePage.setRecommended(false);
    await invitePage.setRecommended(true);

    await expectInvitedOnce(invitePage, ALI);
    await expectInvitedOnce(invitePage, NIKHIL);
  });

  test('TC06 search with Recommended ON returns the invited interpreter', async () => {
    await invitePage.search('Ali');

    await expectInvitedOnce(invitePage, ALI);
    expect(await invitePage.isVisibleInList(CHAUDHARI)).toBe(false);

    await invitePage.clearSearch();
    await expectInvitedOnce(invitePage, ALI);
  });

  test('TC13 rapid Recommended toggling keeps invited rows, no duplicates', async () => {
    await invitePage.clickRecommendedToggle(8);
    await page.waitForLoadState('networkidle');
    await invitePage.waitForList();

    expect(await invitePage.isRecommendedOn()).toBe(true);
    await expectInvitedOnce(invitePage, ALI);
    await expectInvitedOnce(invitePage, NIKHIL);
  });
});

test.describe.serial('DCD-224 bulk invite', () => {
  let page: Page;
  let invitePage: InterpreterInvitePage;

  test.beforeAll(async ({ browser }) => {
    page = await browser.newPage();
    await loginAndCreateBooking(page);
    invitePage = await openNewestBooking(page);
    await invitePage.setRecommended(true);
  });

  test.afterAll(async () => {
    await page.close();
  });

  test('TC09 bulk invite keeps every selected interpreter in the Recommended list', async () => {
    const selected = [ALI, NIKHIL, CHAUDHARI];
    await invitePage.invite(selected);

    expect(await invitePage.isRecommendedOn()).toBe(true);
    for (const name of selected) {
      await expectInvitedOnce(invitePage, name);
    }
  });
});
