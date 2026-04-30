# Email Notification Setup Guide

## Overview

The automation framework automatically sends ExtentReports via email after every test execution, whether you run tests locally in IntelliJ or through CircleCI.

**Recipient:** `sarvesh@curvetomorrow.com.au`

## Features

- ✅ Automatic email after every test run
- ✅ Works for local (IntelliJ) and CI/CD (CircleCI) executions
- ✅ HTML formatted email with test summary
- ✅ ExtentReport attached as HTML file
- ✅ Beautiful email template with test statistics
- ✅ Environment and browser information included

## Email Configuration

### For Local Execution (IntelliJ)

#### Step 1: Get Gmail App Password

Since you're using Gmail, you need to generate an App Password:

1. Go to your Google Account: https://myaccount.google.com/
2. Click on **Security** in the left menu
3. Enable **2-Step Verification** (if not already enabled)
4. Scroll down to **App passwords**
5. Click **App passwords**
6. Select:
   - App: **Mail**
   - Device: **Mac** (or your device)
7. Click **Generate**
8. Copy the 16-character password (e.g., `abcd efgh ijkl mnop`)

#### Step 2: Update config.properties

Open `src/main/resources/config.properties` and update:

```properties
# Email Configuration
email.to=sarvesh@curvetomorrow.com.au
email.from=your-email@gmail.com

# SMTP Configuration
smtp.host=smtp.gmail.com
smtp.port=587
smtp.username=your-email@gmail.com
smtp.password=abcd efgh ijkl mnop  # Use the App Password from Step 1
```

**Example:**
```properties
smtp.username=sarvesh@curvetomorrow.com.au
smtp.password=abcd efgh ijkl mnop
```

#### Step 3: Run Tests

Now run tests normally:

```bash
# Run via Maven
mvn test

# Or run via IntelliJ (right-click test and run)
```

After tests complete, you'll see:
```
📧 Email sent successfully to: sarvesh@curvetomorrow.com.au
📎 Attached report: ExtentReport_2026-04-30_17-30-45.html
```

---

### For CircleCI Execution

#### Step 1: Set Environment Variables in CircleCI

1. Go to CircleCI: https://circleci.com/
2. Select your project: `sarvesh-curve/Auslan-DS-Automation`
3. Click **Project Settings** (gear icon)
4. Click **Environment Variables** in left menu
5. Add two variables:

**Variable 1:**
- Name: `SMTP_USERNAME`
- Value: `your-email@gmail.com` (or sarvesh@curvetomorrow.com.au)

**Variable 2:**
- Name: `SMTP_PASSWORD`
- Value: `abcd efgh ijkl mnop` (your Gmail App Password)

#### Step 2: Verify Configuration

The CircleCI config (`.circleci/config.yml`) is already set up to use these environment variables:

```yaml
- run:
    name: Run Tests
    command: mvn test
    environment:
      SMTP_USERNAME: ${SMTP_USERNAME}
      SMTP_PASSWORD: ${SMTP_PASSWORD}
```

#### Step 3: Push and Verify

```bash
git push origin canary
```

Watch the CircleCI build. After tests complete, email will be sent automatically.

---

## Email Content

### Email Subject
```
✅ Auslan DS - Test Execution Report - 30-Apr-2026 17:30:45
```

### Email Body
The email includes:
- **Test Suite Name**
- **Total Tests**
- **Passed Tests** ✅
- **Failed Tests** ❌
- **Skipped Tests** ⏭️
- **Total Duration**
- **Success Rate** (percentage)
- **Environment** (Canary)
- **Browser** (chromium/firefox/webkit)
- **Execution Time**

### Attachment
- **ExtentReport HTML file** with:
  - Detailed test results
  - Screenshots (for failed tests)
  - Test duration
  - System information
  - Interactive charts and graphs

---

## Testing Email Setup

### Option 1: Quick Test (Single Test)
```bash
mvn test -Dtest=LoginTest
```

You should receive an email within 1-2 minutes with:
- Subject: "Auslan DS - Test Execution Report"
- Body: Test summary
- Attachment: ExtentReport HTML

### Option 2: Full Test Suite
```bash
mvn clean test
```

---

