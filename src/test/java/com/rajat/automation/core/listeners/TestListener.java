
package com.rajat.automation.core.listeners;

import com.rajat.automation.core.driver.DriverManager;
import com.rajat.automation.core.utils.ScreenshotUtil;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener extends AllureTestNg {

    @Override
    public void onTestFailure(ITestResult result) {

        String testName = result.getName();

        Throwable exception = result.getThrowable();

        if (exception != null) {
            System.err.println("Failure reason: " + exception.getMessage());
            exception.printStackTrace();
        }

        try {
            WebDriver driver = DriverManager.getDriver();

            if (driver == null) {
                System.err.println(
                        "Test failed but WebDriver is null for: " + testName);
            } else {

                try {
                    System.out.println("Current URL: " + driver.getCurrentUrl());
                } catch (Exception e) {
                    System.err.println(
                            "Could not retrieve current URL: " + e.getMessage());
                }

                String timestamp = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")
                );

                String filename = testName + "_" + timestamp + ".png";

                File screenshot = ScreenshotUtil.takeScreenshot(driver, filename);

                if (screenshot != null && screenshot.exists()) {
                    System.out.println(
                            "Failure screenshot saved: "
                                    + screenshot.getAbsolutePath()
                    );
                } else {
                    System.err.println(
                            "Screenshot was not saved for test: " + testName);
                }
            }

        } catch (Exception e) {
            System.err.println(
                    "Failed to capture screenshot for test: "
                            + testName
                            + ". Reason: "
                            + e.getMessage()
            );

        } finally {
            // Allow AllureTestNg to complete its failure handling.
            super.onTestFailure(result);
        }
    }
}
