package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public abstract class PaperEditorTab extends Tab {

	protected InkFX inkFX;

	protected BorderPane content;

	public PaperEditorTab(InkFX inkFX) {
		this.inkFX = inkFX;
		setClosable(false);

		content = new BorderPane();

		setContent(content);
	}

}
