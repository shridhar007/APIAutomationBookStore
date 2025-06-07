package com.apiautomationbookstore.tests;

import org.testng.annotations.Test;

public class NegativeTests {


    @Test(groups = {"negative"})
    public void validateUserSignupAPI1() {
        System.out.println("Inside Negative test");

    }

    // Health check API - Call with POST, PUT, PATCH, DELETE, Incorrect end point.
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
