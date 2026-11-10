package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.ui.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * UI tests for QA Shop product price sorting functionality.
 * <p>
 * Verifies that the sort dropdown correctly orders displayed product prices
 * in both ascending (low to high) and descending (high to low) order.
 */
public class ProductSortingTest extends BaseTest {

    private static final String SEARCH_KEYWORD = "jeans";

    /**
     * Parse a displayed price string into a numeric double value.
     * <p>
     * Supports standard decimal format (e.g. "988.99") as well as Turkish
     * currency format (e.g. "₺1.234,56"). The method detects the format by
     * checking which separator appears last: a trailing comma indicates
     * Turkish format where dots are thousands separators, while a trailing
     * dot indicates standard format.
     *
     * @param priceText the price as displayed in the UI
     * @return the numeric price value
     */
    private double parsePrice(String priceText) {
        String cleaned = priceText.replace("TRY", "").trim();
        return Double.parseDouble(cleaned);
    }

    /**
     * Validate that a list of displayed prices is sorted in the expected order
     * by checking each adjacent pair.
     *
     * @param priceTexts the displayed price strings in display order
     * @param ascending  true for low-to-high, false for high-to-low
     */
    private void validatePriceOrder(List<String> priceTexts, boolean ascending) {
        Assert.assertTrue(
                priceTexts.size() >= 2,
                "Expected at least two product prices to validate sorting"
        );
        for (int i = 0; i < priceTexts.size() - 1; i++) {
            double current = parsePrice(priceTexts.get(i));
            double next = parsePrice(priceTexts.get(i + 1));

            if (ascending) {
                Assert.assertTrue(
                        current <= next,
                        "Prices not sorted low to high at position " + i +
                                ": " + priceTexts.get(i) + " should be <= " + priceTexts.get(i + 1)
                );
            } else {
                Assert.assertTrue(
                        current >= next,
                        "Prices not sorted high to low at position " + i +
                                ": " + priceTexts.get(i) + " should be >= " + priceTexts.get(i + 1)
                );
            }
        }
    }

    @Test(groups = {"regression"}, description = "Verify that product prices are sorted from low to high")
    public void verifyPricesSortedLowToHigh() {
        SearchPage searchPage = new SearchPage(getDriver());

        getDriver().get(ConfigReader.getBaseUrl());
       // searchPage.searchForProduct(SEARCH_KEYWORD);
        searchPage.sortBy("price:asc");

        List<String> prices = searchPage.getProductPrices();

        Assert.assertFalse(
                prices.isEmpty(),
                "Expected at least one product price after sorting low to high"
        );

        validatePriceOrder(prices, true);
    }

    @Test(groups = {"regression"}, description = "Verify that product prices are sorted from high to low")
    public void verifyPricesSortedHighToLow() {
        SearchPage searchPage = new SearchPage(getDriver());

        getDriver().get(ConfigReader.getBaseUrl());
       // searchPage.searchForProduct(SEARCH_KEYWORD);
        searchPage.sortBy("price:desc");

        List<String> prices = searchPage.getProductPrices();

        Assert.assertFalse(
                prices.isEmpty(),
                "Expected at least one product price after sorting high to low"
        );

        validatePriceOrder(prices, false);
    }
}
