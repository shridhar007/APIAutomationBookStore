package com.apiautomationbookstore.tests;

import com.apiautomationbookstore.json.schema.JsonSchema;
import com.apiautomationbookstore.util.AllureAsserts;
import com.apiautomationbookstore.util.CommonFunctions;
import com.apiautomationbookstore.util.TestContext;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;


public class HealthCheckTest extends BaseTestCase{

    String requestURL;

    @BeforeClass
    public void testSetup() {
        Allure.label("owner", "Shridhar Porje");
        Allure.label("severity", "critical");

        String baseURL = TestContext.getInstance().get("base_url").toString();
        String portNumber = TestContext.getInstance().get("port").toString();
        requestURL = baseURL + ":" + portNumber;
    }

    @Test(description = "Bookstore API Health Check", groups = {"smoke"})
    public void validateBookStoreAPIHealth() throws IOException {
        String apiEndPoint = "/health";
        Response response = RestAssured
                .given()
                .baseUri(requestURL)
                .when()
                .get(apiEndPoint);

        int statusCode = response.getStatusCode();
        String respBody = response.asString();
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.HEALTHCHECKAPI);

        AllureAsserts.assertEquals(statusCode, 200 ,"API response code Health Check API");
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
    }


}
