# Auslan-DS-Playwright

**Auslan Digital Services - Playwright Java Automation Framework**

A production-ready test automation framework using Playwright Java with Page Object Model (POM), TestNG, and ExtentReports.

## 📋 Table of Contents
- [Quick Command Reference](#quick-command-reference)
- [Framework Architecture](#framework-architecture)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Configuration](#configuration)
- [Writing Tests](#writing-tests)
- [CI/CD Integration](#cicd-integration)

---

## 🚀 Quick Command Reference

**Most Common Commands:**

```bash
# Smoke Tests (Quick validation - 5 tests, ~30s)
mvn test -DsuiteXmlFile=testng-smoke.xml

# Regression Tests (Complete suite - 8 tests, ~50s)  
mvn test -DsuiteXmlFile=testng-regression.xml

# Run Single Test
mvn test -Dtest=LoginTest

# View Report
open test-output/ExtentReport_*.html

# Run All Tests in Parallel
mvn clean test

# Run Tests Sequentially
mvn test -DsuiteXmlFile=testng-sequential.xml
```

**See detailed commands below in [Running Tests](#running-tests) section.**

---

## 🏗️ Framework Architecture

This framework follows industry best practices:

- **Page Object Model (POM)** - Separation of test logic and page elements
- **TestNG** - Test execution and management
- **ExtentReports** - Professional HTML reports with graphs
- **Maven** - Dependency management and build lifecycle
- **Parallel Execution** - Faster test execution with 5 threads
- **Screenshot Capture** - Automatic screenshots on test failure
- **Dynamic Data Generation** - Future dates, unique emails, timestamps

---

## 📦 Prerequisites

Before running the framework, ensure you have:

- **Java JDK 21** or higher
- **Maven 3.6+**
- **Git** (for version control)
- **IntelliJ IDEA** (recommended) or any Java IDE

### Verify Installation:
```bash
java -version
# Output: java version "21.0.7" or higher

mvn -version
# Output: Apache Maven 3.x.x or higher
```

---

## 📁 Project Structure

```
playwright/
├── pom.xml                                 # Maven configuration
├── testng.xml                              # TestNG suite (parallel execution)
├── testng-sequential.xml                   # TestNG suite (sequential execution)
├── README.md                               # This file
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── base/
│   │   │   │   ├── BasePage.java          # Common page actions
│   │   │   │   └── BaseTest.java          # Test setup/teardown + screenshots
│   │   │   │
│   │   │   ├── pages/                     # Page Object classes
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── DashboardPage.java
│   │   │   │   ├── BookingPage.java
│   │   │   │   ├── BookingDetailsPage.java
│   │   │   │   └── SignupPage.java
│   │   │   │
│   │   │   ├── utils/                     # Utility classes
│   │   │   │   ├── ConfigReader.java     # Read config.properties
│   │   │   │   ├── TestDataGenerator.java # Generate dynamic test data
│   │   │   │   ├── TestListener.java     # TestNG listener for reporting
│   │   │   │   └── ExtentReportManager.java # ExtentReports management
│   │   │   │
│   │   │   └── constants/
│   │   │       └── AppConstants.java      # Application constants
│   │   │
│   │   └── resources/
│   │       └── config.properties          # Configuration file
│   │
│   └── test/
│       └── java/
│           └── tests/                     # Test classes
│               ├── LoginTest.java
│               ├── NdisBookingTest.java
│               ├── DuplicateNdisBookingTest.java
│               ├── CancelBookingTest.java
│               ├── UpdateAppointmentDateTest.java
│               ├── SignupNdisClientTest.java
│               ├── SignupOrganisationTest.java
│               └── SignupOver65ClientTest.java
│
├── test-output/                           # Test reports
│   ├── ExtentReport_*.html                # Main report with graphs
│   ├── screenshots/                       # Failure screenshots
│   ├── index.html                         # TestNG detailed report
│   └── emailable-report.html              # TestNG summary
│
├── target/                                # Maven build output
│   └── surefire-reports/                  # Maven test reports
│
└── old_tests/                             # Backup of original tests
```

---

## 🚀 Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd playwright
```

### 2. Install Dependencies
```bash
mvn clean install
```

This will:
- Download all Maven dependencies
- Extract Playwright driver
- Compile the project
- Install Playwright browsers

### 3. Verify Setup
```bash
mvn test -Dtest=LoginTest
```

If the test runs successfully, your setup is complete! ✅

---

## 🧪 Running Tests

### Option 1: Run Single Test

#### Command Line:
```bash
# Run a specific test class
mvn test -Dtest=LoginTest

# Run a specific test method
mvn test -Dtest=LoginTest#testSuccessfulLogin
```

#### IntelliJ IDEA:
1. Open the test file (e.g., `LoginTest.java`)
2. Right-click on the test class or method
3. Select **Run 'LoginTest'**

---

### Option 2: Run All Tests in Parallel

**Default Configuration:** 5 threads, method-level parallelism

#### Command Line:
```bash
# Run all tests in parallel (uses testng.xml)
mvn clean test
```

#### IntelliJ IDEA:
1. Right-click on `testng.xml`
2. Select **Run**

**What happens:**
- All 8 tests run in parallel
- Maximum 5 threads at a time
- Each test gets its own browser instance
- Faster execution (~50 seconds for all tests)

---

### Option 3: Run Tests Sequentially (Without Parallel)

#### Command Line:
```bash
# Run tests one by one
mvn test -DsuiteXmlFile=testng-sequential.xml
```

#### Or modify testng.xml temporarily:
```bash
# Edit testng.xml and change:
<suite name="..." parallel="false" thread-count="1">
```

Then run:
```bash
mvn clean test
```

**What happens:**
- Tests run one after another
- Slower but more stable
- Better for debugging
- Takes longer (~80-100 seconds for all tests)

---

### Option 4: Run Specific Test Groups

#### Run Smoke Tests (Quick Validation - 5 tests):
```bash
# Recommended: Using XML file
mvn test -DsuiteXmlFile=testng-smoke.xml

# Alternative: Using groups parameter
mvn test -Dgroups=smoke
```

**Smoke Tests Include:**
- LoginTest
- NdisBookingTest
- DuplicateNdisBookingTest
- CancelBookingTest
- UpdateAppointmentDateTest

**Duration:** ~30 seconds

---

#### Run Regression Tests (Complete Suite - 8 tests):
```bash
# Recommended: Using XML file
mvn test -DsuiteXmlFile=testng-regression.xml

# Alternative: Using groups parameter
mvn test -Dgroups=regression
```

**Regression Tests Include:**
- All 5 smoke tests
- Plus 3 signup tests (NDIS Client, Organisation, Over 65)

**Duration:** ~50 seconds

---

#### Run Multiple Groups:
```bash
mvn test -Dgroups="smoke,regression"
```

**Test Group Classification:**
```java
// Smoke + Regression (Critical Path)
@Test(groups = {"smoke", "regression"})
public void testSuccessfulLogin() { }

// Regression Only (Extended Validation)
@Test(groups = {"regression"})
public void testSignupNdisClient() { }
```

---

### Option 5: Run Tests by Category

#### Run All Login Tests:
```bash
mvn test -Dtest=*Login*
```

#### Run All Signup Tests:
```bash
mvn test -Dtest=*Signup*
```

#### Run All Booking Tests:
```bash
mvn test -Dtest=*Booking*
```

---

## 📊 Test Reports

### ExtentReports (Recommended)

**Professional HTML report with graphs, charts, and statistics**

#### Generate Report:
```bash
mvn clean test
```

#### View Report:
```bash
# Mac/Linux
open test-output/ExtentReport_*.html

# Windows
start test-output/ExtentReport_*.html

# Or manually navigate to:
# test-output/ExtentReport_YYYY-MM-DD_HH-mm-ss.html
```

**Report Features:**
- 📊 Pie chart showing pass/fail/skip ratio
- 📈 Test statistics dashboard
- ✅ Count of passed tests
- ❌ Failed test details with screenshots
- ⏱️ Test duration
- 🖥️ System information
- 📸 Embedded failure screenshots
- 🗑️ **Automatic cleanup:** Only the latest 5 reports are kept

**Report Location:**
```
test-output/ExtentReport_2026-04-30_15-39-01.html
```

---

### Convert Report to PDF

#### Method 1: Browser Print
1. Open ExtentReport HTML in Chrome
2. Press `Ctrl+P` (Windows) or `Cmd+P` (Mac)
3. Select "Save as PDF"
4. Save

#### Method 2: Command Line (wkhtmltopdf)
```bash
# Install (one-time)
brew install wkhtmltopdf

# Convert
wkhtmltopdf test-output/ExtentReport_*.html TestReport.pdf
```

---

### Other Reports

#### TestNG HTML Reports:
```bash
# Detailed report
open test-output/index.html

# Summary report (for email)
open test-output/emailable-report.html
```

#### Maven Surefire Reports:
```bash
# XML reports (for CI/CD)
ls target/surefire-reports/
```

---

### Screenshots on Failure

**Automatic screenshot capture when tests fail**

**Location:**
```
test-output/screenshots/TestName_YYYYMMDD_HHmmss.png
```

**Example:**
```
test-output/screenshots/LoginTest_20260430_153045.png
```

Screenshots are also **embedded** in the ExtentReport!

---

## ⚙️ Configuration

### config.properties

Location: `src/main/resources/config.properties`

```properties
# Application URL
app.url=http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout

# Browser Configuration
browser=chromium          # chromium, firefox, webkit
headless=false           # true (headless), false (visible browser)

# Test Credentials
test.email=sarvesh@curvetomorrow.com.au
test.password=Curve@2025

# Timeouts (milliseconds)
element.timeout=30000
page.timeout=30000

# Test Data
test.phone=0488883690
```

### Modify Settings:

#### Change Browser:
```properties
browser=firefox
# or
browser=webkit
```

#### Enable Headless Mode:
```properties
headless=true
```

---

### testng.xml Configuration

#### Adjust Thread Count:
```xml
<suite name="..." parallel="methods" thread-count="5">
```

Change `thread-count` to:
- `1` - Sequential execution
- `3` - 3 tests in parallel
- `5` - 5 tests in parallel (default)
- `10` - 10 tests in parallel (if you have many tests)

#### Change Parallel Mode:
```xml
<!-- Method level (recommended) -->
<suite name="..." parallel="methods" thread-count="5">

<!-- Class level -->
<suite name="..." parallel="classes" thread-count="3">

<!-- Sequential -->
<suite name="..." parallel="false" thread-count="1">
```

---

## 📝 Writing Tests

### Create a New Test

#### Step 1: Create Test Class
```java
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;

public class MyNewTest extends BaseTest {

    @Test(description = "Test description here")
    public void testMyFeature() {
        // Navigate to application
        page.navigate(ConfigReader.getAppUrl());
        
        // Use page objects
        LoginPage loginPage = new LoginPage(page);
        loginPage.loginWithDefaultCredentials();
        
        // Add assertions
        Assert.assertTrue(condition, "Assertion message");
        
        System.out.println("✅ Test completed successfully");
    }
}
```

#### Step 2: Add to testng.xml
```xml
<test name="All Tests">
    <classes>
        <class name="tests.LoginTest"/>
        <!-- Add your new test -->
        <class name="tests.MyNewTest"/>
    </classes>
</test>
```

#### Step 3: Run the Test
```bash
mvn test -Dtest=MyNewTest
```

---

### Create a New Page Object

#### Step 1: Create Page Class
```java
package pages;

import base.BasePage;
import com.microsoft.playwright.Page;

public class MyPage extends BasePage {
    
    // Locators
    private static final String BUTTON_SELECTOR = "button#submit";
    
    public MyPage(Page page) {
        super(page);
    }
    
    // Page actions
    public MyPage clickButton() {
        clickElement(BUTTON_SELECTOR);
        return this;
    }
}
```

#### Step 2: Use in Test
```java
MyPage myPage = new MyPage(page);
myPage.clickButton();
```

---

## 🔧 Common Commands

### Build & Test Commands

```bash
# Clean and build
mvn clean install

# Run all tests (parallel)
mvn clean test

# Run smoke tests (5 tests, ~30s)
mvn test -DsuiteXmlFile=testng-smoke.xml

# Run regression tests (8 tests, ~50s)
mvn test -DsuiteXmlFile=testng-regression.xml

# Run sequential tests
mvn test -DsuiteXmlFile=testng-sequential.xml

# Run specific test
mvn test -Dtest=LoginTest

# Run multiple tests
mvn test -Dtest=LoginTest,SignupNdisClientTest

# Run tests matching pattern
mvn test -Dtest=*Booking*

# Run by group (alternative)
mvn test -Dgroups=smoke
mvn test -Dgroups=regression

# Skip tests (compile only)
mvn clean install -DskipTests

# Verbose output
mvn test -X
```

### Report Commands

```bash
# Open ExtentReport (Mac/Linux)
open test-output/ExtentReport_*.html

# Open ExtentReport (Windows)
start test-output/ExtentReport_*.html

# List all reports
ls -la test-output/

# View screenshots
open test-output/screenshots/

# Clean old reports
rm -rf test-output/ target/
```

### IntelliJ Commands

```bash
# Rebuild project
Build → Rebuild Project

# Run single test
Right-click test → Run

# Run all tests
Right-click testng.xml → Run

# Debug test
Right-click test → Debug

# View test results
Run window (bottom panel)
```

---

## 🧪 Test Execution Summary

| Command | Description | Tests | Parallel | Duration |
|---------|-------------|-------|----------|----------|
| `mvn clean test` | All tests (parallel) | 8 | ✅ Yes (5 threads) | ~50s |
| `mvn test -DsuiteXmlFile=testng-smoke.xml` | Smoke tests | 5 | ✅ Yes (5 threads) | ~30s |
| `mvn test -DsuiteXmlFile=testng-regression.xml` | Regression tests | 8 | ✅ Yes (5 threads) | ~50s |
| `mvn test -Dtest=LoginTest` | Single test | 1 | ❌ No | ~7s |
| `mvn test -DsuiteXmlFile=testng-sequential.xml` | All (sequential) | 8 | ❌ No | ~100s |
| `mvn test -Dtest=*Signup*` | Pattern match | 3 | ✅ Yes | ~30s |
| `mvn test -Dgroups=smoke` | Smoke group | 5 | ✅ Yes | ~30s |
| `mvn test -Dgroups=regression` | Regression group | 8 | ✅ Yes | ~50s |

---

## 📈 Test Execution Flow

```
┌─────────────────────────────────────────┐
│  mvn clean test                         │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  Maven Build                            │
│  - Clean target/                        │
│  - Compile code                         │
│  - Extract Playwright driver            │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  TestNG Execution                       │
│  - Initialize ExtentReports             │
│  - Run tests (parallel/sequential)      │
│  - Capture screenshots on failure       │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  Generate Reports                       │
│  - ExtentReport (HTML with graphs)      │
│  - TestNG reports (HTML)                │
│  - Maven surefire reports (XML)         │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  Results Available                      │
│  - test-output/ExtentReport_*.html      │
│  - test-output/screenshots/             │
│  - target/surefire-reports/             │
└─────────────────────────────────────────┘
```

---

## 🔍 Troubleshooting

### Issue: Tests Failing

**Check:**
1. Browser drivers installed: `mvn clean install`
2. Correct Java version: `java -version`
3. Application URL accessible: Check `config.properties`
4. IntelliJ cache: `File → Invalidate Caches → Restart`

### Issue: Report Not Generated

**Solution:**
```bash
# Make sure to run via testng.xml
mvn clean test

# Not just a single test with -Dtest
```

### Issue: Screenshot Not Captured

**Check:**
1. `test-output/screenshots/` folder exists
2. Test actually failed (screenshots only on failure)
3. Check console for: `📸 Screenshot captured: ...`

### Issue: Parallel Execution Not Working

**Check `testng.xml`:**
```xml
<suite name="..." parallel="methods" thread-count="5">
```

Make sure `parallel="methods"` not `parallel="false"`

---

## 🚀 CI/CD Integration

### CircleCI Pipeline Configuration

The project uses **CircleCI** for continuous integration and automated testing. Tests automatically run **ONLY on the `canary` branch**.

**Features:**
- ✅ **Automated test execution** on push to canary branch
- ✅ **Parallel test execution** (5 threads)
- ✅ **Full regression suite** (8 tests)
- ✅ **Artifact storage** (reports, screenshots)
- ✅ **Nightly builds** on canary branch

### Setup CircleCI

#### 1. Connect Repository to CircleCI

1. Go to [CircleCI](https://circleci.com/)
2. Sign in with GitHub
3. Click **"Projects"** in sidebar
4. Find your repository: `sarvesh-curve/Auslan-DS-Automation`
5. Click **"Set Up Project"**
6. CircleCI will detect `.circleci/config.yml` automatically
7. Click **"Start Building"**

#### 2. Configuration File

The pipeline is configured in `.circleci/config.yml`:
- Located at: `.circleci/config.yml`
- Defines jobs, workflows, and triggers
- Uses Docker executor with OpenJDK 21
- Installs Playwright browsers automatically

#### 3. Workflows

**Canary Automation (Main Workflow)**
- Triggers on: **`canary` branch only**
- Runs: Full regression suite (8 tests)
- Duration: ~5-7 minutes
- Artifacts: Reports, screenshots

**Nightly Build**
- Triggers: Daily at midnight UTC
- Runs: Full regression suite
- Branch: **`canary` only**
- Purpose: Daily health check

### Push Code to Trigger CI/CD

```bash
# Push to canary branch (triggers full test suite)
git push origin canary

# Note: Automation ONLY runs on canary branch
# Pushes to main, develop, or feature branches will NOT trigger tests
```

### What Happens on Push to Canary

```
Push to canary → CircleCI Triggered → Install Dependencies → Run Tests → Generate Reports → Store Artifacts
```

**Execution Flow:**
1. 🔄 **Checkout code** from repository
2. 📦 **Install Maven dependencies** (cached)
3. 🌐 **Install Playwright browsers** (Chromium)
4. ⚙️ **Compile project**
5. 🧪 **Run tests** (parallel execution)
6. 📊 **Generate reports** (ExtentReports, TestNG)
7. 📸 **Capture screenshots** on failures
8. 📦 **Store artifacts** (downloadable)

### View Results

**CircleCI Dashboard:**
```
1. Go to CircleCI → Projects
2. Select your project
3. Click on the latest workflow
4. View test results and logs
5. Download artifacts (reports, screenshots)
```

**Download Artifacts:**
```
Workflow → Job → Artifacts Tab → Download test-output or surefire-reports
```

### Configuration Files

| File | Description |
|------|-------------|
| `.circleci/config.yml` | CircleCI pipeline configuration |
| `testng.xml` | Default test suite (parallel execution) |
| `testng-smoke.xml` | Smoke test suite |
| `testng-regression.xml` | Regression test suite |
| `.gitignore` | Git ignore rules |

### Advanced Configuration

#### Run Specific Test Suite

Edit `.circleci/config.yml` to run different test suites:

```yaml
# Run smoke tests
- run:
    name: Run Smoke Tests
    command: mvn test -DsuiteXmlFile=testng-smoke.xml

# Run regression tests
- run:
    name: Run Regression Tests
    command: mvn test -DsuiteXmlFile=testng-regression.xml
```

#### Adjust Parallelism

Modify `resource_class` in `.circleci/config.yml`:

```yaml
executors:
  java-playwright:
    resource_class: large    # Change to: medium, large, xlarge
```

#### Schedule Nightly Builds

Already configured in `.circleci/config.yml`:

```yaml
nightly-canary:
  triggers:
    - schedule:
        cron: "0 0 * * *"  # Daily at midnight UTC
        filters:
          branches:
            only:
              - canary      # Only on canary branch
```

### Troubleshooting

**Issue: Pipeline Fails to Start**
- Verify `.circleci/config.yml` syntax
- Check CircleCI project is set up
- Ensure repository is connected

**Issue: Tests Fail in CI but Pass Locally**
- Check browser compatibility (CI uses Chromium)
- Verify environment variables
- Check timeout settings

**Issue: Artifacts Not Available**
- Ensure `store_artifacts` step is in config
- Check job completed successfully
- Verify artifact paths are correct

---

## 📚 Additional Documentation

### Reports & Testing
- **PDF_REPORT_GUIDE.md** - Detailed reporting guide
- **TEST_REPORTS_GUIDE.md** - TestNG reports guide
- **EXTENT_REPORT_SUMMARY.md** - ExtentReports features
- **EXTENT_REPORT_CLEANUP.md** - Automatic report cleanup feature

### CI/CD & Git
- **GIT_SETUP.md** - Git initialization and push commands
- **CI_CD_SETUP_GUIDE.md** - Complete CI/CD setup (GitHub Actions, Jenkins, GitLab, Azure)

### Framework Documentation
- **FINAL_FRAMEWORK_SUMMARY.md** - Complete framework overview
- **IMPLEMENTATION_COMPLETE.md** - Implementation summary
- **TEST_GROUPS_GUIDE.md** - Test groups (smoke/regression)

---

## 🎯 Quick Start Guide

### For New Team Members:

```bash
# 1. Clone the repository
git clone <repository-url>
cd playwright

# 2. Install dependencies
mvn clean install

# 3. Run a single test to verify setup
mvn test -Dtest=LoginTest

# 4. Run smoke tests (quick validation)
mvn test -DsuiteXmlFile=testng-smoke.xml

# 5. Run all tests (regression)
mvn test -DsuiteXmlFile=testng-regression.xml

# 6. View the report
open test-output/ExtentReport_*.html
```

### For Daily Testing:

```bash
# Quick smoke test (30 seconds)
mvn test -DsuiteXmlFile=testng-smoke.xml

# Full regression suite (50 seconds)
mvn test -DsuiteXmlFile=testng-regression.xml

# Single test for debugging
mvn test -Dtest=NdisBookingTest

# View report
open test-output/ExtentReport_*.html
```

---

## 🎓 Key Features

- ✅ Page Object Model (POM)
- ✅ TestNG Framework
- ✅ ExtentReports with Graphs
- ✅ Parallel Execution (5 threads)
- ✅ Screenshot Capture on Failure
- ✅ Dynamic Test Data Generation
- ✅ Externalized Configuration
- ✅ Professional HTML Reports
- ✅ CI/CD Ready
- ✅ Comprehensive Documentation

---

## 📞 Support

For questions or issues:
1. Check this README
2. Review documentation in project root
3. Check test examples in `src/test/java/tests/`
4. Review page objects in `src/main/java/pages/`

---

## 👥 Team

- **Framework**: Playwright Java
- **Language**: Java 21
- **Build Tool**: Maven
- **Test Framework**: TestNG
- **Reporting**: ExtentReports
- **CI/CD**: GitHub Actions / Jenkins Ready

---

## 📜 License

This project is proprietary and confidential.

---

**Happy Testing!** ✨

For more details, see the documentation files in the project root.
