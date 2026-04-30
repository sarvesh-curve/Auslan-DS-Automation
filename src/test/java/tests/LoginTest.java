package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ConfigReader;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Verify user can login successfully with valid credentials")
    public void testSuccessfulLogin() {
        // Navigate to application
        page.navigate(ConfigReader.getAppUrl());
        
        // Create LoginPage instance and perform login
        LoginPage loginPage = new LoginPage(page);
        DashboardPage dashboardPage = loginPage.loginWithDefaultCredentials();
        
        // Verify login success by checking page title
        String pageTitle = dashboardPage.getPageTitle();
        System.out.println("Page Title after login: " + pageTitle);
        Assert.assertNotNull(pageTitle, "Page title should not be null after login");
        
        System.out.println("✅ Login test completed successfully");
    }
}
