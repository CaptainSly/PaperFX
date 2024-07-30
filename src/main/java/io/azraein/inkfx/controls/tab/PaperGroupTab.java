package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabDragPolicy;

public abstract class PaperGroupTab extends Tab {

    protected InkFX inkFX;
    protected TabPane tb;

    public PaperGroupTab(InkFX inkFX) {
        this.inkFX = inkFX;
        setClosable(false);

        tb = new TabPane();
        tb.setTabDragPolicy(TabDragPolicy.REORDER);
        setContent(tb);
    }

    protected void addTab(Tab tab) {
        tb.getTabs().add(tab);
    }

}