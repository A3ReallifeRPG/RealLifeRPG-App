package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.adapter.InfoAdapter;
import de.realliferpg.app.objects.Vehicle;


public class InfoFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View view;
    private ArrayList<Vehicle> vehicleArray;


    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_info, container, false);

        BottomNavigationView bnv = view.findViewById(R.id.bnv_info);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_info_vehicles:

                    case R.id.action_info_shops:

                    case R.id.action_info_licenses:

                }

                return true;
            }
        });

        ArrayList<String> shops = new ArrayList<>();
        shops.add("Slada Shop");
        shops.add("Mercedes Shop");

        final RecyclerView recyclerView = view.findViewById(R.id.rv_info_main);
        final Spinner shopSelect = view.findViewById(R.id.sp_info_select);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (view.getContext(), android.R.layout.simple_spinner_item,
                        shops); //selected item will look like a spinner set from XML

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        shopSelect.setAdapter(spinnerArrayAdapter);

        shopSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String slectedItem = (String) shopSelect.getItemAtPosition(position);


                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                recyclerView.setHasFixedSize(true);

                // use a linear layout manager
                LinearLayoutManager llM = new LinearLayoutManager(view.getContext());
                recyclerView.setLayoutManager(llM);

                ArrayList<Vehicle> list = new ArrayList<>();
                for(Vehicle tmpVeh : vehicleArray){
                    if(tmpVeh.shop.equals(slectedItem)){
                        list.add(tmpVeh);
                    }
                }

                InfoAdapter infoAdapter = new InfoAdapter(list);
                recyclerView.setAdapter(infoAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO clear list
            }
        });

        vehicleArray = new ArrayList<>();
        vehicleArray.add(new Vehicle("Lada", "Slada Shop"));
        vehicleArray.add(new Vehicle("Slada", "Slada Shop"));
        vehicleArray.add(new Vehicle("E63", "Mercedes Shop"));
        vehicleArray.add(new Vehicle("Mercedes G Klasse", "Mercedes Shop"));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
