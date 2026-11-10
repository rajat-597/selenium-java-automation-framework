
package com.rajat.automation.tests.api;

import com.rajat.automation.core.config.ConfigReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetUserDetailsApiTest {

    @Test(groups = {"api", "smoke"}, description = "Verify authenticated user details using JWT token")
    public void verifyAuthenticatedUserDetails(ITestContext context) {

        String baseUrl = ConfigReader.getApiBaseUrl();

        // Retrieve JWT token and user ID from TestNG context
        String token = (String) context.getAttribute("token");
        Integer expectedUserId = (Integer) context.getAttribute("userId");

        // Validate that required data is available
        Assert.assertNotNull(token,
                "JWT token not found. Login must execute first.");

        Assert.assertFalse(token.isBlank(),
                "JWT token must not be blank");

        Assert.assertNotNull(expectedUserId,
                "User ID not found. Registration must execute first.");

        // Read sandbox key from environment variable
        String sandboxKey = System.getenv("QA_SANDBOX_KEY");

        Assert.assertNotNull(sandboxKey,
                "QA_SANDBOX_KEY environment variable must be set");

        Assert.assertFalse(sandboxKey.isBlank(),
                "QA_SANDBOX_KEY environment variable must not be blank");

        // Send GET request to retrieve authenticated user details
        Response response = given()
                .baseUri(baseUrl)
                .header("X-Sandbox-Key", sandboxKey)
                .header("Authorization", "Bearer " + token)
                .accept("application/json")
                .when()
                .get("/api/v1/auth/me")
                .then()
                .extract().response();

        // Validate HTTP status code
        int statusCode = response.getStatusCode();

        Assert.assertEquals(statusCode, 200,
                "GET /api/v1/auth/me should return status 200");

        // Extract user details from response
        JsonPath jsonPath = response.jsonPath();

        Integer actualUserId =
                jsonPath.getObject("user.id", Integer.class);

        // Verify that the authenticated user is the registered user
        Assert.assertNotNull(actualUserId,
                "Response should contain user.id");

        Assert.assertEquals(actualUserId, expectedUserId,
                "Authenticated user ID should match the registered user ID");

        System.out.println("User details API status: " + statusCode);
        System.out.println("Authenticated user ID: " + actualUserId);
        System.out.println("Authenticated user details verified successfully.");
    }
}
