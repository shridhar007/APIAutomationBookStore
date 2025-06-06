package com.apiautomationbookstore.util;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommonFunctions {


    public static Path getRootFolder() {
        return Path.of(System.getProperty("user.dir"));
    }

    public static boolean validateJsonSchema(String actualResponse, String jsonSchemaFileName) {

        try {
            InputStream schemaStream = Files.newInputStream(CommonFunctions.getRootFolder().resolve("common").resolve("jsonSchema").resolve(jsonSchemaFileName));
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
}
