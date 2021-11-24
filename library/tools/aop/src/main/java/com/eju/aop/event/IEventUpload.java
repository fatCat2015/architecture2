package com.eju.aop.event;

import org.json.JSONObject;

public interface IEventUpload {
    void uploadEvent(String evetName, JSONObject params);
}
