package com.apiautomationbookstore.tests;

import com.apiautomationbookstore.json.schema.JsonSchema;
import com.apiautomationbookstore.util.AllureAsserts;
import com.apiautomationbookstore.util.CommonFunctions;
import com.apiautomationbookstore.util.TestContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        AllureAsserts.assertEquals(statusCode, 200 ,"API response code User Login");
//        String respBody = response.asString();
//        boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBody, JsonSchema.t1);
//        AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
    }

    @Test(description = "Bookstore Get book by ID", groups = {"sanity"})
    public void validateGetBookByIdAPI() throws JsonProcessingException {
        // To validae this API, we need to first get the list of existing books
        String apiEndPointGetAllBooks = "/books/";
        RestAssured.baseURI = requestURL;

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + " " + testContext.get("authToken"))
                .when()
                .get(apiEndPointGetAllBooks);

        int statusCode = response.getStatusCode();
        String respBody = response.asString().replace("[","").replace("]","");
        AllureAsserts.assertEquals(statusCode, 200 ,"API response code User Login");

        Map<String, Object> existingBooks = CommonFunctions.getMapFromJson(respBody);

        if(existingBooks.isEmpty()) {
            System.out.println("No books present in database. Existing test case");
            throw new SkipException("Test aborted - precondition failed: No books in database");
        }
        else {
            // Get id of existing book to fetch its details using API
            String bookIdToFetchDetails = existingBooks.get("id").toString();
            StringBuilder bookDetailByIdEndPoint = new StringBuilder();
            bookDetailByIdEndPoint.append(apiEndPointGetAllBooks).append("/").append(bookIdToFetchDetails);

            Response singleBookDetails = given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer" + " " + testContext.get("authToken"))
                    .when()
                    .get(bookDetailByIdEndPoint.toString());

            int statusCodeSingleBookDetails = response.getStatusCode();
            String respBodySingleBook = response.asString().replace("[","").replace("]","");
            AllureAsserts.assertEquals(statusCodeSingleBookDetails, 200 ,"API response code User Login");

            Map<String, Object> singleBookDetailsMap = CommonFunctions.getMapFromJson(respBody);
            AllureAsserts.assertEquals(existingBooks.get("id").toString(), singleBookDetailsMap.get("id").toString(), "Book Ids matched");
            AllureAsserts.assertEquals(existingBooks.get("author").toString(), singleBookDetailsMap.get("author").toString(), "Book Ids matched");
            AllureAsserts.assertEquals(existingBooks.get("book_summary").toString(), singleBookDetailsMap.get("book_summary").toString(), "Book Ids matched");
            AllureAsserts.assertEquals(existingBooks.get("name").toString(), singleBookDetailsMap.get("name").toString(), "Book Ids matched");
            AllureAsserts.assertEquals(existingBooks.get("published_year").toString(), singleBookDetailsMap.get("published_year").toString(), "Book Ids matched");

            boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBodySingleBook, JsonSchema.GETBOOKDETAILSBYID);
            AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
        }
    }

    @Test(description = "Bookstore Update existing book API")
    public void validateUpdateBookAPI() throws JsonProcessingException {
        // To validae this API, we need to first get the list of existing books
        String apiEndPointGetAllBooks = "/books/";
        RestAssured.baseURI = requestURL;

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + " " + testContext.get("authToken"))
                .when()
                .get(apiEndPointGetAllBooks);

        int statusCode = response.getStatusCode();
        String respBody = response.asString(); //.replace("[","").replace("]","");
        AllureAsserts.assertEquals(statusCode, 200 ,"API response code User Login");
        List<Map<String, Object>> listOfAllBooks = CommonFunctions.getMapListFromJson(respBody);

        if(listOfAllBooks.isEmpty()) {
            System.out.println("No books present in database. Existing test case");
            throw new SkipException("Test aborted - precondition failed: No books in database");
        }
        else {
            Map<String, Object> bookForUpdate = listOfAllBooks.get(1);

            // Update the book
            String bookId = bookForUpdate.get("id").toString();
            Faker faker = new Faker();
            Map<String, String> bookToUpdateMap = new HashMap<>();
            bookToUpdateMap.put("author", faker.name().fullName());
            bookToUpdateMap.put("book_summary", "Explore DBZ characters series.");
            bookToUpdateMap.put("published_year", String.valueOf(CommonFunctions.getRandomInt(1990, 2020)));
            bookToUpdateMap.put("name", "Powers of " + faker.dragonBall().character());

            Response updateBookDetailsResponse = given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer" + " " + testContext.get("authToken"))
                    .body(CommonFunctions.getJsonFromMap(bookToUpdateMap))
                    .when()
                    .put(apiEndPointGetAllBooks.concat(bookId));

            int statusCodeUpdateBookDetails = updateBookDetailsResponse.getStatusCode();
            AllureAsserts.assertEquals(statusCodeUpdateBookDetails, 200 ,"API response code User Login");

            String respBodyUpdateBook = updateBookDetailsResponse.asString();
            boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBodyUpdateBook, JsonSchema.GETBOOKDETAILSBYID);
            AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
            Map<String, Object> bookPostUpdate = CommonFunctions.getMapFromJson(respBodyUpdateBook);

            AllureAsserts.assertEquals(bookForUpdate.get("id").toString(), bookPostUpdate.get("id").toString(),"Book id matches post update");
            AllureAsserts.assertEquals(bookForUpdate.get("book_summary").toString(), bookPostUpdate.get("book_summary").toString(),"Book Summary matches post update");
            AllureAsserts.assertNotEquals(bookForUpdate.get("author").toString(), bookPostUpdate.get("author").toString(),"Book author updated successfully");
            AllureAsserts.assertNotEquals(bookForUpdate.get("published_year").toString(), bookPostUpdate.get("published_year").toString(),"Book published year updated successfully");
            AllureAsserts.assertNotEquals(bookForUpdate.get("name").toString(), bookPostUpdate.get("name").toString(),"Book name updated successfully");

        }
    }

    @Test(description = "Bookstore Delete existing book API")
    public void validateDeleteBookAPI() throws JsonProcessingException {

        // First we will get all the available books.
        String apiEndPointGetAllBooks = "/books/";
        RestAssured.baseURI = requestURL;

        Response responseGetAllBooks = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer" + " " + testContext.get("authToken"))
                .when()
                .get(apiEndPointGetAllBooks);

        int statusCodeGetAllBooks = responseGetAllBooks.getStatusCode();
        String respBodyGetAllBooks = responseGetAllBooks.asString();
        AllureAsserts.assertEquals(statusCodeGetAllBooks, 200 ,"API response code User Login");

        List<Map<String, Object>> listOfAllBooks = CommonFunctions.getMapListFromJson(responseGetAllBooks.asString());
        if(listOfAllBooks.isEmpty()) {
            System.out.println("No books present in database. Existing test case");
            throw new SkipException("Test aborted - precondition failed: No books in database");
        }
        else{
            Map<String, Object> bookForDelete = listOfAllBooks.get(1);
            String bookId = bookForDelete.get("id").toString();
            Response deleteBookDetailsResponse = given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer" + " " + testContext.get("authToken"))
                    .when()
                    .delete(apiEndPointGetAllBooks.concat(bookId));

            int statusCodeUpdateBookDetails = deleteBookDetailsResponse.getStatusCode();
            AllureAsserts.assertEquals(statusCodeUpdateBookDetails, 200 ,"API response code User Login");
            String respBodyUpdateBook = deleteBookDetailsResponse.asString();
            boolean jsonValidationStatus = CommonFunctions.validateJsonSchema(respBodyUpdateBook, JsonSchema.DELETEBOOK);
            AllureAsserts.assertEquals(true, jsonValidationStatus, "Response Json Schema");
        }
    }
}
