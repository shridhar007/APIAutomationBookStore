package com.apiautomationbookstore.json.schema;

public class JsonSchema {

    public static String HEALTHCHECKAPI = """
            {
                    "type": "object",
                    "properties": {
                        "status": {
                            "type": "string",
                            "enum": ["up"]
                        }
                    },
                    "required": ["status"],
                    "additionalProperties": True
            }
        """;

    public static String USERSIGNUP = """
            {
                    "type": "object",
                    "properties": {
                        "message": {
                            "type": "string"
                        }
                    },
                    "required": ["message"],
                    "additionalProperties": True
                }         
            """;

    public static String USERLOGIN = """         
        {
            "type": "object",
            "properties": {
                "access_token": {
                    "type": "string"
                },
                "token_type": {
                    "type": "string",
                    "enum": ["bearer"]
                }
            },
            "required": ["access_token", "token_type"],
            "additionalProperties": False
        }
    
        """;

    public static String ADDBOOKRESPONSE = """
            {
              "type": "object",
              "properties": {
                "author": {
                  "type": "string"
                },
                "book_summary": {
                  "type": "string"
                },
                "id": {
                  "type": "integer"
                },
                "name": {
                  "type": "string"
                },
                "published_year": {
                  "type": "integer"
                }
              },
              "required": [
                "author",
                "book_summary",
                "id",
                "name",
                "published_year"
              ]
            }
            """;
    public static String GETALLBOOKSDETAIL = """
            {
                    "type": "object",
                    "items": {
                        "type": "object",
                        "properties": {
                            "book_summary": {"type": "string"},
                            "id": {"type": "integer"},
                            "name": {"type": "string"},
                            "published_year": {"type": "integer"},
                            "author": {"type": "string"}
                        },
                        "required": ["book_summary", "id", "name", "published_year", "author"],
                        "additionalProperties": True
                    }
                }
            """;
}
