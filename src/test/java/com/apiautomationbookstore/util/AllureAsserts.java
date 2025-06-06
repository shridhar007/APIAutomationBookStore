package com.apiautomationbookstore.util;

import io.qameta.allure.Step;
import org.testng.Assert;

public class AllureAsserts {

    @Step("Assert that condition is true")
    public static void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

    @Step("Assert that condition is false")
    public static void assertFalse(boolean condition) {
        Assert.assertFalse(condition);
    }

    @Step("Assert that actual value '{actual}' equals expected '{expected}'")
    public static void assertEquals(Object actual, Object expected) {
        Assert.assertEquals(actual, expected);
    }

    @Step("Assert that actual value '{actual}' equals expected '{expected}' with message: {message}")
    public static void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    @Step("Assert that two objects are not equal")
    public static void assertNotEquals(Object actual, Object expected) {
        Assert.assertNotEquals(actual, expected);
    }

    @Step("Assert that object is not null")
    public static void assertNotNull(Object object) {
        Assert.assertNotNull(object);
    }

    @Step("Assert that object is null")
    public static void assertNull(Object object) {
        Assert.assertNull(object);
    }

    @Step("Fail the test with message: {message}")
    public static void fail(String message) {
        Assert.fail(message);
    }
}
