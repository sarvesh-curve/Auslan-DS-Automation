# ExtentReport Automatic Cleanup Feature

## 📋 Overview

The framework now automatically manages ExtentReport files, keeping only the **latest 5 reports** and deleting older ones.

---

## ✨ Features

### Automatic Cleanup
- **Limit**: Maximum 5 ExtentReports maintained
- **When**: Runs before creating each new report
- **How**: Deletes oldest reports based on file modification time

### Console Feedback
When cleanup occurs, you'll see:
```
🗑️  Deleted old report: ExtentReport_2026-04-30_15-58-20.html
✅ Cleaned up 1 old report(s). Keeping latest 5 reports.
```

---

## 🔧 Implementation Details

### Location
`src/main/java/utils/ExtentReportManager.java`

### Method
```java
private static void cleanupOldReports()
```

### Logic
1. **Check Directory**: Verifies `test-output/` exists
2. **List Reports**: Gets all files matching `ExtentReport_*.html`
3. **Sort by Date**: Orders files by modification time (newest first)
4. **Delete Oldest**: Removes files beyond the 5 newest
5. **Log Results**: Prints cleanup summary to console

### Trigger Points
- Runs during `TestListener.onStart()` 
- Executes before `ExtentReports` instance is created
- Happens at the start of each test suite execution

---

## 📊 How It Works

### Example Scenario

**Before Test Run:**
```
test-output/
├── ExtentReport_2026-04-30_16-10-02.html  (newest)
├── ExtentReport_2026-04-30_16-08-37.html
├── ExtentReport_2026-04-30_16-06-14.html
├── ExtentReport_2026-04-30_16-02-19.html
└── ExtentReport_2026-04-30_16-01-05.html  (oldest)
```

**After New Test Run:**
```
Cleanup Process:
🗑️  Deleted old report: ExtentReport_2026-04-30_16-01-05.html
✅ Cleaned up 1 old report(s). Keeping latest 5 reports.

test-output/
├── ExtentReport_2026-04-30_16-12-15.html  (new - just created)
├── ExtentReport_2026-04-30_16-10-02.html
├── ExtentReport_2026-04-30_16-08-37.html
├── ExtentReport_2026-04-30_16-06-14.html
└── ExtentReport_2026-04-30_16-02-19.html
```

---

## 🎯 Benefits

### 1. Disk Space Management
- Prevents accumulation of hundreds of report files
- Automatic cleanup without manual intervention
- Maintains clean `test-output/` directory

### 2. Easy Navigation
- Only recent reports visible
- Quick access to latest test results
- Reduced clutter in file explorer

### 3. CI/CD Friendly
- No need for cleanup scripts in CI/CD pipelines
- Consistent behavior across environments
- Automatic maintenance

---

## ⚙️ Configuration

### Change Report Limit

To keep more or fewer reports, modify `ExtentReportManager.java`:

```java
public class ExtentReportManager {
    private static final int MAX_REPORTS_TO_KEEP = 5;  // Change this value
    // ...
}
```

**Examples:**
- `MAX_REPORTS_TO_KEEP = 3` → Keep only 3 reports
- `MAX_REPORTS_TO_KEEP = 10` → Keep 10 reports
- `MAX_REPORTS_TO_KEEP = 1` → Keep only the latest report

---

## 🧪 Testing the Feature

### Verify Cleanup is Working

1. **Run multiple test suites:**
```bash
mvn test -DsuiteXmlFile=testng-smoke.xml
mvn test -DsuiteXmlFile=testng-smoke.xml
mvn test -DsuiteXmlFile=testng-smoke.xml
```

2. **Check console output:**
Look for cleanup messages:
```
🗑️  Deleted old report: ExtentReport_2026-04-30_15-58-20.html
✅ Cleaned up 1 old report(s). Keeping latest 5 reports.
```

3. **Count reports:**
```bash
# Should show exactly 5 reports
ls test-output/ExtentReport_*.html | wc -l
```

---

## 📝 Important Notes

### When Cleanup Doesn't Run

**`mvn clean test`**
- The `clean` goal deletes entire `test-output/` directory
- No old reports exist to cleanup
- Cleanup message won't appear

**Single Test Execution**
```bash
mvn test -Dtest=LoginTest
```
- May not trigger `TestListener.onStart()` reliably
- ExtentReport might not be generated
- Use suite XML files for reliable reporting

### Recommended Commands

**For Reliable Cleanup:**
```bash
# Use suite XML files (not -Dtest)
mvn test -DsuiteXmlFile=testng-smoke.xml
mvn test -DsuiteXmlFile=testng-regression.xml
mvn test                                      # Uses testng.xml
```

---

## 🔍 Troubleshooting

### Issue: Reports not being deleted

**Check 1: File Permissions**
```bash
ls -la test-output/
```
Ensure reports are writable.

**Check 2: Verify Logic**
```bash
# Should see cleanup messages
mvn test -DsuiteXmlFile=testng-smoke.xml 2>&1 | grep "Deleted old report"
```

**Check 3: Count Reports**
```bash
ls -lt test-output/ExtentReport_*.html
```

### Issue: More than 5 reports exist

**Possible Causes:**
1. Running with `mvn clean test` (clears directory each time)
2. Using `-Dtest=` instead of suite XML
3. Manual file copies
4. Parallel test execution race conditions

**Solution:**
```bash
# Manually cleanup if needed
rm test-output/ExtentReport_*.html

# Then run normally
mvn test -DsuiteXmlFile=testng-smoke.xml
```

---

## 🎨 Sample Cleanup Output

### Successful Cleanup
```
🎯 TEST SUITE STARTED: All Tests - Parallel Execution
🗑️  Deleted old report: ExtentReport_2026-04-30_16-00-26.html
🗑️  Deleted old report: ExtentReport_2026-04-30_15-58-39.html
✅ Cleaned up 2 old report(s). Keeping latest 5 reports.

========================================
🚀 STARTING TEST: testSuccessfulLogin
========================================
[... test execution ...]
```

### No Cleanup Needed
```
🎯 TEST SUITE STARTED: All Tests - Parallel Execution

========================================
🚀 STARTING TEST: testSuccessfulLogin
========================================
[... test execution ...]
```
*(No cleanup messages = fewer than 5 reports exist)*

---

## 📚 Related Documentation

- **README.md** - Complete framework guide
- **PDF_REPORT_GUIDE.md** - Converting ExtentReports to PDF
- **EXTENT_REPORT_SUMMARY.md** - ExtentReports features overview
- **TEST_REPORTS_GUIDE.md** - All reporting options

---

## 🎯 Summary

| Feature | Value |
|---------|-------|
| **Max Reports** | 5 |
| **Cleanup Trigger** | Before each new report creation |
| **Sort Order** | By file modification date (newest first) |
| **Console Feedback** | Yes (🗑️ emoji + summary) |
| **Configuration** | `MAX_REPORTS_TO_KEEP` constant |
| **Works With** | Suite XML execution (testng.xml, testng-smoke.xml, etc.) |

---

**✅ Feature Status:** Implemented and Tested

**🎉 Your test-output directory will now stay clean automatically!**
