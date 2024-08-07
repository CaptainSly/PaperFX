package io.azraein.paperfx.system.io.scripting.lib;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.system.Paper;

public class PaperLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue mod, LuaValue env) {
        // Create the Paper Table
        LuaTable paper = new LuaTable();
        paper.set("_VERSION", PaperFX.PAPER_VERSION);
        paper.set("database", CoerceJavaToLua.coerce(Paper.DATABASE));
        paper.set("ppl", CoerceJavaToLua.coerce(Paper.PPL));
        paper.set("ini", CoerceJavaToLua.coerce(Paper.INI));
        paper.set("location", CoerceJavaToLua.coerce(Paper.PAPER_LOCATION_PROPERTY));

        // Set Globals
        setGlobal(env, "paper", paper);
        setGlobal(env, "class", new LuaClassFunction());
        return paper;
    }

    private void setGlobal(LuaValue env, String globalName, LuaValue globalValue) {
        env.set(globalName, globalValue);
        env.get("package").get("loaded").set(globalName, globalValue);
    }

}
