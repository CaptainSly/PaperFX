package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabDragPolicy;

public class LocationGroupTab extends PaperEditorTab {

    public LocationGroupTab(InkFX inkFX) {
        super(inkFX);
        setText("Location Editor");
        setClosable(false);

        TabPane tb = new TabPane();
        tb.setTabDragPolicy(TabDragPolicy.REORDER);

        setContent(tb);
    }

}
