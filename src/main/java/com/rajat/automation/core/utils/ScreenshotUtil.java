
package com.rajat.automation.core.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtil {

    /**
     * Captures the current browser screenshot and saves it
     * in the screenshots directory at the project root.
     *
     * @param driver   the WebDriver instance
     * @param filename the screenshot filename
     * @return the saved screenshot file, or null if capture fails
     */
    public static File takeScreenshot(WebDriver driver, String filename) {

        if (driver == null) {
            System.err.println("Cannot capture screenshot: WebDriver is null.");
            return null;
        }

        try {
            File screenshotFile =
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            Path targetDir = Paths.get("screenshots");
            Files.createDirectories(targetDir);

            Path targetPath = targetDir.resolve(filename);

            Files.copy(
                    screenshotFile.toPath(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            File savedScreenshot = targetPath.toFile();

            System.out.println("Screenshot saved to: "
                    + savedScreenshot.getAbsolutePath());

            return savedScreenshot;

        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }
}
