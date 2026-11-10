package com.rajat.automation.core.driver;

import com.rajat.automation.core.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Factory responsible ONLY for creating WebDriver instances.
 *
 * This class contains browser-specific creation logic. It does NOT manage
 * driver lifecycle, storage, or cleanup — those responsibilities belong
 * to DriverManager. Keeping creation separate from lifecycle management
 * follows the Single Responsibility Principle and makes each class easier
 * to test and modify independently.
 *
 * Browser selection is driven by a string parameter (e.g., "chrome", "firefox", "edge"),
 * which allows the browser to be configured at runtime via system properties or
 * environment variables rather than being hardcoded.
 */
public class DriverFactory {

    // Prevent instantiation — this is a utility factory with static methods only.
    private DriverFactory() {
        throw new UnsupportedOperationException("DriverFactory should not be instantiated.");
    }

    public static WebDriver createDriver(String browser) {
        if (browser == null || browser.trim().isEmpty()) {
            throw new IllegalArgumentException("Browser must be specified.");
        }

        switch (browser.trim().toLowerCase()) {
            case "chrome":
                return createChromeDriver();
            case "firefox":
                return createFirefoxDriver();
            case "edge":
                return createEdgeDriver();
            default:
                throw new IllegalArgumentException(
                        "Unsupported browser: " + browser + ". Supported browsers: chrome, firefox, edge.");
        }
    }

    /**
     * Create a Chrome WebDriver instance.
     *
     * Selenium 4 uses W3C WebDriver protocol and automatic driver management
     * (WebDriverManager is no longer required for managed browsers like Chrome,
     * Firefox, and Edge on standard platforms). The ChromeOptions object allows
     * browser-specific capabilities to be added later (e.g., headless mode,
     * window size, proxy settings) without changing this method's signature.
     */
    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
        }
        return new FirefoxDriver(options);
    }


    private static WebDriver createEdgeDriver() {
        EdgeOptions options = new EdgeOptions();
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        return new EdgeDriver(options);
    }
}
