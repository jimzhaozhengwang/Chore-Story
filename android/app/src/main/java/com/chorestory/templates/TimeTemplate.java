package com.chorestory.templates;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTemplate {
    private int year;
    private int month;
    private int day;

    private int hour;
    private int minute;

    public TimeTemplate(String time, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        DateTime formattedDate = new DateTime(); // use today as a failsafe

        try {
            formattedDate = ISODateTimeFormat.dateTimeParser().parseDateTime(time);
        } catch (Exception e) {
            Log.d("BUG", "Retrofit time parsing error");
            Log.d("BUG", e.getMessage());
        }

        year = formattedDate.getYear();
        month = formattedDate.getMonthOfYear();
        day = formattedDate.getDayOfMonth();
        hour = formattedDate.getHourOfDay();
        minute = formattedDate.getMinuteOfHour();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
