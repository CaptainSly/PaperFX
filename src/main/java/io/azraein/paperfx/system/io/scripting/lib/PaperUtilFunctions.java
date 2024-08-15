package io.azraein.paperfx.system.io.scripting.lib;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import io.azraein.paperfx.system.Paper;

public class PaperUtilFunctions {

    // Location
    public static class PaperLuaLocationSetter extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue arg) {
            String locationId = arg.tojstring();
            Paper.PAPER_LOCATION_PROPERTY.set(Paper.DATABASE.getLocation(locationId));
            return NIL;
        }

    }

    // Calendar Stuff
    public static class PaperLuaSetCalendarDaysMonths extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            LuaTable dayTable = ((LuaTable) arg1);
            LuaTable monthTable = ((LuaTable) arg2);

            String[] days = new String[dayTable.length()];
            String[] months = new String[monthTable.length()];

            for (int i = 0; i < days.length; i++) {
                days[i] = dayTable.get(i + 1).tojstring();
            }

            for (int i = 0; i < months.length; i++) {
                months[i] = monthTable.get(i + 1).tojstring();
            }

            Paper.CALENDAR.setDays(days);
            Paper.CALENDAR.setMonths(months);
            return NIL;
        }

    }

    public static class PaperLuaSetCalendarYear extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            String era;
            if (arg2.isnil()) {
                era = "";
            } else
                era = arg2.tojstring();

            int year = arg1.toint();

            Paper.CALENDAR.setEra(era);
            Paper.CALENDAR.setYear(year);
            return NIL;
        }

    }

    public static class PaperLuaSetCalendarTime extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue minute, LuaValue hour) {
            Paper.CALENDAR.setCurrentMinute(minute.toint());
            Paper.CALENDAR.setCurrentHour(hour.toint());
            return NIL;
        }

    }

    public static class PaperLuaSetCalendarDate extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue day, LuaValue month) {
            Paper.CALENDAR.setCurrentDay(day.toint());
            Paper.CALENDAR.setCurrentMonth(month.toint());
            return NIL;
        }

    }

}
