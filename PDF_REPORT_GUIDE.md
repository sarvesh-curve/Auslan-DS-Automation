# PDF & Enhanced HTML Report Guide

## 📊 ExtentReports - Professional Test Reports

Your framework now includes **ExtentReports** - a powerful reporting library that generates beautiful, interactive HTML reports with graphs, charts, and detailed test information.

---

## ✅ What's Included

### 1. Enhanced HTML Report with Graphs ✅
- **Pie Chart**: Pass/Fail/Skip ratio
- **Test Summary Dashboard**: Total counts at a glance
- **Timeline View**: Test execution timeline
- **Screenshots**: Embedded for failed tests
- **System Information**: Environment details
- **Test Categories**: Organized by test class
- **Pass/Fail Details**: Complete stack traces for failures

### 2. Features in the Report
- ✅ **Visual Graphs** - Pie charts showing test statistics
- ✅ **Pass/Fail/Skip Counts** - Prominent display on dashboard
- ✅ **Failure Reasons** - Complete error messages and stack traces
- ✅ **Screenshots** - Embedded inline for failed tests
- ✅ **Test Duration** - Execution time for each test
- ✅ **System Info** - OS, Browser, Java version, etc.
- ✅ **Responsive Design** - Works on all devices
- ✅ **Dark/Light Theme** - Toggle between themes

---

## 📁 Report Location

### ExtentReports HTML
**Path:**
```
/Users/SarveshChaudhari/Automation/playwright/test-output/ExtentReport_YYYY-MM-DD_HH-mm-ss.html
```

**Example:**
```
/Users/SarveshChaudhari/Automation/playwright/test-output/ExtentReport_2026-04-30_15-39-01.html
```

### Screenshots (if tests fail)
**Path:**
```
/Users/SarveshChaudhari/Automation/playwright/test-output/screenshots/
```

---

## 🚀 How to Generate the Report

### Run All Tests:
```bash
mvn clean test
```

### The Report Will Be Generated Automatically:
```
📊 Extent Report generated: test-output/ExtentReport_*.html
```

### Open the Report:
```bash
# Open the latest report
open test-output/ExtentReport_*.html

# Or navigate to test-output folder and double-click the HTML file
```

---

## 📊 What the Report Shows

### Dashboard View
```
╔════════════════════════════════════════╗
║  Test Execution Dashboard              ║
╠════════════════════════════════════════╣
║  Total: 8     [PIE CHART]             ║
║  ✅ Passed: 8                          ║
║  ❌ Failed: 0                          ║
║  ⏭️  Skipped: 0                         ║
║  Duration: 49.22s                      ║
╚════════════════════════════════════════╝
```

### Test List View
```
Test Name                    Status  Duration  Category
───────────────────────────────────────────────────────
testSuccessfulLogin          ✅ PASS  5.23s    LoginTest
testCreateNdisBooking        ✅ PASS  15.67s   NdisBookingTest
testDuplicateNdisBooking     ✅ PASS  18.45s   DuplicateNdisBookingTest
testCancelBooking            ✅ PASS  12.34s   CancelBookingTest
testUpdateAppointmentDate    ✅ PASS  26.62s   UpdateAppointmentDateTest
testSignupNdisClient         ✅ PASS  8.92s    SignupNdisClientTest
testSignupOrganisation       ✅ PASS  10.18s   SignupOrganisationTest
testSignupOver65Client       ✅ PASS  9.27s    SignupOver65ClientTest
```

### When a Test Fails:
```
Test Name: testFailedExample
Status: ❌ FAILED
Duration: 3.45s
Category: ExampleTest

Error Message:
───────────────────────────────────────────
org.openqa.selenium.NoSuchElementException: 
Unable to locate element: [id='loginButton']
───────────────────────────────────────────

Stack Trace:
[Full stack trace here...]

Screenshot:
[Embedded screenshot image]
```

### System Information Panel
```
Operating System:  macOS 14.3
OS Version:        14.3
Java Version:      21.0.7
User Name:         SarveshChaudhari
Browser:           chromium
Headless Mode:     false
Environment:       Canary
```

---

## 📧 Sharing with Team

### Option 1: Email the HTML Report
The HTML report is **self-contained** - it includes everything:
- All test results
- Embedded screenshots
- Graphs and charts
- CSS and JavaScript included

**To Share:**
1. Locate the report: `test-output/ExtentReport_*.html`
2. Attach to email
3. Recipients can open it directly in any browser

### Option 2: Host on Web Server
Upload the HTML file to:
- Internal company server
- Shared drive
- Cloud storage (Dropbox, Google Drive, etc.)
- Share the link

### Option 3: Convert to PDF

#### Method 1: Browser Print to PDF
1. Open the HTML report in Chrome/Edge
2. Press `Ctrl+P` (Windows) or `Cmd+P` (Mac)
3. Choose "Save as PDF"
4. Click "Save"

#### Method 2: Command Line (wkhtmltopdf)
```bash
# Install wkhtmltopdf (one-time)
brew install wkhtmltopdf

# Convert HTML to PDF
wkhtmltopdf test-output/ExtentReport_*.html TestReport.pdf
```

#### Method 3: Online Converter
- Upload HTML to https://www.html2pdf.com/
- Download the PDF

---

## 🎯 Key Report Features

