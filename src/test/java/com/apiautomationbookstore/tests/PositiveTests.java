package com.apiautomationbookstore.tests;

import com.apiautomationbookstore.json.schema.JsonSchema;
import com.apiautomationbookstore.util.AllureAsserts;
import com.apiautomationbookstore.util.CommonFunctions;
import com.apiautomationbookstore.util.TestContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PositiveTests extends BaseTestCase{

    String requestURL;

    @BeforeClass
    public void testSetup() {
        Allure.label("owner", "Shridhar Porje");
        Allure.label("severity", "critical");

        String portNumber = TestContext.getInstance().get("port").toString();
        String baseURL = TestContext.getInstance().get("base_url").toString();
        requestURL = baseURL + ":" + portNumber;
    }


    @Test(description = "Bookstore User Signup API", groups = {"sanity"})
    public void validateUserSignupAPI() throws JsonProcessingException {
        Faker faker = new Faker();
        String apiEndPoint = "/signup";
        StringBuilder sb = new StringBuilder();
        sb.append(faker.name().fullName()).append("@").append("example.com");
        String defaultUserPwd = "123456";
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", sb.toString());
        requestBodyMap.put("password", defaultUserPwd);

        Response response = given().header("Content-Type", "application/json")
                .body(CommonFunctions.getJsonFromMap(requestBodyMap))
                .when()
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
        RestAssured.baseURI = requestURL;

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", defaultUserEmail);
        requestBodyMap.put("password", defaultUserPwd);

        Response response = given().header("Content-Type", "application/json")
                .body(CommonFunctions.getJsonFromMap(requestBodyMap))
                .when()
                .post(apiEndPoint);

        int statusCode = response.getStatusCode();
        String respBody = response.asString();

        AllureAsserts.assertEquals(statusCode, 200 ,"API response code User Login");
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.USERLOGIN);
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
    }

    @Test(description = "Bookstore Add new book API", groups = {"sanity"})
    public void validateAddNewBookAPI() throws JsonProcessingException {
        String apiEndPoint = "/books/";
        RestAssured.baseURI = requestURL;
        Faker faker = new Faker();

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("author", faker.name().fullName());
        requestBodyMap.put("book_summary", "Explore DBZ characters series.");
        requestBodyMap.put("published_year", String.valueOf(CommonFunctions.getRandomInt(1990, 2020)));
        requestBodyMap.put("name", "Powers of " + faker.dragonBall().character());

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + " " + testContext.get("authToken"))
                .body(CommonFunctions.getJsonFromMap(requestBodyMap))
                .when()
                .post(apiEndPoint);

        int statusCode = response.getStatusCode();
        String respBody = response.asString();

        AllureAsserts.assertEquals(statusCode, 200 ,"API response code User Login");
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.ADDBOOKRESPONSE);
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
    }

    @Test(description = "Bookstore Get All Books API", groups = {"sanity"})
    public void validateGetAllBooksAPI() {
        String apiEndPoint = "/books/";
        RestAssured.baseURI = requestURL;

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + " " + testContext.get("authToken"))
                .when()
                .get(apiEndPoint);

        int statusCode = response.getStatusCode();

        // Here response is returning a pair of square brackets, which was cauising issues in json validation.
        // Hence those are moved.
        String respBody = response.asString().replace("[","").replace("]","");
        AllureAsserts.assertEquals(statusCode, 200 ,"API response code User Login");
        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.GETALLBOOKSDETAIL);
        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
    }


}
