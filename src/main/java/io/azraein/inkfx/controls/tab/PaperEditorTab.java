package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import javafx.scene.control.Tab;

public abstract class PaperEditorTab extends Tab {

	protected InkFX inkFX;

	public PaperEditorTab(InkFX inkFX) {
		this.inkFX = inkFX;
	}

}
