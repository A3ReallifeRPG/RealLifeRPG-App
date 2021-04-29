package de.realliferpg.app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.objects.CompanyShops;
import de.realliferpg.app.objects.ShopCompany;


public class CompanyShopsListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final ArrayList<CompanyShops> companyShopsData;

    public CompanyShopsListAdapter(Context _context, ArrayList<CompanyShops> _companyShopsData) {
        this.context = _context;
        this.companyShopsData = _companyShopsData;
    }

    @Override
    public int getGroupCount() {
        return companyShopsData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return companyShopsData.get(i).shops.length;
    }

    @Override
    public Object getGroup(int i) {
        return companyShopsData.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return companyShopsData.get(groupPosition).shops[childPosition];
    }

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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_group_companyshops, null);

            viewHolder = new ViewHolder();
            viewHolder.position = groupPosition;

            viewHolder.tvCompanyName = convertView.findViewById(R.id.tv_company_shops_company_name);
            viewHolder.tvCompanyIndustrialAreaID = convertView.findViewById(R.id.tv_company_shops_industrial_area_id);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;

        CompanyShops currentCompany = companyShopsData.get(groupPosition);

        viewHolder.tvCompanyName.setText(currentCompany.company.name);
        viewHolder.tvCompanyIndustrialAreaID.setText(context.getResources().getString(R.string.str_industrial_area_id) + " " + currentCompany.industrial_area_id);

        convertView.setTag(viewHolder);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_companyshops, null);
        }

        ImageView ivItem = convertView.findViewById(R.id.iv_list_company_shop_item);
        TextView tvName = convertView.findViewById(R.id.tv_list_company_shop_item_name);
        TextView tvAmount = convertView.findViewById(R.id.tv_company_shops_amount);
        TextView tvPrice = convertView.findViewById(R.id.tv_company_shops_price);

        ShopCompany currentCompanyShop = companyShopsData.get(groupPosition).shops[childPosition];

        String icCurrentMarketItem = "market_" + currentCompanyShop.item;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(icCurrentMarketItem, "drawable", context.getPackageName());
        ivItem.setImageResource(resourceId);

        tvName.setText(currentCompanyShop.item_localized);
        tvAmount.setText(currentCompanyShop.amount + " " + context.getResources().getString(R.string.str_company_shops_amount));
        tvPrice.setText(currentCompanyShop.price + " $");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolder {
        TextView tvCompanyName;
        TextView tvCompanyIndustrialAreaID;
        int position;
    }
}
