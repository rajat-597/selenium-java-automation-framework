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

public class RegisterApiTest {

    @Test(groups = {"api", "smoke"}, description = "Verify that user registration returns 201 and a valid user ID")
    public void verifyUserRegistration(ITestContext context) {

        String baseUrl = ConfigReader.getApiBaseUrl();

        // Read the sandbox key from the environment variable
        String sandboxKey = System.getenv("QA_SANDBOX_KEY");
        Assert.assertNotNull(sandboxKey, "QA_SANDBOX_KEY environment variable must be set");
        Assert.assertFalse(sandboxKey.isBlank(), "QA_SANDBOX_KEY environment variable must not be blank");

        // Generate a unique email for every test execution
        String timestamp = String.valueOf(System.currentTimeMillis());
        String email = "testuser" + timestamp + "@example.com";

        String password = "Password123!";

        // Build the request body as a HashMap — Rest Assured serializes it to JSON automatically
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("name", "Test User");

        Response response = given()
                .baseUri(baseUrl)
                .header("X-Sandbox-Key", sandboxKey)
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/auth/register")
                .then()
                .extract().response();

        // 1. Validate HTTP status code is 201 Created
        int statusCode = response.getStatusCode();
        Assert.assertEquals(
                statusCode, 201,
                "POST /api/v1/auth/register should return status 201 Created"
        );

        // 2. Extract the registered user's ID from the JSON response
        JsonPath jsonPath = response.jsonPath();

        Integer userId = jsonPath.getObject("user.id", Integer.class);

        // 3. Assert user ID is present and greater than zero
        Assert.assertNotNull(
                userId,
                "Response should contain 'user.id'"
        );
        Assert.assertTrue(
                userId > 0,
                "'user.id' should be greater than zero, but was: " + userId
        );

        // Store registration details in TestNG context
        context.setAttribute("userId", userId);
        context.setAttribute("email", email);
        context.setAttribute("password", password);

        // Log non-sensitive response details for debugging
        System.out.println("Response status: " + statusCode);
        System.out.println("Registered email: " + email);
        System.out.println("User ID: " + userId);
    }
}
