package base;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;
import utils.ExtentReportManager;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext browserContext;
    protected Page page;

    @BeforeMethod
    public void setUp() {
        try {
            System.out.println("=== Starting Browser Setup ===");
            
            // Initialize Playwright (let it use default extraction mechanism)
            System.out.println("Creating Playwright instance...");
            playwright = Playwright.create();
            System.out.println("✅ Playwright created successfully");
            
            // Launch browser based on config
            String browserType = ConfigReader.getProperty("browser", "chromium");
            boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless", "false"));
            System.out.println("Browser type: " + browserType + ", Headless: " + headless);
            
            System.out.println("Launching browser...");
            switch (browserType.toLowerCase()) {
                case "firefox":
                    browser = playwright.firefox().launch(
                            new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                case "webkit":
                    browser = playwright.webkit().launch(
                            new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
                default:
                    browser = playwright.chromium().launch(
                            new BrowserType.LaunchOptions().setHeadless(headless));
                    break;
            }
            System.out.println("✅ Browser launched successfully");
            
            // Create browser context and page
            System.out.println("Creating browser context...");
            browserContext = browser.newContext();
            System.out.println("✅ Browser context created");
            
            System.out.println("Creating new page...");
            page = browserContext.newPage();
            System.out.println("✅ Page created successfully");
            
            System.out.println("=== Browser setup completed successfully ===\n");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR during browser setup:");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException("Failed to setup browser", e);
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // Capture screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getName());
            
            // Log to Extent Report (if available)
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().fail("Test failed. Screenshot captured.");
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().pass("Test completed successfully.");
            }
        }
        
        // Close resources
        if (page != null) {
            page.close();
        }
        if (browserContext != null) {
            browserContext.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        System.out.println("Browser teardown completed");
        
        // Remove test from thread local
        ExtentReportManager.removeTest();
    }
    
    /**
     * Capture screenshot on test failure
     */
    private void captureScreenshot(String testName) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotName = testName + "_" + timestamp + ".png";
            String screenshotPath = "test-output/screenshots/" + screenshotName;
            
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(screenshotPath))
                    .setFullPage(true));
            
            System.out.println("📸 Screenshot captured: " + screenshotPath);
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
