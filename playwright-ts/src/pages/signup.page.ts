import { Page } from '@playwright/test';
import {
  generateDOBForOver65,
  generateEmail,
  generateFirstName,
  generatePhoneNumber,
} from '../utils/test-data';
import { BasePage } from './base.page';

export class SignupPage extends BasePage {
  private static readonly BOOK_WITH_US = "a[href='#/register/pre']";
  private static readonly NDIS_CLIENT = "a[name='btnClient']";
  private static readonly OVER65_CLIENT = "a[name='btnInterpreter']";
  private static readonly ORG_SIGNUP = "a[name='btnOrganization']";
  private static readonly FIRST_NAME = "input[name='first_name']";
  private static readonly LAST_NAME = "input[name='last_name']";
  private static readonly PASSWORD = "input[name='password']";
  private static readonly CONFIRM_PASSWORD = "input[name='certainPassword']";
  private static readonly PHONE = "input[name='mobile']";
  private static readonly EMAIL = "input[name='email']";
  private static readonly DOB = "input[name='date_of_birth']";
  private static readonly ORG_NAME = "input[name='business_name']";
  private static readonly CONTACT_FIRST_NAME = "input[name='cn_first_name']";
  private static readonly CONTACT_LAST_NAME = "input[name='cn_last_name']";
  private static readonly CONTACT_EMAIL = "input[name='cn_email']";
  private static readonly CONTACT_PHONE = "input[name='cn_phone']";
  private static readonly COMM_PREFERENCE = "select[name='comm_pref']";
  private static readonly LOCATION_PREFERENCE = "select[name='location_pref']";
  private static readonly FIND_ADDRESS = "input[name='search_address']";
  private static readonly PLAN_MANAGED =
    "select[name='how_would_you_like_to_receive_notes']";
  private static readonly BILLING_CHECKBOX = 'div.mat-checkbox-inner-container';
  private static readonly REGISTER_BTN = "button[name='register_user']";
  private static readonly CONFIRM_MSG = 'div.notification__text';

  constructor(page: Page) {
    super(page);
  }

  async clickBookWithUs(): Promise<this> {
    await this.waitForElement(SignupPage.BOOK_WITH_US);
    await this.clickElement(SignupPage.BOOK_WITH_US);
    return this;
  }

  async selectNdisClientSignup(): Promise<this> {
    await this.clickElement(SignupPage.NDIS_CLIENT);
    return this;
  }

  async selectOver65ClientSignup(): Promise<this> {
    await this.clickElement(SignupPage.OVER65_CLIENT);
    return this;
  }

  async selectOrganisationSignup(): Promise<this> {
    await this.clickElement(SignupPage.ORG_SIGNUP);
    return this;
  }

  async fillPersonalDetails(
    firstNamePrefix: string,
    lastName: string,
    password: string,
  ): Promise<this> {
    const firstName = this.page.locator(SignupPage.FIRST_NAME);
    await firstName.waitFor();
    await firstName.click();
    await firstName.fill(generateFirstName(firstNamePrefix));

    const lastNameField = this.page.locator(SignupPage.LAST_NAME);
    await lastNameField.waitFor();
    await lastNameField.click();
    await lastNameField.fill(lastName);

    const passwordField = this.page.locator(SignupPage.PASSWORD);
    await passwordField.waitFor();
    await passwordField.click();
    await passwordField.fill(password);

    const confirmPasswordField = this.page.locator(SignupPage.CONFIRM_PASSWORD);
    await confirmPasswordField.waitFor();
    await confirmPasswordField.click();
    await confirmPasswordField.fill(password);

    const phoneField = this.page.locator(SignupPage.PHONE);
    await phoneField.waitFor();
    await phoneField.click();
    await phoneField.fill(generatePhoneNumber());

    return this;
  }

  async fillEmail(): Promise<this> {
    const emailField = this.page.locator(SignupPage.EMAIL);
    await emailField.waitFor();
    await emailField.click();
    await emailField.fill(generateEmail());
    return this;
  }