### 1. Pie Chart
Shows visual breakdown of:
- ✅ Passed tests (Green)
- ❌ Failed tests (Red)
- ⏭️ Skipped tests (Yellow)

### 2. Test Summary
```
Total Tests:     8
Passed:          8 (100%)
Failed:          0 (0%)
Skipped:         0 (0%)
Pass Percentage: 100%
```

### 3. Failure Analysis
For each failed test:
- ❌ Test name
- 📝 Error message
- 📚 Full stack trace
- 📸 Screenshot (embedded)
- ⏱️ Test duration

### 4. Timeline
Shows when each test:
- Started
- Ended
- Total duration

### 5. Categories
Tests grouped by:
- Test class name
- Easy to filter and search

---

## 🔧 Customizing the Report

### Change Report Title
Edit `src/main/java/utils/ExtentReportManager.java`:
```java
sparkReporter.config().setDocumentTitle("Your Custom Title");
sparkReporter.config().setReportName("Your Report Name");
```

### Add More System Information
Edit `src/main/java/utils/ExtentReportManager.java`:
```java
extent.setSystemInfo("Build Number", "1.0.0");
extent.setSystemInfo("Tester", "Your Name");
extent.setSystemInfo("Test Environment", "QA");
```

### Change Theme
Edit `src/main/java/utils/ExtentReportManager.java`:
```java
sparkReporter.config().setTheme(Theme.DARK);  // or Theme.STANDARD
```

---

## 📊 Example Report Contents

### Pass Example:
```
✅ testSuccessfulLogin

PASSED

Test Duration: 5234ms

Logs:
- Test started
- Browser launched successfully
- Login successful
- Test completed successfully
```

### Fail Example:
```
❌ testInvalidLogin

FAILED

Test Duration: 3456ms

Error:
Expected: Login to fail with error message
Actual: Error message not displayed

Stack Trace:
java.lang.AssertionError: Expected error message not found
    at tests.LoginTest.testInvalidLogin(LoginTest.java:45)
    ...

Screenshot:
[Image showing the failed state]
```

---

## 💡 Tips for Best Reports

### 1. Run Clean Test
```bash
mvn clean test
```
This ensures old reports don't interfere.

### 2. Descriptive Test Names
Use clear test method names:
```java
@Test(description = "Verify user can login with valid credentials")
public void testSuccessfulLogin() { }
```

### 3. Add Logs in Tests
```java
ExtentReportManager.getTest().info("Step 1: Navigating to login page");
ExtentReportManager.getTest().info("Step 2: Entering credentials");
```

### 4. Review After Each Run
- Check the pie chart for quick status
- Review failed tests immediately
- Check screenshots for visual debugging

---

## 🎓 Understanding the Reports

### What Each Color Means:
- 🟢 **Green** = Passed
- 🔴 **Red** = Failed
- 🟡 **Yellow** = Skipped

### Report Sections:
1. **Dashboard** - Overall summary with pie chart
2. **Tests** - Detailed list of all tests
3. **Categories** - Tests grouped by class
4. **Timeline** - Execution timeline

---

## 📁 Complete File Locations

After running tests, you'll have:

```
test-output/
├── ExtentReport_2026-04-30_15-39-01.html    ← **Main Report with Graphs**
├── screenshots/
│   └── TestName_timestamp.png               ← Failure screenshots
├── index.html                                ← TestNG report
└── emailable-report.html                     ← TestNG summary
```

---

## ✅ Summary

You now have:
- ✅ **HTML Report with Graphs** - Beautiful visual report
- ✅ **Pie Charts** - Visual test statistics
- ✅ **Pass/Fail Counts** - Prominent display
- ✅ **Failure Reasons** - Complete error details
- ✅ **Embedded Screenshots** - Visual debugging
- ✅ **Easy to Share** - Self-contained HTML
- ✅ **PDF Convertible** - Multiple methods available

**The ExtentReport provides everything you need to share professional test results with your team!** 📊

---

## 🎉 Report Example Structure

```
╔══════════════════════════════════════════════════╗
║  PLAYWRIGHT TEST AUTOMATION REPORT               ║
╠══════════════════════════════════════════════════╣
║                                                  ║
║  [PIE CHART]    Total: 8                        ║
║   60% Green     Passed: 8                       ║
║   40% Red       Failed: 0                       ║
║                 Skipped: 0                      ║
║                                                  ║
║  Duration: 49.22 seconds                        ║
║  Pass Rate: 100%                                ║
║                                                  ║
╠══════════════════════════════════════════════════╣
║  System Information:                             ║
║  - OS: macOS 14.3                               ║
║  - Browser: Chromium                            ║
║  - Java: 21.0.7                                 ║
║  - Environment: Canary                          ║
╠══════════════════════════════════════════════════╣
║  Test Results:                                   ║
║  ✅ testSuccessfulLogin           (5.23s)       ║
║  ✅ testCreateNdisBooking         (15.67s)      ║
║  ✅ testDuplicateNdisBooking      (18.45s)      ║
║  ✅ testCancelBooking             (12.34s)      ║
║  ✅ testUpdateAppointmentDate     (26.62s)      ║
║  ✅ testSignupNdisClient          (8.92s)       ║
║  ✅ testSignupOrganisation        (10.18s)      ║
║  ✅ testSignupOver65Client        (9.27s)       ║
╚══════════════════════════════════════════════════╝
```

**Professional, shareable, and informative!** ✨
