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
import de.realliferpg.app.objects.MarketItem;

public class MarketItemAdapter extends ArrayAdapter<MarketItem> {

    Context context;
    int[] serversOnline;

    public MarketItemAdapter(Context _context, ArrayList<MarketItem> _items, int[] _servers) {
        super(_context, 0, _items);
        this.context = _context;
        this.serversOnline = _servers;
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
            viewHolder.tvMarketItemPrice3 = convertView.findViewById(R.id.tv_list_marketitem_price3);
            viewHolder.tvMarketItemTextServer1 = convertView.findViewById(R.id.tv_list_marketitem_server1);
            viewHolder.tvMarketItemTextServer2 = convertView.findViewById(R.id.tv_list_marketitem_server2);
            viewHolder.tvMarketItemTextServer3 = convertView.findViewById(R.id.tv_list_marketitem_server3);
            viewHolder.ivMarketItem = convertView.findViewById(R.id.iv_list_marketitem);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MarketItem marketItem = getItem(position);

        viewHolder.tvMarketItemName.setText(marketItem.name);

        FormatHelper formatHelper = new FormatHelper();
        String formattedPriceServer1 = formatHelper.formatCurrency(marketItem.priceServer1);
        String formattedPriceServer2 = formatHelper.formatCurrency(marketItem.priceServer2);
        String formattedPriceServer3 = formatHelper.formatCurrency(marketItem.priceServer3);

        //String marketPrices = "Server 1: " + formattedPriceServer1 + "\nServer 2: " + formattedPriceServer2 + "\nServer 3: " + formattedPriceServer3;

        viewHolder.tvMarketItemPrice1.setText(formattedPriceServer1);
        viewHolder.tvMarketItemPrice2.setText(formattedPriceServer2);
        viewHolder.tvMarketItemPrice3.setText(formattedPriceServer3);

        viewHolder.tvMarketItemTextServer3.setVisibility(View.INVISIBLE);
        viewHolder.tvMarketItemPrice3.setVisibility(View.INVISIBLE);

        viewHolder.tvMarketItemTextServer3.setHeight(0);
        viewHolder.tvMarketItemPrice3.setHeight(0);

        if (serversOnline.length == 3 && serversOnline[2] == 0)
        {
            viewHolder.tvMarketItemTextServer3.setVisibility(View.VISIBLE);
            viewHolder.tvMarketItemPrice3.setVisibility(View.VISIBLE);
        }

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
        TextView tvMarketItemPrice3;
        TextView tvMarketItemTextServer1;
        TextView tvMarketItemTextServer2;
        TextView tvMarketItemTextServer3;
    }
}
