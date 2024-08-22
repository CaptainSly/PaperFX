package io.azraein.inkfx.system.io.scripting.lib;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.inkfx.system.Ink;
import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.io.scripting.lib.PaperUtilFunctions.PaperLuaLocationSetter;
import io.azraein.inkfx.system.io.scripting.lib.PaperUtilFunctions.PaperLuaSetCalendarDate;
import io.azraein.inkfx.system.io.scripting.lib.PaperUtilFunctions.PaperLuaSetCalendarDaysMonths;
import io.azraein.inkfx.system.io.scripting.lib.PaperUtilFunctions.PaperLuaSetCalendarTime;
import io.azraein.inkfx.system.io.scripting.lib.PaperUtilFunctions.PaperLuaSetCalendarYear;

public class PaperLib extends TwoArgFunction {

    private LuaTable paperTable;

    @Override
    public LuaValue call(LuaValue mod, LuaValue env) {
        // Create the Paper Table
        paperTable = new LuaTable();
        paperTable.set("_VERSION", Ink.VERSION);
        paperTable.set("database", CoerceJavaToLua.coerce(Paper.DATABASE));
        paperTable.set("ppl", CoerceJavaToLua.coerce(Paper.PPL));
        paperTable.set("ini", CoerceJavaToLua.coerce(Paper.INI));
        paperTable.set("calendar", CoerceJavaToLua.coerce(Paper.CALENDAR));
        paperTable.set("world", CoerceJavaToLua.coerce(Paper.PAPER_WORLD_PROPERTY));
        paperTable.set("player", CoerceJavaToLua.coerce(Paper.PAPER_PLAYER_PROPERTY));

        // Location Helper Methods (functions)
        paperTable.set("setLocation", new PaperLuaLocationSetter());

        // Calendar Helper Methods (functions)
        paperTable.set("setCalendarDM", new PaperLuaSetCalendarDaysMonths());
        paperTable.set("setCalendarYear", new PaperLuaSetCalendarYear());
        paperTable.set("setCalendarTime", new PaperLuaSetCalendarTime());
        paperTable.set("setCalendarDate", new PaperLuaSetCalendarDate());

        // Set Globals
        setGlobal(env, "paper", paperTable);
        setGlobal(env, "class", new LuaClassFunction());
        return paperTable;
    }

    private void setGlobal(LuaValue env, String globalName, LuaValue globalValue) {
        env.set(globalName, globalValue);
        env.get("package").get("loaded").set(globalName, globalValue);
    }

    public LuaTable getPaperTable() {
        return paperTable;
    }

}
