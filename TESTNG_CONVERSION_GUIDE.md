# Converting Tests to TestNG Format

## Current Situation

Your test files currently use the `main` method approach:

```java
public class CancelBooking {
    public static void main(String[] args) {
        // test code here
    }
}
```

## Why Convert to TestNG?

To fully utilize Maven's test execution capabilities, you should convert these to proper TestNG test methods:

1. **Parallel Execution**: Run multiple tests simultaneously
2. **Test Reports**: Automatic HTML reports generation
3. **Test Groups**: Organize tests by categories (smoke, regression, etc.)
4. **Annotations**: Use `@BeforeMethod`, `@AfterMethod`, `@Test`
5. **Maven Integration**: `mvn test` will execute all tests
6. **CI/CD Ready**: Standard test execution in pipelines

## Recommended Test Structure

### Before (Current - Main Method)
```java
public class CancelBooking {
    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch();
        // test code
        browser.close();
        playwright.close();
    }
}
```

### After (TestNG Style)
```java
import org.testng.annotations.*;

public class CancelBooking {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    public void testCancelBooking() {
        // Your test code here
        page.navigate("http://example.com");
        // ... rest of test
    }

    @AfterMethod
    public void tearDown() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
```

## Converting Your Tests

### Step-by-Step Conversion

1. **Add TestNG Annotations**
   - Add `@Test` to test methods
   - Add `@BeforeMethod` for setup
   - Add `@AfterMethod` for cleanup

2. **Change Static to Instance Variables**
   - Remove `static` keyword from variables
   - Each test gets fresh instances

3. **Rename main to test method**
   - Change `public static void main(String[] args)` to `@Test public void testMethodName()`

4. **Add Setup and Teardown**
   - Move browser initialization to `@BeforeMethod`
   - Move browser cleanup to `@AfterMethod`

### Example Conversion Template

```java
import com.microsoft.playwright.*;
import org.testng.annotations.*;

public class YourTestClass {
    
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        context = browser.newContext();
        page = context.newPage();
    }

    @Test(description = "Test description here")
    public void testYourFeature() {
        // Your test logic here
    }

    @AfterMethod
    public void tearDown() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
```

## Using TestNG Suite XML

Update `testng.xml` to control test execution:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Playwright Test Suite" parallel="classes" thread-count="3">
    <test name="Smoke Tests">
        <classes>
            <class name="Login1"/>
            <class name="CancelBooking"/>
        </classes>
    </test>
    <test name="Regression Tests">
        <classes>
            <class name="NdisBooking"/>
            <class name="SignupNdisClient"/>
        </classes>
    </test>
</suite>
```

## Running Tests

### With Main Method (Current)
```bash
# Run individual test via IDE or:
java -cp target/classes:dependencies CancelBooking
```

### With TestNG (Recommended)
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=CancelBooking

# Run specific test method
mvn test -Dtest=CancelBooking#testCancelBooking

# Run with TestNG XML
mvn test -DsuiteXmlFile=testng.xml
```

## Optional: Page Object Model

For better maintainability, consider implementing Page Object Model:

```java
// LoginPage.java
public class LoginPage {
    private Page page;
    
    public LoginPage(Page page) {
        this.page = page;
    }
    
    public void login(String username, String password) {
        page.fill("#username", username);
        page.fill("#password", password);
        page.click("#login-button");
    }
}

// LoginTest.java
public class LoginTest {
    @Test
    public void testLogin() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("user", "pass");
        // assertions
    }
}
```

## Benefits Summary

| Feature | Main Method | TestNG |
|---------|-------------|--------|
| Maven `mvn test` | ❌ No | ✅ Yes |
| Parallel Execution | ❌ No | ✅ Yes |
| Test Reports | ❌ No | ✅ Yes |
| Setup/Teardown | Manual | ✅ Automatic |
| CI/CD Integration | ❌ Complex | ✅ Simple |
| Test Groups | ❌ No | ✅ Yes |

## Current Status

Your tests **currently work** with the `main` method approach, and you can run them individually from your IDE. However, they **won't run** with `mvn test` until converted to TestNG format.

## Next Steps (Optional)

If you want to use Maven test execution:

1. Convert one test class to TestNG format as a trial
2. Run `mvn test -Dtest=ClassName` to verify
3. Gradually convert remaining tests
4. Update `testng.xml` with converted test classes

If you prefer to keep the `main` method approach, you can still use Maven for:
- Dependency management
- Compilation
- Packaging
- Project structure

The choice is yours based on your testing needs!

## Need Help Converting?

Let me know if you'd like me to:
- Convert a specific test file to TestNG format
- Create a base test class for common setup/teardown
- Set up Page Object Model structure
- Configure advanced TestNG features
