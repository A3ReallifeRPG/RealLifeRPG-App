package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.Shop;

public class RechnerToolFragment extends Fragment implements CallbackNotifyInterface {

    private View view;
    private FragmentInteractionInterface mListener;
    private int currentCategory;

    public RechnerToolFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_rechnertool, container, false);
        currentCategory = Constants.CATEGORY_CALC_VEHICLES;

        BottomNavigationView bnv = view.findViewById(R.id.bnv_rechner);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bnv_calc_vehicles:
                        currentCategory = Constants.CATEGORY_CALC_VEHICLES;
                        mListener.onFragmentInteraction(RechnerToolFragment.class, Uri.parse("fragment_calc_change_to_vehicles"));
                        break;
                    case R.id.bnv_calc_guns:
                        currentCategory = Constants.CATEGORY_CALC_GUNS;
                        mListener.onFragmentInteraction(RechnerToolFragment.class, Uri.parse("fragment_calc_change_to_guns"));
                        break;
                    case R.id.bnv_calc_sell:
                        currentCategory = Constants.CATEGORY_CALC_SELL;
                        mListener.onFragmentInteraction(RechnerToolFragment.class, Uri.parse("fragment_calc_change_to_sell"));
                        break;
                    case R.id.bnv_calc_sum:
                        currentCategory = Constants.CATEGORY_CALC_SUM;
                        mListener.onFragmentInteraction(RechnerToolFragment.class, Uri.parse("fragment_calc_change_to_sum"));
                        break;
                }
                return true;
            }
        });

        SwipeRefreshLayout srl = view.findViewById(R.id.srl_rechner);
        srl.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));

        return view;
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
    public void onCallback(RequestTypeEnum type) {
        /*
        ProgressBar pbMarketPrices = view.findViewById(R.id.pb_market);
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_market);
        sc.setRefreshing(false);

        switch (type) {
            case CURRENT_MARKET_PRICES:
                ArrayList<MarketItem> marketPrices = Singleton.getInstance().getMarketPrices();
                ArrayList<Server> serverListe = Singleton.getInstance().getServerList();
                Collections.sort(marketPrices, new Comparator<MarketItem>() {
                    @Override
                    public int compare(MarketItem o1, MarketItem o2) {
                        return o1.name.compareTo(o2.name);
                    }
                });
                final ListView listMarketPrices = view.findViewById(R.id.lv_market);

                int[] serversOnline = GetServersOnline(serverListe);

                MarketItemAdapter listAdapter = new MarketItemAdapter(view.getContext(), marketPrices, serversOnline);

                listMarketPrices.setAdapter(listAdapter);

                pbMarketPrices.setVisibility(View.GONE);

                break;
            case NETWORK_ERROR:
                // TODO Testen
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                pbMarketPrices.setVisibility(View.GONE);

                Singleton.getInstance().setErrorMsg(error.toString());

                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.srl_main_market), R.string.str_error_occurred, Constants.ERROR_SNACKBAR_DURATION);

                snackbar.setAction(R.string.str_view, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onFragmentInteraction(ChangelogFragment.class, Uri.parse("open_error"));
                    }
                });

                snackbar.show();
                Singleton.getInstance().setCurrentSnackbar(snackbar);
                break;
                */
        }
    }

