package de.realliferpg.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.objects.Vehicle;

public class InfoAdapter<T> extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private ArrayList<T> dataArray;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHead;
        TextView tvSub;

        private Context context;

        ViewHolder(View itemView) {
            super(itemView);

            tvHead = itemView.findViewById(R.id.tv_list_info_head);
            tvSub = itemView.findViewById(R.id.tv_list_info_sub);

            context = itemView.getContext();
        }

    }

    // Provide a suitable constructor (depends on the kind of dataArray)
    public InfoAdapter(ArrayList<T> dataArray) {
        this.dataArray = dataArray;
    }

    @NonNull
    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_info,parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Object object = dataArray.get(position);

        if(object instanceof Vehicle){
            Vehicle vehicle = (Vehicle) object;

            holder.tvHead.setText(vehicle.displayName);
            holder.tvSub.setText(vehicle.displayName);
        }
    }

    // Return the size of your dataArray (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataArray.size();
    }
}