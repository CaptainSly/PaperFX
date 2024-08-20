package io.azraein.paperfx.ui.screens;

import io.azraein.paperfx.PaperFX;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public abstract class PaperScreen extends Parent {

	protected PaperFX paperFX;
	protected BorderPane container;

	public PaperScreen(PaperFX paperFX) {
		this.paperFX = paperFX;
		container = new BorderPane();
		getChildren().add(container);
	}

	public abstract void init();

	protected void setTop(Node node) {
		container.setTop(node);
	}

	protected void setLeft(Node node) {
		container.setLeft(node);
	}

	protected void setRight(Node node) {
		container.setRight(node);
	}

	protected void setBottom(Node node) {
		container.setBottom(node);
	}

	protected void setCenter(Node node) {
		container.setCenter(node);
	}

}
