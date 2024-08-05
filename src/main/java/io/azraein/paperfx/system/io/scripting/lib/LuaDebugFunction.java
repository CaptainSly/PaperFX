package io.azraein.paperfx.system.io.scripting.lib;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.tinylog.Logger;

public class LuaDebugFunction extends OneArgFunction {

    @Override
    public LuaValue call(LuaValue arg) {
        String message = arg.checkjstring(); // Ensure the argument is a string
        Logger.debug(message); // Call your Logger.debug method
        return LuaValue.NIL; // Lua functions often return NIL if there's no meaningful return value
    }

}
