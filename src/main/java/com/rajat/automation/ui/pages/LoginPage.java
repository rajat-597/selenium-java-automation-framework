package com.rajat.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator to trigger the sign-in modal from the landing page (disappears after login)
    private static final By SIGN_IN_BUTTON = By.cssSelector("[data-testid='giris-ac']");

    // Locators for the sign-in modal form fields
    private static final By EMAIL_INPUT = By.cssSelector("[data-testid='giris-eposta']");
    private static final By PASSWORD_INPUT = By.cssSelector("[data-testid='giris-parola']");
    private static final By SUBMIT_BUTTON = By.cssSelector("[data-testid='giris-yap']");

    // Locator for authenticated state — appears after successful login
    private static final By SIGN_OUT_BUTTON = By.cssSelector("[data-testid='cikis-yap']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openSignInModal() {
        WebElement signInBtn = wait.until(ExpectedConditions.elementToBeClickable(SIGN_IN_BUTTON));
        signInBtn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_INPUT));
    }

    public void enterEmail(String email) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(EMAIL_INPUT));
        input.clear();
        input.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement pass = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_INPUT));
        pass.clear();
        pass.sendKeys(password);
    }

    public void submitLogin() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON));
        btn.click();
        // Wait for the sign-out button to appear after successful login
        wait.until(ExpectedConditions.visibilityOfElementLocated(SIGN_OUT_BUTTON));
    }


    public void login(String email, String password) {
        openSignInModal();
        enterEmail(email);
        enterPassword(password);
        submitLogin();
    }

    public boolean isAuthenticated() {
        return !driver.findElements(SIGN_OUT_BUTTON).isEmpty()
                && driver.findElements(SIGN_IN_BUTTON).isEmpty();
    }
}
