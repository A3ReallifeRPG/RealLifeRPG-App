package de.realliferpg.app.interfaces;

import org.json.JSONObject;

public interface RequestCallbackInterface<T> {
    public void onResponse(JSONObject response, Class<T> type);
}
