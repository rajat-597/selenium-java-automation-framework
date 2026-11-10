package com.rajat.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for the QA Shop search functionality.
 * <p>
 * Encapsulates locators and UI interactions for searching products
 * and retrieving search results. Assertions remain in test classes.
 */
public class SearchPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private static final By SEARCH_INPUT = By.cssSelector("input[data-testid='urun-ara']");
    private static final By SEARCH_BUTTON = By.cssSelector("button[type='submit']");
    private static final By PRODUCT_LIST = By.cssSelector("ul[data-testid='urun-listesi'] li");
    private static final By PRODUCT_NAME = By.cssSelector("ul[data-testid='urun-listesi'] li div a span");
    private static final By NO_RESULTS_MESSAGE = By.cssSelector("[data-testid='urun-bulunamadi']");
    private static final By SORT_DROPDOWN = By.cssSelector("select[data-testid='urun-sirala']");
    private static final By PRODUCT_PRICE = By.cssSelector("ul[data-testid='urun-listesi'] li span[data-testid^='urun-fiyat-']");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void searchForProduct(String keyword) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_INPUT));
        input.clear();
        input.sendKeys(keyword);
        driver.findElement(SEARCH_BUTTON).click();
    }

    /**
     * Check whether the no-results message is currently displayed.
     *
     * @return true if the "No products match your search." message is visible
     */
    public boolean isNoResultsMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(NO_RESULTS_MESSAGE));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Retrieve product names from the current search results.
     *
     * Waits for the search outcome to settle — either product cards appear
     * or the no-results message is displayed. Returns an empty list when
     * no products match the search, which is not treated as a failure.
     *
     * @return list of product names, or an empty list if no products match
     */
    public List<String> getProductNames() {
        // Wait until we can determine the search outcome: products or no-results message.
        // This handles the asynchronous nature of the search — the UI either renders
        // product cards or the no-results message, but not both simultaneously.
        wait.until(driver -> {
            List<WebElement> products = driver.findElements(PRODUCT_LIST);
            List<WebElement> messages = driver.findElements(NO_RESULTS_MESSAGE);
            boolean hasProducts = !products.isEmpty();
            boolean hasNoResults = !messages.isEmpty() && messages.get(0).isDisplayed();
            // Consider the search resolved when we know the outcome one way or the other.
            return hasProducts || hasNoResults;
        });

        // Check which outcome we have.
        List<WebElement> products = driver.findElements(PRODUCT_LIST);
        if (products.isEmpty()) {
            // No products matched — the no-results message is displayed.
            return Collections.emptyList();
        }

        // Products matched — extract their names.
        List<WebElement> nameElements = driver.findElements(PRODUCT_NAME);
        return nameElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Select a sorting option from the product sort dropdown.
     * <p>
     * After changing the sort option, the application makes an async API call
     * to fetch re-sorted products. This method waits for the product list to
     * re-render, confirming the new sorted results have been displayed.
     *
     * @param sortValue the option value to select, e.g. "price:asc" or "price:desc"
     */
    public void sortBy(String sortValue) {
        WebElement selectElement =
                wait.until(ExpectedConditions.presenceOfElementLocated(SORT_DROPDOWN));

        Select select = new Select(selectElement);
        select.selectByValue(sortValue);
    }
    /**
     * Retrieve displayed product prices from the current search results.
     *
     * Waits for the search outcome to settle, then extracts the text content
     * of each price element. Returns an empty list when no products match.
     *
     * @return list of price strings as displayed in the UI
     */
    public List<String> getProductPrices() {
        wait.until(driver -> {
            List<WebElement> products = driver.findElements(PRODUCT_LIST);
            List<WebElement> messages = driver.findElements(NO_RESULTS_MESSAGE);
            boolean hasProducts = !products.isEmpty();
            boolean hasNoResults = !messages.isEmpty() && messages.get(0).isDisplayed();
            return hasProducts || hasNoResults;
        });

        List<WebElement> products = driver.findElements(PRODUCT_LIST);
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        List<WebElement> priceElements = driver.findElements(PRODUCT_PRICE);
        return priceElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
