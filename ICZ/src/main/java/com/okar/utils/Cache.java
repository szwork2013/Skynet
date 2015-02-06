package com.okar.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangfengchen on 15/1/27.
 */
public class Cache {

    private Map<String, Object> cache = new HashMap<String, Object>();

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String ket) {
        return cache.get(ket);
    }

}
