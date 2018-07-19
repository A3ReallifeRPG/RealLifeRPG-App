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

    public String formatApiDate(String input){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = format.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliDate = newDate.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milliDate);
        String test = format.format(cal.getTime());

        return "";
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
