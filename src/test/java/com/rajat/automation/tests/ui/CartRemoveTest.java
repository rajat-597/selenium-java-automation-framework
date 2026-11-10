package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.ui.pages.CartPage;
import com.rajat.automation.ui.pages.LoginPage;
import com.rajat.automation.ui.pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * UI tests for QA Shop cart remove item functionality.
 * <p>
 * Verifies that removing a product from the cart displays the empty-cart message.
 */
public class CartRemoveTest extends BaseTest {

    @Test(groups = {"regression"}, description = "Verify that removing the only cart item displays the empty cart message")
    public void verifyRemoveItemShowsEmptyCartMessage() {
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

        // Assert the first cart item is displayed before removal
        String itemName = cartPage.getFirstItemName();
        Assert.assertFalse(
                itemName.isBlank(),
                "First cart item name should be displayed before removal"
        );

        // Remove the first cart item
        cartPage.removeFirstItem();

        // Assert the empty-cart message is displayed
        Assert.assertTrue(
                cartPage.isEmptyCartMessageDisplayed(),
                "Empty cart message should be displayed after removing the only item"
        );
    }
}
