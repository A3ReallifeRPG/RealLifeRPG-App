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
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.objects.Building;
import de.realliferpg.app.objects.House;
import de.realliferpg.app.objects.PlayerInfo;

public class PlayerBuildingsFragment extends Fragment {

    private View view;
    private FragmentInteractionInterface mListener;

    public PlayerBuildingsFragment() {
        // Required empty public constructor
    }

    public static PlayerBuildingsFragment newInstance() {
        return new PlayerBuildingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_player_buildings, container, false);

        if(Singleton.getInstance().getPlayerInfo() == null){
            Singleton.getInstance().setErrorMsg("PlayerDonationFragment Error Code #1");
            mListener.onFragmentInteraction(PlayerBuildingsFragment.class, Uri.parse("open_error"));
        }else {
            showPlayerInfo();
        }

        return view;
    }

    public void showPlayerInfo(){
        // TODO: Gebäude anzeigen
        ExpandableListView expandableListView = view.findViewById(R.id.elv_buildings);

        PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

        House[] houses = playerInfo.houses;
        Building[] buildings = playerInfo.buildings;

        // - DummyDaten -----------------------------
        /*
        House hausEins = new House();
        hausEins.players = new String[]{"Spieler 1", "Spieler 2"};
        hausEins.id = 1;
        hausEins.payed_for = 30;
        House hausZwei = new House();
        hausZwei.players = new String[]{"Spieler 1", "Spieler 2"};
        hausZwei.id = 2;
        hausZwei.payed_for = 30;
        House hausDrei = new House();
        hausDrei.players = new String[]{"Spieler 1", "Spieler 2"};
        hausDrei.id = 3;
        hausDrei.payed_for = 30;
        House hausVier = new House();
        hausVier.players = new String[]{"Spieler 1", "Spieler 2"};
        hausVier.id = 4;
        hausVier.payed_for = 30;
        House hausFuenf = new House();
        hausFuenf.players = new String[]{"Spieler 1", "Spieler 2"};
        hausFuenf.id = 5;
        hausFuenf.payed_for = 30;
        House[] DummyDatenHaeuser = new House[]{hausEins, hausZwei, hausDrei, hausVier, hausFuenf};
        houses = DummyDatenHaeuser;
        */
        // -----------------------------

        BuildingsListAdapter buildingsListAdapter = new BuildingsListAdapter(this.getContext(), houses, buildings);
        expandableListView.setAdapter(buildingsListAdapter);

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
