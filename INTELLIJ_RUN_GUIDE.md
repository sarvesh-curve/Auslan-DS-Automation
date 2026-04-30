# IntelliJ IDEA - Run Tests Guide

## 🎯 How to Run Tests in Parallel in IntelliJ IDEA

---

## ✅ Prerequisites

1. **TestNG Plugin Installed**
   - Go to: `File → Settings → Plugins`
   - Search: "TestNG"
   - Install if not present
   - Restart IntelliJ

2. **Project Opened**
   - Open your Playwright project in IntelliJ
   - Wait for Maven to sync dependencies

---

## 🚀 Method 1: Run All Tests in Parallel (Easiest)

### Step-by-Step:

1. **Locate testng.xml**
   ```
   Project Root
   └── testng.xml  ← This file
   ```

2. **Right-click on testng.xml**
   
3. **Select: "Run 'testng.xml'"**

4. **Watch Tests Execute**
   - You'll see 5 browser windows open simultaneously
   - Test results appear in the bottom panel
   - All 8 tests run in parallel

### Visual:
```
Project View:
├── src
├── pom.xml
├── testng.xml          ← Right-click here
├── testng-parallel-classes.xml
└── testng-sequential.xml
```

---

## 🎮 Method 2: Create Run Configuration (One-Time Setup)

### Setup Steps:

1. **Open Run Configurations**
   - Menu: `Run → Edit Configurations...`
   - Or: Click dropdown next to Run button → `Edit Configurations...`

2. **Add New Configuration**
   - Click `+` (Add New Configuration)
   - Select `TestNG`

3. **Configure:**
   - **Name:** `All Tests - Parallel`
   - **Test kind:** Suite
   - **Suite:** Click 📁 → Select `testng.xml`
   - **Use classpath of module:** Select your project
   - Click `OK`

4. **Run**
   - Select `All Tests - Parallel` from dropdown
   - Click ▶️ Run button

### Create Multiple Configurations:

**Configuration 1:** Parallel - 5 Threads
- Suite: `testng.xml`

**Configuration 2:** Parallel - 3 Threads
- Suite: `testng-parallel-classes.xml`

**Configuration 3:** Sequential (Debug)
- Suite: `testng-sequential.xml`

---

## 🎯 Method 3: Run Individual Test Class

### Steps:

1. **Open Test Class**
   - Navigate to: `src/test/java/tests/`
   - Open any test file (e.g., `LoginTest.java`)

2. **Run Entire Class**
   - **Option A:** Right-click class name → `Run 'LoginTest'`
   - **Option B:** Click green ▶️ icon next to class
   - **Option C:** Press `Ctrl+Shift+F10` (Windows) or `Cmd+Shift+R` (Mac)

3. **Single Browser Opens**
   - Only runs that one test class
   - Good for quick testing/debugging

---

## 🎪 Method 4: Run Single Test Method

### Steps:

1. **Open Test Class**
   
2. **Find @Test Method**
   ```java
   @Test
   public void testSuccessfulLogin() {  ← Click here
       // test code
   }
   ```

3. **Run Method**
   - Click green ▶️ icon next to method
   - Or right-click method name → `Run 'testSuccessfulLogin'`
   - Or cursor on method → `Ctrl+Shift+F10`

---

## 🎨 Method 5: Run Multiple Selected Tests

### Steps:

1. **Open Project View** (`Alt+1` or `Cmd+1`)

2. **Navigate to Tests Folder**
   ```
   src/test/java/tests/
   ```

3. **Select Multiple Files**
   - **Windows:** Hold `Ctrl` + Click files
   - **Mac:** Hold `Cmd` + Click files
   
   Example:
   ```
   ☑️ LoginTest.java
   ☑️ CancelBookingTest.java
   ☑️ NdisBookingTest.java
   ```

4. **Right-click → Run**

---

## 🔍 Understanding the Test Results Panel

When tests run, you'll see:

```
┌─────────────────────────────────────────────┐
│  Run: testng.xml                        ▶️ ⏹️ │
├─────────────────────────────────────────────┤
│  ├─ ✅ tests.LoginTest                      │
│  │   └─ ✅ testSuccessfulLogin (2.5s)      │
│  ├─ ✅ tests.NdisBookingTest                │
│  │   └─ ✅ testCreateNdisBooking (8.2s)    │
│  ├─ ✅ tests.CancelBookingTest              │
│  │   └─ ✅ testCancelBooking (6.1s)        │
│  └─ ... (more tests)                        │
├─────────────────────────────────────────────┤
│ Tests passed: 8/8                           │
│ Duration: 2m 15s                            │
└─────────────────────────────────────────────┘
```

**Icons:**
- ✅ Green checkmark = Passed
- ❌ Red X = Failed
- ⏭️ Yellow triangle = Skipped

