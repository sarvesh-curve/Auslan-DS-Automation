# Parallel Execution Guide - TestNG

## 🚀 Parallel Execution Configured!

Your tests are now configured to run in parallel using TestNG.

---

## ⚙️ Configuration Details

### Current Setup (testng.xml)
```xml
<suite name="Playwright Test Suite - Parallel Execution" 
       parallel="methods" 
       thread-count="5">
```

**Settings:**
- **parallel="methods"** - Each `@Test` method runs in parallel
- **thread-count="5"** - Maximum 5 browser windows open simultaneously
- **Browser:** Chromium in HEAD mode (visible browser)

---

## 📊 Available TestNG Configurations

### 1. **testng.xml** (Default - Parallel Methods)
- **Mode:** `parallel="methods"`
- **Threads:** 5
- **Best for:** Running all test methods in parallel
- **Speed:** ⚡⚡⚡ Fastest

### 2. **testng-parallel-classes.xml**
- **Mode:** `parallel="classes"`
- **Threads:** 3
- **Best for:** When tests in same class share data
- **Speed:** ⚡⚡ Fast

### 3. **testng-sequential.xml**
- **Mode:** `parallel="false"`
- **Threads:** 1
- **Best for:** Debugging or dependent tests
- **Speed:** ⚡ Slower

---

## 🎯 How to Run in IntelliJ IDEA

### Method 1: Run All Tests in Parallel (Recommended)

#### Option A: Using testng.xml File
1. **Open** `testng.xml` file in IntelliJ
2. **Right-click** anywhere in the file
3. **Select** "Run 'testng.xml'"
4. Watch all tests run in parallel! 🚀

#### Option B: Using Run Configuration
1. **Click** Run menu → Edit Configurations
2. **Click** + (Add New Configuration)
3. **Select** "TestNG"
4. **Configure:**
   - Name: `Parallel Tests`
   - Suite: Browse to `testng.xml`
   - Click OK
5. **Run** the configuration

### Method 2: Run Specific Test Class
1. **Open** any test class (e.g., `LoginTest.java`)
2. **Right-click** on class name
3. **Select** "Run 'LoginTest'"
4. Test runs with single browser

### Method 3: Run Specific Test Method
1. **Open** any test class
2. **Find** the `@Test` method
3. **Click** the green play icon next to method
4. **Or right-click** → Run 'testMethodName'

### Method 4: Run Multiple Selected Tests
1. **Open** Project view (Alt+1 / Cmd+1)
2. **Navigate** to `src/test/java/tests/`
3. **Hold Ctrl/Cmd** and click multiple test files
4. **Right-click** → Run

---

## 🖥️ IntelliJ IDEA Setup (One-Time)

### Install TestNG Plugin
1. **Go to:** File → Settings (Ctrl+Alt+S)
2. **Select:** Plugins
3. **Search:** "TestNG"
4. **Install** if not already installed
5. **Restart** IntelliJ

### Configure TestNG in Project
1. **Right-click** on `testng.xml`
2. **Select:** "Create 'testng.xml'..."
3. IntelliJ automatically configures TestNG

---

## 💻 Command Line Execution

### Run with Default Config (Parallel)
```bash
mvn clean test
```

### Run with Specific TestNG XML
```bash
# Parallel Methods (5 threads)
mvn test -DsuiteXmlFile=testng.xml

# Parallel Classes (3 threads)
mvn test -DsuiteXmlFile=testng-parallel-classes.xml

# Sequential (1 thread)
mvn test -DsuiteXmlFile=testng-sequential.xml
```

### Run Single Test
```bash
mvn test -Dtest=LoginTest
```

---

## 🎚️ Adjusting Thread Count

### To Change Number of Parallel Threads

Edit `testng.xml`:
```xml
<!-- For 3 parallel tests -->
<suite name="..." parallel="methods" thread-count="3">

<!-- For 10 parallel tests -->
<suite name="..." parallel="methods" thread-count="10">
```

**Recommendations:**
- **2-3 threads:** Low-end machines
- **5 threads:** Default (Current)
- **8-10 threads:** High-end machines
- **Don't exceed your CPU cores**

---

## 📋 Parallel Execution Modes Explained

### 1. parallel="methods"
```xml
<suite parallel="methods" thread-count="5">
```
- Runs each `@Test` method in parallel
- Each method gets its own browser
- **Fastest** execution
- **Current setting** ✅

### 2. parallel="classes"
```xml
<suite parallel="classes" thread-count="3">
```
- Runs each test class in parallel
- Methods within a class run sequentially
- Good for tests with shared setup

