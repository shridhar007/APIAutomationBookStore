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
    public static String GETBOOKDETAILSBYID = """
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
              "$schema": "http://json-schema.org/draft-07/schema#",
              "title": "Generated schema for Root",
              "type": "array",
              "items": {
                "type": "object",
                "properties": {
                  "author": {
                    "type": "string"
                  },
                  "book_summary": {
                    "type": "string"
                  },
                  "id": {
                    "type": "number"
                  },
                  "name": {
                    "type": "string"
                  },
                  "published_year": {
                    "type": "number"
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
            }
            """;
    public static String DELETEBOOK = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "title": "Generated schema for Root",
              "type": "object",
              "properties": {
                "message": {
                  "type": "string"
                }
              },
              "required": [
                "message"
              ]
            }
            """;

    public static String InvalidReqMethodResp = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "title": "Generated schema for Root",
              "type": "object",
              "properties": {
                "detail": {
                  "type": "string"
                }
              },
              "required": [
                "detail"
              ]
            }
            """;

    public static String UserRegistrationAPIWithoutBody = """
            {
              "$schema": "http://json-schema.org/draft-07/schema#",
              "title": "Generated schema for Root",
              "type": "object",
              "properties": {
                "detail": {
                  "type": "array",
                  "items": {
                    "type": "object",
                    "properties": {
                      "type": {
                        "type": "string"
                      },
                      "loc": {
                        "type": "array",
                        "items": {
                          "type": "string"
                        }
                      },
                      "msg": {
                        "type": "string"
                      },
                      "input": {}
                    },
                    "required": [
                      "type",
                      "loc",
                      "msg",
                      "input"
                    ]
                  }
                }
              },
              "required": [
                "detail"
              ]
            }
            """;
}

