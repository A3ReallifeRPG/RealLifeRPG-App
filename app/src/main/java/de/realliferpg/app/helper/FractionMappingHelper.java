package de.realliferpg.app.helper;

public class FractionMappingHelper {

    public static String getCopRankAsString(int coplevel) {

        // TODO: ersetzen mit localized Strings
        switch (coplevel) {
            case 0:
                return "Praktikant";  // cop-level 0
            case 1:
                return "Justiz"; // cop-level 1
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
                return"Polizeikommissar";  // cop-level 8
            case 9:
                return"Polizeioberkommissar";  // cop-level 9
            case 10:
                return"Polizeihauptkommissar";  // cop-level 10
            case 11:
                return"Erster Polizeihauptkommissar";  // cop-level 11
            case 12:
                return"Polizeirat";  // cop-level 12
            case 13:
                return"Polizeioberrat";  // cop-level 13
            case 14:
                return"Polizeidirektor";  // cop-level 14
            case 15:
                return"Leitender";  // cop-level 15
            case 16:
                return"Polizeipräsident";  // cop-level 16
            default:
                return "Kein Rang";
        }

    }

    public static String getMedicRankAsString(int mediclevel) {

        // TODO: ersetzen mit localized Strings
        switch (mediclevel) {
            case 0:
                return "Praktikant";  // medic-level 0
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
                return"Co-Leiter";  // medic-level 8
            case 9:
                return"Leiter";  // medic-level 9
            default:
                return "Kein Rang";
        }
    }

    public static String getRacRankAsString(int raclevel) {

        // TODO: ersetzen mit localized Strings
        switch (raclevel) {
            case 0:
                return "Praktikant";  // adac-level 0
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
                return"Co-Leiter";  // adac-level 8
            case 9:
                return"Leiter";  // adac-level 9
            default:
                return "Kein Rang";
        }
    }
}
