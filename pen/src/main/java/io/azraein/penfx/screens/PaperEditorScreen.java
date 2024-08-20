package io.azraein.penfx.screens;

import io.azraein.penfx.PenFX;
import javafx.scene.layout.BorderPane;

public abstract class PaperEditorScreen extends BorderPane {

	protected PenFX inkFX;
	
	public PaperEditorScreen(PenFX penFX) {
		this.inkFX = penFX;
	}
	
}
