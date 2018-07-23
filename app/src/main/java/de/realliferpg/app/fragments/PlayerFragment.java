package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.realliferpg.app.Constants;
import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.PlayerInfo;

public class PlayerFragment extends Fragment implements RequestCallbackInterface {

    private View view;
    private OnFragmentInteractionListener mListener;

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

        mListener.onFragmentInteraction(Uri.parse("fragment_player_change_to_stats"));

        if(Singleton.getInstance().getPlayerInfo() == null){
            ApiHelper apiHelper = new ApiHelper(this);
            apiHelper.getPlayerStats();
        }else{
            showPlayerInfo();
        }

        BottomNavigationView bnv = view.findViewById(R.id.bnv_player);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bnv_player_stats:
                        mListener.onFragmentInteraction(Uri.parse("fragment_player_change_to_stats"));
                        break;
                    case R.id.bnv_player_donation:
                        mListener.onFragmentInteraction(Uri.parse("fragment_player_change_to_donation"));
                        break;
                }
                return true;
            }
        });

        return view;
    }

    public void showPlayerInfo(){
        PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

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
        if (type.equals(PlayerInfo.Wrapper.class)) {
            Gson gson = new Gson();
            FormatHelper formatHelper = new FormatHelper();

            PlayerInfo.Wrapper value = gson.fromJson(response.toString(), PlayerInfo.Wrapper.class);

            PlayerInfo playerInfo = value.data[0];

            Singleton.getInstance().setPlayerInfo(playerInfo);
            mListener.onFragmentInteraction(Uri.parse("update_login_state"));

            showPlayerInfo();
        } else if (type.equals(CustomNetworkError.class)) {
            CustomNetworkError error = (CustomNetworkError) response;
            Snackbar snackbar = Snackbar.make(view.findViewById(R.id.cl_main), error.toString(), Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
