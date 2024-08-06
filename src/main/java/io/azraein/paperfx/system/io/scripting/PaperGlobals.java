package io.azraein.paperfx.system.io.scripting;

import java.io.InputStream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;
import org.luaj.vm2.lib.jse.LuajavaLib;

import io.azraein.paperfx.system.io.scripting.lib.PaperLib;

public class PaperGlobals {

    private final Globals paperGlobals = new Globals();

    public PaperGlobals() {
        // TODO Probably Sandbox the Scripting Engine.
        paperGlobals.load(new JseBaseLib());
        paperGlobals.load(new PackageLib());
        paperGlobals.load(new Bit32Lib());
        paperGlobals.load(new TableLib());
        paperGlobals.load(new StringLib());
        paperGlobals.load(new CoroutineLib());
        paperGlobals.load(new JseMathLib());
        paperGlobals.load(new JseIoLib());
        paperGlobals.load(new JseOsLib());
        paperGlobals.load(new LuajavaLib());
        paperGlobals.load(new PaperLib());
        LoadState.install(paperGlobals);
        LuaC.install(paperGlobals);

        paperGlobals.load("package.path = '/paper/data/scripts/?.lua;'").call();
    }

    public Globals getPaperGlobals() {
        return paperGlobals;
    }

}