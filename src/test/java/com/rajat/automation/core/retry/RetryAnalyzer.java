package com.rajat.automation.core.retry;

import org.openqa.selenium.InvalidSelectorException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 2;

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        Throwable exception = result.getThrowable();

        if (exception == null || !isRetryable(exception)) {
            return false;
        }

        if (retryCount < MAX_RETRIES) {
            retryCount++;

            System.out.println(
                    "Retrying test: " + result.getName()
                            + " | Retry: " + retryCount
                            + " of " + MAX_RETRIES
                            + " | Exception: "
                            + exception.getClass().getSimpleName()
            );

            return true;
        }

        return false;
    }

    private boolean isRetryable(Throwable exception) {
        // Do not retry assertion failures.
        if (exception instanceof AssertionError) {
            return false;
        }

        // Do not retry invalid selectors.
        if (exception instanceof InvalidSelectorException) {
            return false;
        }

        // No failure categories are approved for retry yet.
        // Add a category only when there is evidence
        // that retrying it is justified.
        return false;
    }
}
