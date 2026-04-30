pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 21'
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
    }
    
    parameters {
        choice(
            name: 'TEST_SUITE',
            choices: ['smoke', 'regression', 'all'],
            description: 'Select which test suite to run'
        )
        choice(
            name: 'BROWSER',
            choices: ['chromium', 'firefox', 'webkit'],
            description: 'Select browser for testing'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run tests in headless mode'
        )
    }
    
    triggers {
        // Trigger on push to canary branch
        githubPush()
        
        // Schedule nightly regression tests (2 AM)
        cron('0 2 * * *')
    }
    
    stages {
        stage('📥 Checkout') {
            steps {
                script {
                    echo "=========================================="
                    echo "📥 Checking out code from ${env.BRANCH_NAME}"
                    echo "=========================================="
                }
                checkout scm
            }
        }
        
        stage('🔧 Setup') {
            steps {
                script {
                    echo "=========================================="
                    echo "🔧 Setting up environment"
                    echo "=========================================="
                }
                sh '''
                    java -version
                    mvn -version
                '''
            }
        }
        
        stage('📦 Build') {
            steps {
                script {
                    echo "=========================================="
                    echo "📦 Building project"
                    echo "=========================================="
                }
                sh 'mvn clean install -DskipTests'
            }
        }
        
        stage('🧪 Run Tests') {
            steps {
                script {
                    echo "=========================================="
                    echo "🧪 Running ${params.TEST_SUITE} tests"
                    echo "Browser: ${params.BROWSER}"
                    echo "Headless: ${params.HEADLESS}"
                    echo "=========================================="
                    
                    def suiteFile = ''
                    switch(params.TEST_SUITE) {
                        case 'smoke':
                            suiteFile = 'testng-smoke.xml'
                            break
                        case 'regression':
                            suiteFile = 'testng-regression.xml'
                            break
                        default:
                            suiteFile = 'testng.xml'
                    }
                    
                    sh """
                        mvn test -DsuiteXmlFile=${suiteFile} \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=${params.HEADLESS}
                    """
                }
            }
            post {
                always {
                    script {
                        echo "=========================================="
                        echo "📊 Test execution completed"
                        echo "=========================================="
                    }
                }
            }
        }
        
        stage('📊 Generate Reports') {
            steps {
                script {
                    echo "=========================================="
                    echo "📊 Generating test reports"
                    echo "=========================================="
                }
                
                // Archive test results
                junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                
                // Archive ExtentReports
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'test-output',
                    reportFiles: 'ExtentReport_*.html',
                    reportName: 'ExtentReport',
                    reportTitles: 'Test Execution Report'
                ])
            }
        }
        
        stage('📸 Archive Artifacts') {
            when {
                expression { currentBuild.result == 'FAILURE' || currentBuild.result == 'UNSTABLE' }
            }
            steps {
                script {
                    echo "=========================================="
                    echo "📸 Archiving screenshots and logs"
                    echo "=========================================="
                }
                archiveArtifacts artifacts: 'test-output/screenshots/*.png', allowEmptyArchive: true
                archiveArtifacts artifacts: 'test-output/*.html', allowEmptyArchive: true
            }
        }
    }
    
    post {
        always {
            script {
                echo "=========================================="
                echo "🏁 Pipeline Execution Complete"
                echo "=========================================="
                echo "Build Status: ${currentBuild.currentResult}"
                echo "Duration: ${currentBuild.durationString}"
                echo "=========================================="
            }
            
            // Clean workspace
            cleanWs()
        }
        
        success {
            script {
                echo "✅ All tests passed successfully!"
            }
            // Optional: Send success notification
            // emailext (
            //     subject: "✅ Tests Passed: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
            //     body: "All tests completed successfully on ${env.BRANCH_NAME} branch.",
            //     to: "team@example.com"
            // )
        }
        
        failure {
            script {
                echo "❌ Tests failed! Check the reports for details."
            }
            // Optional: Send failure notification
            // emailext (
            //     subject: "❌ Tests Failed: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
            //     body: "Test execution failed on ${env.BRANCH_NAME} branch. Check ExtentReport for details.",
            //     to: "team@example.com"
            // )
        }
        
        unstable {
            script {
                echo "⚠️  Some tests failed. Build is unstable."
            }
        }
    }
}
