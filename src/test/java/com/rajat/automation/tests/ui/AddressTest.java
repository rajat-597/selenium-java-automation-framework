package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.testdata.AddressTestData;
import com.rajat.automation.ui.pages.AddressPage;
import com.rajat.automation.ui.pages.LoginPage;
import org.testng.annotations.Test;

public class AddressTest extends BaseTest {

    @Test(groups = {"smoke"}, description = "Practice generating and passing dynamic address data to the address form")
    public void verifyDynamicAddressDataEntry() {
        AddressPage addressPage = new AddressPage(getDriver());

        // Navigate to the QA Shop address form page
        getDriver().get(ConfigReader.getBaseUrl() + "?g=odeme");
        addressPage.clickAddNewAddress();
        // Generate dynamic address data
        AddressTestData addressData = AddressTestData.generateAddress();

        // Pass the generated values to the address form
        addressPage.enterAddress(
                addressData.getAddressLabel(),
                addressData.getAddressLine(),
                addressData.getCity(),
                addressData.getPostalCode(),
                addressData.getCountryCode()
        );
    }
}
