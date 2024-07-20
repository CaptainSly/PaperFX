package io.azraein.inkfx.screens;

import io.azraein.inkfx.InkFX;
import javafx.scene.layout.BorderPane;

public abstract class PaperEditorScreen extends BorderPane {

	protected InkFX inkFX;
	
	public PaperEditorScreen(InkFX inkFX) {
		this.inkFX = inkFX;
	}
	
}
