package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.MarketItemAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.MarketItem;

public class MarketFragment extends Fragment implements CallbackNotifyInterface {

    private View view;
    private FragmentInteractionInterface mListener;

    public MarketFragment() {
        // Required empty public constructor
    }

    public static MarketFragment newInstance() {
        return new MarketFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_market, container, false);

        final ProgressBar pbLoadMarketPrices = view.findViewById(R.id.pb_market);
        pbLoadMarketPrices.setVisibility(View.VISIBLE);
        final ListView listMarketPrices = view.findViewById(R.id.lv_market);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        apiHelper.getMarketPrices();

        final SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_market);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getMarketPrices();

                pbLoadMarketPrices.setVisibility(View.VISIBLE);

                listMarketPrices.setAdapter(null);
            }
        });

        listMarketPrices.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRow = (listMarketPrices == null || listMarketPrices.getChildCount() == 0) ?
                        0 : listMarketPrices.getChildAt(0).getTop();
                sc.setEnabled(firstVisibleItem == 0 && topRow >= 0);
            }
        });

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
        ProgressBar pbMarketPrices = view.findViewById(R.id.pb_market);
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_market);
        sc.setRefreshing(false);

        switch (type) {
            case CURRENT_MARKET_PRICES:
                ArrayList<MarketItem> marketPrices = Singleton.getInstance().getMarketPrices();
                Collections.sort(marketPrices, new Comparator<MarketItem>() {
                    @Override
                    public int compare(MarketItem o1, MarketItem o2) {
                        return o1.localized.compareTo(o2.localized);
                    }
                });
                final ListView listMarketPrices = view.findViewById(R.id.lv_market);

                MarketItemAdapter listAdapter = new MarketItemAdapter(view.getContext(), marketPrices);

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
        }
    }
}
