package com.rajat.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class ProductDetailsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators for the product list — clicking a product link navigates to the details page
    private static final By PRODUCT_LINK = By.cssSelector("a[data-testid^='urun-detay-']");
    private static final By FIRST_PRODUCT_NAME_IN_LIST = By.cssSelector("ul[data-testid='urun-listesi'] li:nth-child(1) div a span");
    private static final By FIRST_PRODUCT_PRICE_IN_LIST = By.cssSelector("ul[data-testid='urun-listesi'] li:nth-child(1) span[data-testid^='urun-fiyat-']");
    
    // Locator for the Add to Cart button on the first product card
    private static final By ADD_TO_CART_FIRST_PRODUCT = By.cssSelector("ul[data-testid='urun-listesi'] li:nth-child(1) button[data-testid^='urun-sepete-']");

    // Locators for the product details page
    private static final By PRODUCT_DETAILS_NAME = By.cssSelector("[data-testid='detay-ad']");
    private static final By PRODUCT_DETAILS_PRICE = By.cssSelector("[data-testid='detay-fiyat']");
    private static final By PRODUCT_DETAILS_RATING = By.cssSelector("[data-testid^='urun-puan-']");
    private static final By PRODUCT_DETAILS_DESCRIPTION = By.cssSelector("[data-testid^='detay-aciklama']");

    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Click the first product link on the current page to navigate to its details page.
     * <p>
     * Waits for the product link to be present and clicks it, then waits for the
     * product name element on the details page to become visible, confirming the
     * navigation has completed.
     */
    public void clickFirstProduct() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(PRODUCT_LINK));
        link.click();
        // Wait for the details page to load by checking the product name is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_DETAILS_NAME));
    }

    /**
     * Get the product name displayed on the details page.
     *
     * @return the product name text
     */
    public String getProductName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_DETAILS_NAME)).getText();
    }

    /**
     * Get the product price displayed on the details page.
     *
     * @return the product price text
     */
    public String getProductPrice() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_DETAILS_PRICE)).getText();
    }

    /**
     * Get the product rating displayed on the details page.
     *
     * @return the product rating text
     */
    public String getProductRating() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_DETAILS_RATING)).getText();
    }

    /**
     * Get the product description displayed on the details page.
     *
     * @return the product description text
     */
    public String getProductDescription() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_DETAILS_DESCRIPTION)).getText();
    }

    public String getFirstProductNameInList() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_PRODUCT_NAME_IN_LIST)).getText();
    }

    public String getFirstProductPriceInList() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_PRODUCT_PRICE_IN_LIST)).getText();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Click the Add to Cart button on the first product card in the catalog.
     * <p>
     * Waits for the button to be clickable and clicks it. The caller should
     * subsequently verify the product appears in the cart.
     */
    public void clickAddToCartForFirstProduct() {
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(ADD_TO_CART_FIRST_PRODUCT));
        addToCartBtn.click();
    }
}
