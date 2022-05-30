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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.CBSListAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.CBSData;
import de.realliferpg.app.objects.CustomNetworkError;

public class CBSFragment extends Fragment implements CallbackNotifyInterface{

    private FragmentInteractionInterface mListener;
    private View view;

    public CBSFragment() {
        // Required empty public constructor
    }

    public static CBSFragment newInstance() {
        return new CBSFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_cbs, container, false);
        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        apiHelper.getCBSData();

        final ProgressBar pbChangelog = view.findViewById(R.id.pb_cbs_main);
        pbChangelog.setVisibility(View.VISIBLE);

        SwipeRefreshLayout sc = view.findViewById(R.id.srl_cbs);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getCBSData();
                pbChangelog.setVisibility(View.VISIBLE);

                ExpandableListView expandableListView = view.findViewById(R.id.lv_cbs_main);

                expandableListView.setAdapter((ExpandableListAdapter) null);
            }
        });

        return view;
    }

    @Override
    public void onCallback(RequestTypeEnum type) {
        ProgressBar pbCBS = view.findViewById(R.id.pb_cbs_main);
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_cbs);
        sc.setRefreshing(false);
        final TextView tvKeineDaten = view.findViewById(R.id.tv_no_data_cbs);
        final ExpandableListView expandableListView = view.findViewById(R.id.lv_cbs_main);

        switch (type) {
            case CBS:
                ArrayList<CBSData> cbsData = Singleton.getInstance().getCBSData();
                tvKeineDaten.setVisibility(View.GONE);

                if (cbsData == null || cbsData.isEmpty()){
                    tvKeineDaten.setVisibility(View.VISIBLE);
                    expandableListView.setVisibility(View.INVISIBLE);
                } else {
                    tvKeineDaten.setVisibility(View.INVISIBLE);
                    expandableListView.setVisibility(View.VISIBLE);

                    CBSListAdapter listAdapter = new CBSListAdapter(this.getContext(), cbsData);
                    expandableListView.setAdapter(listAdapter);
                }
                pbCBS.setVisibility(View.GONE);

                // collapse all but selected item
                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousItem = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousItem)
                            expandableListView.collapseGroup(previousItem);
                        previousItem = groupPosition;
                    }
                });
                break;
            case NETWORK_ERROR:
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                pbCBS.setVisibility(View.GONE);
                tvKeineDaten.setVisibility(View.VISIBLE);
                expandableListView.setVisibility(View.INVISIBLE);

                Singleton.getInstance().setErrorMsg(error.toString());
                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_changelog), R.string.str_error_occurred, Snackbar.LENGTH_LONG);

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
