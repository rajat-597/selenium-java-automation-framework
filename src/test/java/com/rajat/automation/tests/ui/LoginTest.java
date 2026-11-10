package com.rajat.automation.tests.ui;

import com.rajat.automation.core.BaseTest;
import com.rajat.automation.core.config.ConfigReader;
import com.rajat.automation.ui.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginCredentials")
    public Object[][] loginCredentials() {
        return new Object[][]{
                {ConfigReader.getLoginEmail(), ConfigReader.getLoginPassword()}
        };
    }

    @Test(dataProvider = "loginCredentials", groups = {"smoke", "regression"}, description = "Verify that login succeeds with valid credentials and the authenticated state is displayed")
    public void verifyLoginWithValidCredentials(String email, String password) {
        LoginPage loginPage = new LoginPage(getDriver());

        // Navigate to the QA Shop landing page
        getDriver().get(ConfigReader.getBaseUrl());

        // Perform login using DataProvider-supplied credentials
        loginPage.login(email, password);

        // Verify the user is authenticated (sign-out button visible and sign-in button gone)
        Assert.assertTrue(
                loginPage.isAuthenticated(),
                "User should be authenticated after login with valid credentials — " +
                "sign-out button should be visible and sign-in button should disappear"
        );
    }
}
