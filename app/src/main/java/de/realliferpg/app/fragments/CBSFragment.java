package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

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

public class CBSFragment extends Fragment {

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

        view = inflater.inflate(R.layout.fragment_cbs, container, false);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        if (Singleton.getInstance().getCBSData() == null) {
            apiHelper.getCBSData();
        } else {
            showCBSData();
        }

        /*
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_cbs);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getCBSData();
            }
        });
        */

        return view;
    }

    private void showCBSData(){
        ExpandableListView expandableListView = view.findViewById(R.id.lv_cbs_main);

        ArrayList<CBSData> cbsData = Singleton.getInstance().getCBSData();

        CBSListAdapter cbsListAdapter = new CBSListAdapter(this.getContext(), cbsData);
        expandableListView.setAdapter(cbsListAdapter);
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
