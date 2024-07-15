package io.azraein.paperfx;

import java.util.HashMap;
import java.util.Map;

import io.azraein.paperfx.screens.GameScreen;
import io.azraein.paperfx.screens.MainMenuScreen;
import io.azraein.paperfx.screens.PaperScreen;
import io.azraein.paperfx.system.io.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaperFX extends Application {

	private Database database;

	private Map<String, PaperScreen> paperScreens;

	// JavaFX Nodes
	private Scene paperScene;

	@Override
	public void init() throws Exception {
		super.init();
		paperScreens = new HashMap<>();
		paperScreens.put("mainMenu", new MainMenuScreen(this));
		paperScreens.put("game", new GameScreen(this));

		database = Database.loadDatabase();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		paperScene = new Scene(paperScreens.get("mainMenu"));
		primaryStage.setScene(paperScene);
		primaryStage.setTitle("PaperFX");
	}

	public void swapScreens(String screenId) {
		PaperScreen screen = paperScreens.get(screenId);
		if (screen != null)
			paperScene.setRoot(screen);
	}

	public Database getDatabase() {
		return database;
	}

}
