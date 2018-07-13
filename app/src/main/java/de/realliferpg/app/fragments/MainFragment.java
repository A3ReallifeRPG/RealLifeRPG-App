package de.realliferpg.app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
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
                apiHelper.getServers();
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
    public void onResponse(JSONObject response, Class type) {
        SwipeRefreshLayout sc = view.findViewById(R.id.sc_main);

        if (type.equals(Server.Wrapper.class)) {
            Gson gson = new Gson();

            Server.Wrapper value = gson.fromJson(response.toString(), Server.Wrapper.class);

            final ArrayList<Server> servers = new ArrayList<>(Arrays.asList(value.data));

            ServerListAdapter adapter = new ServerListAdapter(view.getContext(), servers);

            ListView listView = view.findViewById(R.id.lv_main_serverList);
            listView.setAdapter(adapter);
            sc.setRefreshing(false);

            final ListView lv = view.findViewById(R.id.lv_main_serverList);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                    String selectedFromList = (String) lv.getItemAtPosition(myItemInt).toString();
                    Log.d("test", Arrays.toString(servers.get(myItemInt).Players));

                    AlertDialog ad = new AlertDialog.Builder(view.getContext()).create();
                    ad.setCancelable(false); // This blocks the 'BACK' button

                    String players = "";

                    for(String player: servers.get(myItemInt).Players)
                    {
                        players = players + "\n" + player;
                    }
                    
                    ad.setMessage(players);
                    ad.setButton("schließen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
            });
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
