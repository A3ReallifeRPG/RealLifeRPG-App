package de.realliferpg.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.activities.MainActivity;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.helper.PreferenceHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;

public class SettingsFragment extends PreferenceFragmentCompat implements FragmentInteractionInterface, SharedPreferences.OnSharedPreferenceChangeListener, CallbackNotifyInterface {

    public static final String KEY_PREF_CRASHLYTICS = "pref_crashlytics";
    public static final Integer MAX_DAYS_MAINTENANCE = 20;

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

        Preference prefDaysMaintenance = findPreference("days_maintenance");

        prefDaysMaintenance.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                changeTitleAndShowCurrentValue(preference);
                return false;
            }
        });

        prefDaysMaintenance.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                getDaysForMaintenanceReminder(preference, newValue);
                return false;
            }
        });

        // Preferences for vehicle list
        Preference prefVehicleSold = findPreference("pref_vehicleList_sold");
        prefVehicleSold.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                switchSettingsVehicleList(preference);
                return true;
            }
        });

        Preference prefVehicleDestroyed = findPreference("pref_vehicleList_destroyed");
        prefVehicleDestroyed.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                switchSettingsVehicleList(preference);
                return true;
            }
        });

        Preference prefVehicleImpounded = findPreference("pref_vehicleList_impounded");
        prefVehicleImpounded.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                switchSettingsVehicleList(preference);
                return true;
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

    private void switchSettingsVehicleList(Preference preference){
        if (preference instanceof SwitchPreference)
        {
            final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
            apiHelper.getPlayerVehicles();
        }
    }

    public void changeTitleAndShowCurrentValue(Preference preference){
        if (preference instanceof EditTextPreference)
        {
            // Aktuellen Wert anzeigen
            EditTextPreference editTextPreference =  (EditTextPreference)preference;

            SharedPreferences sharedPreferences =  getPreferenceScreen().getSharedPreferences();
            String savedValue = sharedPreferences.getString(preference.getKey(), "");

            editTextPreference.setText(savedValue);
        }
    }

    public void getDaysForMaintenanceReminder(Preference preference, Object newValue){
        if (preference instanceof EditTextPreference)
        {
            // Behandlung des neuen Werts
            EditTextPreference editTextPreference =  (EditTextPreference)preference;
            String days = newValue.toString();

            // Prüfen, ob das einem Intwert entspricht (und max. 20)
            days = days.trim();
            boolean use = true;

            if (days.length() > 2) {
                use = false;
            }

            String regex = "\\d+";

            if (use && !days.matches(regex)) {
                // falls es keine Zahl ist
                use = false;
            }

            if (use && Integer.valueOf(days) > MAX_DAYS_MAINTENANCE) {
                use = false;
            }

            if (use) {
                Toast.makeText(this.getContext(), getString(R.string.str_new_value) + " " + days, Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(preference.getKey(), days);
                editor.commit();
            }
            else {
                Toast.makeText(this.getContext(), getString(R.string.str_no_valid_number_days_maintenance), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(KEY_PREF_CRASHLYTICS)) {

            PreferenceHelper preferenceHelper = new PreferenceHelper();
            if (preferenceHelper.isCrashlyticsEnabled()) {
                Snackbar snackbar = Snackbar.make(getView(), R.string.str_crashlyticsEnabled, Constants.ERROR_SNACKBAR_DURATION);
                snackbar.show();
                Singleton.getInstance().setCurrentSnackbar(snackbar);
                mListener.onFragmentInteraction(SettingsFragment.class, Uri.parse("enable_crashlytics"));
            }
            else {
                Snackbar snackbar = Snackbar.make(getView(), R.string.str_crashlyticsDisabled, Constants.ERROR_SNACKBAR_DURATION);
                snackbar.show();
                Singleton.getInstance().setCurrentSnackbar(snackbar);
            }
        }
        else if (key.equals("days_maintenance"))
        {
            /*Snackbar snackbar = Snackbar.make(getView(), "Bla", Constants.ERROR_SNACKBAR_DURATION);
            snackbar.show();*/
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

    @Override
    public void onCallback(RequestTypeEnum type) {
        
    }
}