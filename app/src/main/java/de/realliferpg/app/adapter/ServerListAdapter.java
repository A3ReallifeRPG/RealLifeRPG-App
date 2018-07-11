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

    public ServerListAdapter(Context context, ArrayList<Server> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Server server = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_server, parent, false);
        }

        TextView tvS1Head = convertView.findViewById(R.id.tv_main_s1_head);
        ProgressBar pbS1Player = convertView.findViewById(R.id.pb_main_s1_players);

        String title = server.Servername + " - " + server.Playercount + "/" + server.Slots;
        tvS1Head.setText(title);

        double progress = ((double) server.Playercount / (double) server.Slots) * 100;

        pbS1Player.setProgress((int) progress);

        TextView tvS1PlayerInfo = convertView.findViewById(R.id.tv_main_s1_pInfo);

        tvS1PlayerInfo.setText(Html.fromHtml("<font color='" + convertView.getResources().getColor(R.color.colorCiv) + "'>CIV " + server.Civilians +
                "</font> - <font color='" + convertView.getResources().getColor(R.color.colorCop) +
                "'>COP " + server.Cops + "</font> - <font color='" +
                convertView.getResources().getColor(R.color.colorMed) + "'> MED: " + server.Medics + "</font> - <font color='" + convertView.getResources().getColor(R.color.colorRac) + "'>RAC: " + server.Adac + "</font>"));

        return convertView;
    }
}