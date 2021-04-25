package de.realliferpg.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FractionMappingHelper;
import de.realliferpg.app.interfaces.FractionEnum;
import de.realliferpg.app.objects.CompanyShops;
import de.realliferpg.app.objects.PhoneNumbers;


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
            viewHolder.tvCompanyPhone = convertView.findViewById(R.id.tv_company_shops_phone);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;

        // TODO

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
        TextView tvAmount = convertView.findViewById(R.id.tv_company_shops_amount);
        TextView tvPrice = convertView.findViewById(R.id.tv_company_shops_price);

        /*
        String icCurrentMarketItem = "market_" + shop.classname;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(icCurrentMarketItem, "drawable", context.getPackageName());
        viewHolder.ivMarketItem.setImageResource(resourceId);

        ivItem.setI
         */

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private boolean playerHasMultipleOwnNumbers(PhoneNumbers[] phones, FractionEnum fractionEnum) {
        int count = 0;
        for (PhoneNumbers phone : phones) {
            if (phone.disabled != 0) {
                continue;
            }
            if (phone.note.matches("default")) {
                continue;
            }
            if (phone.side.matches(FractionMappingHelper.getSideFromFractionEnum(fractionEnum))) {
                continue;
            }
            count++;
        }

        return count > 1;
    }

    private String getDefaultNumber(PhoneNumbers[] phones) {
        for (PhoneNumbers phone : phones) {
            if (phone.note.matches("default")) {
                return phone.phone;
            }
        }
        return "0";
    }

    private String getNumberFormSide(PhoneNumbers[] phones, FractionEnum fractionEnum) {
        for (PhoneNumbers phone : phones) {
            if (phone.disabled != 0) {
                continue;
            }
            if (phone.note.matches("default")) {
                continue;
            }
            if (phone.side.matches(FractionMappingHelper.getSideFromFractionEnum(fractionEnum))) {
                return phone.phone;
            }
        }
        return "0";
    }

    private static class ViewHolder {
        TextView tvCompanyName;
        TextView tvCompanyPhone;
        int position;
    }
}
