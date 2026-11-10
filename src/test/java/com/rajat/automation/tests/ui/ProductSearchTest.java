package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.ui.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * UI tests for QA Shop product search functionality.
 */
public class ProductSearchTest extends BaseTest {

    private static final String SEARCH_KEYWORD = "jeans";
    private static final String NO_MATCH_KEYWORD = "laptop";
    private static final String NO_RESULTS_MESSAGE = "No products match your search.";

    @Test(groups = {"regression"}, description = "Verify that all products returned by a valid search match the searched keyword")
    public void verifyAllSearchResultsMatchKeyword() {
        SearchPage searchPage = new SearchPage(getDriver());

        // Navigate to the shop and perform search
        getDriver().get(ConfigReader.getBaseUrl());
        searchPage.searchForProduct(SEARCH_KEYWORD);

        // Retrieve all product names from search results
        List<String> productNames = searchPage.getProductNames();

        Assert.assertFalse(
                productNames.isEmpty(),
                "Expected at least one product for search keyword: " + SEARCH_KEYWORD);
        // Verify that every product name contains the search keyword (case-insensitive)
        for (String productName : productNames) {
            Assert.assertTrue(
                    productName.toLowerCase().contains(SEARCH_KEYWORD.toLowerCase()),
                    "Product name '" + productName + "' should contain the keyword '" + SEARCH_KEYWORD + "'"
            );
        }
    }

    @Test(groups = {"regression"}, description = "Verify that searching with no matching products displays the no-results message")
    public void verifySearchWithNoMatchingProducts() {
        SearchPage searchPage = new SearchPage(getDriver());

        // Navigate to the shop and perform search with a keyword that matches no products
        getDriver().get(ConfigReader.getBaseUrl());
        searchPage.searchForProduct(NO_MATCH_KEYWORD);

        // Retrieve product names — should be empty when no products match
        List<String> productNames = searchPage.getProductNames();

        Assert.assertTrue(
                productNames.isEmpty(),
                "Expected no products for search keyword: " + NO_MATCH_KEYWORD
        );

        // Verify the no-results message is displayed
        Assert.assertTrue(
                searchPage.isNoResultsMessageDisplayed(),
                "Expected the no-results message to be displayed: " + NO_RESULTS_MESSAGE
        );
    }
}
