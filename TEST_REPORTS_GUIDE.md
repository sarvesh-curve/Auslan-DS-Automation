# Test Reports Guide

## 📊 Test Reporting Features

Your Playwright automation framework now includes comprehensive test reporting with the following features:

### 1. Screenshot Capture on Failure
- Automatically captures full-page screenshots when any test fails
- Screenshots are saved in `test-output/screenshots/`
- Format: `TestName_YYYYMMDD_HHmmss.png`

### 2. TestNG HTML Reports
- Generated automatically after test execution
- Located in `test-output/` directory
- Includes test results, timings, and error details

### 3. Console Logging
- Real-time test execution status
- Test start/end notifications with duration
- Pass/Fail/Skip counts
- Suite-level summary

---

## 📁 Report Locations

After running tests, you'll find reports in these locations:

```
playwright/
├── test-output/
│   ├── screenshots/              # Failure screenshots
│   │   └── testName_timestamp.png
│   ├── index.html                # Main TestNG report
│   ├── testng-results.xml        # XML results
│   ├── emailable-report.html     # Summary email report
│   └── Suite/
│       └── All Tests.html        # Detailed test results
└── target/
    └── surefire-reports/         # Maven test reports
        ├── TEST-*.xml            # JUnit XML format
        └── *.txt                 # Text summaries
```

---

## 🚀 How to Run Tests and Generate Reports

### Option 1: Run All Tests
```bash
mvn clean test
```

### Option 2: Run Specific Test
```bash
mvn test -Dtest=LoginTest
```

### Option 3: Run via TestNG XML
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Option 4: Run in IntelliJ
1. Right-click on `testng.xml`
2. Select **Run**
3. Reports will be generated in `test-output/`

---

## 📧 Sharing Reports with Team

### 1. HTML Report (Recommended)
**File**: `test-output/emailable-report.html`

This is perfect for sharing via email:
- Open the file in a browser
- Contains summary of all test results
- Shows pass/fail counts and durations
- Includes failure stack traces

**To Share:**
- Attach the HTML file to an email
- Or host it on a shared drive/web server

### 2. Detailed Report
**File**: `test-output/index.html`

More comprehensive report:
- Click-through navigation
- Individual test details
- Timeline view
- Screenshots linked (if failures occurred)

### 3. XML Reports (For CI/CD)
**Files**: `target/surefire-reports/*.xml`

Standard JUnit XML format:
- Integrates with Jenkins, GitLab CI, GitHub Actions
- Machine-readable for automation tools

---

## 📸 Screenshot Handling

When a test fails:
1. **Automatic Capture**: Full-page screenshot is taken
2. **Console Log**: Path is printed: `📸 Screenshot captured: test-output/screenshots/testName_timestamp.png`
3. **File Naming**: Includes test name and timestamp for easy identification

**Example**:
```
test-output/screenshots/
├── LoginTest_20260430_151523.png
└── NdisBookingTest_20260430_151845.png
```

---

## 🎯 Console Output Example

```
================================================================================
🎯 TEST SUITE STARTED: Playwright Test Suite - Parallel Execution
Total Tests: 8
================================================================================

========================================
🚀 STARTING TEST: testSuccessfulLogin
========================================

✅ Login test completed successfully

========================================
✅ TEST PASSED: testSuccessfulLogin
Duration: 5234ms
========================================

========================================
🚀 STARTING TEST: testCreateNdisBooking
========================================

✅ NDIS booking test completed successfully

========================================
✅ TEST PASSED: testCreateNdisBooking
Duration: 15678ms
========================================

================================================================================
📊 TEST SUITE COMPLETED: Playwright Test Suite - Parallel Execution
Passed: 8
Failed: 0
Skipped: 0
Total Duration: 45234ms
================================================================================
```

---

## 🛠️ Customizing Reports

### Change Screenshot Location
Edit `BaseTest.java`:
```java
String screenshotPath = "your-custom-path/screenshots/" + screenshotName;
```

### Add Custom Logging
In your test methods:
```java
System.out.println("Custom log message");
```

### Modify TestNG Report
Edit `testng.xml` to add:
```xml
<suite name="Your Suite Name" verbose="10">
    <!-- verbose level: 0-10 (higher = more detailed) -->
</suite>
```

---

## 📝 Best Practices

1. **Clean Reports Before Each Run**
   ```bash
   rm -rf test-output/
   mvn clean test
   ```

2. **Archive Reports**
   - Save `test-output/` folder after each run
   - Name it with date/build number: `test-reports-2026-04-30/`

3. **Review Screenshots**
   - Check screenshots in `test-output/screenshots/` for failed tests
   - Screenshots show exact state when test failed

4. **Share with Team**
   - Zip the entire `test-output/` folder
   - Or share individual HTML reports
   - Include screenshots for failed tests

---

## 🔍 Analyzing Test Results

### In emailable-report.html:
- **Green row**: Test passed ✅
- **Red row**: Test failed ❌
- **Yellow row**: Test skipped ⏭️

### In index.html:
- Click on test name to see details
- View stack trace for failures
- Check execution timeline
- Navigate through different test groups

### In Console:
- Real-time progress updates
- Immediate feedback on pass/fail
- Duration for each test
- Overall suite statistics

---

## 🎉 Summary

Your test framework now includes:
- ✅ Automatic screenshot capture on failure
- ✅ Detailed HTML reports
- ✅ Console logging with test status
- ✅ Multiple report formats (HTML, XML, TXT)
- ✅ Easy sharing with team members
- ✅ CI/CD integration ready

**All reports are generated automatically after every test run!**