  async fillDateOfBirth(): Promise<this> {
    const dobField = this.page.locator(SignupPage.DOB);
    await dobField.waitFor();
    await dobField.click();
    await this.page.keyboard.type(generateDOBForOver65());
    return this;
  }

  async selectCommunicationPreference(index: number): Promise<this> {
    await this.page.locator(SignupPage.COMM_PREFERENCE).selectOption({ index });
    return this;
  }

  async selectLocationPreference(value: string): Promise<this> {
    await this.page.locator(SignupPage.LOCATION_PREFERENCE).selectOption({ value });
    return this;
  }

  async fillAddress(address: string, suggestion: string): Promise<this> {
    const addressField = this.page.locator(SignupPage.FIND_ADDRESS).first();
    await addressField.waitFor();
    await addressField.click();
    await this.page.keyboard.type(address, { delay: 100 });
    await this.page.getByText(suggestion).first().click();
    return this;
  }

  async selectPlanManaged(value: string): Promise<this> {
    await this.page.locator(SignupPage.PLAN_MANAGED).selectOption({ value });
    return this;
  }

  async clickSameBillingDetails(): Promise<this> {
    await this.page.locator(SignupPage.BILLING_CHECKBOX).first().click();
    return this;
  }

  async acceptTermsAndConditions(index: number): Promise<this> {
    await this.page.locator(SignupPage.BILLING_CHECKBOX).nth(index).click();
    return this;
  }

  async acceptTermsAndConditionsLast(): Promise<this> {
    await this.page.locator(SignupPage.BILLING_CHECKBOX).last().click();
    return this;
  }

  async fillOrganisationDetails(orgNamePrefix: string): Promise<this> {
    const orgName = this.page.locator(SignupPage.ORG_NAME);
    await orgName.waitFor();
    await orgName.click();
    await orgName.fill(generateFirstName(orgNamePrefix));
    return this;
  }

  async fillContactDetails(firstNamePrefix: string, lastName: string): Promise<this> {
    const contactFirstName = this.page.locator(SignupPage.CONTACT_FIRST_NAME);
    await contactFirstName.waitFor();
    await contactFirstName.click();
    await contactFirstName.fill(generateFirstName(firstNamePrefix));

    const contactLastName = this.page.locator(SignupPage.CONTACT_LAST_NAME);
    await contactLastName.waitFor();
    await contactLastName.click();
    await contactLastName.fill(lastName);

    const contactEmail = this.page.locator(SignupPage.CONTACT_EMAIL);
    await contactEmail.waitFor();
    await contactEmail.click();
    await contactEmail.fill(generateEmail());

    const contactPhone = this.page.locator(SignupPage.CONTACT_PHONE);
    await contactPhone.waitFor();
    await contactPhone.click();
    await contactPhone.fill(generatePhoneNumber());

    return this;
  }

  async fillAccountUserDetails(
    firstNamePrefix: string,
    lastName: string,
    password: string,
  ): Promise<this> {
    await this.fillPersonalDetails(firstNamePrefix, lastName, password);
    await this.page.locator(SignupPage.COMM_PREFERENCE).selectOption({ index: 2 });
    await this.fillEmail();

    const phone = this.page.locator(SignupPage.PHONE);
    await phone.waitFor();
    await phone.click();
    await phone.fill(generatePhoneNumber());

    return this;
  }

  async clickRegister(): Promise<this> {
    await this.clickElement(SignupPage.REGISTER_BTN);
    return this;
  }

  async getConfirmationMessage(): Promise<string> {
    const confirmMsg = this.page.locator(SignupPage.CONFIRM_MSG);
    await confirmMsg.waitFor();
    return ((await confirmMsg.textContent()) ?? '').trim();
  }

  async isRegistrationSuccessful(expectedMessage: string): Promise<boolean> {
    const actualMessage = await this.getConfirmationMessage();
    return actualMessage.includes(expectedMessage);
  }
}
