package de.realliferpg.app.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.objects.Server;

public class ServerListAdapter extends ArrayAdapter<Server> {

    Context context;

    public ServerListAdapter(Context _context, ArrayList<Server> _users) {
        super(_context, 0, _users);
        this.context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_server, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.position = position;

            viewHolder.tvPlayerInfo = convertView.findViewById(R.id.tv_list_server_info);
            viewHolder.tvHead = convertView.findViewById(R.id.tv_list_server_head);
            viewHolder.pbPlayers = convertView.findViewById(R.id.pb_list_server);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = position;

        Server server = getItem(position);

        String title = server.Servername + " - " + server.Playercount + "/" + server.Slots;
        viewHolder.tvHead.setText(title);

        double progress = ((double) server.Playercount / (double) server.Slots) * 100;

        viewHolder.pbPlayers.setProgress((int) progress);

        if (server.Servername.toLowerCase().contains("realliferpg 7.0 server") && !server.Servername.toLowerCase().contains("gungame")) {
            // Arma 3 Server
            viewHolder.tvPlayerInfo.setText(Html.fromHtml(
                    "<font color='" + convertView.getResources().getColor(R.color.colorCiv) + "'>CIV " + server.Civilians +
                            "</font> - <font color='" + convertView.getResources().getColor(R.color.colorCop) + "'>COP " + server.Cops +
                            "</font> - <font color='" + convertView.getResources().getColor(R.color.colorMed) + "'>MED " + server.Medics +
                            "</font> - <font color='" + convertView.getResources().getColor(R.color.colorRac) + "'>RAC " + server.Adac +
                            "</font>"));
        }
        else {
            // anderer Server
            viewHolder.tvPlayerInfo.setText(Html.fromHtml(
                    "<font color='" + convertView.getResources().getColor(R.color.colorCiv) + "'>Spieler " + server.Playercount));
        }

        convertView.setTag(viewHolder);

        return convertView;
    }

    static class ViewHolder {
        TextView tvHead;
        TextView tvPlayerInfo;
        ProgressBar pbPlayers;
        int position;
    }
}