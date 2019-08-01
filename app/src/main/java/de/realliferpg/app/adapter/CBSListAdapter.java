package de.realliferpg.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
        String child = null;
        CBSData cbsData = _cbsData.get(groupPosition);

        // TODO ?



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
            convertView = inflater.inflate(R.layout.list_item_group_changelog,null);
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
        final String childText = (String)getChild(groupPosition,childPosition);

        // TODO wie soll Unterelement aussehen

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_changeloglititem,null);
        }

        TextView text_changelog_listitem = convertView.findViewById(R.id.tv_changelog_listitem);
        text_changelog_listitem.setText(Html.fromHtml(childText));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
