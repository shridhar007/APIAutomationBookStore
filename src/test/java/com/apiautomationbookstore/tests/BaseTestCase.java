package com.apiautomationbookstore.tests;

import com.apiautomationbookstore.util.CommonFunctions;
import com.apiautomationbookstore.util.TestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;




abstract class BaseTestCase {

    public static TestContext testContext;

    @BeforeSuite
    @Parameters("env")
    @Step("Setting One time framework environments")
//    public void beforeSuite(String env) throws IOException {
    public void beforeSuite() throws IOException {

        System.out.println("Initialize framework variables here");
        testContext = TestContext.getInstance();
        CommonFunctions.getRootFolder();
        testContext.put("rootFolder", CommonFunctions.getRootFolder());

        String env = "test";
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

    @Step("Initializing test environment data")
    public static void populateEnvData(String env, Object envData) {
        LinkedHashMap<String, String> tempStorage = (LinkedHashMap<String, String>) envData;
        tempStorage.forEach((String key, String value)-> {
            testContext.put(key, value);
        });
    }

    @AfterSuite
    public void removeUnwantedAllureTags() {
        Allure.getLifecycle().updateTestCase(
                testResult -> {
                    testResult.getLabels().removeIf(label -> "suite".equals(label.getName()));
                }
        );

        Allure.getLifecycle().updateTestCase(
                testResult -> {
                    testResult.getLabels().removeIf(label -> label.getName().startsWith("package:"));
                }
        );
    }
}
