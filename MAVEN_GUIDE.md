# Maven Quick Reference for Playwright Java

## Essential Commands

### Build & Compile
```bash
# Clean and compile
mvn clean compile

# Clean, compile, and run tests
mvn clean test

# Full build with all phases
mvn clean install
```

### Running Tests
```bash
# Run all tests (uses testng.xml)
mvn test

# Run a specific test class
mvn test -Dtest=CancelBooking

# Run multiple test classes
mvn test -Dtest=CancelBooking,Login1

# Run tests with specific methods
mvn test -Dtest=CancelBooking#testMethod

# Skip tests during build
mvn install -DskipTests
```

### Playwright Browser Management
```bash
# Install Playwright browsers
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

# Install specific browser
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"

# Check installed browsers
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install-deps"
```

### Project Management
```bash
# View effective POM
mvn help:effective-pom

# Display dependency tree
mvn dependency:tree

# Check for dependency updates
mvn versions:display-dependency-updates

# Clean target directory
mvn clean
```

### Debugging & Troubleshooting
```bash
# Run with debug output
mvn clean test -X

# Run with stack traces
mvn clean test -e

# Update dependencies
mvn clean install -U

# Verify project structure
mvn validate
```

## Project Structure
```
playwright/
├── src/
│   ├── main/java/          # Production code
│   └── test/java/          # Test code
├── target/                 # Compiled classes & test results
├── pom.xml                 # Maven configuration
└── testng.xml             # TestNG suite configuration
```

## TestNG Configuration Tips

Edit `testng.xml` to customize test execution:

### Run tests in parallel
```xml
<suite name="Suite" parallel="tests" thread-count="5">
```

### Run tests sequentially
```xml
<suite name="Suite" parallel="false">
```

### Group tests
```xml
<test name="Smoke Tests">
    <groups>
        <run>
            <include name="smoke"/>
        </run>
    </groups>
</test>
```

## Common Issues & Solutions

### Issue: Browsers not installed
**Solution:**
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Issue: Compilation errors after pull
**Solution:**
```bash
mvn clean compile
```

### Issue: Test failures due to stale dependencies
**Solution:**
```bash
mvn clean install -U
```

### Issue: Port already in use
**Solution:** Check and kill processes using the port:
```bash
lsof -ti:PORT | xargs kill -9
```

## IDE Integration

### IntelliJ IDEA
- Right-click `pom.xml` → Maven → Reload Project
- Use Maven tool window (View → Tool Windows → Maven)
- Run tests: Right-click test class → Run

### VS Code
- Install "Maven for Java" extension
- Use Command Palette (Cmd+Shift+P) → Maven: Execute commands
- Use Maven side panel

### Eclipse
- Right-click project → Maven → Update Project
- Use Run Configurations for test execution

## Environment Variables

Set in your shell or IDE:

```bash
# Set Java home
export JAVA_HOME=/path/to/java21

# Set Maven options
export MAVEN_OPTS="-Xmx1024m"

# Set Playwright options
export PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1  # Skip browser download
```

## CI/CD Integration Examples

### GitHub Actions
```yaml
- name: Set up JDK 21
  uses: actions/setup-java@v3
  with:
    java-version: '21'
    distribution: 'temurin'
    
- name: Cache Maven dependencies
  uses: actions/cache@v3
  with:
    path: ~/.m2
    key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
    
- name: Build and test
  run: mvn clean test
```

### Jenkins
```groovy
pipeline {
    agent any
    tools {
        maven 'Maven 3.9'
        jdk 'JDK 21'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}
```

## Useful Links
- [Maven Documentation](https://maven.apache.org/guides/)
- [Playwright Java API](https://playwright.dev/java/docs/intro)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
