package de.realliferpg.app.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;


import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.activities.MainActivity;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;

public class SettingsFragment extends PreferenceFragmentCompat implements FragmentInteractionInterface {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        PreferenceManager.setDefaultValues(Singleton.getInstance().getContext(), R.xml.preferences, false);

        Preference pref = findPreference("scan_code");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                scanCode();
                return false;
            }
        });

    }

    public void scanCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.addExtra(Intents.Scan.BEEP_ENABLED,false);
        intentIntegrator.initiateScan();
    }

    @Override
    public void onFragmentInteraction(Class type, Uri uri) {
        if(type.equals(MainActivity.class)){
            if(uri.toString().equals("scan_response")){
                EditTextPreference tokenPref = (EditTextPreference) findPreference("api_token");
                tokenPref.setText(Singleton.getInstance().getScanResponse());
            }
        }
    }
}