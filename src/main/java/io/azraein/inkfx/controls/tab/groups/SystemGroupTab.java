package io.azraein.inkfx.controls.tab.groups;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.systemTabs.ActionTab;
import io.azraein.inkfx.controls.tab.systemTabs.LuaTab;

public class SystemGroupTab extends PaperGroupTab {

    public SystemGroupTab(InkFX inkFX) {
        super(inkFX);
        setText("System Editors");
        addTab(new ActionTab(inkFX));
        addTab(new LuaTab(inkFX));

    }

}
