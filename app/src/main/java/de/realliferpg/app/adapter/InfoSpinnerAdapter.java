package de.realliferpg.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.objects.Shop;

public class InfoSpinnerAdapter extends ArrayAdapter<Shop> {
    Context context;

    public InfoSpinnerAdapter(Context context, ArrayList<Shop> shops) {
        super(context, 0, shops);
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final InfoSpinnerAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_simple_spinner, parent, false);

            viewHolder = new InfoSpinnerAdapter.ViewHolder();
            viewHolder.position = position;

            viewHolder.tvHead = convertView.findViewById(R.id.tv_list_spinner_text);

        } else {
            viewHolder = (InfoSpinnerAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.position = position;

        Shop shop = getItem(position);

        viewHolder.tvHead.setText(shop.shopname);

        convertView.setTag(viewHolder);

        return convertView;
    }

    static class ViewHolder {
        TextView tvHead;
        int position;
    }

}