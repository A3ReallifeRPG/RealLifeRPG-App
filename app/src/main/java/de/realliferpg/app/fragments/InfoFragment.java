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

import java.util.ArrayList;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.InfoAdapter;
import de.realliferpg.app.adapter.InfoSpinnerAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.Shop;
import de.realliferpg.app.objects.ShopItem;
import de.realliferpg.app.objects.ShopVehicle;


public class InfoFragment extends Fragment implements CallbackNotifyInterface {

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
        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
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

        ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
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

    private void updateShopInfo(RequestTypeEnum type){
        RecyclerView recyclerView = view.findViewById(R.id.rv_info_main);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llM = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(llM);

        if(type == RequestTypeEnum.SHOP_INFO_VEHICLE) {
            ArrayList<ShopVehicle> shopVehicles = Singleton.getInstance().getShopVehicleList();

            InfoAdapter<ShopVehicle> infoAdapter = new InfoAdapter<>(shopVehicles);
            recyclerView.setAdapter(infoAdapter);
        } else if(type == RequestTypeEnum.SHOP_INFO_ITEM){
            ArrayList<ShopItem> shopItemList = Singleton.getInstance().getShopItemList();

            InfoAdapter<ShopItem> infoAdapter = new InfoAdapter<>(shopItemList);
            recyclerView.setAdapter(infoAdapter);
        }

        ProgressBar pbContent = view.findViewById(R.id.pb_info_content);
        pbContent.setVisibility(View.GONE);
    }

    @Override
    public void onCallback(RequestTypeEnum type) {
        ProgressBar pbCategory = view.findViewById(R.id.pb_info_category);
        ProgressBar pbContent = view.findViewById(R.id.pb_info_content);
        Spinner shopSelect = view.findViewById(R.id.sp_info_select);

        switch (type){
            case SHOP:
                final ArrayList<Shop> shops = Singleton.getInstance().getShopList();

                InfoSpinnerAdapter spinnerArrayAdapter = new InfoSpinnerAdapter(view.getContext(), shops);

                shopSelect.setAdapter(spinnerArrayAdapter);

                pbCategory.setVisibility(View.GONE);
                break;
            case SHOP_INFO_ITEM:
                updateShopInfo(RequestTypeEnum.SHOP_INFO_ITEM);
                break;
            case SHOP_INFO_VEHICLE:
                updateShopInfo(RequestTypeEnum.SHOP_INFO_VEHICLE);
                break;
            case NETWORK_ERROR:
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                Singleton.getInstance().setErrorMsg(error.toString());
                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_info), R.string.str_error_occurred, Constants.ERROR_SNACKBAR_DURATION);

                snackbar.setAction(R.string.str_view, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onFragmentInteraction(InfoFragment.class,Uri.parse("open_error"));
                    }
                });

                pbCategory.setVisibility(View.GONE);
                pbContent.setVisibility(View.GONE);

                snackbar.show();
                Singleton.getInstance().setCurrentSnackbar(snackbar);
                break;
        }
    }

}
