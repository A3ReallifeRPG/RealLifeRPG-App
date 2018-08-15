package de.realliferpg.app.interfaces;

public interface RequestCallbackInterface {
    void onResponse(Object response, Class<?> type);
    void onResponse(RequestTypeEnum type, Object object);
}
