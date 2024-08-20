package io.azraein.penfx.controls.tab.groups;

import io.azraein.penfx.PenFX;
import io.azraein.penfx.controls.tab.systemTabs.ActionTab;
import io.azraein.penfx.controls.tab.systemTabs.LuaTab;

public class SystemGroupTab extends PaperGroupTab {

    public SystemGroupTab(PenFX penFX) {
        super(penFX);
        setText("System Editors");
        addTab(new ActionTab(penFX));
        addTab(new LuaTab(penFX));

    }

}
