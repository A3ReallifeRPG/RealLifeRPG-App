package de.realliferpg.app.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.BuildingsListAdapter;
import de.realliferpg.app.helper.PreferenceHelper;
import de.realliferpg.app.interfaces.BuildingEnum;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
import de.realliferpg.app.interfaces.IBuilding;
import de.realliferpg.app.objects.Building;
import de.realliferpg.app.objects.BuildingGroup;
import de.realliferpg.app.objects.House;
import de.realliferpg.app.objects.PlayerInfo;
import de.realliferpg.app.objects.Rental;

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
            Singleton.getInstance().setErrorMsg("PlayerBuildingsFragment Error Code #1");
            mListener.onFragmentInteraction(PlayerBuildingsFragment.class, Uri.parse("open_error"));
        }else {
            showPlayerInfo();
        }

        return view;
    }

    public void showPlayerInfo(){
        final ExpandableListView expandableListView = view.findViewById(R.id.elv_buildings);
        final TextView tvKeineDaten = view.findViewById(R.id.tvKeineDatenBuildings);

        PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();

        House[] houses = playerInfo.houses;
        Building[] buildings = playerInfo.buildings;
        Rental[] rentals = playerInfo.rentals;

        // - DummyDaten -----------------------------

        House hausEins = new House();
        hausEins.players = new String[]{"Spieler 1", "Spieler 2"};
        hausEins.id = 11;
        hausEins.payed_for = 30*24;
        House hausZwei = new House();
        hausZwei.players = new String[]{"Spieler 1", "Spieler 2"};
        hausZwei.id = 12;
        hausZwei.payed_for = 3*24;
        House[] dummyHaeuser = new House[]{hausEins, hausZwei};
        houses = dummyHaeuser;

        Building buildingEins = new Building();
        buildingEins.id = 21;
        Building buildingZwei = new Building();
        buildingZwei.id = 22;
        Building[] dummyBuildings = new Building[]{buildingEins, buildingZwei};
        buildings = dummyBuildings;

        Rental rentalEins = new Rental();
        rentalEins.id = 31;
        rentalEins.payed_for = 123;
        Rental rentalZwei = new Rental();
        rentalZwei.id = 32;
        rentalZwei.payed_for = 1234;
        Rental[] dummyRentals = new Rental[]{rentalEins, rentalZwei};
        rentals = dummyRentals;

        // -----------------------------

        BuildingGroup[] buildingByType = new BuildingGroup[3];

        buildingByType[0] = new BuildingGroup();
        buildingByType[1] = new BuildingGroup();
        buildingByType[2] = new BuildingGroup();

        buildingByType[0].type = BuildingEnum.HOUSE;
        buildingByType[0].buildings = houses;
        buildingByType[1].type = BuildingEnum.BUILDING;
        buildingByType[1].buildings = buildings;
        buildingByType[2].type = BuildingEnum.RENTAL;
        buildingByType[2].buildings = rentals;

        PreferenceHelper prefHelper = new PreferenceHelper();
        BuildingsListAdapter buildingsListAdapter = new BuildingsListAdapter(this.getContext(), buildingByType, Integer.valueOf(prefHelper.getDaysForReminderMaintenance()));
        expandableListView.setAdapter(buildingsListAdapter);

        if ((playerInfo.houses == null || playerInfo.houses.length == 0) && (playerInfo.buildings == null || playerInfo.buildings.length == 0) && (playerInfo.rentals == null || playerInfo.rentals.length == 0)){
            tvKeineDaten.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.INVISIBLE);
        } else {
            tvKeineDaten.setVisibility(View.INVISIBLE);
            expandableListView.setVisibility(View.VISIBLE);
        }

        showReminder(buildingByType);
    }

    private void showReminder(BuildingGroup[] buildingsByType){
        PreferenceHelper prefHelper = new PreferenceHelper();
        int daysForReminderMaintenance = Integer.valueOf(prefHelper.getDaysForReminderMaintenance());

        for (BuildingGroup buildingGroup : buildingsByType){
            BuildingEnum groupName = buildingGroup.type;
            for (IBuilding building : buildingGroup.buildings){
                if (building.getPayedForDays() <= daysForReminderMaintenance && groupName != BuildingEnum.BUILDING){
                    int messageID = groupName == BuildingEnum.HOUSE ? R.string.str_maintainance_house : R.string.str_maintainance_rental;
                    String message = MessageFormat.format(getString(messageID), getBuildingTypeName(groupName), Integer.toString(building.getId()), building.getPayedForDays(), building.getPayedForHours());
                    Toast.makeText(this.getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private String getBuildingTypeName(BuildingEnum buildingEnum){
        int id = 0;
        switch (buildingEnum.toString().toUpperCase()){
            case "HOUSE":
                id = R.string.str_house;
                break;
            case "RENTAL":
                id = R.string.str_rental;
                break;
            case "BUILDING":
                id = R.string.str_building;
                break;
        }
        return getString(id);
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
