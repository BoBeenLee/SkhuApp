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
    public static final String dateForm1 = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String dateForm2 = "yyyy-MM-dd";

    public static String dateToString(Timestamp timestamp, String dateForm){
        DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern(dateForm);
        return dateStringFormat.print(timestamp.getTime());
    }

    public static Timestamp stringToDate(String string, String dateForm) {
        DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern(dateForm);
        DateTime time = dateStringFormat.parseDateTime(string);

        return new Timestamp(time.getMillis());
    }

    public static String currentTime(String dateForm){
        Calendar calendar = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        return dateToString(timestamp, dateForm);
    }
}