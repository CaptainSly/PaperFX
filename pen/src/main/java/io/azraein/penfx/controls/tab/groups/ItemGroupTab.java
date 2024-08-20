package io.azraein.penfx.controls.tab.groups;

import io.azraein.penfx.PenFX;
import io.azraein.penfx.controls.tab.itemTabs.ItemTab;

public class ItemGroupTab extends PaperGroupTab {

    public ItemGroupTab(PenFX penFX) {
        super(penFX);
        setText("Item Editors");
        addTab(new ItemTab(penFX));
    }

}
