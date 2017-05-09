package com.sss.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shusheng Shi on 2017/5/8.-4
 */
//可以发送任何数据,方便在模板中展示,因此是准们给视图展示而建立的对象,故称ViewObject
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}

