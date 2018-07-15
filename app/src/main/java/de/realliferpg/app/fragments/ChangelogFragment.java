package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.realliferpg.app.R;
import de.realliferpg.app.adapter.ChangelogAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.Changelog;

public class ChangelogFragment extends Fragment implements RequestCallbackInterface {

    private ExpandableListView listView;
    private OnFragmentInteractionListener mListener;
    private View view;

    public ChangelogFragment() {
        // Required empty public constructor
    }

    public static ChangelogFragment newInstance() {
        ChangelogFragment fragment = new ChangelogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_changelog, container, false);

        ApiHelper apiHelper = new ApiHelper(this);
        apiHelper.getChangelog();

        return view;
    }


    @Override
    public void onResponse(Object response, Class type) {

        if (type.equals(Changelog.Wrapper.class)) {

            Gson gson = new Gson();
            Changelog.Wrapper value = gson.fromJson(response.toString(), Changelog.Wrapper.class);

            ArrayList<Changelog> changelogs = new ArrayList<>(Arrays.asList(value.data));

            listView = view.findViewById(R.id.lvExp);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
