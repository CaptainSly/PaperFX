package io.azraein.penfx.controls.tab.groups;

import io.azraein.penfx.PenFX;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabDragPolicy;

public abstract class PaperGroupTab extends Tab {

    protected PenFX penFX;
    protected TabPane tb;

    public PaperGroupTab(PenFX penFX) {
        this.penFX = penFX;
        setClosable(false);

        tb = new TabPane();
        tb.setTabDragPolicy(TabDragPolicy.REORDER);
        setContent(tb);
    }

    protected void addTab(Tab tab) {
        tb.getTabs().add(tab);
    }

}