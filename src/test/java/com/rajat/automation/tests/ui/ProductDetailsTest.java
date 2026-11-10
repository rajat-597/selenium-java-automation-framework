package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.ui.pages.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * UI tests for QA Shop product details page navigation.
 */
public class ProductDetailsTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Verify that the product details page opens when a product is selected from the landing page")
    public void verifyProductDetailsPageOpensWhenProductIsSelected() {
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(getDriver());

        // Navigate to the QA Shop landing page
        getDriver().get(ConfigReader.getBaseUrl());

        // Capture the first product's name and price from the catalog before navigating
        String catalogName = productDetailsPage.getFirstProductNameInList();
        String catalogPrice = productDetailsPage.getFirstProductPriceInList();

        // Click the first product to navigate to its details page
        productDetailsPage.clickFirstProduct();

        // Verify the URL contains the expected product details query parameters
        String currentUrl = productDetailsPage.getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("g=urun") && currentUrl.contains("urun="),
                "Product details URL should contain both 'g=urun' and 'urun='. Current URL: " + currentUrl
        );

        // Verify the product name on the details page matches the catalog name
        String detailsName = productDetailsPage.getProductName();
        Assert.assertEquals(
                detailsName,
                catalogName,
                "Product name on the details page should match the name shown in the catalog"
        );

        // Verify the product price on the details page matches the catalog price
        String detailsPrice = productDetailsPage.getProductPrice();
        Assert.assertEquals(
                detailsPrice,
                catalogPrice,
                "Product price on the details page should match the price shown in the catalog"
        );
    }

    @Test(groups = {"regression"}, description = "Verify that the product rating is displayed on the product details page")
    public void verifyProductRatingIsDisplayed() {
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(getDriver());

        // Navigate to the QA Shop landing page and open a product details page
        getDriver().get(ConfigReader.getBaseUrl());
        productDetailsPage.clickFirstProduct();

        // Verify the product rating is displayed and not blank
        String rating = productDetailsPage.getProductRating();
        Assert.assertFalse(
                rating.isBlank(),
                "Product rating should be displayed on the details page"
        );
    }

    @Test(groups = {"regression"}, description = "Verify that the product description is displayed on the product details page")
    public void verifyProductDescriptionIsDisplayed() {
        ProductDetailsPage productDetailsPage = new ProductDetailsPage(getDriver());

        // Navigate to the QA Shop landing page and open a product details page
        getDriver().get(ConfigReader.getBaseUrl());
        productDetailsPage.clickFirstProduct();

        // Verify the product description is displayed and not blank
        String description = productDetailsPage.getProductDescription();
        Assert.assertFalse(
                description.isBlank(),
                "Product description should be displayed on the details page"
        );
    }
}
