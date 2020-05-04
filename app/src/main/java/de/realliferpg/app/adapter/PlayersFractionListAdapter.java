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
import de.realliferpg.app.helper.FractionMappingHelper;
import de.realliferpg.app.interfaces.FractionEnum;


public class PlayersFractionListAdapter extends ArrayAdapter<Integer> {

    private Context context;
    private ArrayList<Integer> fractionInfo;

    public PlayersFractionListAdapter(Context _context, ArrayList<Integer> _fractioninfo) {
        super(_context, 0, _fractioninfo);
        this.context = _context;
        this.fractionInfo = _fractioninfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int level = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_fractionlist, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.position = position;

            viewHolder.tv_fraction_name = convertView.findViewById(R.id.tv_fractionlist_fraction);
            viewHolder.tv_fraction_rank = convertView.findViewById(R.id.tv_fractionlist_rank);
            viewHolder.iv_fraction_symbol = convertView.findViewById(R.id.iv_fraction);
            viewHolder.iv_fraction_ranksymbol = convertView.findViewById(R.id.iv_fractionlist_ranksymbol);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.position = position;

        FractionEnum fractionEnum = FractionEnum.NONE;

        switch (position) {
            case 0: // 0 = Cops
                fractionEnum =  FractionEnum.COP;
                break;
            case 1: // 1 = Justiz
                fractionEnum =  FractionEnum.JUSTIZ;
                break;
            case 2: // 2 = Medic
                fractionEnum =  FractionEnum.MEDIC;
                break;
            case 3: // 3 = RAC
                fractionEnum =  FractionEnum.RAC;
                break;
            default:
                fractionEnum =  FractionEnum.NONE;
                break;
        }

        viewHolder.tv_fraction_name.setText(getTextFromEnum(fractionEnum));
        viewHolder.tv_fraction_rank.setText(getRankName(fractionEnum, level));
        viewHolder.iv_fraction_symbol.setImageResource(getImageResourceFromEnum(fractionEnum));
        viewHolder.iv_fraction_ranksymbol.setImageResource(getRankSymbol(fractionEnum, level));

        convertView.setTag(viewHolder);

        return convertView;
    }

    private String getTextFromEnum(FractionEnum fractionEnum) {

        String fraction_name = "";

        switch (fractionEnum) {
            case COP:
                fraction_name = "Polizei";
                break;
            case JUSTIZ:
                fraction_name = "Justiz";
                break;
            case MEDIC:
                fraction_name = "Rettungsdienst";
                break;
            case RAC:
                fraction_name = "RealLife Automobil Club";
                break;
            case NONE:
                fraction_name = "Keine Fraktion";
                break;
        }

        return fraction_name;
    }

    private int getImageResourceFromEnum(FractionEnum fraction) {

        Resources resources = context.getResources();
        String tempfractionSymbol = "";
        int fraction_symbol = 0;

        switch (fraction) {
            case COP:
                tempfractionSymbol = "fraction_cop";
                break;
            case JUSTIZ:
                tempfractionSymbol = "fraction_justiz";
                break;
            case MEDIC:
                tempfractionSymbol = "fraction_medic";
                break;
            case RAC:
                tempfractionSymbol = "fraction_rac";
                break;
            case NONE:
                // sollte hier nie reinlaufen, aber man weiß ja nie...
                tempfractionSymbol = "backwasch";
                break;
        }

        fraction_symbol = resources.getIdentifier(tempfractionSymbol, "drawable", context.getPackageName());

        return fraction_symbol;
    }

    private String getRankName(FractionEnum fractionEnum, int level) {
        String fraction_rankname = "";

        switch (fractionEnum) {
            case COP:
                if (level == 1) {// coplevel 1 bedeutet Justizler
                    fraction_rankname = FractionMappingHelper.getCopRankAsString(this.context, 0);
                } else {
                    fraction_rankname = FractionMappingHelper.getCopRankAsString(this.context, level);
                }
                break;
            case JUSTIZ:
                if (level == 1) {// coplevel 1 bedeutet Justizler
                    fraction_rankname = FractionMappingHelper.getCopRankAsString(this.context, level);
                } else {
                    fraction_rankname = FractionMappingHelper.getCopRankAsString(this.context, 0);
                }
                break;
            case MEDIC:
                fraction_rankname = FractionMappingHelper.getMedicRankAsString(this.context, level);
                break;
            case RAC:
                fraction_rankname = FractionMappingHelper.getRacRankAsString(this.context, level);
                break;
            case NONE:
                fraction_rankname = "Kein Rang";
                break;
        }

        return fraction_rankname;
    }

    private int getRankSymbol(FractionEnum fractionEnum, int level) {
        Resources resources = context.getResources();
        String tempSymbol = "";
        int fraction_ranksymbol = 0;

        switch (fractionEnum) {
            case COP:
                tempSymbol = FractionMappingHelper.getCopRankSymbolAsString(level);
                break;
            case JUSTIZ:
                tempSymbol = FractionMappingHelper.getJustizRankSymbolAsString(level);
                break;
            case MEDIC:
                tempSymbol = FractionMappingHelper.getMedicRankSymbolAsString(level);
                break;
            case RAC:
                tempSymbol = FractionMappingHelper.getRacRankSymbolAsString(level);
                break;
            case NONE:
                tempSymbol = "backwasch";
                break;
        }

        fraction_ranksymbol = resources.getIdentifier(tempSymbol, "drawable", context.getPackageName());

        return fraction_ranksymbol;
    }

    static class ViewHolder {
        TextView tv_fraction_name;
        ImageView iv_fraction_symbol;
        TextView tv_fraction_rank;
        ImageView iv_fraction_ranksymbol;
        int position;
    }
}