## Troubleshooting

### Issue: Email Not Sent

**Check Console Output:**
```
⚠️  Email not sent: SMTP credentials not configured in config.properties
```

**Solution:**
- Verify `smtp.username` and `smtp.password` are filled in `config.properties`
- Make sure you're using Gmail App Password, not your regular password

---

### Issue: Authentication Failed

**Error:**
```
❌ Failed to send email: Authentication failed
```

**Solutions:**
1. **Verify App Password:** Make sure you copied the entire 16-character App Password
2. **Enable 2-Step Verification:** Gmail requires 2FA to use App Passwords
3. **Check Email:** Ensure the email address is correct
4. **Remove Spaces:** App password should be without spaces: `abcdefghijklmnop`

---

### Issue: Connection Timeout

**Error:**
```
❌ Failed to send email: Connection timed out
```

**Solutions:**
1. **Check Internet:** Ensure you have internet connectivity
2. **Firewall:** Check if port 587 is blocked
3. **VPN:** Try disabling VPN if active
4. **Alternative Port:** Try port 465 (update in config.properties)

---

### Issue: Email in Spam

**Solution:**
- Check your spam/junk folder
- Mark the email as "Not Spam"
- Add the sender to your contacts
- Gmail might flag automated emails initially

---

## Alternative Email Providers

### Using Microsoft Outlook / Office 365

```properties
smtp.host=smtp.office365.com
smtp.port=587
smtp.username=your-email@outlook.com
smtp.password=your-password
```

### Using SendGrid (Recommended for Production)

```properties
smtp.host=smtp.sendgrid.net
smtp.port=587
smtp.username=apikey
smtp.password=your-sendgrid-api-key
```

---

## Disabling Email Notifications

If you want to disable email notifications temporarily:

**Option 1: Leave credentials empty**
```properties
smtp.username=
smtp.password=
```

**Option 2: Comment out the email call**

In `TestListener.java`, comment out:
```java
// EmailUtil.sendExtentReport(reportPath, testSummary);
```

---

## Security Best Practices

### ✅ DO:
- Use App Passwords (not your main password)
- Store credentials in environment variables for CI/CD
- Use SendGrid or similar service for production
- Never commit real passwords to Git

### ❌ DON'T:
- Don't commit SMTP credentials to Git
- Don't use your main Gmail password
- Don't share your App Password
- Don't hardcode credentials in code

---

## Verification Checklist

Before pushing to production, verify:

- [ ] Gmail App Password generated
- [ ] `config.properties` updated with SMTP credentials
- [ ] Test email sent successfully from local machine
- [ ] CircleCI environment variables configured
- [ ] Test email sent successfully from CircleCI
- [ ] Email received at sarvesh@curvetomorrow.com.au
- [ ] ExtentReport attachment opens correctly
- [ ] No credentials committed to Git

---

## Email Flow

### Local Execution (IntelliJ)
```
Run Test → Test Completes → ExtentReport Generated → Email Sent → Receive Email
```

### CircleCI Execution
```
Push to Canary → CircleCI Triggered → Tests Run → ExtentReport Generated → Email Sent → Receive Email
```

---

## Example Email

### Subject:
```
✅ Auslan DS - Test Execution Report - 30-Apr-2026 17:30:45
```

### Body:
```
🎯 Auslan DS - Test Automation Report
Playwright Test Execution Results

📊 Test Summary
Test Suite: Playwright Test Suite - Parallel Execution
Total Tests: 8
✅ Passed: 7
❌ Failed: 1
⏭️  Skipped: 0
⏱️  Duration: 45623ms (45s)
Success Rate: 87.50%

📎 Detailed ExtentReport is attached
Open the attached HTML file to view the complete test execution report with screenshots and detailed logs.

Environment: Canary
Browser: chromium
Execution Time: 30-Apr-2026 17:30:45
```

### Attachment:
- `ExtentReport_2026-04-30_17-30-45.html` (with all test details and screenshots)

---

## Support

For issues or questions:
1. Check this guide
2. Verify SMTP credentials
3. Test with a simple Gmail test
4. Check CircleCI environment variables
5. Review console output for error messages

---

**Happy Testing!** 📧✨
