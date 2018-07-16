package de.realliferpg.app.helper;

import java.text.DecimalFormat;
import java.util.Locale;

public class FormatHelper {

    public String formatCurrency(int input){

        DecimalFormat format = (DecimalFormat)DecimalFormat.getNumberInstance(Locale.GERMAN);

        return format.format(input) + " $";
    }
}
