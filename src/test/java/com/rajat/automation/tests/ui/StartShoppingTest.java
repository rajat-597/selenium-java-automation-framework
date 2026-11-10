package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.ui.pages.CartPage;
import com.rajat.automation.ui.pages.LoginPage;
import com.rajat.automation.ui.pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * UI tests for QA Shop Start Shopping button functionality.
 * <p>
 * Verifies that clicking the Start Shopping button after removing all cart items
 * navigates back to the product catalog.
 */
public class StartShoppingTest extends BaseTest {

    @Test(groups = {"regression"}, description = "Verify that clicking Start Shopping navigates back to the product catalog")
    public void verifyStartShoppingNavigatesToCatalog() {
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

        // Remove the only cart item
        cartPage.removeFirstItem();

        // Verify the empty-cart message is displayed
        Assert.assertTrue(
                cartPage.isEmptyCartMessageDisplayed(),
                "Empty cart message should be displayed after removing the only item"
        );

        // Click the Start Shopping button
        cartPage.clickStartShopping();

        // Assert that the catalog heading is displayed
        Assert.assertTrue(
                cartPage.isCatalogHeadingDisplayed(),
                "Catalog heading should be displayed after clicking Start Shopping"
        );
    }
}
