package com.apiautomationbookstore.tests.positive;

import com.apiautomationbookstore.json.schema.JsonSchema;
import com.apiautomationbookstore.tests.BaseTestCase;
import com.apiautomationbookstore.util.AllureAsserts;
import com.apiautomationbookstore.util.CommonFunctions;
import com.apiautomationbookstore.util.TestContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class UserManagement extends BaseTestCase {

    @Test(description = "Bookstore User Signup API", groups = {"sanity"})
    public void validateUserSignupAPI() throws JsonProcessingException {
        String apiEndPoint = "/signup";
        StringBuilder sb = new StringBuilder();
        sb.append(faker.name().fullName()).append("@").append("example.com");
        String defaultUserPwd = "123456";
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", sb.toString());
        requestBodyMap.put("password", defaultUserPwd);

        Response response = requestSpecification
                .body(CommonFunctions.getJsonFromMap(requestBodyMap))
                .post(apiEndPoint);

        int statusCode = response.getStatusCode();
        String respBody = response.asString();

        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.USERSIGNUP);
        AllureAsserts.assertEquals(statusCode, 200 ,"API response code User Sign Up");
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
    }

    @Test(description = "Bookstore User Login API", groups = {"sanity"})
    public void validateUserLoginAPI() throws JsonProcessingException {

        // In this test case, we will login with default user configured in execution_config file.
        String apiEndPoint = "/login";
        String defaultUserEmail = TestContext.getInstance().get("default_username").toString();
        String defaultUserPwd = TestContext.getInstance().get("default_password").toString();

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", defaultUserEmail);
        requestBodyMap.put("password", defaultUserPwd);

        Response response = requestSpecification
                .body(CommonFunctions.getJsonFromMap(requestBodyMap))
                .post(apiEndPoint);

        int statusCode = response.getStatusCode();
        String respBody = response.asString();

        AllureAsserts.assertEquals(statusCode, 200 ,"API response code User Login");
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.USERLOGIN);
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
    }

}
