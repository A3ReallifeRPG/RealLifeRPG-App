package de.realliferpg.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.objects.MarketItem;

public class MarketItemAdapter extends ArrayAdapter<MarketItem> {

    Context context;

    public MarketItemAdapter(Context context, ArrayList<MarketItem> users) {
        super(context, 0, users);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_market, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.tvMarketItemName = convertView.findViewById(R.id.tv_list_marketitem_name);
            viewHolder.tvMarketItemPrice = convertView.findViewById(R.id.tv_list_marketitem_price);
            viewHolder.ivMarketItem = convertView.findViewById(R.id.iv_list_marketitem);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MarketItem marketItem = getItem(position);

        viewHolder.tvMarketItemName.setText(marketItem.localized);

        FormatHelper formatHelper = new FormatHelper();
        String formattedPrice = formatHelper.formatCurrency(marketItem.price);

        // TODO Werte von Server 2
        String marketPrices = "Server 1: " + formattedPrice;

        viewHolder.tvMarketItemPrice.setText(marketPrices);

        convertView.setTag(viewHolder);

        return convertView;
    }

    static class ViewHolder {
        ImageView ivMarketItem;
        TextView tvMarketItemName;
        TextView tvMarketItemPrice;
    }
}
