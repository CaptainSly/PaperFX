package io.azraein.penfx.controls.tab;

import io.azraein.penfx.PenFX;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public abstract class PaperEditorTab extends Tab {

	protected PenFX penFX;

	protected BorderPane content;

	public PaperEditorTab(PenFX penFX) {
		this.penFX = penFX;
		setClosable(false);

		content = new BorderPane();

		setContent(content);
	}

}
