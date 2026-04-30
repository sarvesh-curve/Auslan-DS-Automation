import com.microsoft.playwright.*;

public class Login1 {

    // ============================================================
    // VARIABLES
    // ============================================================
    static Playwright playwright;
    static Browser browser;
    static BrowserContext context;
    static Page page;

    // ============================================================
    // LOCATORS
    // ============================================================
    static Locator email;
    static Locator password;
    static Locator loginBtn;

    // ============================================================
    // METHODS
    // ============================================================

    public static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    public static void initializeLocators() {
        email = page.locator("#email");
        password = page.locator("#pass");
        loginBtn = page.locator("button[name='login_user']");
    }

    public static void navigateToApplication() {
        page.navigate("http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout");
    }

    public static void login() {
        email.fill("sarvesh@curvetomorrow.com.au");
        password.fill("Curve@2025");
        loginBtn.click();
    }

    public static void verifyLoginSuccess() {
        System.out.println(page.title());
    }

    public static void tearDownBrowser() {
        context.close();
        page.close();
        playwright.close();
    }

    // ============================================================
    // MAIN - METHOD CALLS
    // ============================================================
    public static void main(String[] args) {

        // Setup
        setupBrowser();
        navigateToApplication();
        initializeLocators();

        // Perform Login
        login();

        // Verify Login
        verifyLoginSuccess();

        // Teardown
        tearDownBrowser();
    }
}
