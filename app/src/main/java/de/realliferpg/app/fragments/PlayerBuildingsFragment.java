package de.realliferpg.app.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.BuildingsListAdapter;
import de.realliferpg.app.helper.PreferenceHelper;
import de.realliferpg.app.helper.ReminderBroadcastReceiver;
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
    private BuildingGroup[] buildingByType;

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
/*
        Calendar dateCurrent = Calendar.getInstance(); // 19.06.2021
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.set(2021,06,18); // 4
        date2.set(2021,06,19); // 5

        long diff1 = date1.getTimeInMillis() - dateCurrent.getTimeInMillis();
        long diff2 = date2.getTimeInMillis() - dateCurrent.getTimeInMillis();

        float dayCount1 = (float) diff1 / (24 * 60 * 60 * 1000);
        float dayCount2 = (float) diff2 / (24 * 60 * 60 * 1000);

        House hausEins = new House();
        hausEins.players = new String[]{"Spieler 1", "Spieler 2"};
        hausEins.id = 11;
        hausEins.payed_for = (int)dayCount1*24;
        House hausZwei = new House();
        hausZwei.players = new String[]{"Spieler 1", "Spieler 2"};
        hausZwei.id = 12;
        hausZwei.payed_for = (int)dayCount2*24;
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

        playerInfo.houses = houses;
        playerInfo.buildings = buildings;
        playerInfo.rentals = rentals;
*/
        // -----------------------------

        buildingByType = new BuildingGroup[3];

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

        Button btnReminder = view.findViewById(R.id.btn_reminder);

        if ((playerInfo.houses == null || playerInfo.houses.length == 0) && (playerInfo.buildings == null || playerInfo.buildings.length == 0) && (playerInfo.rentals == null || playerInfo.rentals.length == 0)){
            tvKeineDaten.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.INVISIBLE);
            btnReminder.setVisibility(View.INVISIBLE);
        } else {
            tvKeineDaten.setVisibility(View.INVISIBLE);
            expandableListView.setVisibility(View.VISIBLE);
            btnReminder.setVisibility(View.VISIBLE);
        }

        btnReminder.setOnClickListener(v -> {
            int daysBefore = 3*24;
            long factorHourToSec = 60 * 60;

            Intent intent = new Intent(this.getActivity(), ReminderBroadcastReceiver.class);
            AlarmManager am = (AlarmManager) this.getContext().getSystemService(Context.ALARM_SERVICE);

            IBuilding chosenBuilding = null;

            // das Haus/Appartment finden, das am nächsten an drei Tagen dran ist
            for (House house : playerInfo.houses){
                long plannedTimeInSeconds =  (house.getPayedForHours() - daysBefore) * factorHourToSec;
                if (plannedTimeInSeconds > 0) { // <= heißt negative Werte, wenn weniger als 3 Tage; wir wollen aber die anderen
                    if (chosenBuilding == null){
                        chosenBuilding = house;
                    }
                    else if (chosenBuilding.getPayedForHours() > house.getPayedForHours()){
                        chosenBuilding = house;
                    }
                }
            }

            for (Rental rental : playerInfo.rentals){
                long plannedTimeInSeconds =  (rental.getPayedForHours() - daysBefore) * factorHourToSec;
                if (plannedTimeInSeconds > 0) { // <= heißt negative Werte, wenn weniger als 3 Tage; wir wollen aber die anderen
                    if (chosenBuilding == null){
                        chosenBuilding = rental;
                    }
                    else if (chosenBuilding.getPayedForHours() > rental.getPayedForHours()){
                        chosenBuilding = rental;
                    }
                }
            }

            // mit der ID eine Erinnerung setzen auf 15 Uhr
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getActivity(), chosenBuilding.getId(), intent, 0);
            Calendar cFuture = Calendar.getInstance();
            cFuture.add(Calendar.HOUR, (int) chosenBuilding.getPayedForHours()-(daysBefore+1));
            cFuture.set(Calendar.HOUR_OF_DAY, 19);
            cFuture.set(Calendar.MINUTE, 0);

            am.set(AlarmManager.RTC_WAKEUP, cFuture.getTimeInMillis(), pendingIntent);

            // Info an Anwender
            String text = "Erinnerung für Gebäude mit ID {0} gesetzt für ";
            text = text.replace("{0}", Integer.toString(chosenBuilding.getId()));
            text += cFuture.get(Calendar.DAY_OF_MONTH) + "." + ((int)cFuture.get(Calendar.MONTH)+1) + "." + cFuture.get(Calendar.YEAR) + " 19:00 Uhr";
            Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
        });
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
