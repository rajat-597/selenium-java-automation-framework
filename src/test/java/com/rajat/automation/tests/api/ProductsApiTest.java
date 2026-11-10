
package com.rajat.automation.tests.api;

import com.rajat.automation.core.config.ConfigReader;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ProductsApiTest {

    @Test(groups = {"api", "smoke"}, description = "Verify that the products endpoint returns a paginated product list")
    public void verifyProductListEndpoint() {

        String baseUrl = ConfigReader.getApiBaseUrl();

        Response response = given()
                .baseUri(baseUrl)
                .header("Accept", "application/json")
                .queryParam("page", 1)
                .queryParam("size", 2)
                .when()
                .get("/api/v1/products")
                .then()
                .extract().response();

        // 1. Validate HTTP status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(
                statusCode, 200,
                "GET /api/v1/products should return status 200"
        );

        // 2. Validate response Content-Type header
        String contentType = response.getHeader("Content-Type");
        Assert.assertNotNull(
                contentType,
                "Response should contain a Content-Type header"
        );
        Assert.assertTrue(
                contentType.contains("application/json"),
                "Expected a JSON response but received: " + contentType
        );

        // 3. Get the complete response body as a String
        String responseBody = response.getBody().asString();
        Assert.assertFalse(
                responseBody.isBlank(),
                "Response body should not be empty"
        );

        // Log the response for debugging
        System.out.println("Response status: " + statusCode);
        System.out.println("Content-Type: " + contentType);
        System.out.println("Response body: " + responseBody);

        // 4. Extract JSON fields for further validation (Extracts individual fields from JSON response.jsonPath())
        JsonPath jsonPath = response.jsonPath();

        Assert.assertNotNull(
                jsonPath.getString("page"),
                "Response should contain 'page' field"
        );
        Assert.assertNotNull(
                jsonPath.getString("size"),
                "Response should contain 'size' field"
        );
        Assert.assertNotNull(
                jsonPath.getString("total"),
                "Response should contain 'total' field"
        );

        // 5. Validate the product list
        List<String> productNames = jsonPath.getList("items.name");

        Assert.assertNotNull(productNames, "Product list should exist");
        Assert.assertFalse(
                productNames.isEmpty(),
                "Response should contain at least one product"
        );

        // 6. Validate the first product's fields
        Assert.assertNotNull(jsonPath.getString("items[0].id"));
        Assert.assertNotNull(jsonPath.getString("items[0].name"));
        Assert.assertNotNull(jsonPath.getString("items[0].sku"));
        Assert.assertNotNull(jsonPath.getString("items[0].price"));
    }
}
