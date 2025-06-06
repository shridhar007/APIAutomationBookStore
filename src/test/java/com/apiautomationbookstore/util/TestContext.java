package com.apiautomationbookstore.util;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private static TestContext instance;
    private Map<String, Object> store;

    private TestContext() {
        store = new HashMap<>();
    }

    public static synchronized TestContext getInstance() {
        if (instance == null) {
            instance = new TestContext();
        }
        return instance;
    }

    public void put(String key, Object value) {
        store.put(key, value);
    }

    public Object get(String key) {
        return store.get(key);
    }

    public boolean contains(String key) {
        return store.containsKey(key);
    }

    public void clear() {
        store.clear();
    }
}
