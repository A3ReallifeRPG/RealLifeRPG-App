package de.realliferpg.app.helper;

import android.content.Context;
import android.content.res.Resources;

import de.realliferpg.app.R;
import de.realliferpg.app.interfaces.FractionEnum;

public class FractionMappingHelper {

    public static int getImageResourceFromEnum(Context context, FractionEnum fraction) {

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
                tempfractionSymbol = "realliferpg_logo";
                break;
        }

        if (tempfractionSymbol == "")
            return -1;

        fraction_symbol = resources.getIdentifier(tempfractionSymbol, "drawable", context.getPackageName());

        return fraction_symbol;
    }

    public static FractionEnum getFractionFromSide(String fractionEnum, int coplevel){
        switch (fractionEnum) {
            case "GUER": // Medic
                return FractionEnum.MEDIC;
            case "WEST": // Pol & Justiz
                if (coplevel == 0) // war mal bei Justiz/Pol
                    return FractionEnum.NONE;
                if (coplevel == 1)
                {
                    return FractionEnum.JUSTIZ;
                } else {
                    return FractionEnum.COP;
                }
            case "EAST":
                return FractionEnum.RAC;
            case "CIV":
                return FractionEnum.NONE;
        }
        return FractionEnum.NONE;
    }

    public static String getCopRankAsString(Context context, int coplevel) {

        // TODO: ersetzen mit localized Strings
        switch (coplevel) {
            case 1:
                return context.getResources().getString(R.string.str_justiz);
            case 2:
                return context.getResources().getString(R.string.str_cop0a);
            case 3:
                return context.getResources().getString(R.string.str_cop01);
            case 4:
                return context.getResources().getString(R.string.str_cop02);
            case 5:
                return context.getResources().getString(R.string.str_cop03);
            case 6:
                return context.getResources().getString(R.string.str_cop04);
            case 7:
                return context.getResources().getString(R.string.str_cop05);
            case 8:
                return context.getResources().getString(R.string.str_cop06);
            case 9:
                return context.getResources().getString(R.string.str_cop07);
            case 10:
                return context.getResources().getString(R.string.str_cop08);
            case 11:
                return context.getResources().getString(R.string.str_cop09);
            case 12:
                return context.getResources().getString(R.string.str_cop10);
            case 13:
                return context.getResources().getString(R.string.str_cop11);
            case 14:
                return context.getResources().getString(R.string.str_cop12);
            case 15:
                return context.getResources().getString(R.string.str_cop13);
            case 16:
                return context.getResources().getString(R.string.str_cop15);
            default:
                return context.getResources().getString(R.string.str_noRank);
        }

    }

    public static String getMedicRankAsString(Context context, int mediclevel) {

        switch (mediclevel) {
            case 1:
                return context.getResources().getString(R.string.str_med01);
            case 2:
                return context.getResources().getString(R.string.str_med02);
            case 3:
                return context.getResources().getString(R.string.str_med03);
            case 4:
                return context.getResources().getString(R.string.str_med04);
            case 5:
                return context.getResources().getString(R.string.str_med05);
            case 6:
                return context.getResources().getString(R.string.str_med06);
            case 7:
                return context.getResources().getString(R.string.str_med07);
            case 8:
                return context.getResources().getString(R.string.str_med08);
            case 9:
                return context.getResources().getString(R.string.str_med09);
            case 10:
                return context.getResources().getString(R.string.str_med10);
            case 11:
                return context.getResources().getString(R.string.str_med11);
            case 12:
                return context.getResources().getString(R.string.str_med12);
            default:
                return context.getResources().getString(R.string.str_noRank);
        }
    }

    public static String getRacRankAsString(Context context, int raclevel) {

        // TODO: ersetzen mit localized Strings
        switch (raclevel) {
            case 1:
                return context.getResources().getString(R.string.str_rac01);
            case 2:
                return context.getResources().getString(R.string.str_rac02);
            case 3:
                return context.getResources().getString(R.string.str_rac03);
            case 4:
                return context.getResources().getString(R.string.str_rac04);
            case 5:
                return context.getResources().getString(R.string.str_rac05);
            case 6:
                return context.getResources().getString(R.string.str_rac06);
            case 7:
                return context.getResources().getString(R.string.str_rac07);
            case 8:
                return context.getResources().getString(R.string.str_rac08);
            case 9:
                return context.getResources().getString(R.string.str_rac09);
            default:
                return context.getResources().getString(R.string.str_noRank);
        }
    }

    public static String getCopRankSymbolAsString(int coplevel) {

        switch (coplevel) {
            case 0:
                return "cop_00";
            case 1:
                return "cop_0a";
            case 2:
                return "cop_01";
            case 3:
                return "cop_02";
            case 4:
                return "cop_03";
            case 5:
                return "cop_04";
            case 6:
                return "cop_05";
            case 7:
                return "cop_06";
            case 8:
                return "cop_07";
            case 9:
                return "cop_08";
            case 10:
                return "cop_09";
            case 11:
                return "cop_10";
            case 12:
                return "cop_11";
            case 13:
                return "cop_12";
            case 14:
                return "cop_13";
            case 15:
                return "cop_14";
            case 16:
                return "cop_15";

            default:
                return "Kein Rang";
        }

    }

    public static String getJustizRankSymbolAsString(int justizlevel) {

        return "Kein Rang";
    }

    public static String getMedicRankSymbolAsString(int mediclevel) {

        switch (mediclevel) {
            case 0:
                return "medic_00";
            case 1:
                return "medic_01";
            case 2:
                return "medic_02";
            case 3:
                return "medic_03";
            case 4:
                return "medic_04";
            case 5:
                return "medic_05";
            case 6:
                return "medic_06";
            case 7:
                return "medic_07";
            case 8:
                return "medic_08";
            case 9:
                return "medic_09";
            case 10:
                return "medic_10";
            case 11:
                return "medic_11";
            case 12:
                return "medic_12";
            default:
                return "Kein Rang";
        }
    }

    public static String getRacRankSymbolAsString(int raclevel) {

        switch (raclevel) {
            case 0:
                return "rac_00";  // cop-level 0
            case 1:
                return "rac_01"; // cop-level 1
            case 2:
                return "rac_02";  // cop-level 2
            case 3:
                return "rac_03";  // cop-level 3
            case 4:
                return "rac_04";  // cop-level 4
            case 5:
                return "rac_05";  // cop-level 5
            case 6:
                return "rac_06";  // cop-level 6
            case 7:
                return "rac_07";  // cop-level 7
            case 8:
                return "rac_08";  // cop-level 8
            case 9:
                return "rac_09";  // cop-level 9
            default:
                return "Kein Rang";
        }
    }
}
