package io.azraein.inkfx.screens;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.ActorClassTab;
import io.azraein.inkfx.controls.tab.ActorRaceTab;
import io.azraein.inkfx.controls.tab.ActorTab;
import javafx.scene.control.TabPane;

public class PluginContentEditorScreen extends PaperEditorScreen {

	private TabPane contentEditorTabPane;

	public PluginContentEditorScreen(InkFX inkFX) {
		super(inkFX);
		contentEditorTabPane = new TabPane();
		contentEditorTabPane.getTabs().add(new ActorTab(inkFX));
		contentEditorTabPane.getTabs().add(new ActorRaceTab(inkFX));
		contentEditorTabPane.getTabs().add(new ActorClassTab(inkFX));

		setCenter(contentEditorTabPane);
	}

}
