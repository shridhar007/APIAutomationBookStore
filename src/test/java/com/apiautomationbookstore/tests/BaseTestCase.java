package com.apiautomationbookstore.tests;

import com.apiautomationbookstore.util.CommonFunctions;
import com.apiautomationbookstore.util.TestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;




public abstract class BaseTestCase {

    public static TestContext testContext;
    public String requestURL;
    public RequestSpecification requestSpecification;
    public Faker faker = new Faker();

    @DataProvider(name = "methodsToCall")
    public Object[] getData() {
//        return new Object[] {"post"};
        return new Object[] {"get","post", "patch", "put", "delete"};
    }

    @BeforeSuite
    @Parameters("env")
    @Step("Setting One time framework environments")
    public void beforeSuite(String env) throws IOException {
//    public void beforeSuite() throws IOException {
        System.out.println("Initialize framework variables here");
        testContext = TestContext.getInstance();
        testContext.put("rootFolder", CommonFunctions.getRootFolder());

        // Clean up allure report.
        String allure_result_path = ((Path)testContext.get("rootFolder")).resolve("allure-results").toString();
        CommonFunctions.deleteAllFiles(allure_result_path);

//        String env = "test";
        System.out.println("From Before Suite: " + env);

        // Read the config file and add its values to testContext for later use.
        Path config_file_path = ((Path) testContext.get("rootFolder")).resolve("common").resolve("data").resolve("execution_config.json");
        if(Files.exists(config_file_path)){
            ObjectMapper mapper = new ObjectMapper();
            Map map = (Map) mapper.readValue(new File(config_file_path.toString()), Map.class).get("environments");

            if(env.equalsIgnoreCase("TEST")) {
              populateEnvData("test", map.get("test"));
            } else if (env.equalsIgnoreCase("STAGE")) {
                populateEnvData("stage", map.get("stage"));
            }
            else {
                System.out.println("Invalid environment argument. Existing the execution...");
                System.exit(0);
            }

            // Initialize default system user
            CommonFunctions.addDefaultUser();
            String authToken = CommonFunctions.generateAuthToken();
            Map<String, Object> respMap = CommonFunctions.getMapFromJson(authToken);
            testContext.put("authToken", respMap.get("access_token").toString());
        }
        else {
            System.out.println("Framework Config file is missing. Existing the execution...");
            System.exit(0);
        }
    }

    @BeforeMethod
    public void testSetup() {
        Allure.label("owner", "Shridhar Porje");
        Allure.label("severity", "critical");

        String portNumber = TestContext.getInstance().get("port").toString();
        String baseURL = TestContext.getInstance().get("base_url").toString();
        requestURL = baseURL + ":" + portNumber;

        requestSpecification = RestAssured.given();
        requestSpecification.baseUri(requestURL);
        requestSpecification.header("Content-Type", "application/json");
    }

    @Step("Initializing test environment data")
    public static void populateEnvData(String env, Object envData) {
        LinkedHashMap<String, String> tempStorage = (LinkedHashMap<String, String>) envData;
        tempStorage.forEach((String key, String value)-> {
            testContext.put(key, value);
        });
    }

    @AfterSuite
    public void generateSingleFileAllureReport() {

        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("allure", "generate", "--clean", "--single-file", "./allure-results/", "-o", "./allure-report");
            Process process = builder.start();

            // Capture output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
