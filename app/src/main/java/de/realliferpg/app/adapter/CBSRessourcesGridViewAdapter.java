package de.realliferpg.app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.realliferpg.app.R;
import de.realliferpg.app.objects.RessourceInfo;


public class CBSRessourcesGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RessourceInfo> _ressourcesList;

    public CBSRessourcesGridViewAdapter(Context _context, ArrayList<RessourceInfo> items) {
        this.context = _context;
        this._ressourcesList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View elementView = LayoutInflater.from(this.context).inflate(R.layout.list_item_cbs_ressource, parent, false);

        ImageView ivCBSItem = elementView.findViewById(R.id.iv_cbs_ressource);
        TextView tvCBSItemDeliveredRequired = elementView.findViewById(R.id.tv_cbs_delivered_required);

        RessourceInfo ressoruceInfo = getItem(position);

        // Bild der Ressource
        String icCurrentRessourceItem = "market_" + ressoruceInfo.class_name;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(icCurrentRessourceItem, "drawable", context.getPackageName());
        ivCBSItem.setImageResource(resourceId);

        // Delivered/Required für Ressource
        tvCBSItemDeliveredRequired.setText(ressoruceInfo.delivered + "/" + ressoruceInfo.required);

        return elementView;
    }

    @Override
    public RessourceInfo getItem(int position) {
        return this._ressourcesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount(){
        return this._ressourcesList.size();
    }

}
