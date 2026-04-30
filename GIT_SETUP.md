# Git Setup & Push to Repository

## 🚀 Quick Setup (Copy & Paste)

### Step 1: Initialize Git and Create Canary Branch

```bash
cd /Users/SarveshChaudhari/Automation/playwright

# Initialize Git repository
git init

# Add all files
git add .

# Create initial commit
git commit -m "feat: Initial commit - Playwright Java automation framework with POM + TestNG"

# Create and switch to canary branch
git checkout -b canary
```

### Step 2: Create GitHub Repository

1. Go to [GitHub](https://github.com)
2. Click **New Repository** (+ icon, top right)
3. Fill in:
   - **Repository name**: `playwright-automation` (or your preferred name)
   - **Description**: `Playwright Java automation framework with POM, TestNG, and ExtentReports`
   - **Visibility**: Choose Public or Private
   - **DO NOT** initialize with README (we already have one)
4. Click **Create repository**

### Step 3: Connect and Push to GitHub

```bash
# Add remote repository (replace YOUR_USERNAME and YOUR_REPO)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# Push canary branch to remote
git push -u origin canary

# Push main/master branch (if needed)
git checkout -b main
git push -u origin main
git checkout canary
```

---

## 📝 Example Commands (Replace with Your Details)

```bash
# Example with actual repository
git remote add origin https://github.com/johndoe/playwright-automation.git
git push -u origin canary
```

---

## 🔄 Daily Workflow

### Making Changes and Pushing

```bash
# Check status
git status

# Stage all changes
git add .

# Commit with message
git commit -m "feat: Add new booking test case"

# Push to canary branch
git push origin canary
```

### Common Git Commands

```bash
# Check current branch
git branch

# Switch to canary branch
git checkout canary

# Pull latest changes
git pull origin canary

# View commit history
git log --oneline

# View file changes
git diff

# Discard local changes
git restore <filename>

# Create new branch from canary
git checkout -b feature/new-test canary
```

---

## 📋 Commit Message Best Practices

Use conventional commit format:

```bash
# New feature
git commit -m "feat: Add login with OTP test case"

# Bug fix
git commit -m "fix: Resolve timeout issue in booking flow"

# Test updates
git commit -m "test: Update signup test with dynamic data"

# Documentation
git commit -m "docs: Update README with CI/CD setup"

# Refactoring
git commit -m "refactor: Improve page object structure"

# Configuration
git commit -m "chore: Update Maven dependencies"
```

---

## 🌿 Branch Strategy

### Recommended Branches

```
main/master    → Production-ready code
├── canary     → Pre-production testing (CI/CD triggers here)
├── develop    → Development integration
└── feature/*  → Individual features
```

### Creating Feature Branches

```bash
# Create feature branch from canary
git checkout canary
git pull origin canary
git checkout -b feature/add-payment-test

# Make changes...
git add .
git commit -m "feat: Add payment gateway test"

# Push feature branch
git push origin feature/add-payment-test

# Create Pull Request on GitHub to merge into canary
```

---

## 🔐 Using SSH Instead of HTTPS (Recommended)

### Setup SSH Key

```bash
# Generate SSH key
ssh-keygen -t ed25519 -C "your_email@example.com"

# Start SSH agent
eval "$(ssh-agent -s)"

# Add SSH key
ssh-add ~/.ssh/id_ed25519

# Copy public key
cat ~/.ssh/id_ed25519.pub
```

### Add to GitHub

1. Go to GitHub → **Settings** → **SSH and GPG keys**
2. Click **New SSH key**
3. Paste your public key
4. Click **Add SSH key**

### Use SSH Remote URL

```bash
# Change remote from HTTPS to SSH
git remote set-url origin git@github.com:YOUR_USERNAME/YOUR_REPO.git

# Verify
git remote -v
```

---

## 📊 Check What Will Be Committed

```bash
# See all changes
git status

# See detailed file changes
git diff

# See staged changes
git diff --cached

# See files that will be committed
git diff --name-only --cached
```

---

## 🎯 First Time Push Commands

Copy and run these commands (replace placeholders):

```bash
# 1. Navigate to project
cd /Users/SarveshChaudhari/Automation/playwright

# 2. Initialize Git
git init

# 3. Add all files
git add .

# 4. Check what will be committed
git status

# 5. Create initial commit
git commit -m "feat: Initial commit - Playwright automation framework

- POM + TestNG framework structure
- 8 test cases (Login, Booking, Signup)
- ExtentReports with automatic cleanup
- Parallel and sequential execution support
- CI/CD pipeline configuration (GitHub Actions, Jenkins)
- Comprehensive documentation"

# 6. Create canary branch
git checkout -b canary

# 7. Add remote (REPLACE WITH YOUR REPO URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# 8. Push to remote
git push -u origin canary

# 9. Optionally push main branch
git checkout -b main
git push -u origin main

# 10. Switch back to canary for work
git checkout canary
```

---

## ✅ Verify Git Setup

```bash
# Check remote URL
git remote -v

# Should show:
# origin  https://github.com/YOUR_USERNAME/YOUR_REPO.git (fetch)
# origin  https://github.com/YOUR_USERNAME/YOUR_REPO.git (push)

# Check current branch
git branch

# Should show:
# * canary

# Check commit history
git log --oneline

# Check repository status
git status
```

---

## 🎉 After First Push

Once you push, your CI/CD pipeline will automatically trigger!

**What happens:**
1. ✅ GitHub Actions workflow starts
2. ✅ Smoke tests run (5 tests)
3. ✅ Regression tests run (8 tests)
4. ✅ ExtentReports generated
5. ✅ Artifacts uploaded

**View Results:**
```
GitHub Repository → Actions Tab → Latest Workflow Run
```

---

## 🔄 Updating Code (Daily Use)

```bash
# 1. Make changes to test files

# 2. Check what changed
git status
git diff

# 3. Stage changes
git add .

# 4. Commit
git commit -m "test: Update booking test with new date logic"

# 5. Push to canary
git push origin canary

# 6. CI/CD pipeline runs automatically!
```

---

## 📸 .gitignore (Already Created)

The `.gitignore` file ensures these are NOT committed:

```
✅ target/ (compiled files)
✅ test-output/ (reports)
✅ .idea/ (IDE settings)
✅ *.class (compiled Java)
✅ screenshots/ (test screenshots)
```

---

## 🐛 Troubleshooting

### Issue: Permission denied (publickey)

**Solution 1: Use HTTPS instead**
```bash
git remote set-url origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
```

**Solution 2: Setup SSH key**
See [Using SSH](#using-ssh-instead-of-https-recommended) section above.

### Issue: Remote already exists

```bash
# Remove existing remote
git remote remove origin

# Add new remote
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
```

### Issue: Rejected push (non-fast-forward)

```bash
# Pull latest changes first
git pull origin canary --rebase

# Then push
git push origin canary
```

### Issue: Large files (>100MB)

```bash
# Check file sizes
find . -type f -size +50M

# Remove from git if accidentally added
git rm --cached path/to/large/file

# Add to .gitignore
echo "path/to/large/file" >> .gitignore
```

---

## 📚 Useful Git Resources

- [Git Documentation](https://git-scm.com/doc)
- [GitHub Guides](https://guides.github.com/)
- [Git Cheat Sheet](https://education.github.com/git-cheat-sheet-education.pdf)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

## 🎓 Git Quick Reference

| Command | Description |
|---------|-------------|
| `git status` | Show working tree status |
| `git add .` | Stage all changes |
| `git commit -m "message"` | Commit staged changes |
| `git push` | Push to remote |
| `git pull` | Pull from remote |
| `git branch` | List branches |
| `git checkout <branch>` | Switch branch |
| `git log` | Show commit history |
| `git diff` | Show file changes |
| `git restore <file>` | Discard changes |

---

**✅ Ready to push your code!** 🚀

Run the commands in [First Time Push](#first-time-push-commands) section to get started!
