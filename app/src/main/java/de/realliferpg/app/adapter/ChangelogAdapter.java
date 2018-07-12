package de.realliferpg.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.realliferpg.app.R;
import de.realliferpg.app.objects.Changelog;

public class ChangelogAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Changelog> changelogs;
    private HashMap<String, List<String>> listHashMap;

    public ChangelogAdapter(Context context, ArrayList<Changelog> changelogs, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.changelogs = changelogs;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return changelogs.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int count = 0;
        Changelog changelog = changelogs.get(i);

        count += changelog.change_mission.length;
        count += changelog.change_map.length;
        count += changelog.change_mod.length;

        if( !changelog.note.equals("")){
            count++;
        }

        return count;
    }

    @Override
    public Object getGroup(int i) {
        return changelogs.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String child = "";
        Changelog changelog = changelogs.get(groupPosition);

        if(childPosition < changelog.change_mission.length){
            child = "&bull;" +changelog.change_mission[childPosition];
        }else{
            int offsetMission = changelog.change_mission.length;
            if((childPosition - offsetMission) < changelog.change_map.length){
                child = "&bull;" +changelog.change_map[(childPosition - offsetMission)];
            }else {
                int offsetMap =  offsetMission + changelog.change_map.length;
                if((childPosition - offsetMap) < changelog.change_mod.length){
                    child = "&bull;" + changelog.change_mod[(childPosition - offsetMission)];
                }else{
                    child = "<i><font color='red'>" + changelog.note + "</font></i>";
                }
            }
        }

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
        Gson gson = new Gson();

        Changelog changelog = (Changelog) getGroup(groupPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_group_changelog,null);
        }
        TextView tv_groupHeader = convertView.findViewById(R.id.tv_changelog_group_header);
        tv_groupHeader.setTypeface(null, Typeface.BOLD);

        String itemHeader = "v" + changelog.version;
        String itemSubtitle = "Veröffentlicht am "; // TODO localize

        SimpleDateFormat format = new SimpleDateFormat("dd.MMM.yyyy hh:mm", Locale.GERMANY);
        Date newDate = null;
        try {
            newDate = format.parse(changelog.release_at);
            itemSubtitle += format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            itemSubtitle += "ERROR";
        }

        TextView tv_groupSubtitle = convertView.findViewById(R.id.tv_cahngelog_group_subtitle);


        tv_groupHeader.setText(itemHeader);
        tv_groupSubtitle.setText(changelog.release_at);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition,childPosition);

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
