package com.rajat.automation.core.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    // ThreadLocal provides isolated storage so that each thread can have its own WebDriver reference.
    // Without ThreadLocal, parallel tests would share the same driver reference,
    // causing race conditions and unpredictable test failures.
    private static final ThreadLocal<WebDriver> DRIVER_HOLDER = new ThreadLocal<>();

    public static WebDriver getDriver(String browser) {
        // ThreadLocal.get() returns null on first call for a new thread.
        // We only create a new driver if one doesn't already exist for this thread.
        // DRIVER_HOLDER.get() and DRIVER_HOLDER.set() are ThreadLocal methods.They are not methods of DriverManager.
        // Get the WebDriver stored for this current thread and Store this WebDriver for this current thread."
        if (DRIVER_HOLDER.get() == null) {
            WebDriver driver = DriverFactory.createDriver(browser);
            DRIVER_HOLDER.set(driver);
        }
        return DRIVER_HOLDER.get();
    }

    /**
     * Return the WebDriver for the current thread without creating a new one.
     *
     * This method simply returns the already-initialized driver stored in ThreadLocal.
     * It does not create a new WebDriver. Callers should ensure that getDriver(String browser)
     * has been called first to initialize the driver for this thread.
     *
     * @return the WebDriver for the current thread, or null if not yet initialized
     */
    public static WebDriver getDriver() {
        return DRIVER_HOLDER.get();
    }

    /**
     * Quit the current thread's WebDriver and clean up the ThreadLocal reference.
     *
     * This method performs two critical cleanup steps:
     * 1. quit() — closes the browser and ends the WebDriver session, releasing
     *    the browser process and associated resources.
     * 2. remove() — clears the ThreadLocal reference so the next call to
     *    getDriver() creates a fresh driver instead of reusing a stale reference.
     *    But the ThreadLocal can still contain the reference to that now-dead driver.
     *
     * Failing to call remove() after quit() would leave a null or stale reference
     * in ThreadLocal. On thread reuse (e.g., thread pool scenarios), this could
     * cause NullPointerException or attempts to use an already-quit driver.
     */

    public static void quitDriver() {
        WebDriver driver = DRIVER_HOLDER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                // Always remove the ThreadLocal reference, even if quit() throws.
                // This prevents stale driver references from persisting in ThreadLocal.
                DRIVER_HOLDER.remove();
            }
        }
    }
}