### 3. parallel="tests"
```xml
<suite parallel="tests" thread-count="2">
```
- Runs each `<test>` block in parallel
- Classes within a test run sequentially

### 4. parallel="false"
```xml
<suite parallel="false">
```
- All tests run one after another
- Only use for debugging

---

## 🎬 What Happens During Parallel Execution

```
Test Execution Timeline (5 parallel threads):

Thread 1: [LoginTest........................] ✅
Thread 2: [NdisBookingTest.......................] ✅
Thread 3: [CancelBookingTest....................] ✅
Thread 4: [SignupNdisClientTest.................] ✅
Thread 5: [SignupOrganisationTest...............] ✅
          [DuplicateNdisBookingTest.............] ✅
          [SignupOver65ClientTest...............] ✅
          [UpdateAppointmentDateTest............] ✅

Total Time: ~2 minutes (vs 10 minutes sequential)
```

---

## ⚠️ Important Notes

### 1. **Each Test Gets Own Browser**
- `@BeforeMethod` creates new browser for each test
- Tests are completely isolated
- No data sharing between tests

### 2. **Resource Usage**
- 5 parallel threads = 5 browsers open
- Monitor CPU and memory usage
- Reduce thread count if system slows down

### 3. **Test Independence**
- Tests must be independent
- No shared state between tests
- Each test should work in any order

### 4. **Headless vs Head Mode**
Current config: **HEAD mode (visible browsers)**
- You'll see 5 browser windows open
- To run headless, set `headless=true` in `config.properties`

---

## 🐛 Troubleshooting

### Issue: Too many browsers crash system
**Solution:** Reduce thread count
```xml
<suite parallel="methods" thread-count="3">
```

### Issue: Tests fail in parallel but pass individually
**Reason:** Tests might have dependencies
**Solution:** 
1. Make tests independent
2. Or use `parallel="classes"` or `parallel="false"`

### Issue: Can't run testng.xml in IntelliJ
**Solution:**
1. Right-click `testng.xml` → "Create 'testng.xml'..."
2. Or install TestNG plugin
3. Or run via Maven: `mvn test`

### Issue: Browsers not closing after test
**Solution:** 
- Check `@AfterMethod` is present in `BaseTest`
- Ensure no exceptions in teardown

---

## 📊 Performance Comparison

| Mode | Thread Count | Execution Time | Resource Usage |
|------|--------------|----------------|----------------|
| Sequential | 1 | ~10 min | Low |
| Parallel Classes | 3 | ~4 min | Medium |
| Parallel Methods | 5 | ~2 min | High |
| Parallel Methods | 10 | ~1.5 min | Very High |

**Current Setup:** Parallel Methods with 5 threads ⚡

---

## 🎯 Quick Commands for IntelliJ

| Action | Shortcut |
|--------|----------|
| Run testng.xml | Right-click file → Run |
| Run test class | Ctrl+Shift+F10 / Cmd+Shift+R |
| Run test method | Click green arrow → Run |
| Debug test | Ctrl+Shift+F9 / Cmd+Shift+D |
| Stop tests | Ctrl+F2 / Cmd+F2 |
| Rerun failed tests | Click "Rerun Failed Tests" in results |

---

## 🎨 IntelliJ Run Configuration Template

### Create Custom Run Configuration:

1. **Run** → Edit Configurations
2. **Add** → TestNG
3. **Name:** Parallel Tests (5 threads)
4. **Suite:** `testng.xml`
5. **VM options:** `-Xmx2g` (optional, for more memory)
6. **Save**

### Create Multiple Configurations:
- "Parallel - 5 threads" → testng.xml
- "Parallel - 3 threads" → testng-parallel-classes.xml
- "Sequential" → testng-sequential.xml
- "Smoke Tests" → Custom XML with critical tests

---

## ✅ Verification

### Test Parallel Execution:
1. **Run:** `mvn clean test`
2. **Watch:** Multiple browser windows open
3. **Verify:** Tests run simultaneously
4. **Check:** TestNG report in `target/surefire-reports/`

### In IntelliJ:
1. **Right-click** testng.xml → Run
2. **Observe:** Test results panel shows parallel execution
3. **Check:** Multiple tests running at same time

---

## 🎉 You're All Set!

Your tests will now run in parallel with:
- ✅ 5 parallel threads
- ✅ Chromium browser in HEAD mode
- ✅ Full isolation between tests
- ✅ Faster execution time

### To Run Now:
**In IntelliJ:** Right-click `testng.xml` → Run
**Command Line:** `mvn clean test`

Happy Parallel Testing! 🚀
