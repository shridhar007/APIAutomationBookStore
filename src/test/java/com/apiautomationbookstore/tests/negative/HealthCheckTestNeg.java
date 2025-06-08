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
import java.util.Map;

public class HealthCheckTestNeg extends BaseTestCase {

    //Scenarios :  Health check API - Call with POST, PUT, PATCH, DELETE, Incorrect end point.

    @Test(description = "Hit Health Check API with not allowed request methods", dataProvider = "methodsToCall", groups = {"negative"})
    public void validateHealthCheckAPIForInvalidReqMethods(String methodCall) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, JsonProcessingException {

        if(methodCall.equals("get")) {
            System.out.println("Skipping this test as 'get' is positive test case.");
            throw new SkipException("Skipping this test as get is positive test case.");
        }
        String apiEndPoint = "/health";
        Response response = null;
        requestSpecification.baseUri(requestURL.concat(apiEndPoint));

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

    @Test(description = "Hit Health Check API with not allowed request methods",dataProvider = "methodsToCall", groups = {"negative"})
    public void validateHealthCheckAPIWithInvalidEndPoint(String methodCall) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, JsonProcessingException {
        String apiEndPoint = "/health_";
        Response response = null;
        requestSpecification.baseUri(requestURL.concat(apiEndPoint));

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



    // User Registration - Call with GET, PUT, PATCH, DELETE, Incorrect end point, Incorrect body
    // User Login - Call with GET, PUT, PATCH, DELETE, Incorrect end point, Incorrect body
    // Add New Book - Call with  PUT, PATCH, DELETE, Without token, Incorrect end point, Incorrect body
    // Get Book Details - Call with POST, Without token, Incorrect end point, Invalid book id [Alphanumeric Value]
    // Get ALl Books - Call with Patch, Put, Delete. Without token, Incorrect end point
    // Update existing book - Call with POST, Without token, Incorrect end point, Invalid book id [Alphanumeric Value]
    // Delete existing book - Call with POST, Without token, Incorrect end point, Invalid book id [Alphanumeric Value]

    // Design workflow file.
    // Create README.MD file.

}
