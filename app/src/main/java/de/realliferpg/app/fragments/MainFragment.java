package de.realliferpg.app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.R;
import de.realliferpg.app.adapter.ServerListAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.PlayerInfo;
import de.realliferpg.app.objects.Server;


public class MainFragment extends Fragment implements RequestCallbackInterface {

    private OnFragmentInteractionListener mListener;

    private View view;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
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

        this.view = inflater.inflate(R.layout.fragment_main, container, false);

        final ApiHelper apiHelper = new ApiHelper(this);
        apiHelper.getServers();
        apiHelper.getPlayerStats();

        SwipeRefreshLayout sc = view.findViewById(R.id.sc_main);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getServers(); apiHelper.getPlayerStats();
            }
        });



        return view;
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

    @Override
    public void onResponse(Object response, Class type) {
        SwipeRefreshLayout sc = view.findViewById(R.id.sc_main);

        if (type.equals(Server.Wrapper.class)) {
            Gson gson = new Gson();

            Server.Wrapper value = gson.fromJson(response.toString(), Server.Wrapper.class);

            final ArrayList<Server> servers = new ArrayList<>(Arrays.asList(value.data));

            ServerListAdapter adapter = new ServerListAdapter(view.getContext(), servers);

            final ListView listView = view.findViewById(R.id.lv_main_serverList);
            listView.setAdapter(adapter);
            sc.setRefreshing(false);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                    String selectedFromList = (String) listView.getItemAtPosition(myItemInt).toString();
                    Log.d("MainFragment", Arrays.toString(servers.get(myItemInt).Players));

                    AlertDialog ad = new AlertDialog.Builder(view.getContext()).create();
                    ad.setCancelable(false); // This blocks the 'BACK' button

                    StringBuilder players = new StringBuilder();

                    for(String player: servers.get(myItemInt).Players)
                    {
                        players.append("\n").append(player);
                    }
                    
                    ad.setMessage(players.toString());
                    ad.setButton("schließen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
            });
        }else if (type.equals(PlayerInfo.Wrapper.class)) {
            Gson gson = new Gson();

            PlayerInfo.Wrapper value = gson.fromJson(response.toString(), PlayerInfo.Wrapper.class);

            PlayerInfo playerInfo = value.data[0];

            TextView tvPiName = view.findViewById(R.id.tv_main_playerInfo_name);
            TextView tvPiPID = view.findViewById(R.id.tv_main_playerInfo_pid);
            TextView tvPiGUID = view.findViewById(R.id.tv_main_playerInfo_guid);

            TextView tvPiInfoBank = view.findViewById(R.id.tv_main_playerInfo_bank);
            TextView tvPiInfoCash = view.findViewById(R.id.tv_main_playerInfo_cash);
            TextView tvPiInfoLevel = view.findViewById(R.id.tv_main_playerInfo_level);
            TextView tvPiInfoSkill = view.findViewById(R.id.tv_main_playerInfo_skill);

            tvPiName.setText(playerInfo.name);
            tvPiPID.setText(playerInfo.pid);
            tvPiGUID.setText(playerInfo.guid);

            tvPiInfoBank.setText(String.valueOf( playerInfo.bankacc + " $"));
            tvPiInfoCash.setText(String.valueOf( playerInfo.cash  + " $"));
            tvPiInfoLevel.setText(String.valueOf( playerInfo.level));
            tvPiInfoSkill.setText(String.valueOf( playerInfo.skillpoint));

        }else if (type.equals(CustomNetworkError.class)){

        }
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
