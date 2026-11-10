
package com.rajat.automation.tests.api;

import com.rajat.automation.core.config.ConfigReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RegistrationLoginApiTest {

    @Test(groups = {"api", "smoke"}, description = "Verify login using dynamically registered user")
    public void verifyLoginWithRegisteredUser(ITestContext context) {

        String baseUrl = ConfigReader.getApiBaseUrl();

        // Retrieve registration details from TestNG context
        String email = (String) context.getAttribute("email");
        String password = (String) context.getAttribute("password");

        // Validate that registration data is available
        Assert.assertNotNull(email,
                "Email not found in TestNG context. Registration must run first.");

        Assert.assertNotNull(password,
                "Password not found in TestNG context. Registration must run first.");

        // Read sandbox key from environment variable
        String sandboxKey = System.getenv("QA_SANDBOX_KEY");

        Assert.assertNotNull(sandboxKey,
                "QA_SANDBOX_KEY environment variable must be set");

        Assert.assertFalse(sandboxKey.isBlank(),
                "QA_SANDBOX_KEY environment variable must not be blank");

        // Prepare login request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        // Send login request
        Response response = given()
                .baseUri(baseUrl)
                .header("X-Sandbox-Key", sandboxKey)
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .extract().response();

        // Validate HTTP status code
        int statusCode = response.getStatusCode();

        Assert.assertEquals(statusCode, 200,
                "Login API should return status 200");

        // Extract JWT token from response
        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.getString("token");

        Assert.assertNotNull(token,
                "Login response should contain JWT token");

        Assert.assertFalse(token.isBlank(),
                "JWT token should not be blank");

        // Store JWT token for the next test class
        context.setAttribute("token", token);

        // Log non-sensitive details
        System.out.println("Login status: " + statusCode);
        System.out.println("Logged-in email: " + email);
        System.out.println("JWT token successfully received.");
    }
}
