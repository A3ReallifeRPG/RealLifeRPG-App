package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.CompanyShopsListAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.PhoneNumbers;

public class CompanyShopsFragment extends Fragment implements CallbackNotifyInterface {

    private View view;
    private FragmentInteractionInterface mListener;

    public CompanyShopsFragment() {
        // Required empty public constructor
    }

    public static CompanyShopsFragment newInstance() {
        return new CompanyShopsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_companyshops, container, false);

        final ProgressBar pbLoadCompanyShop = view.findViewById(R.id.pb_company_shops);
        pbLoadCompanyShop.setVisibility(View.VISIBLE);
        final ExpandableListView elv_company_shops = view.findViewById(R.id.lv_company_shops);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        apiHelper.getCompanyShop();

        final SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_company_shops);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getCompanyShop();

                pbLoadCompanyShop.setVisibility(View.VISIBLE);

                elv_company_shops.setAdapter((BaseExpandableListAdapter) null);
            }
        });

        elv_company_shops.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRow = (elv_company_shops == null || elv_company_shops.getChildCount() == 0) ?
                        0 : elv_company_shops.getChildAt(0).getTop();
                sc.setEnabled(firstVisibleItem == 0 && topRow >= 0);
            }
        });

        /*
        ArrayList<CompanyShops> companyShopsData = Singleton.getInstance().getCompanyShopsData();

        if (companyShopsData == null || companyShopsData.size() == 0){
            tvKeineDaten.setVisibility(View.VISIBLE);
            elv_company_shops.setVisibility(View.INVISIBLE);
        } else {
            tvKeineDaten.setVisibility(View.INVISIBLE);
            elv_company_shops.setVisibility(View.VISIBLE);
        }
        */

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
        ProgressBar pb_company_shops = view.findViewById(R.id.pb_company_shops);
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_company_shops);
        sc.setRefreshing(false);

        switch (type) {
            case COMPANY_SHOPS:
                final ExpandableListView elv_company_shops = view.findViewById(R.id.lv_company_shops);
                CompanyShopsListAdapter listAdapter = new CompanyShopsListAdapter(this.getContext(), Singleton.getInstance().getCompanyShopsData());

                elv_company_shops.setAdapter(listAdapter);

                pb_company_shops.setVisibility(View.GONE);

                // collapse all but selected item
                elv_company_shops.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousItem = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousItem)
                            elv_company_shops.collapseGroup(previousItem);
                        previousItem = groupPosition;
                    }
                });
                break;
            case NETWORK_ERROR:
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                pb_company_shops.setVisibility(View.GONE);

                Singleton.getInstance().setErrorMsg(error.toString());

                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_main_company_shops), R.string.str_error_occurred, Snackbar.LENGTH_LONG);

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

    private String getDefaultNumber(PhoneNumbers[] phones) {
        for (PhoneNumbers phone : phones) {
            if (phone.note.matches("default")) {
                return phone.phone;
            }
        }
        return "0";
    }
}
