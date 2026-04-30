package pages;

import base.BasePage;
import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import constants.AppConstants;
import utils.TestDataGenerator;

public class SignupPage extends BasePage {

    // Locators
    private static final String BOOK_WITH_US_BTN_SELECTOR = "a[href='#/register/pre']";
    private static final String NDIS_CLIENT_BTN_SELECTOR = "a[name='btnClient']";
    private static final String OVER65_CLIENT_BTN_SELECTOR = "a[name='btnInterpreter']";
    private static final String ORG_SIGNUP_BTN_SELECTOR = "a[name='btnOrganization']";
    
    // Personal Details
    private static final String FIRST_NAME_SELECTOR = "input[name='first_name']";
    private static final String LAST_NAME_SELECTOR = "input[name='last_name']";
    private static final String PASSWORD_SELECTOR = "input[name='password']";
    private static final String CONFIRM_PASSWORD_SELECTOR = "input[name='certainPassword']";
    private static final String PHONE_SELECTOR = "input[name='mobile']";
    private static final String EMAIL_SELECTOR = "input[name='email']";
    private static final String DOB_SELECTOR = "input[name='date_of_birth']";
    
    // Organization Details
    private static final String ORG_NAME_SELECTOR = "input[name='business_name']";
    private static final String CONTACT_FIRST_NAME_SELECTOR = "input[name='cn_first_name']";
    private static final String CONTACT_LAST_NAME_SELECTOR = "input[name='cn_last_name']";
    private static final String CONTACT_EMAIL_SELECTOR = "input[name='cn_email']";
    private static final String CONTACT_PHONE_SELECTOR = "input[name='cn_phone']";
    
    // Other Fields
    private static final String COMM_PREFERENCE_SELECTOR = "select[name='comm_pref']";
    private static final String LOCATION_PREFERENCE_SELECTOR = "select[name='location_pref']";
    private static final String FIND_ADDRESS_SELECTOR = "input[name='search_address']";
    private static final String PLAN_MANAGED_SELECTOR = "select[name='how_would_you_like_to_receive_notes']";
    private static final String BILLING_CHECKBOX_SELECTOR = "div.mat-checkbox-inner-container";
    private static final String REGISTER_BTN_SELECTOR = "button[name='register_user']";
    private static final String CONFIRM_MSG_SELECTOR = "div.notification__text";

    public SignupPage(Page page) {
        super(page);
    }

    public SignupPage clickBookWithUs() {
        waitForElement(BOOK_WITH_US_BTN_SELECTOR);
        clickElement(BOOK_WITH_US_BTN_SELECTOR);
        System.out.println("Clicked Book With Us button");
        return this;
    }

    public SignupPage selectNdisClientSignup() {
        clickElement(NDIS_CLIENT_BTN_SELECTOR);
        System.out.println("Selected NDIS Client Signup");
        return this;
    }

    public SignupPage selectOver65ClientSignup() {
        clickElement(OVER65_CLIENT_BTN_SELECTOR);
        System.out.println("Selected Over 65 Client Signup");
        return this;
    }

    public SignupPage selectOrganisationSignup() {
        clickElement(ORG_SIGNUP_BTN_SELECTOR);
        System.out.println("Selected Organisation Signup");
        return this;
    }

    public SignupPage fillPersonalDetails(String firstNamePrefix, String lastName, String password) {
        Locator firstName = page.locator(FIRST_NAME_SELECTOR);
        firstName.waitFor();
        firstName.click();
        firstName.fill(TestDataGenerator.generateFirstName(firstNamePrefix));

        Locator lastNameField = page.locator(LAST_NAME_SELECTOR);
        lastNameField.waitFor();
        lastNameField.click();
        lastNameField.fill(lastName);

        Locator passwordField = page.locator(PASSWORD_SELECTOR);
        passwordField.waitFor();
        passwordField.click();
        passwordField.fill(password);

        Locator confirmPasswordField = page.locator(CONFIRM_PASSWORD_SELECTOR);
        confirmPasswordField.waitFor();
        confirmPasswordField.click();
        confirmPasswordField.fill(password);

        Locator phoneField = page.locator(PHONE_SELECTOR);
        phoneField.waitFor();
        phoneField.click();
        phoneField.fill(TestDataGenerator.generatePhoneNumber());

        System.out.println("Filled personal details");
        return this;
    }

    public SignupPage fillEmail() {
        Locator emailField = page.locator(EMAIL_SELECTOR);
        emailField.waitFor();
        emailField.click();
        emailField.fill(TestDataGenerator.generateEmail());
        System.out.println("Filled email");
        return this;
    }

