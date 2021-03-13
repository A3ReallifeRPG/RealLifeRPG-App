package de.realliferpg.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.objects.Building;
import de.realliferpg.app.objects.House;
import de.realliferpg.app.objects.Rental;

public class BuildingsListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private House[] houses;
    private Building[] buildings; // eher uninteressant bisher, daher nicht angezeigt
    private Rental[] rentals;

    public BuildingsListAdapter(Context _context, House[] _houses, Building[] _buildings, Rental[] _rentals){
        this.context = _context;
        this.houses = _houses;
        this.buildings = _buildings;
        this.rentals = _rentals;
    }

    @Override
    public int getGroupCount() {
        int length = 0;

        if (this.houses != null)
            length = this.houses.length;

        return length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.houses[groupPosition].players.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.houses[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) { return this.houses[groupPosition].players[childPosition]; }

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

            viewHolder.tvBezeichnung = convertView.findViewById(R.id.tv_buildings_name);
            viewHolder.tvBezahlteTage = convertView.findViewById(R.id.tv_buildings_played_for);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;

        viewHolder.tvBezeichnung.setText("ID: " + houses[groupPosition].id);
        viewHolder.tvBezahlteTage.setText("Gewartet für: " + (int) houses[groupPosition].payed_for / 24 + " Tage");

        convertView.setTag(viewHolder);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition,childPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_buildingslist,null);
        }

        TextView text_buildings_players = convertView.findViewById(R.id.tv_buildings_players);
        text_buildings_players.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView tvBezeichnung;
        TextView tvBezahlteTage;
        int position;
    }
}
