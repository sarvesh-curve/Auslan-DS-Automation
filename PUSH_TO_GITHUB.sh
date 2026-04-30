#!/bin/bash

# Push Auslan-DS-Playwright Framework to GitHub
# Repository: https://github.com/sarvesh-curve/Auslan-DS-Automation

echo "=========================================="
echo "🚀 Pushing to GitHub"
echo "=========================================="
echo ""

# Navigate to project directory
cd /Users/SarveshChaudhari/Automation/playwright

# Initialize Git (if not already done)
if [ ! -d .git ]; then
    echo "📦 Initializing Git repository..."
    git init
else
    echo "✅ Git already initialized"
fi

# Add all files
echo "📁 Adding all files to Git..."
git add .

# Create initial commit
echo "💾 Creating initial commit..."
git commit -m "feat: Playwright Java automation framework with POM, TestNG, and CI/CD

Framework Features:
- Page Object Model (POM) architecture
- TestNG integration with parallel execution
- 8 automated tests (Login, Booking, Signup)
- ExtentReports with graphs and automatic cleanup
- Screenshot capture on test failure
- Dynamic test data generation
- CI/CD pipelines (GitHub Actions, Jenkins)
- Comprehensive documentation

Test Suites:
- Smoke Tests: 5 tests (quick validation)
- Regression Tests: 8 tests (complete suite)

CI/CD:
- GitHub Actions workflow configured
- Jenkins pipeline ready
- Auto-triggers on push to canary branch

Documentation:
- Complete setup guides
- Git workflow documentation
- CI/CD integration guides
- Framework usage instructions"

# Create canary branch
echo "🌿 Creating canary branch..."
git checkout -b canary

# Add remote repository
echo "🔗 Adding remote repository..."
git remote add origin https://github.com/sarvesh-curve/Auslan-DS-Automation.git

# Push to GitHub
echo "⬆️  Pushing to GitHub (canary branch)..."
git push -u origin canary

echo ""
echo "=========================================="
echo "✅ Push Complete!"
echo "=========================================="
echo ""
echo "🎉 Your code is now on GitHub!"
echo ""
echo "📊 CI/CD Pipeline Status:"
echo "   Repository: https://github.com/sarvesh-curve/Auslan-DS-Automation"
echo "   Branch: canary"
echo "   CI/CD: Will trigger automatically"
echo ""
echo "🔍 View Results:"
echo "   1. Go to: https://github.com/sarvesh-curve/Auslan-DS-Automation"
echo "   2. Click 'Actions' tab"
echo "   3. View latest workflow run"
echo ""
echo "📦 Next Steps:"
echo "   • Check GitHub Actions for test results"
echo "   • Download ExtentReports from artifacts"
echo "   • Review test execution logs"
echo ""
echo "=========================================="
