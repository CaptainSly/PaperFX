package io.azraein.paperfx;

import java.util.HashMap;
import java.util.Map;

import io.azraein.paperfx.screens.GameScreen;
import io.azraein.paperfx.screens.MainMenuScreen;
import io.azraein.paperfx.screens.PaperScreen;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.plugins.PaperPluginLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaperFX extends Application {

	private Map<String, PaperScreen> paperScreens;
	private PaperPluginLoader ppl;

	private static Database database;

	// JavaFX Nodes
	private Scene paperScene;

	@Override
	public void init() throws Exception {
		super.init();

		ppl = new PaperPluginLoader();
		database = new Database();

		paperScreens = new HashMap<>();
		paperScreens.put("mainMenu", new MainMenuScreen(this));
		paperScreens.put("game", new GameScreen(this));

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

	public PaperPluginLoader getPaperPluginLoader() {
		return ppl;
	}

	public static Database getDatabase() {
		return database;
	}

}
