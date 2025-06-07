package com.apiautomationbookstore.util;

import io.qameta.allure.Allure;
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

    public static void assertEquals(int actual, int expected, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Validation of ").append(message).append(" ")
                .append(actual)
                .append(" and ")
                .append(expected)
                .append(" is ").append(" ").append(actual == expected).append(" ");


        Allure.step(sb.toString());
        Assert.assertEquals(actual, expected, message);
    }

    @Step("Assert that actual value '{actual}' equals expected '{expected}' with message: {message}")
    public static void assertEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
    }

    @Step("Assert that actual value '{actual}' equals expected '{expected}' with message: {message}")
    public static void assertEquals(boolean actual, boolean expected, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Validation of ").append(message).append(" ")
                .append(actual)
                .append(" and ")
                .append(expected)
                .append(" is ").append(" ").append(actual == expected).append(" ");

        Allure.step(sb.toString());
        Assert.assertEquals(actual, expected, message);
    }

    @Step("Assert that two Strings are not equal")
    public static void assertNotEquals(String actual, String expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
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
