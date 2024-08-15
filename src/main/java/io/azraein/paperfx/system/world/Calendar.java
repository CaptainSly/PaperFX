package io.azraein.paperfx.system.world;

import java.io.Serializable;

import io.azraein.paperfx.system.Utils;

public class Calendar implements Serializable {

    private static final long serialVersionUID = 1861797232380365886L;

    private int currentMinute, currentHour, currentDay, currentMonth, currentYear;

    private int currentDaySelector = 0;
    private int currentMonthSelector = 0;

    private int daysInMonth = 24;

    private String era = "";

    private String amPmString = "AM";

    private String[] months = { "Janurary", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };

    private String[] days = { "Monday", "Tuesdsay", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

    public Calendar() {
        currentMinute = 0;
        currentHour = 0;
        currentDay = 1;
        currentMonth = 1;
        currentYear = 1;
    }

    public void update() {
        currentMinute++;
        if (currentMinute >= 60) { // We've gone over an hour
            currentMinute = 0; // Set minutes back to 0
            currentHour++; // Increase hour by 1

            if (currentHour >= 24) { // It's been an entire day!
                currentHour = 0; // Set the hour back to 0
                currentDay++; // Increase day by 1
                currentDaySelector++; // Increase the dayselector by 1

                if (currentDaySelector >= days.length) // Is the dayselector greater than or equal to it's length?
                    currentDaySelector = 0; // Set it back to 0

                if (currentDay > daysInMonth) { // Did the days surpass the amount in a month?
                    currentDay = 1; // Back to day 1
                    currentMonth++; // increase the month by 1
                    currentMonthSelector++; // Increase the month selector by 1

                    if (currentMonthSelector >= months.length) // Is the monthselector greater than or equal to it's
                                                               // length?
                        currentMonthSelector = 0; // Set it back to 0

                    if (currentMonth > months.length) { // Did the amount of days surpass the amount of months the
                                                        // exist?
                        currentMonth = 1; // Welp set it back to 1
                        currentYear++; // Increase the year by 1
                    }
                }
            }
        }

        amPmString = (currentHour >= 12) ? "PM" : "AM";
    }

    public void setDays(String... days) {
        this.days = days;
    }

    public void setMonths(String... months) {
        this.months = months;
    }

    public void setEra(String era) {
        this.era = era;
    }

    public void setYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
        currentMonthSelector = currentMonth - 1;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
        currentDaySelector = currentDay - 1;
    }

    public void setCurrentMinute(int currentMinute) {
        this.currentMinute = currentMinute;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public String getTimeAsString() {
        return String.format("%02d:%02d %s", getNormalHour(), currentMinute, amPmString);
    }

    public String getDateAsString() {
        return String.format("%s the %s of %s, %04d%s", getDay(), Utils.getSuffix(currentDay), getMonth(), currentYear,
                era);
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getNormalHour() {
        int curTime = currentHour;
        if (curTime > 12)
            curTime -= 12;

        if (curTime == 0)
            curTime = 12;

        return curTime;
    }

    public String getAmPmString() {
        return amPmString;
    }

    public String getDay() {
        return days[currentDaySelector];
    }

    public String getMonth() {
        return months[currentMonthSelector];
    }

    public String[] getMonths() {
        return months;
    }

    public String[] getDays() {
        return days;
    }

}
