package de.realliferpg.app.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.objects.ShopItem;
import de.realliferpg.app.objects.ShopVehicle;

public class InfoAdapter<T> extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private ArrayList<T> info;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHead, tvSub, tvRight;

        private Context context;

        ViewHolder(View itemView) {
            super(itemView);

            tvHead = itemView.findViewById(R.id.tv_list_info_head);
            tvSub = itemView.findViewById(R.id.tv_list_info_sub);
            tvRight = itemView.findViewById(R.id.tv_list_info_right);

            context = itemView.getContext();
        }

    }

    // Provide a suitable constructor (depends on the kind of info)
    public InfoAdapter(ArrayList<T> _infos) {
        this.info = _infos;
    }

    @NonNull
    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_info, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FormatHelper formatHelper = new FormatHelper();
        Object object = info.get(position);

        if (object instanceof ShopVehicle) {
            ShopVehicle shopVehicle = (ShopVehicle) object;

            holder.tvHead.setText(shopVehicle.name);
            holder.tvSub.setText("V-Items: " + shopVehicle.v_space + " kg\nLevel: " + shopVehicle.level);

            holder.tvRight.setText(formatHelper.formatCurrency(shopVehicle.price));
        } else if (object instanceof ShopItem) {
            ShopItem shop = (ShopItem) object;

            holder.tvHead.setText(shop.name);
            holder.tvSub.setText("Level: " + shop.level);

            holder.tvRight.setText(formatHelper.formatCurrency(shop.price));
        }
    }

    // Return the size of your info (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return info.size();
    }
}