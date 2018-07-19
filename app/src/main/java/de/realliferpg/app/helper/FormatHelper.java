package de.realliferpg.app.helper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormatHelper {

    public String formatCurrency(int input){

        DecimalFormat format = (DecimalFormat)DecimalFormat.getNumberInstance(Locale.GERMAN);

        return format.format(input) + " $";
    }

    public String toDateTime(Date input){

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        return format.format(input);
    }

    public Date getApiDate(String input){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDate;
    }
}
