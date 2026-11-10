
package com.rajat.automation.tests.api;

import com.rajat.automation.core.config.ConfigReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LoginApiTest {


    @Test(description = "Verify login with valid credentials and API chaining")
    public void verifyLoginWithValidCredentials() {

        String baseUrl = ConfigReader.getApiBaseUrl();

        // Read sandbox key from environment variable
        String sandboxKey = System.getenv("QA_SANDBOX_KEY");

        Assert.assertNotNull(sandboxKey, "Sandbox key is missing");
        Assert.assertFalse(sandboxKey.isBlank(), "Sandbox key is empty");

        // Prepare login request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "demo@qashop.test");
        requestBody.put("password", "Password123!");

        // Step 1: Send login request
        Response response = given()
                .baseUri(baseUrl)
                .header("X-Sandbox-Key", sandboxKey)
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .extract()
                .response();

        // Validate login response
        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Login API should return 200"
        );

        System.out.println("Login successful. Status code: "
                + response.getStatusCode());

        // Step 2: Extract JWT token
        JsonPath jsonPath = response.jsonPath();

        String token = jsonPath.getString("token");

        Assert.assertNotNull(token, "JWT token should not be null");
        Assert.assertFalse(token.isBlank(), "JWT token should not be empty");

        System.out.println("JWT token extracted successfully");

        // Step 3: Use JWT token to call authenticated API
        Response meResponse = given()
                .baseUri(baseUrl)
                .header("X-Sandbox-Key", sandboxKey)
                .header("Authorization", "Bearer " + token)
                .accept("application/json")
                .when()
                .get("/api/v1/auth/me")
                .then()
                .extract()
                .response();

        // Step 4: Validate authenticated user API
        Assert.assertEquals(
                meResponse.getStatusCode(),
                200,
                "Authenticated user API should return 200"
        );

        JsonPath meJsonPath = meResponse.jsonPath();

        System.out.println("Authenticated user API successful. Status code: "
                + meResponse.getStatusCode());

        System.out.println("Authenticated user response fields: "
                + meJsonPath.getMap("$").keySet());
    }

    @Test(description = "Verify login with invalid password")
    public void verifyLoginWithInvalidPassword() {

        String baseUrl = ConfigReader.getApiBaseUrl();
        String sandboxKey = System.getenv("QA_SANDBOX_KEY");

        Assert.assertNotNull(sandboxKey, "QA_SANDBOX_KEY environment variable is missing");
        Assert.assertFalse(sandboxKey.isBlank());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "demo@qashop.test");
        requestBody.put("password", "WrongPassword123!");

        Response response = given()
                .baseUri(baseUrl)
                .header("X-Sandbox-Key", sandboxKey)
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        //System.out.println("Error response fields: " + jsonPath.getMap("error").keySet()); Note :$ represent whole body

        Assert.assertEquals(
                response.getStatusCode(),
                401,
                "Invalid password should return 401"
        );

        String errorCode = jsonPath.getString("error.code");
        String errorMessage = jsonPath.getString("error.message");

        // Validate error code
        Assert.assertEquals(
                errorCode,
                "UNAUTHORIZED",
                "Incorrect error code returned"
        );

        System.out.println("Error code: " + errorCode);
        System.out.println("Error message: " + errorMessage);
        System.out.println("Invalid login correctly rejected");

        // 4. Validate error message
        Assert.assertNotNull(errorMessage, "Error message should not be null");
        Assert.assertFalse(errorMessage.isBlank(), "Error message should not be empty");

        // 5. Validate that no token is issued
        Assert.assertNull(
                jsonPath.getString("token"),
                "Token should not be issued for invalid credentials"
        );
    }

    @Test
    public void verifyLoginWithoutSandboxKey() {

        String baseUrl = ConfigReader.getApiBaseUrl();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "demo@qashop.test");
        requestBody.put("password", "Password123!");

        Response response =
                given()
                        .baseUri(baseUrl)
                        .contentType("application/json")
                        .accept("application/json")
                        .body(requestBody)
                        .when()
                        .post("/api/v1/auth/login");

        Assert.assertEquals(response.getStatusCode(), 401,
                "Login should fail when Sandbox Key is missing");

        JsonPath jsonPath = response.jsonPath();

        Assert.assertNotNull(jsonPath.getString("error.code"),
                "Error code should be present");

        Assert.assertNull(jsonPath.getString("token"),
                "Token should not be generated without Sandbox Key");

        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());
    }
}
