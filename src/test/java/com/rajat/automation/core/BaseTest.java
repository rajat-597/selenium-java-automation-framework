package com.rajat.automation.core;

import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.core.driver.DriverManager;
import com.rajat.automation.core.listeners.TestListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Listeners(TestListener.class)
public class BaseTest {

    private String browser;
    /**
     * Set up the WebDriver before each test method.
     *
     * Configuration is resolved through ConfigReader, which applies the
     * precedence chain: defaults → config.properties → env vars → system properties.
     * BaseTest does not need to know where the value came from.
     */

  /*  @BeforeMethod(alwaysRun = true)
    public void setupDriver() {
        browser = ConfigReader.getBrowser();
        WebDriver driver = DriverManager.getDriver(browser);
        driver.manage().window().maximize();

    } */

    // New setup method — reads the browser from TestNG XML
    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUpWithBrowserParameter(@Optional("") String browser) {

        this.browser = (browser == null || browser.isBlank())
                ? ConfigReader.getBrowser() : browser;

        DriverManager.getDriver(this.browser).manage().window().maximize();
    }

    /**
     * Tear down the WebDriver after each test method.
     *
     * Quits the browser session and clears the ThreadLocal reference so the next
     * test starts with a fresh driver. Using @AfterMethod (instead of @AfterClass)
     * ensures each test method gets an isolated browser session, which is important
     * for test independence and reliable parallel execution.
     */
    @AfterMethod(alwaysRun = true)
    public void teardownDriver() {
        DriverManager.quitDriver();
    }

    /**
     * Convenience method for test classes to access the current thread's WebDriver.
     *
     * Test classes call this method instead of accessing DriverManager directly,
     * keeping the API simple. The actual driver reference is stored in ThreadLocal
     * by DriverManager, ensuring parallel test isolation.
     *
     * @return the WebDriver instance for the current thread
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver(browser);
    }
}
