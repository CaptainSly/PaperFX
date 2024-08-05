package io.azraein.paperfx.system.io.scripting;

import org.tinylog.Logger;

import io.azraein.paperfx.system.io.SaveSystem;

public class ScriptEngine {

    private PaperGlobals paperGlobals;

    public ScriptEngine() {
        paperGlobals = new PaperGlobals();
    }

    public void runScript(String scriptName) {
        Logger.debug("Running Lua Script: " + scriptName);
        paperGlobals.getPaperGlobals().loadfile(SaveSystem.PAPER_SCRIPT_FOLDER + scriptName).call();
    }

    public PaperGlobals getPaperGlobals() {
        return paperGlobals;
    }

}
