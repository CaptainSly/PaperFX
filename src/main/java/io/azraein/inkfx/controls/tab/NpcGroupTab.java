package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.actorTabs.ActorClassTab;
import io.azraein.inkfx.controls.tab.actorTabs.ActorRaceTab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabDragPolicy;

public class NpcGroupTab extends PaperEditorTab {

    public NpcGroupTab(InkFX inkFX) {
        super(inkFX);
        setText("NPC Editor");
        setClosable(false);

        TabPane tb = new TabPane();
        tb.setTabDragPolicy(TabDragPolicy.REORDER);
		tb.getTabs().add(new ActorClassTab(inkFX));
		tb.getTabs().add(new ActorRaceTab(inkFX));

        setContent(tb);
    }

}
