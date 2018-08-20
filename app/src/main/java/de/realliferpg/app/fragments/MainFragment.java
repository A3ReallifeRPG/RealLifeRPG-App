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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.ServerListAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.PlayerInfo;
import de.realliferpg.app.objects.Server;


public class MainFragment extends Fragment implements CallbackNotifyInterface {

    private FragmentInteractionInterface mListener;

    private View view;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_main, container, false);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
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
    public void onCallback(RequestTypeEnum type) {
        final ProgressBar pbPlayer = view.findViewById(R.id.pb_main_player);
        final ProgressBar pbServer = view.findViewById(R.id.pb_main_server);

        SwipeRefreshLayout sc = view.findViewById(R.id.srl_main);

        TextView tvPiName = view.findViewById(R.id.tv_main_playerInfo_name);
        TextView tvPiPID = view.findViewById(R.id.tv_main_playerInfo_pid);
        TextView tvPiGUID = view.findViewById(R.id.tv_main_playerInfo_guid);

        TextView tvPiInfoBank = view.findViewById(R.id.tv_main_playerInfo_bank);
        TextView tvPiInfoCash = view.findViewById(R.id.tv_main_playerInfo_cash);
        TextView tvPiInfoLevel = view.findViewById(R.id.tv_main_playerInfo_level);
        TextView tvPiInfoSkill = view.findViewById(R.id.tv_main_playerInfo_skill);

        switch (type){
            case PLAYER:
                FormatHelper formatHelper = new FormatHelper();

                PlayerInfo playerInfo = (PlayerInfo) Singleton.getInstance().getPlayerInfo();
                mListener.onFragmentInteraction(MainFragment.class, Uri.parse("update_login_state"));

                tvPiName.setText(playerInfo.name);
                tvPiPID.setText(playerInfo.pid);
                tvPiGUID.setText(playerInfo.guid);

                tvPiInfoBank.setText(formatHelper.formatCurrency(playerInfo.bankacc));
                tvPiInfoCash.setText(formatHelper.formatCurrency(playerInfo.cash));
                tvPiInfoLevel.setText(String.valueOf(playerInfo.level));
                tvPiInfoSkill.setText(String.valueOf(playerInfo.skillpoint));


                pbPlayer.setVisibility(View.GONE);

                Singleton.getInstance().setPlayerInfo(playerInfo);
                mListener.onFragmentInteraction(MainFragment.class, Uri.parse("update_login_state"));
                break;
            case SERVER:
                final ArrayList<Server> servers = Singleton.getInstance().getServerList();

                ServerListAdapter adapter = new ServerListAdapter(view.getContext(), servers);

                pbServer.setVisibility(View.GONE);

                final ListView listView = view.findViewById(R.id.lv_main_serverList);
                listView.setAdapter(adapter);
                sc.setRefreshing(false);
                break;
            case NETWORK_ERROR:
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                sc.setRefreshing(false);

                if (error.requestType == RequestTypeEnum.PLAYER) {
                    pbPlayer.setVisibility(View.GONE);

                    tvPiInfoBank.setText("?");
                    tvPiInfoCash.setText("?");
                    tvPiInfoLevel.setText("?");
                    tvPiInfoSkill.setText("?");

                } else if (error.requestType == RequestTypeEnum.SERVER) {
                    pbServer.setVisibility(View.GONE);
                }


                Singleton.getInstance().setErrorMsg(error.toString());
                Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_main), R.string.str_error_occurred, Constants.ERROR_SNACKBAR_DURATION);

                snackbar.setAction(R.string.str_view, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onFragmentInteraction(MainFragment.class, Uri.parse("open_error"));
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
