package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.itemTabs.ItemTab;

public class ItemGroupTab extends PaperGroupTab {

    public ItemGroupTab(InkFX inkFX) {
        super(inkFX);
        setText("Item Editors");
        addTab(new ItemTab(inkFX));
    }

}
