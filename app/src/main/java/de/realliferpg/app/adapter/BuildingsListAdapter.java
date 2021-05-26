package de.realliferpg.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.interfaces.BuildingEnum;
import de.realliferpg.app.interfaces.IBuilding;
import de.realliferpg.app.objects.Building;
import de.realliferpg.app.objects.BuildingGroup;

public class BuildingsListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private BuildingGroup[] buildingByType;
    private int daysMaintenance;

    public BuildingsListAdapter(Context _context, BuildingGroup[] _buildingByType, int _daysMaintenance) {
        this.context = _context;
        this.buildingByType = _buildingByType;
        this.daysMaintenance = _daysMaintenance;
    }

    @Override
    public int getGroupCount() {
        int length = 0;

        if (this.buildingByType != null)
            length = this.buildingByType.length;

        return length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;

        if (this.buildingByType != null && this.buildingByType[groupPosition].buildings != null)
            count = this.buildingByType[groupPosition].buildings.length;

        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.buildingByType[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.buildingByType[groupPosition].buildings[childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_group_buildingslist, null);

            viewHolder = new ViewHolder();
            viewHolder.position = groupPosition;

            viewHolder.tvBuildingCategory = convertView.findViewById(R.id.tv_buildings_category);
            viewHolder.ivBuildingGroupWarning = convertView.findViewById(R.id.iv_buildings_group_warning);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;
        viewHolder.tvBuildingCategory.setText(this.getTypeOfBuilding(buildingByType[groupPosition].type));
        if (buildingByType[groupPosition].type != BuildingEnum.BUILDING) {
            viewHolder.ivBuildingGroupWarning.setVisibility(getVisibilityForGroupWarning(buildingByType[groupPosition].buildings));
        }
        else {
            viewHolder.ivBuildingGroupWarning.setVisibility(View.INVISIBLE);
        }

        convertView.setTag(viewHolder);

        return convertView;
    }

    private String getTypeOfBuilding(BuildingEnum type) {
        switch (type.name().toLowerCase()) {
            case "house":
                return context.getResources().getString(R.string.str_house);
            case "building":
                return context.getResources().getString(R.string.str_building);
            case "rental":
                return context.getResources().getString(R.string.str_rental);
        }
        return null;
    }

    private int getVisibilityForGroupWarning(IBuilding[] buildingGroup) {
        for (IBuilding building : buildingGroup) {
            boolean showWarning = building.getPayedForDays() <= daysMaintenance;
            if (showWarning) {
                return View.VISIBLE;
            }
        }
        return View.INVISIBLE;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderChild viewHolderChild;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_buildingslist, null);

            viewHolderChild = new ViewHolderChild();
            viewHolderChild.position = childPosition;

            viewHolderChild.tvBezeichnung = convertView.findViewById(R.id.tv_building_name);
            viewHolderChild.tvBezahlteTage = convertView.findViewById(R.id.tv_building_played_for);
            viewHolderChild.tvMitglieder = convertView.findViewById(R.id.tv_building_players);
            viewHolderChild.ivListItemWarning = convertView.findViewById(R.id.iv_buildings_list_item_warning);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        viewHolderChild.position = childPosition;

        IBuilding building = this.buildingByType[groupPosition].buildings[childPosition];
        String buildingName = "ID: " + Integer.toString(building.getId());

        if (building.getDisabled() != 0) // 0 heißt aktiv
        {
            viewHolderChild.tvBezeichnung.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            buildingName += " " + context.getResources().getString(R.string.str_inactive);
        }

        viewHolderChild.tvBezeichnung.setText(buildingName);
        viewHolderChild.tvMitglieder.setText(getPlayersWithKey(building.getPlayers()));
        viewHolderChild.ivListItemWarning.setVisibility(building.getPayedForDays() <= daysMaintenance ? View.VISIBLE : View.INVISIBLE);

        if (buildingByType[groupPosition].type != BuildingEnum.BUILDING) {
            viewHolderChild.tvBezahlteTage.setText(context.getString(R.string.str_buildingsList_buildingPayedFor).replace("{0}", Integer.toString(building.getPayedForDays())));
            viewHolderChild.tvBezahlteTage.setVisibility(View.VISIBLE);
            viewHolderChild.ivListItemWarning.setVisibility(building.getPayedForDays() <= daysMaintenance ? View.VISIBLE : View.INVISIBLE);
        }
        else {
            viewHolderChild.ivListItemWarning.setVisibility(View.INVISIBLE);
            Building buildingCast = (Building)building;
            viewHolderChild.tvBezahlteTage.setText("Stage: " + buildingCast.stage);
        }

        convertView.setTag(viewHolderChild);

        return convertView;
    }

    private String getPlayersWithKey(String[] players) {
        if (players == null || players.length == 0)
            return context.getResources().getString(R.string.str_no_person_with_key);

        String playersWithKey = context.getResources().getString(R.string.str_persons_with_key) + " ";
        for (String name : players) {
            playersWithKey += name + ", ";
        }

        return playersWithKey.substring(0, playersWithKey.length()-2);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView tvBuildingCategory;
        ImageView ivBuildingGroupWarning;
        int position;
    }

    static class ViewHolderChild {
        TextView tvBezeichnung;
        TextView tvBezahlteTage;
        TextView tvMitglieder;
        ImageView ivListItemWarning;
        int position;
    }
}
