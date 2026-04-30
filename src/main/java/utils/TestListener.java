package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class TestListener implements ITestListener {
    private static ExtentReports extent;

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\n========================================");
        System.out.println("🚀 STARTING TEST: " + result.getMethod().getMethodName());
        System.out.println("========================================");
        
        // Create test in Extent Report
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        
        // Add test description if available
        if (result.getMethod().getDescription() != null) {
            test.info(result.getMethod().getDescription());
        }
        
        ExtentReportManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("\n========================================");
        System.out.println("✅ TEST PASSED: " + result.getMethod().getMethodName());
        System.out.println("Duration: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        System.out.println("========================================\n");
        
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.PASS, MarkupHelper.createLabel("TEST PASSED", ExtentColor.GREEN));
        test.pass("Test Duration: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("\n========================================");
        System.out.println("❌ TEST FAILED: " + result.getMethod().getMethodName());
        System.out.println("Error: " + result.getThrowable().getMessage());
        System.out.println("Duration: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        System.out.println("========================================\n");
        
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.FAIL, MarkupHelper.createLabel("TEST FAILED", ExtentColor.RED));
        test.fail(result.getThrowable());
        
        // Attach screenshot if available
        String screenshotPath = "test-output/screenshots/" + result.getMethod().getMethodName() + "_*.png";
        File screenshotDir = new File("test-output/screenshots/");
        if (screenshotDir.exists()) {
            File[] screenshots = screenshotDir.listFiles((dir, name) -> 
                name.startsWith(result.getMethod().getMethodName()) && name.endsWith(".png"));
            
            if (screenshots != null && screenshots.length > 0) {
                try {
                    String base64Screenshot = encodeFileToBase64(screenshots[0].getAbsolutePath());
                    test.addScreenCaptureFromBase64String(base64Screenshot, "Failed Screenshot");
                } catch (IOException e) {
                    test.info("Screenshot capture failed: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("\n========================================");
        System.out.println("⏭️  TEST SKIPPED: " + result.getMethod().getMethodName());
        System.out.println("========================================\n");
        
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.SKIP, MarkupHelper.createLabel("TEST SKIPPED", ExtentColor.YELLOW));
        test.skip(result.getThrowable());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🎯 TEST SUITE STARTED: " + context.getName());
        System.out.println("Total Tests: " + context.getAllTestMethods().length);
        System.out.println("=".repeat(80) + "\n");
        
        // Initialize Extent Reports
        extent = ExtentReportManager.createInstance();
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 TEST SUITE COMPLETED: " + context.getName());
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("Total Duration: " + (context.getEndDate().getTime() - context.getStartDate().getTime()) + "ms");
        System.out.println("=".repeat(80) + "\n");
        
        // Flush Extent Reports
        if (extent != null) {
            extent.flush();
            String reportPath = getLatestExtentReport();
            System.out.println("📊 Extent Report generated: " + reportPath);
            
            // Send email with report
            String testSummary = buildTestSummary(context);
            EmailUtil.sendExtentReport(reportPath, testSummary);
        }
    }
    
    /**
     * Get the latest ExtentReport file path
     */
    private String getLatestExtentReport() {
        try {
            File testOutputDir = new File("test-output");
            File[] reportFiles = testOutputDir.listFiles((dir, name) -> 
                name.startsWith("ExtentReport_") && name.endsWith(".html"));
            
            if (reportFiles != null && reportFiles.length > 0) {
                // Sort by last modified date (newest first)
                java.util.Arrays.sort(reportFiles, java.util.Comparator.comparingLong(File::lastModified).reversed());
                return reportFiles[0].getAbsolutePath();
            }
        } catch (Exception e) {
            System.err.println("Failed to find latest report: " + e.getMessage());
        }
        return "test-output/ExtentReport_latest.html";
    }
    
    /**
     * Build test summary for email
     */
    private String buildTestSummary(ITestContext context) {
        int total = context.getAllTestMethods().length;
        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        long duration = context.getEndDate().getTime() - context.getStartDate().getTime();
        
        StringBuilder summary = new StringBuilder();
        summary.append("Test Suite: ").append(context.getName()).append("\n");
        summary.append("Total Tests: ").append(total).append("\n");
        summary.append("✅ Passed: ").append(passed).append("\n");
        summary.append("❌ Failed: ").append(failed).append("\n");
        summary.append("⏭️  Skipped: ").append(skipped).append("\n");
        summary.append("⏱️  Duration: ").append(duration).append("ms (").append(duration / 1000).append("s)\n");
        summary.append("Success Rate: ").append(String.format("%.2f", (passed * 100.0 / total))).append("%\n");
        
        return summary.toString();
    }
    
    private String encodeFileToBase64(String filePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
