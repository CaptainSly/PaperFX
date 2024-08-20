package io.azraein.penfx.dialog;

import io.azraein.penfx.PenFX;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AboutAlert {

	private final Alert alert;


	public AboutAlert() {
		alert = new Alert(AlertType.NONE);
		alert.setTitle("About Ink");
		alert.setHeaderText("You need some Ink to write on Paper");
		alert.setContentText("Ink Version: " + PenFX.INK_VERSION + "\n"
				+ "Ink was made with JavaFX, TinyLog, Gson, Ini4J, and Zstd\n");
		alert.getButtonTypes().add(ButtonType.CLOSE);
	}

	public void show() {
		alert.showAndWait();
	}

}
