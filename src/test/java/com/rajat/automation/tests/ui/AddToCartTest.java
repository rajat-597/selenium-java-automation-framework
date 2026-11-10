package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.ui.pages.CartPage;
import com.rajat.automation.ui.pages.LoginPage;
import com.rajat.automation.ui.pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddToCartTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Verify that the product added to cart is displayed in the cart with matching name and price")
    public void verifyAddedProductIsDisplayedInCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        // Navigate to the QA Shop landing page
        getDriver().get(ConfigReader.getBaseUrl());

        // Log in with configured demo credentials
        String email = ConfigReader.getLoginEmail();
        String password = ConfigReader.getLoginPassword();
        loginPage.login(email, password);

        // Capture the first product's name and price from the catalog before adding to cart
        String catalogName = productDetailsPage.getFirstProductNameInList();
        String catalogPrice = productDetailsPage.getFirstProductPriceInList();

        // Add the first product to the cart
        productDetailsPage.clickAddToCartForFirstProduct();

        // Open the cart
        cartPage.openCart();

        // Verify the cart displays the same product name and price
        String cartName = cartPage.getFirstItemName();
        String cartPrice = cartPage.getFirstItemPrice();

        Assert.assertEquals(
                cartName,
                catalogName,
                "Product name in the cart should match the product name from the catalog"
        );

        Assert.assertEquals(
                cartPrice,
                catalogPrice,
                "Product price in the cart should match the product price from the catalog"
        );
    }
}
