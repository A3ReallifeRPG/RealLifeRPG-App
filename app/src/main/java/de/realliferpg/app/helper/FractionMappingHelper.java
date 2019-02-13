package de.realliferpg.app.helper;

public class FractionMappingHelper {

    public static String getCopRankAsString(int coplevel) {

        // TODO: ersetzen mit localized Strings
        switch (coplevel) {
            case 0:
                return "Kein Rang";  // cop-level 0
            case 1:
                return "Justizbeamter"; // cop-level 1
            case 2:
                return "Polizeimeisteranwärter";  // cop-level 2
            case 3:
                return "Polizeimeister";  // cop-level 3
            case 4:
                return "Polizeiobermeister";  // cop-level 4
            case 5:
                return "Polizeihauptmeister";  // cop-level 5
            case 6:
                return "Polizeihauptmeister mit Amtszulage";  // cop-level 6
            case 7:
                return "Polizeikommissaranwärter";  // cop-level 7
            case 8:
                return "Polizeikommissar";  // cop-level 8
            case 9:
                return "Polizeioberkommissar";  // cop-level 9
            case 10:
                return "Polizeihauptkommissar";  // cop-level 10
            case 11:
                return "Erster Polizeihauptkommissar";  // cop-level 11
            case 12:
                return "Polizeirat";  // cop-level 12
            case 13:
                return "Polizeioberrat";  // cop-level 13
            case 14:
                return "Polizeidirektor";  // cop-level 14
            case 15:
                return "Leitender Polizeidirektor";  // cop-level 15
            case 16:
                return "Polizeipräsident";  // cop-level 16
            default:
                return "Kein Rang";
        }

    }

    public static String getMedicRankAsString(int mediclevel) {

        // TODO: ersetzen mit localized Strings
        switch (mediclevel) {
            case 0:
                return "Kein Rang";  // medic-level 0
            case 1:
                return "Ersthelfer"; // medic-level 1
            case 2:
                return "Rettungshelfer";  // medic-level 2
            case 3:
                return "Rettungssanitäter";  // medic-level 3
            case 4:
                return "Notfallhelfer";  // medic-level 4
            case 5:
                return "Notfallsanitäter";  // medic-level 5
            case 6:
                return "Notarzt";  // medic-level 6
            case 7:
                return "Ausbilder";  // medic-level 7
            case 8:
                return "(Co-)Leiter";  // medic-level 8
            case 9:
                return "Leiter";  // medic-level 9; gibts wohl nicht (gleiche Rechte wie der Co-Leiter)
            default:
                return "Kein Rang";
        }
    }

    public static String getRacRankAsString(int raclevel) {

        // TODO: ersetzen mit localized Strings
        switch (raclevel) {
            case 0:
                return "Kein Rang";  // adac-level 0
            case 1:
                return "Azubi"; // adac-level 1
            case 2:
                return "Geselle";  // adac-level 2
            case 3:
                return "Mechaniker";  // adac-level 3
            case 4:
                return "Mechatroniker";  // adac-level 4
            case 5:
                return "Techniker";  // adac-level 5
            case 6:
                return "Meister";  // adac-level 6
            case 7:
                return "Ingenieur";  // adac-level 7
            case 8:
                return "(Co-)Leiter";  // adac-level 8
            case 9:
                return "Leiter";  // adac-level 9
            default:
                return "Kein Rang";
        }
    }

    public static String getCopRankSymbolAsString(int coplevel) {

        // TODO: fehlende Bilder ersetzen
        switch (coplevel) {
            case 0:
                return "cop_00";  // cop-level 0
            case 1:
                return "cop_01"; // cop-level 1
            case 2:
                return "cop_02";  // cop-level 2
            case 3:
                return "cop_03";  // cop-level 3
            case 4:
                return "cop_04";  // cop-level 4
            case 5:
                return "cop_05";  // cop-level 5
            case 6:
                return "cop_06";  // cop-level 6
            case 7:
                return "cop_07";  // cop-level 7
            case 8:
                return "cop_08";  // cop-level 8
            case 9:
                return "cop_09";  // cop-level 9
            case 10:
                return "cop_10";  // cop-level 10
            case 11:
                return "cop_11";  // cop-level 11
            case 12:
                return "cop_12";  // cop-level 12
            case 13:
                return "cop_13";  // cop-level 13
            case 14:
                return "cop_14";  // cop-level 14
            case 15:
                return "cop_15";  // cop-level 15
            case 16:
                return "cop_16";  // cop-level 16
            default:
                return "Kein Rang";
        }

    }

    public static String getJustizRankSymbolAsString(int justizlevel) {

        // TODO: fehlende Bilder ersetzen
        return "Kein Rang";
    }

    public static String getMedicRankSymbolAsString(int mediclevel) {

        // TODO: fehlende Bilder ersetzen
        switch (mediclevel) {
            case 0:
                return "medic_00";  // cop-level 0
            case 1:
                return "medic_01"; // cop-level 1
            case 2:
                return "medic_02";  // cop-level 2
            case 3:
                return "medic_03";  // cop-level 3
            case 4:
                return "medic_04";  // cop-level 4
            case 5:
                return "medic_05";  // cop-level 5
            case 6:
                return "medic_06";  // cop-level 6
            case 7:
                return "medic_07";  // cop-level 7
            case 8:
                return "medic_08";  // cop-level 8
            case 9:
                return "medic_09";  // cop-level 9
            default:
                return "Kein Rang";
        }
    }

    public static String getRacRankSymbolAsString(int raclevel) {

        // TODO: fehlende Bilder ersetzen
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
