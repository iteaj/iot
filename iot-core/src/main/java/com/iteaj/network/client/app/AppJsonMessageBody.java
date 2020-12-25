package com.iteaj.network.client.app;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class AppJsonMessageBody extends JSONObject implements AppClientMessageBody {

    public AppJsonMessageBody() { }

    public AppJsonMessageBody(Map<String, Object> map) {
        super(map);
    }

    public AppJsonMessageBody add(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
