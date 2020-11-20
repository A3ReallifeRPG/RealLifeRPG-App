package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.BuildingsListAdapter;
import de.realliferpg.app.adapter.VehiclesListAdapter;
import de.realliferpg.app.helper.ApiHelper;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.Building;
import de.realliferpg.app.objects.House;
import de.realliferpg.app.objects.PlayerInfo;

public class PlayerVehiclesFragment extends Fragment {

    private View view;
    private FragmentInteractionInterface mListener;

    public PlayerVehiclesFragment() {
        // Required empty public constructor
    }

    public static PlayerVehiclesFragment newInstance() {
        return new PlayerVehiclesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player_vehicles, container, false);

        if(Singleton.getInstance().getPlayerInfo() == null){
            Singleton.getInstance().setErrorMsg("PlayerVehiclesFragment Error Code #1");
            mListener.onFragmentInteraction(PlayerVehiclesFragment.class, Uri.parse("open_error"));
        }else {
            showPlayerInfo();
        }

        return view;
    }

    public void showPlayerInfo(){
        final ExpandableListView expandableListView = view.findViewById(R.id.elv_vehicles);
        final TextView tvKeineDaten = view.findViewById(R.id.tvKeineDatenVehicle);

        PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

        if (playerInfo.vehiclesByType == null)
        {
            Log.e("Fehler", "No vehicles");
            final ApiHelper apiHelper = new ApiHelper((RequestCallbackInterface) getActivity());
            apiHelper.getPlayerVehicles();
        }

        VehiclesListAdapter vehiclesListAdapter = new VehiclesListAdapter(this.getContext(), playerInfo.vehiclesByType, Integer.parseInt(Singleton.getInstance().getPlayerInfo().coplevel));
        expandableListView.setAdapter(vehiclesListAdapter);

        if (playerInfo.vehiclesByType == null){
            tvKeineDaten.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.INVISIBLE);
        } else {
            tvKeineDaten.setVisibility(View.INVISIBLE);
            expandableListView.setVisibility(View.VISIBLE);
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
