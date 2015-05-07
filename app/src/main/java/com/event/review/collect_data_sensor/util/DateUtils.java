package com.event.review.collect_data_sensor.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class DateUtils {
    public static String getSensorDate(long millis, boolean includingSecond) {
        TimeZone tz = TimeZone.getDefault();
        DateFormat df = includingSecond ? new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss a") : new SimpleDateFormat("dd-MM-yyyy, HH:mm a");
        df.setTimeZone(tz);
        String timeString = df.format(millis);
        return timeString;
    }

    public static String getCurrentDate(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aaa");
        Date date = new Date(millis);
        return format.format(date);
    }
}
