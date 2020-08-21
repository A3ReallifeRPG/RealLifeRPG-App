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
import de.realliferpg.app.helper.FractionMappingHelper;
import de.realliferpg.app.interfaces.VehicleEnum;
import de.realliferpg.app.objects.Vehicle;
import de.realliferpg.app.objects.VehicleGroup;

public class VehiclesListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private VehicleGroup[] vehiclesByType;
    private int copLevel;

    public VehiclesListAdapter(Context _context, VehicleGroup[] _vehiclesByType, int _copLevel){
        this.context = _context;
        this.vehiclesByType = _vehiclesByType;
        this.copLevel = _copLevel;
    }

    @Override
    public int getGroupCount() {
        return this.vehiclesByType.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.vehiclesByType[groupPosition].vehicles.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.vehiclesByType[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) { return this.vehiclesByType[groupPosition].vehicles.get(childPosition); }

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
            convertView = inflater.inflate(R.layout.list_item_group_vehiclelist,null);

            viewHolder = new ViewHolder();
            viewHolder.position = groupPosition;

            viewHolder.tvVehicleCategory = convertView.findViewById(R.id.tv_vehicles_category);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;
        viewHolder.tvVehicleCategory.setText(this.getTypeOfVehicle(vehiclesByType[groupPosition].type));

        convertView.setTag(viewHolder);

        return convertView;

    }

    private String getTypeOfVehicle(VehicleEnum type) {
        switch (type.name().toLowerCase()){
            case "car":
                return "Kraftfahrzeug";
            case "air":
                return "Flugzeug";
            case "ship":
                return "Wasserfahrzeug";
        }
        return "Amphibienfahrzeug";
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderChild viewHolderChild;
        
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_vehicleslist, null);

            viewHolderChild = new ViewHolderChild();
            viewHolderChild.position = childPosition;

            viewHolderChild.tvVehicleName = convertView.findViewById(R.id.tv_vehicle_name);
            viewHolderChild.ivVehiclesFraction = convertView.findViewById(R.id.iv_vehicle_fraction);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        viewHolderChild.position = childPosition;

        Vehicle vehicle = this.vehiclesByType[groupPosition].vehicles.get(childPosition);

        int imageFraction = FractionMappingHelper.getImageResourceFromEnum(this.context, (FractionMappingHelper.getFractionFromSide(vehicle.side, copLevel)));

        if (imageFraction != -1)
            viewHolderChild.ivVehiclesFraction.setImageResource(imageFraction);

        String vehicleName = vehicle.vehicle_data.name;

        if (vehicle.alive == 0) // verkauft == 0
        {
            viewHolderChild.tvVehicleName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            vehicleName += " - verkauft";
        }
        else if (vehicle.impound == 1) // beschlagnahmt == 1
        {
            viewHolderChild.tvVehicleName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            vehicleName += " - beschlagnahmt";
        } else if (vehicle.disabled == 1) // zerstört == 1
        {
            viewHolderChild.tvVehicleName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            vehicleName += " - zerstört";
        }

        viewHolderChild.tvVehicleName.setText(vehicleName);

        convertView.setTag(viewHolderChild);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView tvVehicleCategory;
        int position;
    }

    static class ViewHolderChild {
        TextView tvVehicleName;
        ImageView ivVehiclesFraction;
        int position;
    }
}
