package de.realliferpg.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.objects.CBSData;

public class CBSListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<CBSData> _cbsData;

    public CBSListAdapter(Context _context, ArrayList<CBSData> _cbsData) {
        this.context = _context;
        this._cbsData = _cbsData;
    }

    @Override
    public int getGroupCount() {
        return _cbsData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        // TODO: anzahl unterelemente
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return _cbsData.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        CBSData child = null;

        child = _cbsData.get(groupPosition);

        return child;
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

        CBSData cbsData = (CBSData) getGroup(groupPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_group_cbs,null);
        }

        TextView tv_groupSubtitle = convertView.findViewById(R.id.tv_changelog_group_subtitle);
        TextView tv_groupHeader = convertView.findViewById(R.id.tv_changelog_group_header);


        tv_groupHeader.setTypeface(null, Typeface.BOLD);

        tv_groupHeader.setText(cbsData.title);

        String status = "";
        if (cbsData.finished == 1){
            status = "</font><font color='" + convertView.getResources().getColor(R.color.colorMed) + "'>abgeschlossen";
        } else {
            status = "</font><font color='" + convertView.getResources().getColor(R.color.colorRac) + "'>in Arbeit";
        }

        tv_groupSubtitle.setText(Html.fromHtml(status));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final CBSData cbsData = (CBSData) getChild(groupPosition,childPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_cbs,null);
        }


        TextView text_cbs_desc = convertView.findViewById(R.id.tv_cbs_desc);
        TextView text_cbs_money_value = convertView.findViewById(R.id.tv_cbs_money_value);

        text_cbs_desc.setText(cbsData.desc + "\n"); // \n sind Platzhalter

        FormatHelper formatHelper = new FormatHelper();
        text_cbs_money_value.setText(formatHelper.formatCurrency(cbsData.amount) + "/" + formatHelper.formatCurrency(cbsData.funding_required)+ "\n");

        ImageView imageView_cbs = convertView.findViewById(R.id.iv_cbs_profile);
        Picasso.get().load(cbsData.image).into(imageView_cbs);
        //imageView_cbs.setBackgroundColor(convertView.getResources().getColor(R.color.colorMed));


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
