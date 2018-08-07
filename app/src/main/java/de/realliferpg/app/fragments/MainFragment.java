package de.realliferpg.app.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.ServerListAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.PlayerInfo;
import de.realliferpg.app.objects.Server;


public class MainFragment extends Fragment implements RequestCallbackInterface {

    private FragmentInteractionInterface mListener;

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

        final TextView tvPiName = view.findViewById(R.id.tv_main_playerInfo_name);
        final TextView tvPiPID = view.findViewById(R.id.tv_main_playerInfo_pid);
        final TextView tvPiGUID = view.findViewById(R.id.tv_main_playerInfo_guid);
        tvPiName.setText("");
        tvPiPID.setText("");
        tvPiGUID.setText("");

        final ProgressBar pbServer = view.findViewById(R.id.pb_main_server);
        final ProgressBar pbPlayer = view.findViewById(R.id.pb_main_player);
        pbPlayer.setVisibility(View.VISIBLE);
        pbServer.setVisibility(View.VISIBLE);

        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getServers();
                apiHelper.getPlayerStats();

                pbPlayer.setVisibility(View.VISIBLE);
                pbServer.setVisibility(View.VISIBLE);

                tvPiName.setText("");
                tvPiPID.setText("");
                tvPiGUID.setText("");

                final ListView listView = view.findViewById(R.id.lv_main_serverList);
                listView.setAdapter(null);
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
    public void onResponse(Object response, Class type) {
        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main);

        TextView tvPiName = view.findViewById(R.id.tv_main_playerInfo_name);
        TextView tvPiPID = view.findViewById(R.id.tv_main_playerInfo_pid);
        TextView tvPiGUID = view.findViewById(R.id.tv_main_playerInfo_guid);

        TextView tvPiInfoBank = view.findViewById(R.id.tv_main_playerInfo_bank);
        TextView tvPiInfoCash = view.findViewById(R.id.tv_main_playerInfo_cash);
        TextView tvPiInfoLevel = view.findViewById(R.id.tv_main_playerInfo_level);
        TextView tvPiInfoSkill = view.findViewById(R.id.tv_main_playerInfo_skill);

        if (type.equals(Server.Wrapper.class)) {
            Gson gson = new Gson();

            Server.Wrapper value = gson.fromJson(response.toString(), Server.Wrapper.class);

            final ArrayList<Server> servers = new ArrayList<>(Arrays.asList(value.data));

            ServerListAdapter adapter = new ServerListAdapter(view.getContext(), servers);

            final ProgressBar pbServer = view.findViewById(R.id.pb_main_server);
            pbServer.setVisibility(View.GONE);

            final ListView listView = view.findViewById(R.id.lv_main_serverList);
            listView.setAdapter(adapter);
            sc.setRefreshing(false);
        }else if (type.equals(PlayerInfo.Wrapper.class)) {
            Gson gson = new Gson();
            FormatHelper formatHelper = new FormatHelper();

            PlayerInfo.Wrapper value = gson.fromJson(response.toString(), PlayerInfo.Wrapper.class);

            PlayerInfo playerInfo = value.data[0];

            Singleton.getInstance().setPlayerInfo(playerInfo);
            mListener.onFragmentInteraction(MainFragment.class,Uri.parse("update_login_state"));

            tvPiName.setText(playerInfo.name);
            tvPiPID.setText(playerInfo.pid);
            tvPiGUID.setText(playerInfo.guid);

            tvPiInfoBank.setText(formatHelper.formatCurrency(playerInfo.bankacc));
            tvPiInfoCash.setText(formatHelper.formatCurrency(playerInfo.cash));
            tvPiInfoLevel.setText(String.valueOf( playerInfo.level));
            tvPiInfoSkill.setText(String.valueOf( playerInfo.skillpoint));

            final ProgressBar pbPlayer = view.findViewById(R.id.pb_main_player);
            pbPlayer.setVisibility(View.GONE);

            Singleton.getInstance().setPlayerInfo(playerInfo);
            mListener.onFragmentInteraction(MainFragment.class,Uri.parse("update_login_state"));
        }else if (type.equals(CustomNetworkError.class)){
            CustomNetworkError error = (CustomNetworkError) response;

            sc.setRefreshing(false);

            if(error.requestReturnClass.equals(PlayerInfo.Wrapper.class)){
                final ProgressBar pbPlayer = view.findViewById(R.id.pb_main_player);
                pbPlayer.setVisibility(View.GONE);

                tvPiInfoBank.setText("?");
                tvPiInfoCash.setText("?");
                tvPiInfoLevel.setText("?");
                tvPiInfoSkill.setText("?");

            }else if(error.requestReturnClass.equals(Server.Wrapper.class)){
                final ProgressBar pbServer = view.findViewById(R.id.pb_main_server);
                pbServer.setVisibility(View.GONE);
            }


            Singleton.getInstance().setErrorMsg(error.toString());
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_main), R.string.str_error_occurred, Constants.ERROR_SNACKBAR_DURATION);

            snackbar.setAction(R.string.str_view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFragmentInteraction(MainFragment.class,Uri.parse("open_error"));
                }
            });

            snackbar.show();
            Singleton.getInstance().setCurrentSnackbar(snackbar);
        }
    }

}
