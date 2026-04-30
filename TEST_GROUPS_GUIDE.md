# Test Groups Guide - Smoke & Regression

## ✅ Test Groups Implementation Complete!

Your tests are now organized into **Smoke** and **Regression** groups for better test execution control.

---

## 📊 Test Group Classification

### Smoke Tests (5 tests) - Critical Functionality
Quick validation of core features (~30 seconds)

| Test | Groups | Description |
|------|--------|-------------|
| **LoginTest** | smoke, regression | User login validation |
| **NdisBookingTest** | smoke, regression | Create NDIS booking |
| **DuplicateNdisBookingTest** | smoke, regression | Duplicate existing booking |
| **CancelBookingTest** | smoke, regression | Cancel booking |
| **UpdateAppointmentDateTest** | smoke, regression | Update appointment date |

### Regression Tests (8 tests) - Complete Functionality
Full validation of all features (~50 seconds)

| Test | Groups | Description |
|------|--------|-------------|
| **LoginTest** | smoke, regression | User login validation |
| **NdisBookingTest** | smoke, regression | Create NDIS booking |
| **DuplicateNdisBookingTest** | smoke, regression | Duplicate existing booking |
| **CancelBookingTest** | smoke, regression | Cancel booking |
| **UpdateAppointmentDateTest** | smoke, regression | Update appointment date |
| **SignupNdisClientTest** | regression | NDIS client registration |
| **SignupOrganisationTest** | regression | Organisation registration |
| **SignupOver65ClientTest** | regression | Over 65 client registration |

---

## 🚀 Running Tests by Group

### Method 1: Using Maven Command (Recommended)

#### Run Smoke Tests Only:
```bash
mvn test -DsuiteXmlFile=testng-smoke.xml
```

**What runs:**
- ✅ LoginTest
- ✅ NdisBookingTest
- ✅ DuplicateNdisBookingTest
- ✅ CancelBookingTest
- ✅ UpdateAppointmentDateTest

**Duration:** ~30 seconds (5 tests in parallel)

---

#### Run Regression Tests (All Tests):
```bash
mvn test -DsuiteXmlFile=testng-regression.xml
```

**What runs:**
- ✅ All 5 smoke tests
- ✅ Plus 3 signup tests (regression only)

**Duration:** ~50 seconds (8 tests in parallel)

---

### Method 2: Using Groups Parameter

#### Run Smoke Tests:
```bash
mvn test -Dgroups=smoke
```

#### Run Regression Tests:
```bash
mvn test -Dgroups=regression
```

#### Run Both:
```bash
mvn test -Dgroups="smoke,regression"
```

---

### Method 3: Using IntelliJ IDEA

#### Run Smoke Tests:
1. Right-click on `testng-smoke.xml`
2. Select **Run**

#### Run Regression Tests:
1. Right-click on `testng-regression.xml`
2. Select **Run**

---

## 📁 TestNG XML Files

### testng-smoke.xml
```xml
<suite name="Smoke Test Suite" parallel="methods" thread-count="5">
    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <classes>
            <!-- Only classes with smoke tests -->
        </classes>
    </test>
</suite>
```

### testng-regression.xml
```xml
<suite name="Regression Test Suite" parallel="methods" thread-count="5">
    <test name="Regression Tests">
        <groups>
            <run>
                <include name="regression"/>
            </run>
        </groups>
        <classes>
            <!-- All test classes -->
        </classes>
    </test>
</suite>
```

---

## 🎯 When to Use Which Group

### Use Smoke Tests When:
- ✅ Quick validation after deployment
- ✅ Pre-commit checks
- ✅ Build verification testing (BVT)
- ✅ Sanity testing
- ✅ Fast feedback needed (~30 seconds)

### Use Regression Tests When:
- ✅ Complete functionality validation
- ✅ Before release/deployment
- ✅ After major changes
- ✅ Nightly builds
- ✅ Comprehensive testing needed (~50 seconds)

---

## 📊 Execution Comparison

| Suite | Tests | Duration | Use Case |
|-------|-------|----------|----------|
| **Smoke** | 5 | ~30s | Quick validation, Pre-commit |
| **Regression** | 8 | ~50s | Complete testing, Pre-release |
| **All (testng.xml)** | 8 | ~50s | Same as regression |

---

## 💡 Examples

