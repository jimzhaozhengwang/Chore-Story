package com.chorestory.templates;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTemplate {
    private String year;
    private String month;
    private String day;

    private String hour;
    private String minute;

    public TimeTemplate(String time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date formattedDate = new Date(); // use today as a failsafe

        try {
            formattedDate = formatter.parse(time);
        } catch (ParseException e) {
            Log.d("BUG", "Retrofit time parsing error");
            Log.d("BUG", e.getMessage());
        }

        year = new SimpleDateFormat("yyyy").format(formattedDate);
        month = new SimpleDateFormat("MM").format(formattedDate);
        day = new SimpleDateFormat("dd").format(formattedDate);
        hour = new SimpleDateFormat("HH").format(formattedDate);
        minute = new SimpleDateFormat("mm").format(formattedDate);
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }
}
