package de.realliferpg.app.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Format;
import java.util.Formatter;
import java.util.Locale;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FormatHelper;
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
        int length = 0;

        if (this.vehiclesByType != null)
            length = this.vehiclesByType.length;

        return length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;

        if (this.vehiclesByType != null && this.vehiclesByType[groupPosition].vehicles != null)
            count = this.vehiclesByType[groupPosition].vehicles.size();

        return count;
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
            viewHolderChild.tvVehiclePlate = convertView.findViewById(R.id.tv_vehicle_plate);
            viewHolderChild.tvVehicleKM = convertView.findViewById(R.id.tv_vehicle_km);
            viewHolderChild.tvLastGarage = convertView.findViewById(R.id.tv_vehicle_lastgarage);
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
            vehicleName += " - " + context.getString(R.string.str_veh_sold);
        } else if (vehicle.impound == 1) // beschlagnahmt == 1
        {
            viewHolderChild.tvVehicleName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            vehicleName += " - " + context.getString(R.string.str_veh_impound);
        } else if (vehicle.disabled == 1) // zerstört == 1
        {
            viewHolderChild.tvVehicleName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            vehicleName += " - " + context.getString(R.string.str_veh_disabled);
        } else if (vehicle.alive == 1 && vehicle.impound == 0 && vehicle.disabled == 0) {
            viewHolderChild.tvVehicleName.setPaintFlags(0);
        }

        FormatHelper formatHelper = new FormatHelper();

        viewHolderChild.tvVehicleName.setText(vehicleName);
        viewHolderChild.tvVehiclePlate.setText(context.getString(R.string.str_plate) + " " + formatHelper.formatPlate(vehicle.plate));
        viewHolderChild.tvVehicleKM.setText(context.getString(R.string.str_mileage) + " " + formatHelper.formatKilometer(vehicle.kilometer_total) + " km");
        viewHolderChild.tvLastGarage.setText(context.getString(R.string.str_last_garage) + " " + getNameGarage(vehicle.lastgarage));

        convertView.setTag(viewHolderChild);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private String getNameGarage(String lastGarage){
        if (lastGarage.toLowerCase().equals("unknown")){
            lastGarage = "garage_unknown";
        }

        if (lastGarage.toLowerCase().startsWith("custom_garage")){
            lastGarage = "garage_selfbuild";
        }

        String strLastGarage = new StringBuilder().append("str_").append(lastGarage).toString();
        Resources resources = context.getResources();
        int resourceId = 0;
        try {
            resourceId = resources.getIdentifier(strLastGarage, "string", context.getPackageName());
        } catch (Exception e) {
            // Fehlermeldung ist hier nicht sinnig, es wird einfach "Garage: unbekannt" angezeigt
            resourceId = resources.getIdentifier("str_garage_unknown", "string", context.getPackageName());;
        }

        return resources.getString(resourceId);
    }

    static class ViewHolder {
        TextView tvVehicleCategory;
        int position;
    }

    static class ViewHolderChild {
        TextView tvVehicleName;
        ImageView ivVehiclesFraction;
        TextView tvVehiclePlate;
        TextView tvVehicleKM;
        TextView tvLastGarage;
        int position;
    }
}
