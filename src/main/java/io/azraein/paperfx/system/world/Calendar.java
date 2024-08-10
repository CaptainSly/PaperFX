package io.azraein.paperfx.system.world;

import java.io.Serializable;

import io.azraein.paperfx.system.Utils;

public class Calendar implements Serializable {

    private static final long serialVersionUID = 1861797232380365886L;

    private int currentMinute, currentHour, currentDay, currentWeek, currentMonth, currentYear;

    private int currentDaySelector = 0;
    private int currentMonthSelector = 0;

    private int daysInMonth = 24;
    private int weeksInMonth = 4;

    private String amPmString = "AM";

    private String[] months = { "Janurary", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December" };

    private String[] days = { "Monday", "Tuesdsay", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

    public Calendar() {
        currentMinute = 0;
        currentHour = 12;
        currentDay = 1;
        currentWeek = 1;
        currentMonth = 1;
        currentYear = 1;
    }

    public void update() {
        currentMinute++;
        if (currentMinute >= 60) {
            currentMinute = 0;
            currentHour++;
            if (currentHour >= 24) {
                currentHour = 0;
                currentDay++;
                if (currentDay > daysInMonth) {
                    currentDay = 1;
                    currentWeek++;
                    currentDaySelector++;

                    if (currentDaySelector >= days.length)
                        currentDaySelector = 0;

                    if (currentWeek > weeksInMonth) {
                        currentWeek = 1;
                        currentMonth++;
                        currentMonthSelector++;

                        if (currentMonthSelector >= months.length)
                            currentMonthSelector = 0;

                        if (currentMonth > months.length) {
                            currentMonth = 1;
                            currentYear++;
                        }
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

    public String getTimeAsString() {
        return String.format("%02d:%02d %s", getNormalHour(), currentMinute, amPmString);
    }

    public String getDateAsString() {
        return String.format("%s the %s of %s, %04d", getDay(), Utils.getSuffix(currentDay), getMonth(), currentYear);
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

    public int getCurrentWeek() {
        return currentWeek;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getWeeksInMonth() {
        return weeksInMonth;
    }

    public int getNormalHour() {
        int curTime = currentHour;
        if (curTime > 12)
            curTime -= 12;

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
