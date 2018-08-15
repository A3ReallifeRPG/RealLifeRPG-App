package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.realliferpg.app.BuildConfig;
import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.helper.PreferenceHelper;
import de.realliferpg.app.interfaces.CallbackNotifyInterface;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.PlayerInfo;

public class PlayerFragment extends Fragment implements CallbackNotifyInterface {

    private View view;
    private FragmentInteractionInterface mListener;

    public PlayerFragment() {
        // Required empty public constructor
    }

    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
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
        view = inflater.inflate(R.layout.fragment_player, container, false);

        final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
        if (Singleton.getInstance().getPlayerInfo() == null) {
            apiHelper.getPlayerStats();
        } else {
            showPlayerInfo();
        }

        BottomNavigationView bnv = view.findViewById(R.id.bnv_player);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bnv_player_stats:
                        mListener.onFragmentInteraction(PlayerFragment.class, Uri.parse("fragment_player_change_to_stats"));
                        break;
                    case R.id.bnv_player_donation:
                        mListener.onFragmentInteraction(PlayerFragment.class, Uri.parse("fragment_player_change_to_donation"));
                        break;
                }
                return true;
            }
        });

        SwipeRefreshLayout sc = view.findViewById(R.id.srl_info);
        sc.setColorSchemeColors(view.getResources().getColor(R.color.primaryColor));
        sc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiHelper.getPlayerStats();
            }
        });

        return view;
    }

    public void showPlayerInfo() {
        PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

        mListener.onFragmentInteraction(PlayerFragment.class, Uri.parse("fragment_player_change_to_stats"));

        BottomNavigationView bnv = view.findViewById(R.id.bnv_player);
        bnv.setSelectedItemId(R.id.bnv_player_stats);

        ImageView ivProfilePic = view.findViewById(R.id.iv_player_profile);

        Picasso.get().load(playerInfo.avatar_full).into(ivProfilePic);

        TextView tvName = view.findViewById(R.id.tv_player_name);
        TextView tvPid = view.findViewById(R.id.tv_player_pid);

        tvName.setText(playerInfo.name);
        tvPid.setText(playerInfo.pid);
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

        switch (type){
            case PLAYER:
                mListener.onFragmentInteraction(PlayerFragment.class, Uri.parse("update_login_state"));

                SwipeRefreshLayout sc = view.findViewById(R.id.srl_info);
                sc.setRefreshing(false);

                showPlayerInfo();
                break;
            case NETWORK_ERROR:
                CustomNetworkError error = Singleton.getInstance().getNetworkError();

                Singleton.getInstance().setErrorMsg(error.toString());
                mListener.onFragmentInteraction(PlayerFragment.class, Uri.parse("open_error"));
                break;
        }

    }

}
