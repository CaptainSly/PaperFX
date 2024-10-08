package io.azraein.inkfx.system.io.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.LuajavaLib;
import org.tinylog.Logger;

import io.azraein.inkfx.system.io.SaveSystem;
import io.azraein.inkfx.system.io.scripting.lib.PaperLib;

public class ScriptEngine {

    private final Globals paperGlobals = new Globals();

    private PaperLib paperLib;

    public ScriptEngine() {
        paperLib = new PaperLib();

        paperGlobals.load(new JseBaseLib());
        paperGlobals.load(new PackageLib());
        paperGlobals.load(new Bit32Lib());
        paperGlobals.load(new TableLib());
        paperGlobals.load(new StringLib());
        paperGlobals.load(new CoroutineLib());
        paperGlobals.load(new JseMathLib());
        paperGlobals.load(new LuajavaLib());
        paperGlobals.load(paperLib);
        LoadState.install(paperGlobals);
        LuaC.install(paperGlobals);

        paperGlobals.load("package.path = package.path..';paper/data/scripts/?.lua;'").call();
    }

    public void setGlobal(String globalName, LuaValue globalValue) {
        paperGlobals.set(globalName, globalValue);
        paperGlobals.get("package").get("loaded").set(globalName, globalValue);
    }

    public void setPaperGlobal(String globalName, LuaValue globalValue) {
        paperLib.getPaperTable().set(globalName, globalValue);
    }

    public LuaValue runScript(String scriptName) {
        if (!scriptName.contains(".lua"))
            scriptName += ".lua";
        return getPaperGlobals().get("dofile").call(LuaValue.valueOf(SaveSystem.PAPER_SCRIPT_FOLDER + scriptName));
    }

    public Varargs runFunction(String scriptName, String functionName) {
        if (!scriptName.contains(".lua"))
            scriptName += ".lua";
        getPaperGlobals().get("dofile").call(LuaValue.valueOf(SaveSystem.PAPER_SCRIPT_FOLDER + scriptName));
        LuaValue function = getPaperGlobals().get(functionName);
        return function.call();
    }

    public Varargs runFunction(String scriptName, String functionName, LuaValue... arguments) {
        if (!scriptName.contains(".lua"))
            scriptName += ".lua";
        getPaperGlobals().get("dofile").call(LuaValue.valueOf(SaveSystem.PAPER_SCRIPT_FOLDER + scriptName));
        LuaValue function = getPaperGlobals().get(functionName);
        return function.invoke(arguments);
    }

    public Globals getPaperGlobals() {
        return paperGlobals;
    }

}
