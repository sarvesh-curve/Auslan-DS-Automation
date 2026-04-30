import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import org.testng.Assert;

public class NdisBooking {

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
    static Locator newBookingBtn;
    static Locator clientName;
    static Locator yesImAccountHolder;
    static Locator yesBestContactPerson;
    static Locator noOfInterpreter;
    static Locator dateOfAppointment;
    static Locator useProfileAddress;
    static Locator startTime;
    static Locator endTime;
    static Locator natureOfAppointment;
    static Locator whatWillBeDiscussed;
    static Locator notesForInterpreter;
    static Locator termsAndCondition;
    static Locator finishBtn;
    static Locator successMsg;
    static Locator dsQServiceDropdown;
    static Locator dsqGlobal;
    static Locator descOrder;

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
        newBookingBtn = page.locator("a[id='lnkNewBooking']");
        clientName = page.locator("input[placeholder='Start typing...']");
        yesImAccountHolder = page.locator("label[for='same_yes']");
        yesBestContactPerson = page.locator("label[for='is_same_yes']");
        noOfInterpreter = page.locator("input[name='AuslanField']");
        dateOfAppointment = page.locator("input[name='dpDate']");
        startTime = page.locator("input[name='dpEventDate']");
        endTime = page.locator("input[name='dpEventEndTime']");
        natureOfAppointment = page.locator("id=natureOfAppointment");
        whatWillBeDiscussed = page.locator("id=specificAppointmentTypesNew");
        notesForInterpreter = page.locator("id=specialInstructions");
        termsAndCondition = page.locator("Label[name='tnc']");
        successMsg = page.locator("div.notification__text");
        dsQServiceDropdown = page.locator("ul.dsq-service-dropdown");
        dsqGlobal = page.locator(".deaf-global");
        descOrder = page.getByText("Job");
    }

    public static void navigateToApplication() {
        page.navigate("http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout");
    }

    public static void login() {
        email.fill("sarvesh@curvetomorrow.com.au");
        password.fill("Curve@2025");
        loginBtn.click();
        System.out.println(page.title());
    }

    public static void clickNewBooking() {
        newBookingBtn.click();
    }

    public static void selectClient() {
        clientName.click();
        clientName.page().keyboard().type("Sarvesh");

        String suggestion = "Sarvesh newndis - NDIS";
        page.locator(".ui-autocomplete-items span",
                new Page.LocatorOptions().setHasText(suggestion)).click();

        clientName.press("Tab");
    }

    public static void selectAccountHolderOptions() {
        yesImAccountHolder.click();
        yesBestContactPerson.click();
    }

    public static void selectBookingType() {
        clickBookingType("btnInterpreter");
        noOfInterpreter.fill(String.valueOf(1));
    }

    public static void fillAppointmentDetails() {
        dateOfAppointment.click();
        dateOfAppointment.page().keyboard().type("28/04/2026");

        useProfileAddress = page.getByRole(AriaRole.LINK).getByText("Use address on my profile");
        useProfileAddress.click();

        startTime.waitFor();
        startTime.click();
        startTime.fill("01:30 PM");

        endTime.waitFor();
        endTime.click();
        endTime.fill("02:10 PM");
        endTime.press("Tab");
    }

    public static void fillDetailsPage() {
        natureOfAppointment.selectOption(new SelectOption().setIndex(0));
        whatWillBeDiscussed.selectOption(new SelectOption().setIndex(1));
        notesForInterpreter.fill("No notes for interreter");
    }

    public static void acceptTermsAndFinish() {
        termsAndCondition.click();
        finishBtn = page.getByRole(AriaRole.BUTTON).getByText("FINISH");
        finishBtn.click();
        System.out.println("Clicked on finish button");
    }

    public static void verifyBookingCreation() {
        successMsg.waitFor();
        System.out.println(successMsg.textContent());
        String alertSuccessMsg = "The Booking has been created.";
        Assert.assertEquals(successMsg.textContent(), alertSuccessMsg);
    }

    public static void verifyBookingInDashboard() {
        dsQServiceDropdown.click();
        dsqGlobal.click();
        descOrder.click();
    }

    public static void clickBookingType(String buttonName) {
        page.locator(String.format("button[name='%s']", buttonName)).click();
    }

    public static void clickNextBtn() {
        Locator nextBtn = page.getByRole(AriaRole.BUTTON).getByText("Next");
        nextBtn.click();
    }

    // ============================================================
    // MAIN - METHOD CALLS
    // ============================================================
    public static void main(String[] args) throws InterruptedException {

        // Setup
        setupBrowser();
        navigateToApplication();
        initializeLocators();

        // Login
        login();

        // Create New Booking
        clickNewBooking();
        selectClient();
        selectAccountHolderOptions();
        clickNextBtn();

        // Booking Type
        selectBookingType();
        clickNextBtn();

        // Appointment Details
        fillAppointmentDetails();
        clickNextBtn();

        // Details Page
        fillDetailsPage();
        clickNextBtn();

        // Auslan User Page
        clickNextBtn();

        // Preferences Page
        clickNextBtn();

        // Billing Details
        acceptTermsAndFinish();

        // Verification
        verifyBookingCreation();
        verifyBookingInDashboard();
    }
}
