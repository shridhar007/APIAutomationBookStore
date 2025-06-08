package com.apiautomationbookstore.tests.positive;

import com.apiautomationbookstore.json.schema.JsonSchema;
import com.apiautomationbookstore.tests.BaseTestCase;
import com.apiautomationbookstore.util.AllureAsserts;
import com.apiautomationbookstore.util.CommonFunctions;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;


public class HealthCheckTest extends BaseTestCase {

    @Test(description = "Bookstore API Health Check", groups = {"smoke"})
    public void validateBookStoreAPIHealth() throws IOException {
        String apiEndPoint = "/health";
        Response response  = requestSpecification.get(apiEndPoint);

        int statusCode = response.getStatusCode();
        String respBody = response.asString();
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.HEALTHCHECKAPI);

        AllureAsserts.assertEquals(statusCode, 200 ,"API response code Health Check API");
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
    }
}
