package com.example.t2m.moneytracker.utilities;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private DateUtils() {
    }

    public static int compareYear(Date date1,Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int year1 =cal1.get(Calendar.YEAR);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int year2 =cal1.get(Calendar.YEAR);

        return year1 - year2;
    }

    public static int compareMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int month1 =cal1.get(Calendar.MONTH);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int month2 =cal1.get(Calendar.MONTH);

        return month1 - month2;
    }

    public static int compareDayOfMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int day1 =cal1.get(Calendar.DAY_OF_MONTH);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day2 =cal1.get(Calendar.DAY_OF_MONTH);

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
