package com.rajat.automation.tests.api;

import com.rajat.automation.core.config.ConfigReader;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.given;

public class HealthCheckApiTest {

    @Test(groups = {"api", "smoke"}, description = "Verify that the API health endpoint returns 200 and status ok")
    public void verifyApiHealthEndpoint() {
        String baseUrl = ConfigReader.getApiBaseUrl();

        JsonPath jsonPath = given()
                .baseUri(baseUrl)
                .when()
                .get("/health")
                .then()
                .statusCode(200)
                .body("status", equalTo("ok"))
                .log().all()
                .extract()
                .jsonPath();

        // Validate the JSON response contains "status": "ok"
        String databaseStatus = jsonPath.getString("database");

        Assert.assertEquals(databaseStatus, "up", "Database is down ");
    }
}
