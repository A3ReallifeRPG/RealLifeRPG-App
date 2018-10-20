package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.PlayersListAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
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
        final ExpandableListView listPlayers = view.findViewById(R.id.lv_playersList);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        apiHelper.getPlayersList();

        final SwipeRefreshLayout sc = view.findViewById(R.id.srl_main_playerslist);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getPlayersList();

                pbLoadPlayersList.setVisibility(View.VISIBLE);

                listPlayers.setAdapter((BaseExpandableListAdapter) null);
            }
        });

        listPlayers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRow = (listPlayers == null || listPlayers.getChildCount() == 0) ?
                        0 : listPlayers.getChildAt(0).getTop();
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
                // TODO Testen
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                pbListPlayers.setVisibility(View.GONE);

                Singleton.getInstance().setErrorMsg(error.toString());

                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_main_playerslist), R.string.str_error_occurred, Constants.ERROR_SNACKBAR_DURATION);

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
