package com.skhu.bobinlee.skhuapp.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
    //	yyyy-MM-dd'T'hh:mm:ssZ - facebook
    public static final String dateForm = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static String dateToString(Timestamp timestamp){
        DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern(dateForm);
        return dateStringFormat.print(timestamp.getTime());
    }

    public static Timestamp stringToDate(String string) {
        DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern(dateForm);
        DateTime time = dateStringFormat.parseDateTime(string);

        return new Timestamp(time.getMillis());
    }

    public static String currentTime(){
        Calendar calendar = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        return dateToString(timestamp);
    }
}