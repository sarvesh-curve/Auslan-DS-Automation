# Quick Start Guide - POM + TestNG Framework

## 🚀 Getting Started in 5 Minutes

### Step 1: Verify Installation
```bash
# Check Maven is installed
mvn --version

# Check Java is installed
java --version
```

### Step 2: Compile Project
```bash
cd /Users/SarveshChaudhari/Automation/playwright
mvn clean compile
```

Expected output: `BUILD SUCCESS`

### Step 3: Run Your First Test
```bash
# Run login test
mvn test -Dtest=LoginTest
```

### Step 4: Run All Tests
```bash
mvn clean test
```

---

## 📝 Test List

| Test Class | Description | Status |
|------------|-------------|--------|
| LoginTest | Simple login test | ✅ Ready |
| CancelBookingTest | Cancel a booking | ✅ Ready |
| NdisBookingTest | Create NDIS booking | ✅ Ready |
| DuplicateNdisBookingTest | Duplicate booking | ✅ Ready |
| SignupNdisClientTest | NDIS client signup | ✅ Ready |
| SignupOrganisationTest | Organisation signup | ✅ Ready |
| SignupOver65ClientTest | Over 65 signup | ✅ Ready |
| UpdateAppointmentDateTest | Update appointment | ✅ Ready |

---

## ⚙️ Configuration

Edit `src/main/resources/config.properties`:

```properties
# Change browser
browser=chromium          # Options: chromium, firefox, webkit

# Enable headless mode
headless=false           # Set to true for headless

# Update credentials
test.email=your@email.com
test.password=yourpassword
```

---

## 🎯 Common Commands

```bash
# Run single test
mvn test -Dtest=LoginTest

# Run multiple tests
mvn test -Dtest=LoginTest,CancelBookingTest

# Run with TestNG XML
mvn test -DsuiteXmlFile=testng.xml

# Clean and run
mvn clean test

# Skip browser install phase
mvn test -DskipExec
```

---

## 📁 Key Files

| File | Purpose |
|------|---------|
| `src/test/java/tests/` | Your test classes |
| `src/main/java/pages/` | Page Object classes |
| `src/main/java/base/` | Base classes |
| `src/main/resources/config.properties` | Configuration |
| `testng.xml` | TestNG suite |
| `pom.xml` | Maven configuration |

---

## 🐛 Quick Troubleshooting

### Issue: Tests not running
```bash
mvn clean install
```

### Issue: Browser not launching
Check `config.properties`:
```properties
headless=false
browser=chromium
```

### Issue: Compilation errors
```bash
mvn clean compile
```

---

## 📚 Documentation

- **POM_FRAMEWORK_GUIDE.md** - Complete framework documentation
- **FRAMEWORK_SUMMARY.md** - What was implemented
- **QUICK_START.md** - This file

---

## ✅ Verification Checklist

- [ ] Project compiles: `mvn clean compile`
- [ ] Single test runs: `mvn test -Dtest=LoginTest`
- [ ] All tests run: `mvn test`
- [ ] Configuration loaded correctly
- [ ] TestNG reports generated in `target/surefire-reports`

---

## 🎉 You're All Set!

Your framework is ready to use. Start by running:

```bash
mvn clean test
```

For detailed documentation, see **POM_FRAMEWORK_GUIDE.md**

Happy Testing! 🚀
