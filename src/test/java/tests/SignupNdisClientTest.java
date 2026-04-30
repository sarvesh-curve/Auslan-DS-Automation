package tests;

import base.BaseTest;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SignupPage;
import utils.ConfigReader;

public class SignupNdisClientTest extends BaseTest {

    @Test(groups = {"regression"}, description = "Register a new NDIS client and verify account is created successfully")
    public void testSignupNdisClient() {
        // Navigate to application
        page.navigate(ConfigReader.getAppUrl());

        // Create SignupPage instance
        SignupPage signupPage = new SignupPage(page);

        // Navigate to NDIS Client Signup
        signupPage.clickBookWithUs()
                .selectNdisClientSignup();

        // Fill Personal Details
        signupPage.fillPersonalDetails("AutoNDIS", "AuttLastName", AppConstants.TEST_PASSWORD)
                .fillEmail();

        // Select Preferences
        signupPage.selectCommunicationPreference(2)
                .selectLocationPreference("VIC");

        // Fill Address
        signupPage.fillAddress(AppConstants.ADDRESS, AppConstants.ADDRESS_SUGGESTION);

        // Select Plan and Billing
        signupPage.selectPlanManaged("self_managed")
                .clickSameBillingDetails();

        // Accept Terms and Register
        signupPage.acceptTermsAndConditions(1)
                .clickRegister();

        // Verify Registration
        boolean isRegistered = signupPage.isRegistrationSuccessful(AppConstants.ACCOUNT_CREATED_MSG);
        Assert.assertTrue(isRegistered, "NDIS client registration should be successful");

        System.out.println("✅ NDIS client signup test completed successfully");
    }
}
