package com.example.t2m.moneytracker.utilities;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private DateUtils() {
    }

    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    public static int getMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    public static int compareYear(Date date1,Date date2) {
        int year1 = DateUtils.getYear(date1);
        int year2 = DateUtils.getYear(date2);
        return year1 - year2;
    }

    public static int compareMonth(Date date1, Date date2) {
        int month1 = DateUtils.getMonth(date1);
        int month2 = DateUtils.getMonth(date2);
        return month1 - month2;
    }
    public static int compareDayOfMonth(Date date1, Date date2) {
        int day1 = DateUtils.getDayOfMonth(date1);
        int day2 = DateUtils.getDayOfMonth(date2);
        return day1 - day2;
    }

    public static int compareDate(Date date1, Date date2) {
        int result = compareYear(date1,date2);
        if(result == 0) {
            result = compareMonth(date1,date2);
            if(result ==0 ) {
                return compareDayOfMonth(date1,date2);
            }
            else {
                return result;
            }
        }
        else {
            return result;
        }
    }
}
