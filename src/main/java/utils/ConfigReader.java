package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.out.println("Config file not found, using default values");
            properties = new Properties();
            // Set default values
            properties.setProperty("app.url", "http://auslan-ds-canary-app.s3-website-ap-southeast-2.amazonaws.com/#/authenticate/logout");
            properties.setProperty("browser", "chromium");
            properties.setProperty("headless", "false");
            properties.setProperty("test.email", "sarvesh@curvetomorrow.com.au");
            properties.setProperty("test.password", "Curve@2025");
        }
    }

    public static String getProperty(String key) {
        // First check environment variables (for CI/CD)
        String envValue = getEnvironmentVariable(key);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        // Then check properties file
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        // First check environment variables (for CI/CD)
        String envValue = getEnvironmentVariable(key);
        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }
        // Then check properties file
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get value from environment variable
     * Converts property key format to environment variable format
     * Example: smtp.username -> SMTP_USERNAME
     */
    private static String getEnvironmentVariable(String key) {
        String envKey = key.replace(".", "_").toUpperCase();
        return System.getenv(envKey);
    }

    public static String getAppUrl() {
        return getProperty("app.url");
    }

    public static String getTestEmail() {
        return getProperty("test.email");
    }

    public static String getTestPassword() {
        return getProperty("test.password");
    }
}