---

## ⚡ Quick Keyboard Shortcuts

| Action | Windows/Linux | Mac |
|--------|---------------|-----|
| Run test/class | `Ctrl+Shift+F10` | `Cmd+Shift+R` |
| Debug test | `Ctrl+Shift+F9` | `Cmd+Shift+D` |
| Stop tests | `Ctrl+F2` | `Cmd+F2` |
| Rerun tests | `Shift+F10` | `Ctrl+R` |
| Rerun failed tests | Click icon in results | Click icon in results |
| Run anything | `Ctrl+Ctrl` (double) | `Ctrl+Ctrl` (double) |

---

## 🎬 What You'll See During Parallel Execution

### With 5 Parallel Threads:

1. **5 Browser Windows Open**
   - All visible (HEAD mode)
   - Each running different test
   - Executing simultaneously

2. **Test Results Update in Real-Time**
   - Green checkmarks as tests pass
   - See all tests progress together

3. **Browsers Close After Each Test**
   - Automatic cleanup via `@AfterMethod`

### Timeline:
```
0:00 → 5 browsers open (Tests 1-5 start)
0:30 → LoginTest completes, SignupNdisClientTest starts
1:00 → NdisBookingTest completes, DuplicateNdisBookingTest starts
1:30 → More tests complete/start
2:15 → All tests complete ✅
```

---

## 🔧 Advanced: Custom VM Options

### For Better Performance:

1. **Edit Run Configuration**
2. **Add VM Options:**
   ```
   -Xmx2g -XX:+UseG1GC
   ```
   - Allocates 2GB RAM
   - Uses better garbage collector

3. **Save and Run**

---

## 🐛 Common Issues & Solutions

### Issue 1: "Cannot find testng.xml"
**Solution:**
- Right-click `testng.xml` → "Create 'testng.xml'..."
- IntelliJ will auto-configure

### Issue 2: Green Run Icon Not Showing
**Solution:**
- Install TestNG plugin
- Reimport Maven project: `Maven → Reload Project`

### Issue 3: Tests Run But No Browser Opens
**Solution:**
- Check `config.properties`: `headless=false`
- Rebuild project: `Build → Rebuild Project`

### Issue 4: "Class not found" Error
**Solution:**
- Rebuild project: `Build → Rebuild Project`
- Clean Maven: `mvn clean compile` in terminal

### Issue 5: Too Many Browsers Slow Down System
**Solution:**
- Use `testng-parallel-classes.xml` (3 threads)
- Or edit `testng.xml` → change `thread-count="3"`

---

## 📊 Monitoring Test Execution

### View Console Output:
1. **Click** "Console" tab in test results panel
2. **See** all System.out.println() messages
3. **Monitor** test progress

### View TestNG Reports:
After tests complete:
```
target/
└── surefire-reports/
    ├── index.html          ← Open in browser
    ├── testng-results.xml
    └── emailable-report.html
```

---

## 🎯 Best Practices for IntelliJ

### 1. Use Descriptive Run Configurations
Create configurations for different scenarios:
- "Smoke Tests" - Quick critical tests
- "Full Suite" - All tests
- "Debug Single Test" - For debugging

### 2. Use Test Groups (Optional Enhancement)
```java
@Test(groups = {"smoke", "login"})
public void testSuccessfulLogin() {
    // test code
}
```

Then in testng.xml:
```xml
<groups>
    <run>
        <include name="smoke"/>
    </run>
</groups>
```

### 3. Keyboard Shortcuts
- Learn the shortcuts above
- Faster than using mouse

### 4. Failed Test Rerun
- Click "Rerun Failed Tests" icon
- Only reruns failed tests
- Saves time

---

## 🎨 Pretty Test Results (Optional)

### Enable Better Test Runner:
1. `Settings → Build → Build Tools → Gradle/Maven`
2. Enable: "Use IntelliJ IDEA Test Runner"
3. Better UI for test results

---

## ✅ Quick Start Checklist

- [ ] TestNG plugin installed
- [ ] Project synced (Maven dependencies downloaded)
- [ ] Located `testng.xml` file
- [ ] Right-clicked and ran `testng.xml`
- [ ] Observed 5 browsers opening
- [ ] All tests passed ✅

---

## 🎉 You're Ready!

### To Run All Tests in Parallel:
1. **Right-click** `testng.xml`
2. **Select** "Run 'testng.xml'"
3. **Watch** 5 browsers execute tests simultaneously

### Expected Result:
- ✅ 8 tests run in ~2-3 minutes
- ✅ All tests pass
- ✅ Chromium browsers in HEAD mode (visible)
- ✅ Automatic cleanup after each test

**That's it! Happy Testing in IntelliJ! 🚀**
