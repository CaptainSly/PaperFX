package io.azraein.paperfx.system.io.scripting.lib;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.system.Paper;

public class PaperLib extends TwoArgFunction {

    private LuaTable paperTable;

    @Override
    public LuaValue call(LuaValue mod, LuaValue env) {
        // Create the Paper Table
        paperTable = new LuaTable();
        paperTable.set("_VERSION", PaperFX.PAPER_VERSION);
        paperTable.set("database", CoerceJavaToLua.coerce(Paper.DATABASE));
        paperTable.set("ppl", CoerceJavaToLua.coerce(Paper.PPL));
        paperTable.set("ini", CoerceJavaToLua.coerce(Paper.INI));
        paperTable.set("location", CoerceJavaToLua.coerce(Paper.PAPER_LOCATION_PROPERTY));
        paperTable.set("calendar", CoerceJavaToLua.coerce(Paper.CALENDAR));
        paperTable.set("world", CoerceJavaToLua.coerce(Paper.PAPER_WORLD_PROPERTY));
        paperTable.set("player", CoerceJavaToLua.coerce(Paper.PAPER_PLAYER_PROPERTY));

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
