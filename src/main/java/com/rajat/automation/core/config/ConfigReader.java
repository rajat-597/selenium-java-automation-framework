package com.rajat.automation.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized configuration reader with a 4-level precedence system.
 *
 * Precedence (lowest to highest):
 *   1. Default values hardcoded in this class
 *   2. Values from config.properties file
 *   3. Environment variables (e.g., BROWSER, ENVIRONMENT, BASE_URL, TIMEOUT)
 *   4. Maven/System properties (e.g., -Dbrowser=firefox)
 *
 * This design means a Maven system property always wins, which is important
 * for CI/CD pipelines where runtime overrides are common. Environment variables
 * are the next priority, making them suitable for secrets (passwords, tokens)
 * that should never be committed to a properties file.
 *
 * ConfigReader is loaded once and cached — the Properties object is static
 * so the file is read only once per JVM lifecycle, not on every getter call.
 */
public class ConfigReader {

    // Load config.properties once at class load time. The file lives on the
    // classpath under src/main/resources, so it's available via classloader.
    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                PROPERTIES.load(input);
            }
            // If the file is missing, PROPERTIES remains empty.
            // Defaults in each getter method handle that case gracefully.
        } catch (IOException e) {
            // If the file exists but can't be read, log a warning and fall back to defaults.
            // This prevents the entire framework from crashing due to a config file issue.
            System.err.println("Warning: Failed to load config.properties. Using default values.");
        }
    }

    /**
     * Resolve a configuration value using the 4-level precedence.
     *
     * The resolution order is:
     * 1. System property (highest priority — Maven runtime override)
     * 2. Environment variable
     * 3. Properties file value
     * 4. Default value (lowest priority)
     *
     * @param propertyKey the key used in config.properties and as the lowercase system property name
     * @param envVarName the uppercase environment variable name
     * @param defaultValue the fallback value when no other source provides a value
     * @return the resolved configuration value
     */
    private static String resolve(String propertyKey, String envVarName, String defaultValue) {
        // Level 4: System property (highest priority)
        String value = System.getProperty(propertyKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        // Level 3: Environment variable
        value = System.getenv(envVarName);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        // Level 2: Properties file
        value = PROPERTIES.getProperty(propertyKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        // Level 1: Default value (lowest priority)
        return defaultValue;
    }

    /**
     * Get the browser to use for test execution.
     * Supported values: chrome, firefox, edge.
     *
     * @return the browser name
     */
    public static String getBrowser() {
        return resolve("browser", "BROWSER", "chrome");
    }

    /**
     * Get the target environment name (e.g., qa, staging, uat).
     *
     * @return the environment name
     */
    public static String getEnvironment() {

        return resolve("environment", "ENVIRONMENT", "qa");
    }

    /**
     * Get the base URL of the application under test.
     *
     * @return the base URL
     */
    public static String getBaseUrl() {

        return resolve("baseUrl", "BASE_URL", "http://localhost");
    }

    /**
     * Get the default timeout in seconds.
     * Returns an int for type-safe usage — callers don't need to parse a String.
     *
     * @return the timeout value in seconds
     * @throws NumberFormatException if the resolved value is not a valid integer
     */
    public static int getTimeout() {
        String value = resolve("timeout", "TIMEOUT", "10");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // If the resolved value is not a valid number, fall back to the default.
            // This prevents tests from crashing due to a misconfigured timeout value.
            System.err.println("Warning: Invalid timeout value '" + value + "'. Using default: 10.");
            return 10;
        }
    }

    /**
     * Get the login email for authenticated tests.
     * Environment variable LOGIN_EMAIL or system property -DloginEmail=... can override the config file value.
     *
     * @return the login email address
     */
    public static String getLoginEmail() {
        return resolve("loginEmail", "LOGIN_EMAIL", "demo@qashop.test");
    }

    /**
     * Get the login password for authenticated tests.
     * Environment variable LOGIN_PASSWORD or system property -DloginPassword=... can override the config file value.
     * Never log this value.
     *
     * @return the login password
     */
    public static String getLoginPassword() {
        return resolve("loginPassword", "LOGIN_PASSWORD", "Password123!");
    }

    /**
     * Get the base URL of the API under test (e.g., QA Shop API).
     * Environment variable API_BASE_URL or system property -Dapi.baseUrl=... can override the config file value.
     *
     * @return the API base URL
     */
    public static String getApiBaseUrl() {

        return resolve("api.baseUrl", "API_BASE_URL", "http://localhost:4000");
    }

    /**
     * Check whether browsers should run in headless mode.
     * System property -Dheadless=true or environment variable HEADLESS=true will enable headless execution.
     * This is primarily used for CI/CD pipelines where no display is available.
     *
     * @return true if headless mode is enabled, false otherwise
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(
                resolve("headless", "HEADLESS", "false")
        );
    }
}
