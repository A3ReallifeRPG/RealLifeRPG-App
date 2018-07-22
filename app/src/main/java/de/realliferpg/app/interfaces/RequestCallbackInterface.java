package de.realliferpg.app.interfaces;

public interface RequestCallbackInterface<T> {
    public void onResponse(Object response, Class<T> type);
}
