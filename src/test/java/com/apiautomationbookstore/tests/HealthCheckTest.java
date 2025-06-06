package com.apiautomationbookstore.tests;

import com.apiautomationbookstore.util.AllureAsserts;
import com.apiautomationbookstore.util.CommonFunctions;
import com.apiautomationbookstore.util.TestContext;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;


public class HealthCheckTest extends BaseTestCase{

    String requestURL;

    @BeforeClass
    public void SetupRestClient() {
        String portNumber = TestContext.getInstance().get("port").toString();
        String baseURL = TestContext.getInstance().get("base_url").toString();
        requestURL = baseURL + ":" + portNumber;
    }

    @Test(groups = {"smoke"})
    @Description("Test case to check if Bookstore API is up and running")
    public void validateBookStoreAPIHealth() throws IOException {
        String apiEndPoint = "/health";

        Response response = RestAssured
                .given()
                .baseUri(requestURL)
                .when()
                .get(apiEndPoint);

        int statusCode = response.getStatusCode();
        String respBody = response.asString();
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, "healthCheck.json");

        AllureAsserts.assertEquals(statusCode, 200 ,"API response code matched successfully");
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema validation successful");
    }


}
