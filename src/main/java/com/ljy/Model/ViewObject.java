package com.ljy.Model;

import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ljy on 2017/2/13.
 */
public class ViewObject {
    private Map<String ,Object> objs = new HashedMap();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }


}
