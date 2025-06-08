package com.apiautomationbookstore.tests.negative;

import com.apiautomationbookstore.json.schema.JsonSchema;
import com.apiautomationbookstore.tests.BaseTestCase;
import com.apiautomationbookstore.util.AllureAsserts;
import com.apiautomationbookstore.util.CommonFunctions;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserManagementNeg extends BaseTestCase {

    // Scenarios User Registration - Call with GET, PUT, PATCH, DELETE, Incorrect end point, Incorrect body

    @Test(description = "Hit User registration API with not allowed request methods", dataProvider = "methodsToCall", groups = {"negative"})
    public void validateUserRegAPIWithInvalidReqMethods(String methodCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, JsonProcessingException {
        if(methodCall.equals("post")) {
            System.out.println("Skipping this test as 'post' is positive test case.");
            throw new SkipException("Skipping this test as 'post' is positive test case.");
        }

        String apiEndPoint = "/signup";
        Response response = null;
        String defaultUserPwd = "123456";

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", faker.name().fullName() + "@" + "example.com");
        requestBodyMap.put("password", defaultUserPwd);
        requestSpecification.baseUri(requestURL.concat(apiEndPoint));
        requestSpecification.body(CommonFunctions.getJsonFromMap(requestBodyMap));

        Method method = requestSpecification.getClass().getMethod(methodCall);
        response = (Response) method.invoke(requestSpecification);

        int statusCode = response.getStatusCode();
        AllureAsserts.assertEquals(statusCode, 405 ,"API response code");

        String respBody = response.asString();
        Map<String, Object> respDict = CommonFunctions.getMapFromJson(respBody);
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.InvalidReqMethodResp);

        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
        AllureAsserts.assertEquals("Method Not Allowed", respDict.get("detail").toString(), "API Response validation");
    }

    @Test(description = "Hit User registration API with invalid end point", dataProvider = "methodsToCall", groups = {"negative"})
    public void validateUserRegAPIWithInvalidEndPoint(String methodCall) throws JsonProcessingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String apiEndPoint = "/signup_";
        Response response = null;
        String defaultUserPwd = "123456";

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", faker.name().fullName() + "@" + "example.com");
        requestBodyMap.put("password", defaultUserPwd);
        requestSpecification.baseUri(requestURL.concat(apiEndPoint));
        requestSpecification.body(CommonFunctions.getJsonFromMap(requestBodyMap));

        Method method = requestSpecification.getClass().getMethod(methodCall);
        response = (Response) method.invoke(requestSpecification);

        int statusCode = response.getStatusCode();
        AllureAsserts.assertEquals(statusCode, 404 ,"API response code");

        String respBody = response.asString();
        Map<String, Object> respDict = CommonFunctions.getMapFromJson(respBody);
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.InvalidReqMethodResp);
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
        AllureAsserts.assertEquals("Not Found", respDict.get("detail").toString(), "API Response validation");
    }

    @Test(description = "Hit User registration API without request body", dataProvider = "methodsToCall", groups = {"negative"})
    public void validateUserRegAPIWithoutReqBody(String methodCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, JsonProcessingException {
        String apiEndPoint = "/signup";
        Response response = null;
        requestSpecification.baseUri(requestURL.concat(apiEndPoint));

        Method method = requestSpecification.getClass().getMethod(methodCall);
        response = (Response) method.invoke(requestSpecification);

        if(!methodCall.equals("post")) {
            int statusCode = response.getStatusCode();
            AllureAsserts.assertEquals(statusCode, 405 ,"API response code");

            String respBody = response.asString();
            Map<String, Object> respDict = CommonFunctions.getMapFromJson(respBody);
            boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.InvalidReqMethodResp);
            AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
            AllureAsserts.assertEquals("Method Not Allowed", respDict.get("detail").toString(), "API Response validation");
        }
        else{
            int statusCode = response.getStatusCode();
            AllureAsserts.assertEquals(statusCode, 422 ,"API response code");
            String respBody = response.asString();
            Map<String, Object> respDict = CommonFunctions.getMapFromJson(respBody);
            boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.UserRegistrationAPIWithoutBody);
            AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
            ArrayList<LinkedHashMap<String, String>> tempResults = (ArrayList<LinkedHashMap<String, String>>) respDict.get("detail");
            LinkedHashMap<String, String> tempLinkedHashMapResult = tempResults.get(0);
            AllureAsserts.assertEquals("Field required",tempLinkedHashMapResult.get("msg"), "API Response validation");
        }
    }
}
