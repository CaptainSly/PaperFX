package io.azraein.inkfx.screens;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.ActorClassTab;
import io.azraein.inkfx.controls.tab.ActorRaceTab;
import io.azraein.inkfx.controls.tab.NpcActorTab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabDragPolicy;

public class PluginContentEditorScreen extends PaperEditorScreen {

	private TabPane contentEditorTabPane;

	public PluginContentEditorScreen(InkFX inkFX) {
		super(inkFX);
		contentEditorTabPane = new TabPane();
		contentEditorTabPane.setTabDragPolicy(TabDragPolicy.REORDER);
		contentEditorTabPane.getTabs().add(new NpcActorTab(inkFX));
		contentEditorTabPane.getTabs().add(new ActorClassTab(inkFX));
		contentEditorTabPane.getTabs().add(new ActorRaceTab(inkFX));

		setCenter(contentEditorTabPane);
	}

}
