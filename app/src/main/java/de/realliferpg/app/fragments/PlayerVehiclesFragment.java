package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.BuildingsListAdapter;
import de.realliferpg.app.adapter.VehiclesListAdapter;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
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
        ExpandableListView expandableListView = view.findViewById(R.id.elv_vehicles);

        PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

        VehiclesListAdapter vehiclesListAdapter = new VehiclesListAdapter(this.getContext(), playerInfo.vehiclesByType, Integer.parseInt(Singleton.getInstance().getPlayerInfo().coplevel));
        expandableListView.setAdapter(vehiclesListAdapter);

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
