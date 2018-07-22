package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
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
        if (type.equals(PlayerInfo.Wrapper.class)) {
            Gson gson = new Gson();
            FormatHelper formatHelper = new FormatHelper();

            PlayerInfo.Wrapper value = gson.fromJson(response.toString(), PlayerInfo.Wrapper.class);

            PlayerInfo playerInfo = value.data[0];

            Singleton.getInstance().setPlayerInfo(playerInfo);
            mListener.onFragmentInteraction(Uri.parse("update_login_state"));
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
