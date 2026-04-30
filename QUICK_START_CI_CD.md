# 🚀 Quick Start: Git & CI/CD

## Step 1: Push to GitHub (5 minutes)

### Create GitHub Repository
1. Go to https://github.com/new
2. Name: `playwright-automation` (or your choice)
3. **Don't** initialize with README
4. Click **Create repository**

### Push Your Code
```bash
cd /Users/SarveshChaudhari/Automation/playwright

# Initialize Git
git init
git add .
git commit -m "feat: Playwright automation framework with CI/CD"

# Create canary branch
git checkout -b canary

# Add your repository (REPLACE WITH YOUR URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# Push to GitHub
git push -u origin canary
```

**✅ Done! CI/CD pipeline runs automatically!**

---

## Step 2: View Test Results (2 minutes)

### On GitHub
1. Go to your repository
2. Click **Actions** tab
3. Click latest workflow run
4. View test execution logs

### Download Reports
```
Actions → Select Run → Artifacts → Download
  ├── smoke-test-results (ExtentReport HTML)
  └── regression-test-results (ExtentReport HTML)
```

---

## Step 3: Daily Workflow

### Making Changes
```bash
# Make changes to test files
vim src/test/java/tests/LoginTest.java

# Check what changed
git status
git diff

# Commit and push
git add .
git commit -m "test: Update login test"
git push origin canary

# CI/CD runs automatically! ✅
```

---

## 📊 What Runs in CI/CD

```
Push → Smoke Tests (5 tests, 30s) → Regression Tests (8 tests, 50s) → Reports
```

**Test Breakdown:**

**Smoke Tests (5 tests):**
- ✅ LoginTest
- ✅ NdisBookingTest
- ✅ DuplicateNdisBookingTest
- ✅ CancelBookingTest
- ✅ UpdateAppointmentDateTest

**Regression Tests (8 tests):**
- ✅ All above + SignupNdisClientTest + SignupOrganisationTest + SignupOver65ClientTest

---

## 🎯 Quick Commands

```bash
# Check Git status
git status

# View recent commits
git log --oneline

# Pull latest changes
git pull origin canary

# Create new branch
git checkout -b feature/new-test

# Switch back to canary
git checkout canary
```

---

## 📚 Need More Details?

| Question | See Document |
|----------|--------------|
| Git commands? | `GIT_SETUP.md` |
| CI/CD setup? | `CI_CD_SETUP_GUIDE.md` |
| Jenkins setup? | `CI_CD_SETUP_GUIDE.md` (Jenkins section) |
| Run tests locally? | `README.md` |
| Framework overview? | `README.md` |

---

## ⚡ Super Quick Reference

**Run tests locally:**
```bash
mvn test -DsuiteXmlFile=testng-smoke.xml
```

**View local report:**
```bash
open test-output/ExtentReport_*.html
```

**Push to trigger CI/CD:**
```bash
git add . && git commit -m "your message" && git push origin canary
```

---

**That's it! You're all set! 🎉**
