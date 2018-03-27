package com.zhou.wenda.wenda.domain;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {
    private Map<String, Object> Objectmap = new HashMap<String, Object>();

    public void set(String key, Object value) {
        Objectmap.put(key, value);
    }

    public Object get(String key) {
        return Objectmap.get(key);
    }
}
