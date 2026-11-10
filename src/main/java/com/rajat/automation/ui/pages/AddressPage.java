package com.rajat.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddressPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for the address form fields (checkout/payment page)
    private static final By ADD_NEW_ADDRESS = By.cssSelector("[data-testid='yeni-adres-ac']");
    private static final By ADDRESS_LABEL_INPUT = By.cssSelector("[data-testid='yeni-adres-label']");
    private static final By ADDRESS_LINE_INPUT = By.cssSelector("[data-testid='yeni-adres-line1']");
    private static final By CITY_INPUT = By.cssSelector("[data-testid='yeni-adres-city']");
    private static final By POSTAL_CODE_INPUT = By.cssSelector("[data-testid='yeni-adres-postal_code']");
    private static final By COUNTRY_CODE_INPUT = By.cssSelector("[data-testid='yeni-adres-country']");

    public AddressPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickAddNewAddress() {

        WebElement addAddressButton = wait.until(ExpectedConditions.presenceOfElementLocated(ADD_NEW_ADDRESS));

        // Scroll the button into the visible area
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addAddressButton);

        // Wait until the button is clickable
        wait.until(ExpectedConditions.elementToBeClickable(ADD_NEW_ADDRESS));

        addAddressButton.click();
    }

    public void enterAddress(String addressLabel, String addressLine, String city, String postalCode, String countryCode) {
        WebElement labelInput = wait.until(ExpectedConditions.visibilityOfElementLocated(ADDRESS_LABEL_INPUT));
        labelInput.clear();
        labelInput.sendKeys(addressLabel);

        WebElement lineInput = wait.until(ExpectedConditions.visibilityOfElementLocated(ADDRESS_LINE_INPUT));
        lineInput.clear();
        lineInput.sendKeys(addressLine);

        WebElement cityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(CITY_INPUT));
        cityInput.clear();
        cityInput.sendKeys(city);

        WebElement postalCodeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(POSTAL_CODE_INPUT));
        postalCodeInput.clear();
        postalCodeInput.sendKeys(postalCode);

        WebElement countryCodeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(COUNTRY_CODE_INPUT));
        countryCodeInput.clear();
        countryCodeInput.sendKeys(countryCode);
    }
}
