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
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.InfoAdapter;
import de.realliferpg.app.adapter.InfoSpinnerAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.Shop;
import de.realliferpg.app.objects.ShopEntry;
import de.realliferpg.app.objects.Vehicle;


public class InfoFragment extends Fragment implements RequestCallbackInterface {

    private FragmentInteractionInterface mListener;
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
        pbContent.setVisibility(View.VISIBLE);
        final ProgressBar pbCategory = view.findViewById(R.id.pb_info_category);
        pbCategory.setVisibility(View.VISIBLE);


        BottomNavigationView bnv = view.findViewById(R.id.bnv_info);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bnv_info_vehicles:
                        currentCategory = Constants.CATEGORY_VEHICLE;
                        break;
                    case R.id.bnv_info_shops:
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
        // clear spinner
        Spinner shopSelect = view.findViewById(R.id.sp_info_select);
        shopSelect.setAdapter(null);

        ProgressBar pbCategory = view.findViewById(R.id.pb_info_category);
        pbCategory.setVisibility(View.VISIBLE);

        // clear content
        RecyclerView recyclerView = view.findViewById(R.id.rv_info_main);
        recyclerView.setAdapter(null);
        ProgressBar pbContent = view.findViewById(R.id.pb_info_content);
        pbContent.setVisibility(View.VISIBLE);

        ApiHelper apiHelper = new ApiHelper(this);
        apiHelper.getShops(currentCategory);
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

            Singleton.getInstance().setErrorMsg(error.toString());
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_info), R.string.str_error_occurred, Constants.ERROR_SNACKBAR_DURATION);

            snackbar.setAction(R.string.str_view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFragmentInteraction(InfoFragment.class,Uri.parse("open_error"));
                }
            });

            ProgressBar pbCategory = view.findViewById(R.id.pb_info_category);
            pbCategory.setVisibility(View.GONE);
            ProgressBar pbContent = view.findViewById(R.id.pb_info_content);
            pbContent.setVisibility(View.GONE);

            snackbar.show();
            Singleton.getInstance().setCurrentSnackbar(snackbar);
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


}
