package de.realliferpg.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.Calendar;

import de.realliferpg.app.R;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.adapter.BuildingsListAdapter;
import de.realliferpg.app.helper.PreferenceHelper;
import de.realliferpg.app.interfaces.BuildingEnum;
import de.realliferpg.app.interfaces.FragmentInteractionInterface;
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

        Building[] buildingsWithoutStageMinusOne = buildings.clone();
        int counter = 0;
        for (Building b : buildings) {
            if (b.stage < 0){
                counter++;
                buildingsWithoutStageMinusOne = ArrayUtils.removeAll(buildings, b);
            }
        }
        if (counter == buildings.length){
            buildings = new Building[0];
        } else {
            buildings = buildingsWithoutStageMinusOne.clone();
        }

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
            int daysLeft = 100;

            // Minimum an verbleibenden Tagen finden
            for (House house : houses) {
                if (house.getPayedForDays() < daysLeft)
                {
                    daysLeft = house.getPayedForDays();
                }
            }

            // Davon prefHelper.getDaysForReminderMaintenance Tage abziehen (wenn nicht schon kleiner gleich 5)
            int prefDaysForReminder = Integer.valueOf(prefHelper.getDaysForReminderMaintenance());

            if (daysLeft >= prefDaysForReminder) {
                daysLeft = daysLeft - prefDaysForReminder;
            }

            // Kalenderevent von heute + daysLeft erzeugen
            Calendar calendarEvent = Calendar.getInstance();
            calendarEvent.add(Calendar.DAY_OF_YEAR, daysLeft);

            // Kalender-App aufrufen
            Intent i = new Intent(Intent.ACTION_EDIT);
            i.setType("vnd.android.cursor.item/event");
            i.putExtra("beginTime", calendarEvent.getTimeInMillis());
            i.putExtra("allDay", true);
            i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
                        i.putExtra("title", getResources().getString(R.string.str_notifications_reminder_maintenance_title));
            startActivity(i);
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
