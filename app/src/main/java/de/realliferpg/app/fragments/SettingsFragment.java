package de.realliferpg.app.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;


import com.crashlytics.android.Crashlytics;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.activities.MainActivity;
import de.realliferpg.app.helper.PreferenceHelper;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import io.fabric.sdk.android.Fabric;

public class SettingsFragment extends PreferenceFragmentCompat implements FragmentInteractionInterface, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_CRASHLYTICS = "pref_crashlytics";

    private FragmentInteractionInterface mListener;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        PreferenceManager.setDefaultValues(Singleton.getInstance().getContext(), R.xml.preferences, false);

        Preference prefScan = findPreference("scan_code");
        prefScan.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                scanCode();
                return false;
            }
        });

        Preference prefScanHelp = findPreference("scan_code_help");
        prefScanHelp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wiki.realliferpg.de/index.php?title=RealLifeRPG_App"));
                startActivity(browserIntent);
                return false;
            }
        });

    }

    public void scanCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.addExtra(Intents.Scan.BEEP_ENABLED,false);
        intentIntegrator.setPrompt(getString(R.string.str_qrScannerInfo));
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(KEY_PREF_CRASHLYTICS)) {

            PreferenceHelper preferenceHelper = new PreferenceHelper();
            if(preferenceHelper.isCrashlyticsEnabled()){
                Snackbar snackbar = Snackbar.make(getView(), R.string.str_crashlyticsEnabled, Constants.ERROR_SNACKBAR_DURATION);
                snackbar.show();
                Singleton.getInstance().setCurrentSnackbar(snackbar);
                mListener.onFragmentInteraction(SettingsFragment.class, Uri.parse("enable_crashlytics"));
            }else{
                Snackbar snackbar = Snackbar.make(getView(), R.string.str_crashlyticsDisabled, Constants.ERROR_SNACKBAR_DURATION);
                snackbar.show();
                Singleton.getInstance().setCurrentSnackbar(snackbar);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionInterface) {
            mListener = (FragmentInteractionInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}