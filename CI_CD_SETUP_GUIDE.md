# CI/CD Pipeline Setup Guide

## 📋 Overview

This guide covers setting up automated test execution for the Playwright framework when code is pushed to the **canary** branch.

---

## 🎯 Supported CI/CD Platforms

- ✅ **GitHub Actions** (Recommended for GitHub repositories)
- ✅ **Jenkins** (For on-premise or custom CI/CD setups)
- ✅ **GitLab CI** (Configuration provided below)
- ✅ **Azure DevOps** (Configuration provided below)

---

## 🚀 Quick Setup Steps

### 1️⃣ Initialize Git Repository

```bash
cd /Users/SarveshChaudhari/Automation/playwright

# Initialize Git
git init

# Add all files
git add .

# Create initial commit
git commit -m "Initial commit: Playwright Java automation framework"

# Create canary branch
git checkout -b canary

# Add remote repository (replace with your repo URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# Push to remote
git push -u origin canary
```

### 2️⃣ Choose Your CI/CD Platform

Pick one of the following based on your setup:
- [GitHub Actions](#github-actions-setup)
- [Jenkins](#jenkins-setup)
- [GitLab CI](#gitlab-ci-setup)
- [Azure DevOps](#azure-devops-setup)

---

## 🟢 GitHub Actions Setup

### Automatic Setup (Already Done!)

The workflow file is already created at:
```
.github/workflows/canary-tests.yml
```

### What Happens Automatically

When you push to the **canary** branch:

1. **Smoke Tests** run first (5 tests, ~30 seconds)
   - Login test
   - Booking tests
   - Quick validation

2. **Regression Tests** run if smoke passes (8 tests, ~50 seconds)
   - All smoke tests
   - Signup tests
   - Complete validation

3. **Reports Generated**
   - ExtentReports (HTML)
   - TestNG reports (XML)
   - Screenshots (on failure)

### Workflow Triggers

The workflow runs on:
- ✅ Push to `canary` branch
- ✅ Pull requests to `canary` branch
- ✅ Manual trigger from GitHub UI

### View Test Results

1. Go to your GitHub repository
2. Click **Actions** tab
3. Select the latest workflow run
4. View:
   - ✅ Test execution logs
   - 📊 ExtentReports (download artifacts)
   - 📸 Screenshots (if tests failed)

### Download Test Reports

```
Actions → Select Run → Artifacts
  ├── smoke-test-results
  │   └── test-output/ExtentReport_*.html
  ├── regression-test-results
  │   └── test-output/ExtentReport_*.html
  └── screenshots/ (if failed)
```

---

## 🔵 Jenkins Setup

### Prerequisites

1. **Jenkins Server** with:
   - Java 21
   - Maven 3.9.6
   - Git plugin
   - Pipeline plugin
   - HTML Publisher plugin

### Setup Steps

#### 1. Create Jenkins Job

1. Open Jenkins Dashboard
2. Click **New Item**
3. Enter job name: `Playwright-Canary-Tests`
4. Select **Pipeline**
5. Click **OK**

#### 2. Configure Pipeline

**General Settings:**
```
✅ GitHub project (optional): https://github.com/YOUR_USERNAME/YOUR_REPO
```

**Build Triggers:**
```
✅ GitHub hook trigger for GITScm polling
✅ Poll SCM: H/5 * * * *  (every 5 minutes)
```

**Pipeline Definition:**
```
Pipeline script from SCM
  SCM: Git
  Repository URL: https://github.com/YOUR_USERNAME/YOUR_REPO.git
  Branch: canary
  Script Path: Jenkinsfile
```

#### 3. Configure GitHub Webhook (Optional)

1. Go to GitHub repo → **Settings** → **Webhooks**
2. Click **Add webhook**
3. Payload URL: `http://YOUR_JENKINS_URL/github-webhook/`
4. Content type: `application/json`
5. Select: **Just the push event**
6. Click **Add webhook**

#### 4. Run Pipeline

**Manual Run:**
1. Go to Jenkins job
2. Click **Build with Parameters**
3. Select:
   - Test Suite: `smoke` / `regression` / `all`
   - Browser: `chromium` / `firefox` / `webkit`
   - Headless: `true` / `false`
4. Click **Build**

**Automatic Run:**
- Pipeline triggers automatically on push to `canary` branch

#### 5. View Results

**Test Reports:**
```
Jenkins Job → Latest Build → ExtentReport
```

**Console Output:**
```
Jenkins Job → Latest Build → Console Output
```

**Screenshots (on failure):**
```
Jenkins Job → Latest Build → Artifacts
```

---

## 🟠 GitLab CI Setup

Create `.gitlab-ci.yml` in project root:

```yaml
image: maven:3.9.6-eclipse-temurin-21

variables:
  MAVEN_OPTS: "-Xmx1024m"

stages:
  - build
  - test-smoke
  - test-regression
  - report

cache:
  paths:
    - .m2/repository

before_script:
  - java -version
  - mvn -version

build:
  stage: build
  script:
    - mvn clean install -DskipTests
  artifacts:
    paths:
      - target/
    expire_in: 1 hour

smoke-tests:
  stage: test-smoke
  script:
    - mvn test -DsuiteXmlFile=testng-smoke.xml
  artifacts:
    when: always
    paths:
      - test-output/
      - target/surefire-reports/
    expire_in: 30 days
  only:
    - canary

regression-tests:
  stage: test-regression
  dependencies:
    - smoke-tests
  script:
    - mvn test -DsuiteXmlFile=testng-regression.xml
  artifacts:
    when: always
    paths:
      - test-output/
      - target/surefire-reports/
    expire_in: 30 days
  only:
    - canary

generate-report:
  stage: report
  script:
    - echo "Test reports generated"
  artifacts:
    paths:
      - test-output/ExtentReport_*.html
    expire_in: 30 days
  only:
    - canary
```

**View Results:**
```
GitLab → CI/CD → Pipelines → Select Run → Browse Artifacts
```

---

## 🔷 Azure DevOps Setup

Create `azure-pipelines.yml` in project root:

```yaml
trigger:
  branches:
    include:
      - canary

pool:
  vmImage: 'ubuntu-latest'

variables:
  MAVEN_CACHE_FOLDER: $(Pipeline.Workspace)/.m2/repository
  MAVEN_OPTS: '-Xmx1024m'

steps:
- task: JavaToolInstaller@0
  inputs:
    versionSpec: '21'
    jdkArchitectureOption: 'x64'
    jdkSourceOption: 'PreInstalled'

- task: Maven@3
  displayName: 'Build Project'
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'clean install'
    options: '-DskipTests'
    publishJUnitResults: false

- task: Maven@3
  displayName: 'Run Smoke Tests'
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'test'
    options: '-DsuiteXmlFile=testng-smoke.xml'
    publishJUnitResults: true
    testResultsFiles: '**/target/surefire-reports/TEST-*.xml'
    testRunTitle: 'Smoke Tests'

- task: Maven@3
  displayName: 'Run Regression Tests'
  condition: succeeded()
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'test'
    options: '-DsuiteXmlFile=testng-regression.xml'
    publishJUnitResults: true
    testResultsFiles: '**/target/surefire-reports/TEST-*.xml'
    testRunTitle: 'Regression Tests'

- task: PublishTestResults@2
  displayName: 'Publish Test Results'
  condition: always()
  inputs:
    testResultsFormat: 'JUnit'
    testResultsFiles: '**/target/surefire-reports/TEST-*.xml'
    failTaskOnFailedTests: true

- task: PublishBuildArtifacts@1
  displayName: 'Publish ExtentReports'
  condition: always()
  inputs:
    PathtoPublish: 'test-output'
    ArtifactName: 'test-reports'

- task: PublishBuildArtifacts@1
  displayName: 'Publish Screenshots'
  condition: failed()
  inputs:
    PathtoPublish: 'test-output/screenshots'
    ArtifactName: 'screenshots'
```

**View Results:**
```
Azure DevOps → Pipelines → Select Run → Tests / Artifacts
```

---

## ⚙️ Configuration Options

### Environment-Specific Settings

Update `config.properties` for different environments:

```properties
# Development
app.url=https://dev.canary.com
browser=chromium
headless=true

# Canary (CI/CD)
app.url=https://canary.example.com
browser=chromium
headless=true

# Production (if testing prod)
app.url=https://prod.example.com
browser=chromium
headless=true
```

### CI/CD Environment Variables

Set these in your CI/CD platform:

| Variable | Description | Example |
|----------|-------------|---------|
| `APP_URL` | Application URL | `https://canary.example.com` |
| `BROWSER` | Browser type | `chromium` |
| `HEADLESS` | Headless mode | `true` |
| `TEST_EMAIL` | Test user email | `test@example.com` |
| `TEST_PASSWORD` | Test user password | `SecurePassword123` |

**GitHub Actions:**
```
Repo → Settings → Secrets and variables → Actions → New repository secret
```

**Jenkins:**
```
Jenkins → Manage Jenkins → Configure System → Environment variables
```

---

## 📊 Test Execution Flow

### On Push to Canary Branch

```
┌─────────────────────────────────────┐
│  Developer pushes to canary branch  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  CI/CD Pipeline Triggered           │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  1. Build Project (mvn clean)       │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  2. Run Smoke Tests (5 tests)       │
│     ✅ Login                         │
│     ✅ Booking scenarios             │
└──────────────┬──────────────────────┘
               │
               ├── ✅ Pass → Continue
               │
               └── ❌ Fail → Stop & Notify
               │
               ▼
┌─────────────────────────────────────┐
│  3. Run Regression Tests (8 tests)  │
│     ✅ All smoke tests               │
│     ✅ Signup tests                  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  4. Generate Reports                │
│     📊 ExtentReports                 │
│     📸 Screenshots (if failed)       │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  5. Publish Artifacts               │
│     📦 Test reports                  │
│     📦 Screenshots                   │
└─────────────────────────────────────┘
```

---

## 🔔 Notifications (Optional)

### Email Notifications

**Jenkins:**
```groovy
// Add to Jenkinsfile post section
emailext (
    subject: "Tests ${currentBuild.currentResult}: ${env.JOB_NAME}",
    body: "Check results at: ${env.BUILD_URL}",
    to: "team@example.com"
)
```

**GitHub Actions:**
```yaml
# Add step to workflow
- name: Send Email
  uses: dawidd6/action-send-mail@v3
  with:
    server_address: smtp.gmail.com
    server_port: 587
    username: ${{secrets.MAIL_USERNAME}}
    password: ${{secrets.MAIL_PASSWORD}}
    subject: Test Results - ${{github.repository}}
    to: team@example.com
    from: CI/CD Pipeline
    body: Build job of ${{github.repository}} completed.
```

### Slack Notifications

**Jenkins:**
```groovy
// Install Slack Notification Plugin
slackSend(
    color: currentBuild.result == 'SUCCESS' ? 'good' : 'danger',
    message: "Tests ${currentBuild.result}: ${env.JOB_NAME} - ${env.BUILD_NUMBER}"
)
```

**GitHub Actions:**
```yaml
- name: Slack Notification
  uses: 8398a7/action-slack@v3
  with:
    status: ${{ job.status }}
    text: 'Test execution completed'
    webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

---

## 🐛 Troubleshooting

### Issue: Tests fail in CI but pass locally

**Possible Causes:**
1. Timing issues (CI servers may be slower)
2. Headless mode differences
3. Missing dependencies

**Solutions:**
```bash
# Test locally with headless mode
mvn test -DsuiteXmlFile=testng-smoke.xml -Dheadless=true

# Increase timeouts in AppConstants.java if needed
public static final int TIMEOUT = 60000;  // 60 seconds
```

### Issue: Browser installation fails

**GitHub Actions:**
```yaml
# Already handled in workflow
# Playwright installs browsers automatically
```

**Jenkins:**
```bash
# Install browsers manually on Jenkins server
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Issue: Reports not generated

**Check:**
1. TestNG listener is configured in `testng.xml`
2. `test-output/` directory exists
3. Tests are actually running (not skipped)

**Debug:**
```bash
# Run with verbose output
mvn test -X -DsuiteXmlFile=testng-smoke.xml
```

---

## 📊 Monitoring & Analytics

### Key Metrics to Track

1. **Test Pass Rate**: `Passed / Total * 100`
2. **Test Execution Time**: Duration of each suite
3. **Failure Trends**: Track which tests fail most
4. **Code Coverage**: (if you add JaCoCo later)

### CI/CD Dashboards

**GitHub Actions:**
- View trends in Actions tab
- Track workflow runs over time

**Jenkins:**
- Use Blue Ocean plugin for visual pipelines
- Track test trends with Test Results Analyzer plugin

---

## 🎯 Best Practices

### 1. Fast Feedback
```
✅ Run smoke tests first (quick validation)
✅ Run regression only if smoke passes
✅ Keep smoke suite under 1 minute
```

### 2. Reliable Tests
```
✅ Use explicit waits, not sleep()
✅ Handle dynamic data properly
✅ Clean up test data after execution
```

### 3. Clear Reports
```
✅ Capture screenshots on failure
✅ Add descriptive test names
✅ Log important steps
```

### 4. Maintenance
```
✅ Review failed tests immediately
✅ Update selectors when UI changes
✅ Keep dependencies updated
```

---

## 📚 Next Steps

After setting up CI/CD:

1. ✅ Push code to `canary` branch
2. ✅ Verify pipeline runs automatically
3. ✅ Check test reports in CI/CD platform
4. ✅ Configure notifications (email/Slack)
5. ✅ Set up scheduled runs (nightly builds)
6. ✅ Add code coverage (JaCoCo plugin)

---

## 🔗 Useful Links

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [GitLab CI/CD Documentation](https://docs.gitlab.com/ee/ci/)
- [Azure Pipelines Documentation](https://docs.microsoft.com/en-us/azure/devops/pipelines/)
- [Playwright CI Documentation](https://playwright.dev/java/docs/ci)

---

## 📞 Support

For issues:
1. Check pipeline logs
2. Review test execution output
3. Verify configuration files
4. Check this guide's troubleshooting section

---

**✅ CI/CD Setup Complete!** 🎉

Your tests will now run automatically on every push to the `canary` branch!
