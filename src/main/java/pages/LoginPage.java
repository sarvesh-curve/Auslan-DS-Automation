package pages;

import base.BasePage;
import com.microsoft.playwright.Page;
import constants.AppConstants;

public class LoginPage extends BasePage {

    // Locators
    private static final String EMAIL_SELECTOR = "#email";
    private static final String PASSWORD_SELECTOR = "#pass";
    private static final String LOGIN_BTN_SELECTOR = "button[name='login_user']";

    public LoginPage(Page page) {
        super(page);
    }

    public LoginPage enterEmail(String email) {
        fillElement(EMAIL_SELECTOR, email);
        System.out.println("Entered email: " + email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        fillElement(PASSWORD_SELECTOR, password);
        System.out.println("Entered password");
        return this;
    }

    public DashboardPage clickLoginButton() {
        clickElement(LOGIN_BTN_SELECTOR);
        System.out.println("Clicked login button");
        return new DashboardPage(page);
    }

    // Fluent interface method
    public DashboardPage login(String email, String password) {
        return enterEmail(email)
                .enterPassword(password)
                .clickLoginButton();
    }

    public DashboardPage loginWithDefaultCredentials() {
        return login(AppConstants.TEST_EMAIL, AppConstants.TEST_PASSWORD);
    }

    public String getPageTitle() {
        return page.title();
    }
}
