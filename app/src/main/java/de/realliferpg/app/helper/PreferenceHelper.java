package de.realliferpg.app.helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.realliferpg.app.Singleton;

public class PreferenceHelper {
    private SharedPreferences prefs;

    public PreferenceHelper(){
        prefs = PreferenceManager.getDefaultSharedPreferences(Singleton.getInstance().getContext());
    }

    public String getPlayerAPIToken(){
        return prefs.getString("api_token","");
    }

    public boolean isCrashlyticsEnabled(){
        return prefs.getBoolean("pref_crashlytics",false);
    }
}
