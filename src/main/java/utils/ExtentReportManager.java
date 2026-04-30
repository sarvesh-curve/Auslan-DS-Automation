package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final int MAX_REPORTS_TO_KEEP = 5;
    
    public static ExtentReports createInstance() {
        // Clean up old reports before creating new one
        cleanupOldReports();
        
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = "test-output/ExtentReport_" + timestamp + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);
        
        // Configure the report
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Playwright Test Automation Report");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTimeStampFormat("dd-MMM-yyyy HH:mm:ss");
        
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // System information
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("OS Version", System.getProperty("os.version"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User Name", System.getProperty("user.name"));
        extent.setSystemInfo("Browser", ConfigReader.getProperty("browser", "chromium"));
        extent.setSystemInfo("Headless Mode", ConfigReader.getProperty("headless", "false"));
        extent.setSystemInfo("Environment", "Canary");
        
        return extent;
    }
    
    /**
     * Clean up old ExtentReports, keeping only the latest 5
     * This runs before creating a new report, so we keep MAX_REPORTS_TO_KEEP - 1
     * to ensure total stays at MAX_REPORTS_TO_KEEP after new report is created
     */
    private static void cleanupOldReports() {
        try {
            File testOutputDir = new File("test-output");
            if (!testOutputDir.exists()) {
                return;
            }
            
            // Get all ExtentReport HTML files
            File[] reportFiles = testOutputDir.listFiles((dir, name) -> 
                name.startsWith("ExtentReport_") && name.endsWith(".html"));
            
            if (reportFiles == null || reportFiles.length < MAX_REPORTS_TO_KEEP) {
                return;
            }
            
            // Sort files by last modified date (newest first)
            Arrays.sort(reportFiles, Comparator.comparingLong(File::lastModified).reversed());
            
            // Delete older reports (keep MAX_REPORTS_TO_KEEP - 1 to account for new report being created)
            int deletedCount = 0;
            for (int i = MAX_REPORTS_TO_KEEP - 1; i < reportFiles.length; i++) {
                if (reportFiles[i].delete()) {
                    deletedCount++;
                    System.out.println("🗑️  Deleted old report: " + reportFiles[i].getName());
                }
            }
            
            if (deletedCount > 0) {
                System.out.println("✅ Cleaned up " + deletedCount + " old report(s). Keeping latest " + MAX_REPORTS_TO_KEEP + " reports.");
            }
            
        } catch (Exception e) {
            System.err.println("⚠️  Failed to cleanup old reports: " + e.getMessage());
        }
    }
    
    public static ExtentReports getExtent() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }
    
    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }
    
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    public static void removeTest() {
        extentTest.remove();
    }
}
