package tests;

import base.BaseTest;
import constants.AppConstants;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SignupPage;
import utils.ConfigReader;

public class SignupOrganisationTest extends BaseTest {

    @Test(groups = {"regression"}, description = "Register a new organisation and verify registration is successful")
    public void testSignupOrganisation() {
        // Navigate to application
        page.navigate(ConfigReader.getAppUrl());

        // Create SignupPage instance
        SignupPage signupPage = new SignupPage(page);

        // Navigate to Organisation Signup
        signupPage.clickBookWithUs()
                .selectOrganisationSignup();

        // Fill Organisation Details
        signupPage.fillOrganisationDetails("Autoorg")
                .selectLocationPreference("VIC");

        // Fill Address
        signupPage.fillAddress(AppConstants.ADDRESS, AppConstants.ADDRESS_SUGGESTION)
                .clickSameBillingDetails();

        // Fill Contact Details
        signupPage.fillContactDetails("autoorg", "autoorg");

        // Fill Account User Details
        signupPage.fillAccountUserDetails("Org+", "Aut0LastName", AppConstants.TEST_PASSWORD);

        // Accept Terms and Register
        signupPage.acceptTermsAndConditions(1)
                .clickRegister();

        // Verify Registration
        boolean isRegistered = signupPage.isRegistrationSuccessful(AppConstants.ORG_REGISTERED_MSG);
        Assert.assertTrue(isRegistered, "Organisation registration should be successful");

        System.out.println("✅ Organisation signup test completed successfully");
    }
}
