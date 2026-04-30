# Page Object Model (POM) + TestNG Framework Guide

## 🎉 Framework Implementation Complete!

Your Playwright Java project has been successfully transformed into a **Page Object Model (POM) + TestNG** framework.

---

## 📁 New Project Structure

```
playwright/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── base/                    # Base classes
│   │   │   │   ├── BasePage.java       # Common page methods
│   │   │   │   └── BaseTest.java       # TestNG setup/teardown
│   │   │   ├── pages/                   # Page Object classes
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── DashboardPage.java
│   │   │   │   ├── BookingPage.java
│   │   │   │   ├── BookingDetailsPage.java
│   │   │   │   └── SignupPage.java
│   │   │   ├── utils/                   # Utility classes
│   │   │   │   ├── ConfigReader.java
│   │   │   │   └── TestDataGenerator.java
│   │   │   └── constants/               # Constants
│   │   │       └── AppConstants.java
│   │   └── resources/
│   │       └── config.properties        # Configuration file
│   │
│   └── test/
│       └── java/
│           └── tests/                    # Test classes (TestNG)
│               ├── LoginTest.java
│               ├── CancelBookingTest.java
│               ├── NdisBookingTest.java
│               ├── DuplicateNdisBookingTest.java
│               ├── SignupNdisClientTest.java
│               ├── SignupOrganisationTest.java
│               ├── SignupOver65ClientTest.java
│               └── UpdateAppointmentDateTest.java
│
├── old_tests/                           # Backup of old test files
├── pom.xml                              # Maven configuration
└── testng.xml                           # TestNG suite configuration
```

---

## 🔑 Key Components

### 1. **Base Classes**

#### BasePage.java
- Common methods for all page objects
- Methods like `clickElement()`, `fillElement()`, `waitForElement()`
- All page classes extend this base class

#### BaseTest.java
- TestNG `@BeforeMethod` and `@AfterMethod` annotations
- Browser setup and teardown
- All test classes extend this base class

### 2. **Page Object Classes**

Each page has its own class with:
- **Locators**: Defined as constants at the top
- **Methods**: Business logic methods
- **Fluent Interface**: Methods return page objects for chaining

**Example:**
```java
LoginPage loginPage = new LoginPage(page);
DashboardPage dashboard = loginPage
    .enterEmail("user@email.com")
    .enterPassword("password")
    .clickLoginButton();
```

### 3. **Utility Classes**

#### ConfigReader.java
- Reads properties from `config.properties`
- Provides easy access to configuration values

#### TestDataGenerator.java
- Generates test data (timestamps, emails, DOB)
- Ensures unique test data for each run

### 4. **Constants**
- AppConstants.java: Application-wide constants
- URLs, messages, timeouts, test data

### 5. **Test Classes**

All tests now use:
- `@Test` annotation (no more `main` method)
- Extend `BaseTest` for automatic setup/teardown
- Use Page Objects for interactions
- Clear, readable test flow

---

## 🚀 How to Run Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Suite
```bash
# Run only Login Tests
mvn test -DsuiteXmlFile=testng.xml -Dgroups="Login Tests"

# Run only Booking Tests
mvn test -DsuiteXmlFile=testng.xml -Dgroups="Booking Tests"
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=LoginTest#testSuccessfulLogin
```

### Run from IDE
1. Right-click on test class or method
2. Select "Run 'TestName'"
3. Or use TestNG plugin to run from testng.xml

---

## ⚙️ Configuration

### config.properties
Located at: `src/main/resources/config.properties`

```properties
# Application Configuration
app.url=http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout

# Browser Configuration
browser=chromium        # Options: chromium, firefox, webkit
headless=false         # Set to true for headless mode

# Test User Credentials
test.email=sarvesh@curvetomorrow.com.au
test.password=Curve@2025

# Test Data
client.name=Sarvesh newndis
address=2 Victory Road, Clarinda VIC, Australia
phone.number=0488883690
```

### testng.xml
Located at: `testng.xml`

```xml
<!-- Configure parallel execution -->
<suite name="Playwright Test Suite" parallel="false" thread-count="1">
```

**Parallel Execution Options:**
- `parallel="false"` - Sequential execution
- `parallel="tests"` - Run test groups in parallel
- `parallel="classes"` - Run test classes in parallel
- `parallel="methods"` - Run test methods in parallel

---

## 📊 Test Structure Example

### Old Way (main method):
```java
public static void main(String[] args) {
    Playwright playwright = Playwright.create();
    Browser browser = playwright.chromium().launch();
    Page page = browser.newPage();
    
    page.navigate("url");
    page.locator("#email").fill("email");
    page.locator("#pass").fill("pass");
    page.locator("button").click();
    
    browser.close();
}
```