    public SignupPage fillDateOfBirth() {
        Locator dobField = page.locator(DOB_SELECTOR);
        dobField.waitFor();
        dobField.click();
        dobField.page().keyboard().type(TestDataGenerator.generateDOBForOver65());
        System.out.println("Filled date of birth");
        return this;
    }

    public SignupPage selectCommunicationPreference(int index) {
        page.locator(COMM_PREFERENCE_SELECTOR)
                .selectOption(new SelectOption().setIndex(index));
        System.out.println("Selected communication preference");
        return this;
    }

    public SignupPage selectLocationPreference(String value) {
        page.locator(LOCATION_PREFERENCE_SELECTOR)
                .selectOption(new SelectOption().setValue(value));
        System.out.println("Selected location preference: " + value);
        return this;
    }

    public SignupPage fillAddress(String address, String suggestion) {
        Locator addressField = page.locator(FIND_ADDRESS_SELECTOR).first();
        addressField.waitFor();
        addressField.click();
        addressField.page().keyboard().type(address, new Keyboard.TypeOptions().setDelay(100));

        page.locator("text=" + suggestion).first().click();
        System.out.println("Filled address");
        return this;
    }

    public SignupPage selectPlanManaged(String value) {
        page.locator(PLAN_MANAGED_SELECTOR)
                .selectOption(new SelectOption().setValue(value));
        System.out.println("Selected plan managed: " + value);
        return this;
    }

    public SignupPage clickSameBillingDetails() {
        page.locator(BILLING_CHECKBOX_SELECTOR).first().click();
        System.out.println("Clicked same billing details checkbox");
        return this;
    }

    public SignupPage acceptTermsAndConditions(int index) {
        page.locator(BILLING_CHECKBOX_SELECTOR).nth(index).click();
        System.out.println("Accepted terms and conditions");
        return this;
    }
    
    public SignupPage acceptTermsAndConditionsLast() {
        page.locator(BILLING_CHECKBOX_SELECTOR).last().click();
        System.out.println("Accepted terms and conditions (last checkbox)");
        return this;
    }

    public SignupPage fillOrganisationDetails(String orgNamePrefix) {
        Locator orgName = page.locator(ORG_NAME_SELECTOR);
        orgName.waitFor();
        orgName.click();
        orgName.fill(TestDataGenerator.generateFirstName(orgNamePrefix));
        System.out.println("Filled organisation name");
        return this;
    }

    public SignupPage fillContactDetails(String firstNamePrefix, String lastName) {
        Locator contactFirstName = page.locator(CONTACT_FIRST_NAME_SELECTOR);
        contactFirstName.waitFor();
        contactFirstName.click();
        contactFirstName.fill(TestDataGenerator.generateFirstName(firstNamePrefix));

        Locator contactLastName = page.locator(CONTACT_LAST_NAME_SELECTOR);
        contactLastName.waitFor();
        contactLastName.click();
        contactLastName.fill(lastName);

        Locator contactEmail = page.locator(CONTACT_EMAIL_SELECTOR);
        contactEmail.waitFor();
        contactEmail.click();
        contactEmail.fill(TestDataGenerator.generateEmail());

        Locator contactPhone = page.locator(CONTACT_PHONE_SELECTOR);
        contactPhone.waitFor();
        contactPhone.click();
        contactPhone.fill(TestDataGenerator.generatePhoneNumber());

        System.out.println("Filled contact details");
        return this;
    }

    public SignupPage fillAccountUserDetails(String firstNamePrefix, String lastName, String password) {
        fillPersonalDetails(firstNamePrefix, lastName, password);
        
        Locator commPref = page.locator(COMM_PREFERENCE_SELECTOR);
        commPref.selectOption(new SelectOption().setIndex(2));

        fillEmail();

        Locator phone = page.locator(PHONE_SELECTOR);
        phone.waitFor();
        phone.click();
        phone.fill(TestDataGenerator.generatePhoneNumber());

        System.out.println("Filled account user details");
        return this;
    }

    public SignupPage clickRegister() {
        clickElement(REGISTER_BTN_SELECTOR);
        System.out.println("Clicked Register button");
        return this;
    }

    public String getConfirmationMessage() {
        Locator confirmMsg = page.locator(CONFIRM_MSG_SELECTOR);
        confirmMsg.waitFor();
        String message = confirmMsg.textContent().trim();
        System.out.println("Confirmation message: " + message);
        return message;
    }

    public boolean isRegistrationSuccessful(String expectedMessage) {
        String actualMessage = getConfirmationMessage();
        return actualMessage.contains(expectedMessage);
    }
}
