package io.azraein.paperfx.system.io.scripting.lib;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class LuaClassFunction extends TwoArgFunction {

    private LuaValue base = NIL;
    private LuaValue init = NIL;

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        // A new class instance
        LuaTable classInstance = new LuaTable();
        base = arg1; // set the base
        init = arg2; // set the init

        if (init.isnil() && base.isfunction()) {
            init = base;
            base = NIL;
        } else if (base.istable()) {
            LuaValue k = NIL;
            while (true) {
                Varargs n = base.next(k);
                if ((k = n.arg1()).isnil())
                    break;

                LuaValue v = n.arg(2);
                classInstance.set(k, v);
            }

            classInstance.set("_base", base);
        }

        classInstance.set("__index", classInstance);

        LuaTable metatable = new LuaTable();
        metatable.set("__call", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaTable object = new LuaTable();
                object.setmetatable(classInstance);

                if (!init.isnil()) {
                    init.invoke(varargsOf(object, args.subargs(2)));
                } else {
                    if (!base.isnil() && !base.get("init").isnil()) {
                        base.get("init").invoke(varargsOf(object, args.subargs(2)));
                    }
                }

                return object;
            }
        });

        classInstance.set("init", init);
        classInstance.set("is_a", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue klass) {
                LuaValue meta = self.getmetatable();
                while (!meta.isnil()) {
                    if (meta == klass)
                        return TRUE;

                    meta = meta.get("_base");
                }

                return FALSE;
            }
        });

        classInstance.setmetatable(metatable);
        return classInstance;
    }
}
