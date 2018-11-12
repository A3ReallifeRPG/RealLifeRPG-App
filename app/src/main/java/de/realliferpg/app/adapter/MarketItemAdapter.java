package de.realliferpg.app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.objects.MarketServerObject;
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
            viewHolder.tvMarketItemPrice1 = convertView.findViewById(R.id.tv_list_marketitem_price1);
            viewHolder.tvMarketItemPrice2 = convertView.findViewById(R.id.tv_list_marketitem_price2);
            viewHolder.ivMarketItem = convertView.findViewById(R.id.iv_list_marketitem);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MarketItem marketItem = getItem(position);

        viewHolder.tvMarketItemName.setText(marketItem.name);

        FormatHelper formatHelper = new FormatHelper();
        String formattedPriceServer1 = formatHelper.formatCurrency(marketItem.priceServer1);
        String formattedPriceServer2 = formatHelper.formatCurrency(marketItem.priceServer2);

        String marketPrices = "Server 1: " + formattedPriceServer1 + "\nServer 2: " + formattedPriceServer2;

        viewHolder.tvMarketItemPrice1.setText(formattedPriceServer1);
        viewHolder.tvMarketItemPrice2.setText(formattedPriceServer2);

        String icCurrentMarketItem = "market_" + marketItem.classname;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(icCurrentMarketItem, "drawable", context.getPackageName());
        viewHolder.ivMarketItem.setImageResource(resourceId);

        convertView.setTag(viewHolder);

        return convertView;
    }

    static class ViewHolder {
        ImageView ivMarketItem;
        TextView tvMarketItemName;
        TextView tvMarketItemPrice1;
        TextView tvMarketItemPrice2;
    }
}
