package de.realliferpg.app.interfaces;

import org.json.JSONObject;

public interface RequestCallbackInterface {
    void onResponse(RequestTypeEnum type, JSONObject object);
}
