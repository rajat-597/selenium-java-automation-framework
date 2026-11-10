package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.ui.pages.CartPage;
import com.rajat.automation.ui.pages.LoginPage;
import com.rajat.automation.ui.pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class CartQuantityTest extends BaseTest {

    private BigDecimal parsePrice(String priceText) {
        String cleaned = priceText.replace("TRY", "").trim();
        return new BigDecimal(cleaned);
    }

    @Test(groups = {"regression"}, description = "Verify that cart quantity increase and decrease correctly updates the item total")
    public void verifyCartQuantityUpdateChangesItemTotal() {

        LoginPage loginPage = new LoginPage(getDriver());
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        // Navigate to the QA Shop landing page
        getDriver().get(ConfigReader.getBaseUrl());

        // Log in with configured demo credentials
        String email = ConfigReader.getLoginEmail();
        String password = ConfigReader.getLoginPassword();
        loginPage.login(email, password);

        // Add the first product to the cart
        productDetailsPage.clickAddToCartForFirstProduct();

        // Open the cart
        cartPage.openCart();

        // Read the initial quantity, unit price, and item total
        String initialQuantityText = cartPage.getFirstItemQuantity();
        String unitPriceText = cartPage.getFirstItemUnitPrice();
        String initialTotalText = cartPage.getFirstItemTotal();

        int initialQuantity = Integer.parseInt(initialQuantityText.trim());
        BigDecimal unitPrice = parsePrice(unitPriceText);
        BigDecimal initialTotal = parsePrice(initialTotalText);

        // Verify initial total equals quantity multiplied by unit price
        BigDecimal expectedInitialTotal = unitPrice.multiply(BigDecimal.valueOf(initialQuantity));

        Assert.assertEquals(
                initialTotal.compareTo(expectedInitialTotal), 0,
                "Initial item total should equal quantity multiplied by unit price"
        );

        // Increase quantity by one
        cartPage.clickIncreaseQuantity(initialQuantityText);

        String increasedQuantityText = cartPage.getFirstItemQuantity();
        String increasedTotalText = cartPage.getFirstItemTotal();

        int increasedQuantity = Integer.parseInt(increasedQuantityText.trim());
        BigDecimal increasedTotal = parsePrice(increasedTotalText);

        // Verify quantity increased by one
        Assert.assertEquals(
                increasedQuantity,
                initialQuantity + 1,
                "Quantity should increase by one after clicking increase"
        );

        // Verify updated item total
        BigDecimal expectedIncreasedTotal =
                unitPrice.multiply(BigDecimal.valueOf(increasedQuantity));

        Assert.assertEquals(
                increasedTotal.compareTo(expectedIncreasedTotal),
                0,
                "Item total should equal the updated quantity multiplied by unit price"
        );

        // Decrease quantity by one
        cartPage.clickDecreaseQuantity(increasedQuantityText);

        String decreasedQuantityText = cartPage.getFirstItemQuantity();
        String decreasedTotalText = cartPage.getFirstItemTotal();

        int decreasedQuantity =
                Integer.parseInt(decreasedQuantityText.trim());
        BigDecimal decreasedTotal = parsePrice(decreasedTotalText);

        // Verify quantity returned to its initial value
        Assert.assertEquals(
                decreasedQuantity,
                initialQuantity,
                "Quantity should return to its initial value after clicking decrease"
        );

        // Verify item total returned to its initial value
        BigDecimal expectedDecreasedTotal =
                unitPrice.multiply(BigDecimal.valueOf(decreasedQuantity));

        Assert.assertEquals(
                decreasedTotal.compareTo(expectedDecreasedTotal),
                0,
                "Item total should equal the quantity multiplied by unit price after decrease"
        );
    }
}
