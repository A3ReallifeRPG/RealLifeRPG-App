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

    public boolean showSold() { return prefs.getBoolean("pref_vehicleList_sold", false);}

    public boolean showDestroyed() { return prefs.getBoolean("pref_vehicleList_destroyed", false);}

    public boolean showImpounded() { return prefs.getBoolean("pref_vehicleList_impounded", false);}

    public int getDaysForReminderMaintenance() {
        String days = prefs.getString("pref_days_maintenance", "14");
        if (days == null || days.isEmpty()){
            days = "14";
        }
        return Integer.valueOf(days);
    }
}
