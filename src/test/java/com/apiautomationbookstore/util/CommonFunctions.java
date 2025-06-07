package com.apiautomationbookstore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

public class CommonFunctions {


    public static void addDefaultUser() throws JsonProcessingException {
        String baseURL = TestContext.getInstance().get("base_url").toString();
        String portNumber = TestContext.getInstance().get("port").toString();
        String requestURL = baseURL + ":" + portNumber;
        String endpoint = "/signup";
        String defaultUserEmail = TestContext.getInstance().get("default_username").toString();
        String defaultUserPwd = TestContext.getInstance().get("default_password").toString();
        RestAssured.baseURI = requestURL;

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", defaultUserEmail);
        requestBodyMap.put("password", defaultUserPwd);

        Response response = given().header("Content-Type", "application/json")
                .body(getJsonFromMap(requestBodyMap))
                .when()
                .post(endpoint);

        int statusCode = response.getStatusCode();
        String respBody = response.asString();
    }

    public static String generateAuthToken() throws JsonProcessingException {
        String baseURL = TestContext.getInstance().get("base_url").toString();
        String portNumber = TestContext.getInstance().get("port").toString();
        String requestURL = baseURL + ":" + portNumber;
        String apiEndPoint = "/login";
        String defaultUserEmail = TestContext.getInstance().get("default_username").toString();
        String defaultUserPwd = TestContext.getInstance().get("default_password").toString();
        RestAssured.baseURI = requestURL;

        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("email", defaultUserEmail);
        requestBodyMap.put("password", defaultUserPwd);

        Response response = given().header("Content-Type", "application/json")
                .body(getJsonFromMap(requestBodyMap))
                .when()
                .post(apiEndPoint);

        return response.asString();
    }

    public static Path getRootFolder() {
        return Path.of(System.getProperty("user.dir"));
    }

    public static boolean validateJsonSchema(String actualResponse, String expectedJsonSchema) {

        try {
            InputStream schemaStream = new ByteArrayInputStream(expectedJsonSchema.getBytes(StandardCharsets.UTF_8));
            JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
            Schema schema = SchemaLoader.load(rawSchema);
            JSONObject jsonResponse = new JSONObject(actualResponse);
            schema.validate(jsonResponse);
            return true;
        }
        catch (Exception e) {
            System.out.println("Exception in validating JSON");
            return false;
        }
    }

    public static String getJsonFromMap(Map<String, String> inputMap) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRepresentation = objectMapper.writeValueAsString(inputMap);
        return jsonRepresentation;

    }

    public static Map<String, Object> getMapFromJson(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonString, Map.class);
        return map;
    }

    public static int getRandomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
