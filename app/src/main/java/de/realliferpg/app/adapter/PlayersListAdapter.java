package de.realliferpg.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FormatHelper;
import de.realliferpg.app.objects.Server;


public class PlayersListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Server> servers;

    public PlayersListAdapter(Context context, ArrayList<Server> servers) {
        this.context = context;
        this.servers = servers;
    }

    @Override
    public int getGroupCount() {
        return servers.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int count = 0;
        Server server = servers.get(i);

        count += server.Players.length;

        return count;
    }

    @Override
    public Object getGroup(int i) {
        return servers.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String child = "";
        Server server = servers.get(groupPosition);

        String[] sortedPlayers = server.Players;
        Arrays.sort(sortedPlayers);

        child = sortedPlayers[childPosition];

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
        Server server = (Server) getGroup(groupPosition);

        final ViewHolder viewHolder;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_group_playerslist,null);

            viewHolder = new ViewHolder();
            viewHolder.position = groupPosition;

            viewHolder.tvPlayersGroupInfo = convertView.findViewById(R.id.tv_playerslist_info);
            viewHolder.tvServerHead = convertView.findViewById(R.id.tv_playerslist_grouphead);
            viewHolder.pbCountPlayers = convertView.findViewById(R.id.pb_playerslist_count);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;

        String title = server.Servername + " - " + server.Playercount + "/" + server.Slots;
        viewHolder.tvServerHead.setText(title);

        double progress = ((double) server.Playercount / (double) server.Slots) * 100;

        viewHolder.pbCountPlayers.setProgress((int) progress);

        viewHolder.tvPlayersGroupInfo.setText(Html.fromHtml(
                "<font color='" + convertView.getResources().getColor(R.color.colorCiv) + "'>CIV " + server.Civilians +
                        "</font> - <font color='" + convertView.getResources().getColor(R.color.colorCop) + "'>COP " + server.Cops +
                        "</font> - <font color='" + convertView.getResources().getColor(R.color.colorMed) + "'>MED " + server.Medics +
                        "</font> - <font color='" + convertView.getResources().getColor(R.color.colorRac) + "'>RAC " + server.Adac +
                        "</font>"));

        convertView.setTag(viewHolder);


        /*
        TextView tv_groupCount = convertView.findViewById(R.id.tv_playerslist_group_playerscount);
        TextView tv_groupHeader = convertView.findViewById(R.id.tv_playerslist_group_header);


        tv_groupHeader.setTypeface(null, Typeface.BOLD);

        String itemHeader = "Server " + server.Id;
        String itemCount = Integer.toString(server.Playercount);

        tv_groupHeader.setText(itemHeader);
        tv_groupCount.setText(itemCount);
*/
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition,childPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_playerslist,null);
        }

        TextView text_playerslist_listitem = convertView.findViewById(R.id.tv_playerslist_listitem);
        text_playerslist_listitem.setText(Html.fromHtml(childText));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView tvServerHead;
        TextView tvPlayersGroupInfo;
        ProgressBar pbCountPlayers;
        int position;
    }
}
