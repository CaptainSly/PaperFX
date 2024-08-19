package io.azraein.inkfx.screens;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.groups.ItemGroupTab;
import io.azraein.inkfx.controls.tab.groups.LocationGroupTab;
import io.azraein.inkfx.controls.tab.groups.NpcGroupTab;
import io.azraein.inkfx.controls.tab.groups.QuestGroupTab;
import io.azraein.inkfx.controls.tab.groups.SystemGroupTab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabDragPolicy;

public class PluginContentEditorScreen extends PaperEditorScreen {

	public PluginContentEditorScreen(InkFX inkFX) {
		super(inkFX);
		TabPane contentEditorTabPane = new TabPane();
		contentEditorTabPane.setTabDragPolicy(TabDragPolicy.REORDER);
		contentEditorTabPane.getTabs().add(new NpcGroupTab(inkFX));
		contentEditorTabPane.getTabs().add(new LocationGroupTab(inkFX));
		contentEditorTabPane.getTabs().add(new ItemGroupTab(inkFX));
		contentEditorTabPane.getTabs().add(new QuestGroupTab(inkFX));
		contentEditorTabPane.getTabs().add(new SystemGroupTab(inkFX));

		setCenter(contentEditorTabPane);
	}

}
