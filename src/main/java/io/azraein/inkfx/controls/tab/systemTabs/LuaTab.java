package io.azraein.inkfx.controls.tab.systemTabs;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.PaperEditorTab;

// TODO: Get basic Lua Syntax Highlighting Started and we can call this finished
/*
 * Ultimately it'd be nice to have a few more features than just being
 * a glorified text editor, but it'd honestly add a lot of bloat and the 
 * text editor part itself is already going to be a hassle. 
 */


public class LuaTab extends PaperEditorTab {

    public LuaTab(InkFX inkFX) {
        super(inkFX);
        setText("Lua Editor");
    }

}
