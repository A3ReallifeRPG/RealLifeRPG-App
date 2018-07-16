package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.adapter.InfoAdapter;
import de.realliferpg.app.adapter.InfoSpinnerAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.License;
import de.realliferpg.app.objects.Server;
import de.realliferpg.app.objects.Shop;
import de.realliferpg.app.objects.ShopEntry;
import de.realliferpg.app.objects.Vehicle;


public class InfoFragment extends Fragment implements RequestCallbackInterface {

    private OnFragmentInteractionListener mListener;
    private View view;

    private int currentCategory;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_info, container, false);

        currentCategory = Constants.CATEGORY_VEHICLE;

        final ProgressBar pbContent = view.findViewById(R.id.pb_info_content);
        pbContent.setVisibility(View.GONE);
        final ProgressBar pbCategory = view.findViewById(R.id.pb_info_category);
        pbCategory.setVisibility(View.GONE);


        BottomNavigationView bnv = view.findViewById(R.id.bnv_info);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_info_vehicles:
                        currentCategory = Constants.CATEGORY_VEHICLE;
                        break;
                    case R.id.action_info_shops:
                        currentCategory = Constants.CATEGORY_SHOP;
                        break;
                    //case R.id.action_info_licenses:
                        //currentCategory = Constants.CATEGORY_LICENSE;
                        //break;
                }
                onCategoryChanged();
                return true;
            }
        });

        final RecyclerView recyclerView = view.findViewById(R.id.rv_info_main);
        final ApiHelper apiHelper = new ApiHelper(this);
        apiHelper.getShops(currentCategory);

        final Spinner shopSelect = view.findViewById(R.id.sp_info_select);

        shopSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pbContent.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(null);

                Shop shop = (Shop) shopSelect.getItemAtPosition(position);
                apiHelper.getShopInfo(currentCategory,shop.shoptype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recyclerView.setAdapter(null);

                pbContent.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void onCategoryChanged(){
        Spinner shopSelect = view.findViewById(R.id.sp_info_select);
        shopSelect.setAdapter(null);

        ProgressBar pbCategory = view.findViewById(R.id.pb_info_category);
        pbCategory.setVisibility(View.VISIBLE);

        ApiHelper apiHelper = new ApiHelper(this);
        apiHelper.getShops(currentCategory);
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

    @Override
    public void onResponse(Object response, Class type) {
        if (type.equals(Shop.Wrapper.class)) {
            Gson gson = new Gson();

            Shop.Wrapper value = gson.fromJson(response.toString(), Shop.Wrapper.class);
            final ArrayList<Shop> shops = new ArrayList<>(Arrays.asList(value.data));

            Spinner shopSelect = view.findViewById(R.id.sp_info_select);

            InfoSpinnerAdapter spinnerArrayAdapter = new InfoSpinnerAdapter(view.getContext(), shops);

            shopSelect.setAdapter(spinnerArrayAdapter);

            ProgressBar pbCategory = view.findViewById(R.id.pb_info_category);
            pbCategory.setVisibility(View.GONE);
        }else if (type.equals(CustomNetworkError.class)){
            CustomNetworkError error = (CustomNetworkError) response;
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_info), error.toString(), Snackbar.LENGTH_LONG);

            snackbar.show();
        }else {
            Gson gson = new Gson();

            RecyclerView recyclerView = view.findViewById(R.id.rv_info_main);
            recyclerView.setHasFixedSize(true);

            LinearLayoutManager llM = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(llM);

            if(type.equals(Vehicle.Wrapper.class)){
                Vehicle.Wrapper value = gson.fromJson(response.toString(), Vehicle.Wrapper.class);
                ArrayList<Vehicle> vehicles = new ArrayList<>(Arrays.asList(value.data));

                InfoAdapter<Vehicle> infoAdapter = new InfoAdapter<>(vehicles);
                recyclerView.setAdapter(infoAdapter);
            } else if(type.equals(ShopEntry.Wrapper.class)){
                ShopEntry.Wrapper value = gson.fromJson(response.toString(), ShopEntry.Wrapper.class);
                ArrayList<ShopEntry> vehicles = new ArrayList<>(Arrays.asList(value.data));

                InfoAdapter<ShopEntry> infoAdapter = new InfoAdapter<>(vehicles);
                recyclerView.setAdapter(infoAdapter);
            }

            ProgressBar pbContent = view.findViewById(R.id.pb_info_content);
            pbContent.setVisibility(View.GONE);
        }
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
