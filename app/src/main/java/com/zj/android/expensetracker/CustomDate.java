package com.zj.android.expensetracker;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDate extends Date {

    Calendar mCalendar;

    public CustomDate(Calendar cal) {
        mCalendar = cal;
    }

    /**
     * Format of date (ISO 8601): 2022-11-09 22:11:35
     */
    public CustomDate(String date) {
        String[] tmp = date.split(" ");
        String[] dates = tmp[0].split("-"), time = tmp[1].split(":");
        mCalendar = Calendar.getInstance();
        mCalendar.set(Integer.parseInt(dates[0]),
                Integer.parseInt(dates[1]) - 1,
                Integer.parseInt(dates[2]),
                Integer.parseInt(time[0]),
                Integer.parseInt(time[1]),
                Integer.parseInt(time[2])
        );
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    /**
     * Converts a number from 1-7, to a day in a week that it corresponds to
     *
     * @return day of the week
     */
    public String getDayString() {
        switch (mCalendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return null;
        }
    }

    /**
     * Converts a number from 0-11, to the month that it corresponds to
     *
     * @return month
     */
    public String getMonthString() {
        switch (mCalendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                return "January";
            case Calendar.FEBRUARY:
                return "February";
            case Calendar.MARCH:
                return "March";
            case Calendar.APRIL:
                return "April";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "June";
            case Calendar.JULY:
                return "July";
            case Calendar.AUGUST:
                return "August";
            case Calendar.SEPTEMBER:
                return "September";
            case Calendar.OCTOBER:
                return "October";
            case Calendar.NOVEMBER:
                return "November";
            case Calendar.DECEMBER:
                return "December";
            default:
                return null;
        }
    }


    /**
     * Returns a string that is in the format of eg. Friday, 5/6/2022
     *
     * @return a string that is in the format of "Friday, 5/6/2022"
     */
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s, %d/%d/%d",
                getDayString(),
                mCalendar.get(Calendar.MONTH) + 1,
                mCalendar.get(Calendar.DATE),
                mCalendar.get(Calendar.YEAR));
    }

    /**
     * @return string in ISO 8601 format
     */
    public String toDatabaseString() {
        return String.format(Locale.ENGLISH, "%04d-%02d-%02d %02d:%02d:%02d",
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH) + 1,
                mCalendar.get(Calendar.DATE),
                mCalendar.get(Calendar.HOUR_OF_DAY),
                mCalendar.get(Calendar.MINUTE),
                mCalendar.get(Calendar.SECOND));
    }
}
