package de.realliferpg.app.fragments;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.preference.PreferenceFragmentCompat;

import de.realliferpg.app.R;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;


public class SettingsFragment extends PreferenceFragmentCompat {

    View view;
    private FragmentInteractionInterface mListener;

    public SettingsFragment() {

    }


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