### New Way (POM + TestNG):
```java
@Test
public void testLogin() {
    page.navigate(ConfigReader.getAppUrl());
    
    LoginPage loginPage = new LoginPage(page);
    DashboardPage dashboard = loginPage.loginWithDefaultCredentials();
    
    Assert.assertNotNull(dashboard.getPageTitle());
}
```

---

## 🎯 Benefits of This Framework

### 1. **Maintainability**
- Locators in one place (Page Objects)
- Change locator once, affects all tests
- Easy to update when UI changes

### 2. **Reusability**
- Common methods in BasePage
- Page Objects can be reused across tests
- Utility classes shared across framework

### 3. **Readability**
- Test flow is clear and readable
- Business logic separated from test logic
- Fluent interface for better code

### 4. **Scalability**
- Easy to add new pages
- Easy to add new tests
- Framework grows with your needs

### 5. **Parallel Execution**
- TestNG supports parallel execution
- Run multiple tests simultaneously
- Faster test execution

### 6. **Reporting**
- TestNG generates HTML reports
- Easy to add Extent Reports or Allure
- Screenshots on failure (can be added)

---

## 📝 Adding New Tests

### Step 1: Create Page Object (if needed)
```java
// src/main/java/pages/NewPage.java
package pages;

import base.BasePage;
import com.microsoft.playwright.Page;

public class NewPage extends BasePage {
    // Locators
    private static final String ELEMENT_SELECTOR = "#element";
    
    public NewPage(Page page) {
        super(page);
    }
    
    // Methods
    public NewPage clickElement() {
        clickElement(ELEMENT_SELECTOR);
        return this;
    }
}
```

### Step 2: Create Test Class
```java
// src/test/java/tests/NewTest.java
package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.NewPage;
import utils.ConfigReader;

public class NewTest extends BaseTest {
    
    @Test
    public void testNewFeature() {
        page.navigate(ConfigReader.getAppUrl());
        
        NewPage newPage = new NewPage(page);
        newPage.clickElement();
        
        // Assertions
    }
}
```

### Step 3: Add to testng.xml
```xml
<test name="New Tests">
    <classes>
        <class name="tests.NewTest"/>
    </classes>
</test>
```

---

## 🔧 Maven Commands Reference

```bash
# Clean project
mvn clean

# Compile main code
mvn compile

# Compile test code
mvn test-compile

# Run tests
mvn test

# Run tests with specific TestNG suite
mvn test -DsuiteXmlFile=testng-smoke.xml

# Skip tests during build
mvn clean install -DskipTests

# Run with debug output
mvn test -X

# Run single test
mvn test -Dtest=LoginTest

# Run multiple tests
mvn test -Dtest=LoginTest,CancelBookingTest

# Generate test reports
mvn surefire-report:report
```

---

## 📈 Next Steps (Optional Enhancements)

### 1. **Add Logging**
- Add Log4j2 dependency
- Create logger in BaseTest
- Log test execution steps

### 2. **Add Reporting**
- Extent Reports for beautiful HTML reports
- Allure for interactive reports
- Screenshot on test failure

### 3. **Add Data-Driven Testing**
- Read test data from Excel/CSV/JSON
- Use TestNG DataProviders
- Run same test with different data

### 4. **Add Listeners**
- TestNG listeners for custom behavior
- Retry failed tests
- Send notifications on test completion

### 5. **CI/CD Integration**
- GitHub Actions / Jenkins
- Automated test execution
- Report publishing

### 6. **Page Factory Pattern**
- Use @FindBy annotations
- Initialize elements with PageFactory

---

## 🐛 Troubleshooting

### Issue: Tests not running
**Solution:**
```bash
# Recompile everything
mvn clean install

# Check TestNG XML is correct
mvn test -DsuiteXmlFile=testng.xml
```

### Issue: Page not found
**Solution:**
- Check imports in test class
- Verify Page Object class name
- Ensure class is in correct package

### Issue: Configuration not loading
**Solution:**
- Verify config.properties location
- Check file name spelling
- Ensure it's in src/main/resources

---

## 📚 Additional Resources

- [TestNG Documentation](https://testng.org/doc/)
- [Playwright Java Documentation](https://playwright.dev/java/)
- [Page Object Model Pattern](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/)

---

## ✅ Summary

Your framework now has:
- ✅ Page Object Model implementation
- ✅ TestNG integration with @Test annotations
- ✅ Base classes for common functionality
- ✅ Utility classes for helpers
- ✅ Configuration management
- ✅ Clean, maintainable test structure
- ✅ Maven build system
- ✅ Ready for CI/CD

**All tests compile successfully and are ready to run!**

---

## 📞 Support

For questions or issues with the framework:
1. Check this guide
2. Review the code examples
3. Check TestNG and Playwright documentation

Happy Testing! 🚀
