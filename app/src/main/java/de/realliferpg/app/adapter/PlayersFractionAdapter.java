package de.realliferpg.app.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FractionMappingHelper;
import de.realliferpg.app.objects.PlayerInfo;


public class PlayersFractionAdapter implements ListAdapter {

    private Context context;
    private PlayerInfo playerInfo;

    public PlayersFractionAdapter(Context context, PlayerInfo playerInfo) {
        this.context = context;
        this.playerInfo = playerInfo;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 4; // Cop, Justiz, Medic, RAC
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_fraction,null);

            viewHolder = new ViewHolder();

            viewHolder.ivFraction = convertView.findViewById(R.id.iv_list_fraction);
            viewHolder.ivRank = convertView.findViewById(R.id.iv_list_rank);
            viewHolder.tvRankName = convertView.findViewById(R.id.tv_list_rank_name);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int coplevel = Integer.parseInt(this.playerInfo.coplevel);
        int medivlevel = Integer.parseInt(this.playerInfo.mediclevel);
        int raclevel = Integer.parseInt(this.playerInfo.adaclevel);

        viewHolder.tvRankName.setText(FractionMappingHelper.getCopRankAsString(coplevel));

        convertView.setTag(viewHolder);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    static class ViewHolder {
        ImageView ivFraction;
        ImageView ivRank;
        TextView tvRankName;
        int position;
    }

}
