package tests;

import base.BaseTest;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SignupPage;
import utils.ConfigReader;

public class SignupOver65ClientTest extends BaseTest {

    @Test(groups = {"regression"}, description = "Register a new Over 65 client and verify account is created successfully")
    public void testSignupOver65Client() {
        // Navigate to application
        page.navigate(ConfigReader.getAppUrl());

        // Create SignupPage instance
        SignupPage signupPage = new SignupPage(page);

        // Navigate to Over 65 Client Signup
        signupPage.clickBookWithUs()
                .selectOver65ClientSignup();

        // Fill Personal Details
        signupPage.fillPersonalDetails("Auto65+", "Aut0LastName", AppConstants.TEST_PASSWORD)
                .fillDateOfBirth()
                .fillEmail();

        // Select Preferences
        signupPage.selectCommunicationPreference(2)
                .selectLocationPreference("VIC");

        // Fill Address
        signupPage.fillAddress(AppConstants.ADDRESS, AppConstants.ADDRESS_SUGGESTION);

        // Accept Terms and Register (using the last checkbox for terms)
        signupPage.acceptTermsAndConditionsLast()
                .clickRegister();

        // Verify Registration
        boolean isRegistered = signupPage.isRegistrationSuccessful(AppConstants.ACCOUNT_CREATED_MSG);
        Assert.assertTrue(isRegistered, "Over 65 client registration should be successful");

        System.out.println("✅ Over 65 client signup test completed successfully");
    }
}
