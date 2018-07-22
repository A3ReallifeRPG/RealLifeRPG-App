package de.realliferpg.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.objects.Donation;

public class DonationListAdapter extends RecyclerView.Adapter<DonationListAdapter.ViewHolder> {

    private ArrayList<Donation> donations;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHead;
        ImageView ivIcon;

        private Context context;

        ViewHolder(View itemView) {
            super(itemView);

            tvHead = itemView.findViewById(R.id.tv_list_donate_head);
            ivIcon = itemView.findViewById(R.id.iv_list_donate);

            context = itemView.getContext();
        }

    }

    // Provide a suitable constructor (depends on the kind of donations)
    public DonationListAdapter(ArrayList<Donation> dataArray) {
        this.donations = dataArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_donation, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull DonationListAdapter.ViewHolder holder, int position) {
        FormatHelper formatHelper = new FormatHelper();

        Donation donation = donations.get(position);

        holder.tvHead.setText(String.valueOf(donation.amount));

        switch (donation.level){
            case 1:{
                holder.ivIcon.setImageResource(R.drawable.donate_bronze);
                break;
            }
            case 2:{
                holder.ivIcon.setImageResource(R.drawable.donate_silver);
                break;
            }
            case 3:{
                holder.ivIcon.setImageResource(R.drawable.donate_gold);
                break;
            }
            case 4:{
                holder.ivIcon.setImageResource(R.drawable.donate_platin);
                break;
            }
        }

    }

    // Return the size of your donations (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return donations.size();
    }
}