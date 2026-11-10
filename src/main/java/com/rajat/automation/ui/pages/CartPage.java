package com.rajat.automation.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the QA Shop cart functionality.
 * <p>
 * Encapsulates locators and UI interactions for the cart button,
 * cart contents, and quantity controls. Assertions remain in test classes.
 */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locator for the cart button/icon
    private static final By CART_BUTTON = By.cssSelector("[data-testid='sepet-butonu']");

    // Cart items list container
    private static final By CART_ITEMS_LIST = By.cssSelector("ul[data-testid='sepet-satirlari'] li");

    // Locators for the first cart item's details
    private static final By FIRST_ITEM_NAME = By.cssSelector("ul[data-testid='sepet-satirlari'] li:nth-child(1) p.text-sm.font-bold");
    // Unit price is embedded in combined text like "S · Green · TRY 101.99"
    // No data-testid available; uses confirmed CSS classes
    private static final By FIRST_ITEM_UNIT_PRICE = By.cssSelector("ul[data-testid='sepet-satirlari'] li:nth-child(1) p.text-xs.opacity-60");

    // Quantity controls for the first cart item
    private static final By FIRST_ITEM_QUANTITY = By.cssSelector("ul[data-testid='sepet-satirlari'] li:nth-child(1) span[data-testid^='adet-']");
    private static final By INCREASE_BUTTON = By.cssSelector("ul[data-testid='sepet-satirlari'] li:nth-child(1) button[data-testid^='adet-artir-']");
    private static final By DECREASE_BUTTON = By.cssSelector("ul[data-testid='sepet-satirlari'] li:nth-child(1) button[data-testid^='adet-azalt-']");

    // Item total for the first cart item (no data-testid available; uses confirmed CSS classes)
    private static final By FIRST_ITEM_TOTAL = By.cssSelector("ul[data-testid='sepet-satirlari'] li:nth-child(1) span.w-24.text-right.text-sm.font-extrabold");

    // Remove button
    private static final By REMOVE_BUTTON = By.cssSelector("ul[data-testid='sepet-satirlari'] li:nth-child(1) button[data-testid^='sepet-sil-']");
    private static final By EMPTY_CART_MESSAGE = By.cssSelector("p[data-testid='sepet-bos']");

    // start shopping again after removing product
    private static final By START_SHOPPING_BUTTON = By.cssSelector("button[data-testid='sepet-bos-vitrin']");
    private static final By CATALOG_HEADING = By.cssSelector("h1[data-testid='vitrin-basligi']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openCart() {
        WebElement cartBtn = wait.until(ExpectedConditions.elementToBeClickable(CART_BUTTON));
        cartBtn.click();
        // Wait for the cart to display items
        wait.until(ExpectedConditions.presenceOfElementLocated(FIRST_ITEM_NAME));
    }

    public String getFirstItemName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_ITEM_NAME)).getText();
    }

    public String getFirstItemUnitPrice() {
        String text = wait.until(
                ExpectedConditions.visibilityOfElementLocated(FIRST_ITEM_UNIT_PRICE)).getText();

        return text.substring(text.indexOf("TRY"));
    }

    public String getFirstItemPrice() {
        return getFirstItemUnitPrice();
    }

    public String getFirstItemQuantity() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_ITEM_QUANTITY)).getText();
    }

    public String getFirstItemTotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_ITEM_TOTAL)).getText();
    }

    public void clickIncreaseQuantity(String previousQuantity) {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(INCREASE_BUTTON));
        btn.click();
        // Wait for the item total to update, confirming the quantity change was processed
        wait.until(ExpectedConditions.textToBe(
                FIRST_ITEM_QUANTITY,
                String.valueOf(Integer.parseInt(previousQuantity) + 1)
        ));
    }


    public void clickDecreaseQuantity(String previousQuantity) {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(DECREASE_BUTTON));
        btn.click();
        // Wait for the item total to update, confirming the quantity change was processed
        wait.until(ExpectedConditions.textToBe(
                FIRST_ITEM_QUANTITY,
                String.valueOf(Integer.parseInt(previousQuantity) - 1)
        ));
        }

    public void removeFirstItem() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(REMOVE_BUTTON));
        btn.click();
    }

    public boolean isEmptyCartMessageDisplayed(){
       WebElement message =  wait.until(ExpectedConditions.presenceOfElementLocated(EMPTY_CART_MESSAGE));
       return  message.isDisplayed();
    }

    public void clickStartShopping() {
        wait.until(
                ExpectedConditions.elementToBeClickable(START_SHOPPING_BUTTON)).click();
    }

    public boolean isCatalogHeadingDisplayed() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(CATALOG_HEADING)).isDisplayed();
    }
}

