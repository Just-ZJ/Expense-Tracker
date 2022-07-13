package com.zj.android.expensetracker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomDate extends Date {

    Calendar mCalendar;

    public CustomDate(Calendar cal) {
        super(cal.getTimeInMillis());
        mCalendar = cal;
    }

    /**
     * Converts a number from 1-7, to a day in a week that it corresponds to
     *
     * @return day of the week
     */
    private String getDayString() {
        switch (mCalendar.get(Calendar.DATE)) {
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
        }
        return null;
    }

    /**
     * Converts a number from 0-11, to the month that it corresponds to
     *
     * @return month
     */
    private String getMonthString() {
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
            default:
                return "December";
        }
    }

    /**
     * Converts the month in text to a number from 0-11
     *
     * @return month in int
     */
    private int getMonthInt(String month) {
        switch (month) {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
            default:
                return 11;
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
}
