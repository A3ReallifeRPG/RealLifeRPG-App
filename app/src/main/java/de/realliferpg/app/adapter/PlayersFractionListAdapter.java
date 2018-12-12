package de.realliferpg.app.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.realliferpg.app.R;
import de.realliferpg.app.helper.FractionMappingHelper;
import de.realliferpg.app.interfaces.FractionEnum;
import de.realliferpg.app.objects.FractionInfo;
import de.realliferpg.app.objects.PlayerInfo;


public class PlayersFractionListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private FractionInfo fractionInfo;

    public PlayersFractionListAdapter(Context context, FractionInfo fractioninfo) {
        this.context = context;
        this.fractionInfo = fractioninfo;
    }

    @Override
    public int getGroupCount() {
        // 4 Fraktionen (Justiz, Polizei, Rettungsdienst, RAC)
        return this.fractionInfo.sizeGroup;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // Jede Fraktion hat nur ein Child (der Rang wird angezeigt)
        return this.fractionInfo.sizeChild;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // Abhängig von der Reihenfolge werden verschiedene Infos angezeigt
        // Folgende Festlegung
        switch (groupPosition) {
            case 0: // 0 = Cops
                return FractionEnum.COP;
            case 1: // 1 = Justiz
                return FractionEnum.JUSTIZ;
            case 2: // 2 = Medic
                return FractionEnum.MEDIC;
            case 3: // 3 = RAC
                return FractionEnum.RAC;
            default:
                return FractionEnum.NONE;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // Abhängig von der Reihenfolge werden verschiedene Infos angezeigt
        // Folgende Festlegung
        switch (groupPosition) {
            case 0: // 0 = Cops
                return FractionEnum.COP;
            case 1: // 1 = Justiz
                return FractionEnum.JUSTIZ;
            case 2: // 2 = Medic
                return FractionEnum.MEDIC;
            case 3: // 3 = RAC
                return FractionEnum.RAC;
            default:
                return FractionEnum.NONE;
        }
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
        FractionEnum fraction = (FractionEnum) getGroup(groupPosition);

        final PlayersFractionListAdapter.ViewHolder viewHolder;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_group_fractionlist,null);

            viewHolder = new PlayersFractionListAdapter.ViewHolder();
            viewHolder.position = groupPosition;

            viewHolder.tv_fraction_name = convertView.findViewById(R.id.tv_fractionlist_fraction);
            viewHolder.iv_fraction_symbol = convertView.findViewById(R.id.iv_fraction);
        } else {
            viewHolder = (PlayersFractionListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.position = groupPosition;

        viewHolder.tv_fraction_name.setText(getTextFromEnum(fraction));

        viewHolder.iv_fraction_symbol.setImageResource(getImageResourceFromEnum(fraction));

        convertView.setTag(viewHolder);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        FractionEnum childFraction = (FractionEnum) getChild(groupPosition, childPosition);

        final PlayersFractionListAdapter.ViewHolderChild viewHolderChild;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_fractionlist,null);

            viewHolderChild = new PlayersFractionListAdapter.ViewHolderChild();
            viewHolderChild.position = groupPosition;

            viewHolderChild.tv_fraction_rank = convertView.findViewById(R.id.tv_fractionlist_rank);
            viewHolderChild.iv_fraction_ranksymbol = convertView.findViewById(R.id.iv_fractionlist_ranksymbol);
        }
        else {
            viewHolderChild = (PlayersFractionListAdapter.ViewHolderChild) convertView.getTag();
        }

        viewHolderChild.position = groupPosition;

        viewHolderChild.tv_fraction_rank.setText(getRankName(childFraction));

        viewHolderChild.iv_fraction_ranksymbol.setImageResource(getRankSymbol(childFraction));

        convertView.setTag(viewHolderChild);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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

    private String getRankName(FractionEnum childFraction) {
        String fraction_rankname = "";

        switch (childFraction) {
            case COP:
                if (this.fractionInfo.coplevel == 1) {// coplevel 1 bedeutet Justizler
                    fraction_rankname = FractionMappingHelper.getCopRankAsString(0);
                } else {
                    fraction_rankname = FractionMappingHelper.getCopRankAsString(this.fractionInfo.coplevel);
                }
                break;
            case JUSTIZ:
                if (this.fractionInfo.coplevel == 1) {// coplevel 1 bedeutet Justizler
                    fraction_rankname = FractionMappingHelper.getCopRankAsString(this.fractionInfo.coplevel);
                } else {
                    fraction_rankname = FractionMappingHelper.getCopRankAsString(0);
                }
                break;
            case MEDIC:
                fraction_rankname = FractionMappingHelper.getMedicRankAsString(this.fractionInfo.mediclevel);
                break;
            case RAC:
                fraction_rankname = FractionMappingHelper.getRacRankAsString(this.fractionInfo.raclevel);
                break;
            case NONE:
                fraction_rankname = "Kein Rang";
                break;
        }

        return fraction_rankname;
    }

    private int getRankSymbol(FractionEnum childFraction) {
        Resources resources = context.getResources();
        String tempSymbol = "";
        int fraction_ranksymbol = 0;

            switch (childFraction) {
                case COP:
                    tempSymbol = FractionMappingHelper.getCopRankSymbolAsString(this.fractionInfo.coplevel);
                    break;
                case JUSTIZ:
                    tempSymbol = FractionMappingHelper.getJustizRankSymbolAsString(this.fractionInfo.coplevel);
                    break;
                case MEDIC:
                    tempSymbol = FractionMappingHelper.getMedicRankSymbolAsString(this.fractionInfo.mediclevel);
                    break;
                case RAC:
                    tempSymbol = FractionMappingHelper.getRacRankSymbolAsString(this.fractionInfo.raclevel);
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
        int position;
    }

    static class ViewHolderChild {
        TextView tv_fraction_rank;
        ImageView iv_fraction_ranksymbol;
        int position;
    }
}
