package de.realliferpg.app.interfaces;

import org.json.JSONObject;

public interface RequestCallbackInterface<T> {
    public void onResponse(Object response, Class<T> type);
}