### Example 1: Daily Build
```bash
# Morning smoke test (quick check)
mvn test -DsuiteXmlFile=testng-smoke.xml

# If smoke passes, proceed with development
# If smoke fails, stop and fix
```

### Example 2: Before Release
```bash
# Run complete regression suite
mvn test -DsuiteXmlFile=testng-regression.xml

# Must pass all 8 tests before release
```

### Example 3: CI/CD Pipeline
```yaml
# Stage 1: Smoke Tests (Fast Feedback)
smoke-tests:
  script:
    - mvn test -DsuiteXmlFile=testng-smoke.xml

# Stage 2: Full Regression (If smoke passes)
regression-tests:
  script:
    - mvn test -DsuiteXmlFile=testng-regression.xml
  needs: [smoke-tests]
```

---

## 🔧 Adding Tests to Groups

### To Add Test to Smoke Group:
```java
@Test(groups = {"smoke", "regression"})
public void testMyFeature() {
    // test code
}
```

### To Add Test to Regression Only:
```java
@Test(groups = {"regression"})
public void testDetailedFeature() {
    // test code
}
```

### To Add Test to Both:
```java
@Test(groups = {"smoke", "regression"})
public void testCriticalFeature() {
    // test code
}
```

---

## 📋 Current Group Assignment

### Smoke + Regression (5 tests):
```java
// LoginTest.java
@Test(groups = {"smoke", "regression"})
public void testSuccessfulLogin() { }

// NdisBookingTest.java
@Test(groups = {"smoke", "regression"})
public void testCreateNdisBooking() { }

// DuplicateNdisBookingTest.java
@Test(groups = {"smoke", "regression"})
public void testDuplicateNdisBooking() { }

// CancelBookingTest.java
@Test(groups = {"smoke", "regression"})
public void testCancelBooking() { }

// UpdateAppointmentDateTest.java
@Test(groups = {"smoke", "regression"})
public void testUpdateAppointmentDate() { }
```

### Regression Only (3 tests):
```java
// SignupNdisClientTest.java
@Test(groups = {"regression"})
public void testSignupNdisClient() { }

// SignupOrganisationTest.java
@Test(groups = {"regression"})
public void testSignupOrganisation() { }

// SignupOver65ClientTest.java
@Test(groups = {"regression"})
public void testSignupOver65Client() { }
```

---

## 📊 Test Execution Report

After running tests, the ExtentReport will show:

```
╔════════════════════════════════════════════╗
║  SMOKE TEST SUITE                          ║
╠════════════════════════════════════════════╣
║  Total: 5                                  ║
║  Passed: 5                                 ║
║  Failed: 0                                 ║
║  Duration: 30s                             ║
╚════════════════════════════════════════════╝

Tests:
✅ testSuccessfulLogin
✅ testCreateNdisBooking
✅ testDuplicateNdisBooking
✅ testCancelBooking
✅ testUpdateAppointmentDate
```

---

## 🎯 Quick Commands Summary

```bash
# Smoke Tests (5 tests, ~30s)
mvn test -DsuiteXmlFile=testng-smoke.xml

# Regression Tests (8 tests, ~50s)
mvn test -DsuiteXmlFile=testng-regression.xml

# All Tests (same as regression)
mvn clean test

# Using groups parameter
mvn test -Dgroups=smoke
mvn test -Dgroups=regression
```

---

## 📁 Files Created

1. **testng-smoke.xml** - Smoke test suite configuration
2. **testng-regression.xml** - Regression test suite configuration
3. **Updated all test files** - Added group annotations

---

## ✅ Benefits

### Fast Feedback
- Smoke tests run in 30 seconds
- Quick validation of critical features
- Faster CI/CD pipelines

### Flexible Execution
- Run only what you need
- Smoke for quick checks
- Regression for thorough testing

### Better Organization
- Tests categorized by purpose
- Easy to understand what's critical
- Clear test prioritization

### CI/CD Ready
- Separate stages for smoke and regression
- Fast failure detection
- Comprehensive validation

---

## 🎉 Summary

Your tests are now organized as:

**Smoke Tests (Critical Path):**
- Login ✅
- Create Booking ✅
- Duplicate Booking ✅
- Cancel Booking ✅
- Update Booking ✅

**Regression Only (Extended Validation):**
- Signup NDIS Client ✅
- Signup Organisation ✅
- Signup Over 65 Client ✅

**Run smoke tests for quick validation, regression tests for complete coverage!** 🚀
