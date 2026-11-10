package com.rajat.automation.tests.api;

import com.rajat.automation.core.config.ConfigReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class CreateSandboxApiTest {

    @Test(groups = {"api", "smoke"}, description = "Verify that creating a sandbox returns 201 and contains sandboxId and apiKey")
    public void verifyCreateSandboxEndpoint() {

        String baseUrl = ConfigReader.getApiBaseUrl();

        // Build the request body as a HashMap — Rest Assured serializes it to JSON automatically
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("label", "automation-test-sandbox");

        Response response = given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .accept("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/sandbox")
                .then()
                .extract().response();

        // 1. Validate HTTP status code is 201 Created
        int statusCode = response.getStatusCode();
        Assert.assertEquals(
                statusCode, 201,
                "POST /api/v1/sandbox should return status 201 Created"
        );

        // 2. Extract the JSON response fields
        JsonPath jsonPath = response.jsonPath();

        String sandboxId = jsonPath.getString("sandboxId");
        String apiKey = jsonPath.getString("apiKey");

        // 3. Assert sandboxId is present and not blank
        Assert.assertNotNull(
                sandboxId,
                "Response should contain 'sandboxId'"
        );
        Assert.assertFalse(
                sandboxId.isBlank(),
                "'sandboxId' should not be blank"
        );

        // 4. Assert apiKey is present and not blank
        Assert.assertNotNull(
                apiKey,
                "Response should contain 'apiKey'"
        );
        Assert.assertFalse(
                apiKey.isBlank(),
                "'apiKey' should not be blank"
        );

        // Log non-sensitive response details for debugging
        System.out.println("Response status: " + statusCode);
        System.out.println("Sandbox ID: " + sandboxId);
    }
}
