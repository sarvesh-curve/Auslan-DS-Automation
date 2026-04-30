# CircleCI Setup Guide

## Overview

This project uses CircleCI for continuous integration and automated testing. The pipeline automatically runs tests when code is pushed to specific branches.

## Quick Setup (5 minutes)

### Step 1: Sign Up / Sign In to CircleCI

1. Go to [https://circleci.com/](https://circleci.com/)
2. Click **"Sign Up"** or **"Log In"**
3. Choose **"Sign up with GitHub"**
4. Authorize CircleCI to access your GitHub account

### Step 2: Connect Your Repository

1. After signing in, you'll see the CircleCI dashboard
2. Click **"Projects"** in the left sidebar
3. Find your repository: `sarvesh-curve/Auslan-DS-Automation`
4. Click **"Set Up Project"** button next to your repo

### Step 3: Configure the Project

1. CircleCI will automatically detect `.circleci/config.yml`
2. You'll see a preview of the configuration
3. Click **"Start Building"** or **"Use Existing Config"**
4. CircleCI will immediately trigger the first build

### Step 4: Verify Pipeline

1. Watch the first pipeline run in real-time
2. Check the build status (should turn green ✅)
3. Review test results and logs
4. Download artifacts if needed

## Pipeline Configuration

### File Location
```
.circleci/config.yml
```

### Workflows

#### 1. Build and Test (Main Workflow)
**Triggers:** Push to `main`, `develop`, or `canary` branches

**Steps:**
- Checkout code
- Install dependencies (with caching)
- Install Playwright browsers
- Compile project
- Run full regression test suite (8 tests)
- Generate reports
- Store artifacts

**Duration:** ~5-7 minutes

#### 2. Smoke Tests (PR Workflow)
**Triggers:** Push to `feature/*` or `bugfix/*` branches

**Steps:**
- Checkout code
- Install dependencies (with caching)
- Install Playwright browsers
- Run smoke tests only (5 tests)
- Generate reports
- Store artifacts

**Duration:** ~3-4 minutes

#### 3. Nightly Build
**Triggers:** Scheduled daily at midnight UTC

**Steps:**
- Same as Build and Test workflow
- Runs on `main` branch only
- Provides daily health check

**Duration:** ~5-7 minutes

## Viewing Results

### Dashboard

1. Go to CircleCI dashboard
2. Select your project
3. Click on the workflow run
4. View:
   - Build status
   - Test results
   - Execution logs
   - Duration

### Test Results

CircleCI automatically parses TestNG XML reports and displays:
- Total tests
- Passed tests
- Failed tests
- Skipped tests
- Test duration

### Artifacts

Download test reports and screenshots:

1. Click on a completed job
2. Go to **"Artifacts"** tab
3. Download:
   - `test-output/` - ExtentReports and screenshots
   - `surefire-reports/` - TestNG XML reports

## Triggering Builds

### Automatic Triggers

**On Push to `canary` branch:**
```bash
git add .
git commit -m "feat: Add new test"
git push origin canary
```
→ Full regression test suite runs

**On Push to `feature/*` branch:**
```bash
git checkout -b feature/new-feature
git push origin feature/new-feature
```
→ Smoke tests run

### Manual Triggers

1. Go to CircleCI dashboard
2. Select your project
3. Click **"Trigger Pipeline"** button
4. Select branch
5. Click **"Trigger Pipeline"**

## Branch Strategy

| Branch Pattern | Workflow | Test Suite | Duration |
|---------------|----------|------------|----------|
| `main` | Build and Test | Full regression (8 tests) | ~5-7 min |
| `develop` | Build and Test | Full regression (8 tests) | ~5-7 min |
| `canary` | Build and Test | Full regression (8 tests) | ~5-7 min |
| `feature/*` | Smoke Tests | Quick validation (5 tests) | ~3-4 min |
| `bugfix/*` | Smoke Tests | Quick validation (5 tests) | ~3-4 min |

## Configuration Details

### Executor

**Docker Image:** `cimg/openjdk:21.0`
- Pre-installed: Java 21, Maven, Git
- Resource Class: `large` (4 vCPU, 8GB RAM)

### Caching

Maven dependencies are cached to speed up builds:
- Cache key: `v1-maven-deps-{{ checksum "pom.xml" }}`
- Cache location: `~/.m2`
- Reduces build time by 2-3 minutes

### Parallelism

Tests run in parallel with 5 threads:
- Configured in `testng.xml`: `thread-count="5"`
- Faster execution than sequential
- Each test gets its own browser instance

### Artifacts Storage

**Stored Artifacts:**
- Test results: `target/surefire-reports/`
- Test reports: `test-output/`
- Screenshots: `test-output/screenshots/`

**Retention:** 30 days (CircleCI default)

## Environment Variables (Optional)

If you need to add secrets or environment variables:

1. Go to CircleCI dashboard
2. Select project → **Project Settings**
3. Go to **Environment Variables**
4. Add variables:
   - `TEST_EMAIL`
   - `TEST_PASSWORD`
   - etc.

Update `config.yml` to use them:
```yaml
- run:
    command: mvn test
    environment:
      TEST_EMAIL: ${TEST_EMAIL}
      TEST_PASSWORD: ${TEST_PASSWORD}
```

## Notifications

### Slack Integration (Optional)

1. Go to Project Settings → Integrations
2. Add Slack Webhook
3. Configure notification preferences:
   - Build success
   - Build failure
   - Only on main branch

### Email Notifications

Automatic email notifications are sent by default:
- Build failures
- Build status changes

Configure in: User Settings → Notifications

## Troubleshooting

### Issue: Pipeline Not Triggered

**Check:**
- Is repository connected to CircleCI?
- Is `.circleci/config.yml` present?
- Are you pushing to the correct branch?

**Solution:**
- Go to CircleCI → Projects → Set Up Project

### Issue: Tests Failing in CI but Passing Locally

**Common Causes:**
- Different environment (Linux in CI vs Mac/Windows locally)
- Missing dependencies
- Browser differences
- Timeout issues

**Solutions:**
1. Check CI logs for specific errors
2. Run tests locally with `headless=true` in `config.properties`
3. Increase timeouts if needed
4. Ensure all dependencies are in `pom.xml`

### Issue: Build Taking Too Long

**Solutions:**
1. Check if Maven cache is working
2. Verify `restore_cache` step in config
3. Consider reducing `thread-count` if resource issues
4. Upgrade to larger resource class:
   ```yaml
   resource_class: xlarge  # 8 vCPU, 16GB RAM
   ```

### Issue: Artifacts Not Available

**Check:**
- Is `store_artifacts` step in config?
- Did the job complete successfully?
- Are artifact paths correct?

**Solution:**
- Verify paths in `.circleci/config.yml`
- Check job logs for artifact upload messages

### Issue: Browser Installation Fails

**Solution:**
Update browser installation command:
```yaml
- run:
    name: Install Playwright Browsers
    command: mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"
```

## Advanced Configuration

### Run Different Test Suites

Edit `.circleci/config.yml`:

```yaml
# Run smoke tests
- run:
    name: Run Smoke Tests
    command: mvn test -DsuiteXmlFile=testng-smoke.xml

# Run regression tests
- run:
    name: Run Regression Tests
    command: mvn test -DsuiteXmlFile=testng-regression.xml

# Run specific test
- run:
    name: Run Login Test
    command: mvn test -Dtest=LoginTest
```

### Add Multiple Browsers

```yaml
jobs:
  test-chromium:
    # ... chromium tests
  
  test-firefox:
    # ... firefox tests
  
  test-webkit:
    # ... webkit tests

workflows:
  cross-browser-testing:
    jobs:
      - test-chromium
      - test-firefox
      - test-webkit
```

### Conditional Execution

Run jobs only on specific conditions:

```yaml
workflows:
  build-test:
    jobs:
      - build-and-test:
          filters:
            branches:
              only:
                - main
                - /^release-.*/
```

## Cost & Credits

### Free Plan
- 6,000 build minutes/month (for open source)
- 2,500 credits/week (for private repos)
- 1 concurrent job

### Paid Plans
- Performance Plan: $30/month
- Scale Plan: Custom pricing
- More credits and concurrency

**Check Usage:**
- CircleCI Dashboard → Plan → Usage

## Best Practices

1. **Cache Dependencies:** Always cache Maven dependencies
2. **Parallel Execution:** Use parallel tests for faster builds
3. **Fail Fast:** Configure to stop on first failure if needed
4. **Artifact Retention:** Store only essential artifacts
5. **Branch Strategy:** Use smoke tests on feature branches
6. **Notifications:** Set up Slack/email for failures
7. **Environment Variables:** Never commit secrets to code
8. **Resource Class:** Start with `medium`, upgrade if needed

## Support & Resources

### CircleCI Documentation
- [Getting Started](https://circleci.com/docs/getting-started/)
- [Configuration Reference](https://circleci.com/docs/configuration-reference/)
- [Maven on CircleCI](https://circleci.com/docs/language-java-maven/)

### Project Resources
- CircleCI Config: `.circleci/config.yml`
- README: `README.md`
- TestNG Suites: `testng.xml`, `testng-smoke.xml`, `testng-regression.xml`

### Getting Help
- CircleCI Support: [support.circleci.com](https://support.circleci.com/)
- CircleCI Community: [discuss.circleci.com](https://discuss.circleci.com/)
- GitHub Issues: Your repository issues page

## Next Steps

1. ✅ Complete CircleCI setup (follow steps above)
2. ✅ Push code to `canary` branch to trigger first build
3. ✅ Verify build passes successfully
4. ✅ Download and review test artifacts
5. ✅ Configure notifications (optional)
6. ✅ Add environment variables (if needed)
7. ✅ Share CircleCI dashboard with team

---

**Happy Testing with CircleCI!** 🎉
