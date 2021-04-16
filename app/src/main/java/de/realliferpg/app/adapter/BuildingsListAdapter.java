package de.realliferpg.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.interfaces.BuildingEnum;
import de.realliferpg.app.interfaces.IBuilding;
import de.realliferpg.app.objects.BuildingGroup;

public class BuildingsListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private BuildingGroup[] buildingByType;

    public BuildingsListAdapter(Context _context, BuildingGroup[] _buildingByType){
        this.context = _context;
        this.buildingByType = _buildingByType;
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
    public Object getChild(int groupPosition, int childPosition) { return this.buildingByType[groupPosition].buildings[childPosition]; }

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

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_group_buildingslist,null);

            viewHolder = new ViewHolder();
            viewHolder.position = groupPosition;

            viewHolder.tvBuildingCategory = convertView.findViewById(R.id.tv_buildings_category);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;
        viewHolder.tvBuildingCategory.setText(this.getTypeOfBuilding(buildingByType[groupPosition].type));

        convertView.setTag(viewHolder);

        return convertView;
    }

    private String getTypeOfBuilding(BuildingEnum type) {
        switch (type.name().toLowerCase()){
            case "house":
                return "Haus"; // TODO Ressource str...
            case "building":
                return "Gebäude"; // TODO Ressource str...
            case "rental":
                return "Appartment"; // TODO Ressource str...
        }
        return "Anderes";
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderChild viewHolderChild;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_buildingslist,null);

            viewHolderChild = new ViewHolderChild();
            viewHolderChild.position = childPosition;

            viewHolderChild.tvBezeichnung = convertView.findViewById(R.id.tv_building_name);
            viewHolderChild.tvBezahlteTage = convertView.findViewById(R.id.tv_building_played_for);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        viewHolderChild.position = childPosition;

        IBuilding building = this.buildingByType[groupPosition].buildings[childPosition];
        String buildingName = "ID: " +Integer.toString(building.getId());

        if (building.getDisabled() != 0) // 0 heißt aktiv
        {
            viewHolderChild.tvBezeichnung.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            buildingName += " - inaktiv"; // TODO Ressource str...
        }

        viewHolderChild.tvBezeichnung.setText(buildingName);
        viewHolderChild.tvBezahlteTage.setText(context.getString(R.string.str_buildingsList_buildingPayedFor).replace("{0}", Integer.toString(building.getPayedForDays())));

        convertView.setTag(viewHolderChild);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView tvBuildingCategory;
        int position;
    }

    static class ViewHolderChild {
        TextView tvBezeichnung;
        TextView tvBezahlteTage;
        int position;
    }
}
