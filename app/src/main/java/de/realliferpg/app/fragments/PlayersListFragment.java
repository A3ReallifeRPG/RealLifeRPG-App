package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.ChangelogAdapter;
import de.realliferpg.app.adapter.PlayersListAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.Changelog;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.Server;

public class PlayersListFragment extends Fragment implements CallbackNotifyInterface {

    private View view;
    private FragmentInteractionInterface mListener;

    public PlayersListFragment() {
        // Required empty public constructor
    }

    public static PlayersListFragment newInstance() {
        return new PlayersListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_players_list, container, false);

        final ProgressBar pbLoadPlayersList = view.findViewById(R.id.pb_playerslist);
        pbLoadPlayersList.setVisibility(View.VISIBLE);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        apiHelper.getPlayersList();

        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_playerslist);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getPlayersList();

                pbLoadPlayersList.setVisibility(View.VISIBLE);

                final ExpandableListView listPlayers = view.findViewById(R.id.lv_playersList);
                listPlayers.setAdapter((BaseExpandableListAdapter) null);
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
        ProgressBar pbListPlayers = view.findViewById(R.id.pb_playerslist);
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_playerslist);
        sc.setRefreshing(false);

        switch (type) {
            case SERVER:
                ArrayList<Server> servers = Singleton.getInstance().getServerList();
                final ExpandableListView listPlayers = view.findViewById(R.id.lv_playersList);

                PlayersListAdapter listAdapter = new PlayersListAdapter(this.getContext(), servers);

                listPlayers.setAdapter(listAdapter);

                pbListPlayers.setVisibility(View.GONE);

                // collapse all but selected item
                listPlayers.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousItem = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousItem)
                            listPlayers.collapseGroup(previousItem);
                        previousItem = groupPosition;
                    }
                });
                break;
            case NETWORK_ERROR:
                // TODO
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                pbListPlayers.setVisibility(View.GONE);

                Singleton.getInstance().setErrorMsg(error.toString());
                // TODO Was ist eine Snackbar?
                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_changelog), R.string.str_error_occurred, Constants.ERROR_SNACKBAR_DURATION);

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
