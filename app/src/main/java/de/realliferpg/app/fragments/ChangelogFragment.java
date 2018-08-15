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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.ChangelogAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.Changelog;
import de.realliferpg.app.objects.CustomNetworkError;

public class ChangelogFragment extends Fragment implements CallbackNotifyInterface {

    private FragmentInteractionInterface mListener;
    private View view;

    public ChangelogFragment() {
        // Required empty public constructor
    }

    public static ChangelogFragment newInstance() {
        return new ChangelogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_changelog, container, false);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        apiHelper.getChangelog();

        final ProgressBar pbChangelog = view.findViewById(R.id.pb_changelog_main);
        pbChangelog.setVisibility(View.VISIBLE);

        SwipeRefreshLayout sc = view.findViewById(R.id.srl_changelog);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getChangelog();
                pbChangelog.setVisibility(View.VISIBLE);

                ExpandableListView listView = view.findViewById(R.id.lv_changelog_main);
                listView.setAdapter((BaseExpandableListAdapter) null);
            }
        });

        return view;
    }

    @Override
    public void onCallback(RequestTypeEnum type) {
        ProgressBar pbChangelog = view.findViewById(R.id.pb_changelog_main);
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_changelog);
        sc.setRefreshing(false);

        switch (type) {
            case CHANGELOG:
                ArrayList<Changelog> changelogs = Singleton.getInstance().getChangelogList();

                final ExpandableListView listView = view.findViewById(R.id.lv_changelog_main);

                ArrayList<Changelog> temp = new ArrayList<>();

                for (Changelog temp_changelog : changelogs) {

                    if (temp_changelog.change_mission.length > 0) {
                        temp_changelog.change_mission = addHeader("<b>Mission</b>", temp_changelog.change_mission);
                    }

                    if (temp_changelog.change_mod.length > 0) {
                        temp_changelog.change_mod = addHeader("<b>Mod</b>", temp_changelog.change_mod);
                    }

                    if (temp_changelog.change_map.length > 0) {
                        temp_changelog.change_map = addHeader("<b>Map</b>", temp_changelog.change_map);
                    }

                    temp.add(temp_changelog);
                }

                ChangelogAdapter listAdapter = new ChangelogAdapter(this.getContext(), temp);

                listView.setAdapter(listAdapter);

                pbChangelog.setVisibility(View.GONE);

                // collapse all but selected item
                listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousItem = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousItem)
                            listView.collapseGroup(previousItem);
                        previousItem = groupPosition;
                    }
                });
                break;
            case NETWORK_ERROR:
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                pbChangelog.setVisibility(View.GONE);

                Singleton.getInstance().setErrorMsg(error.toString());
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


    String[] addHeader(String Headline, String[] changelog_spe) {
        int currentSize = changelog_spe.length;
        int newSize = currentSize + 1;
        String[] tempArray = new String[newSize];
        System.arraycopy(changelog_spe, 0, tempArray, 1, currentSize);
        tempArray[0] = Headline;
        return tempArray;
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
