package io.azraein.paperfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.tinylog.Logger;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.actors.dialogue.DialogueParser;
import io.azraein.inkfx.system.io.Database;
import io.azraein.inkfx.system.io.PaperIni;
import io.azraein.inkfx.system.io.SaveSystem;
import io.azraein.inkfx.system.io.plugins.PaperPluginLoader;
import io.azraein.inkfx.system.io.plugins.PaperPluginMetadata;
import io.azraein.inkfx.system.io.scripting.ScriptEngine;
import io.azraein.paperfx.ui.controls.dialog.PaperPluginSelectionDialog;
import io.azraein.paperfx.ui.controls.dialog.player.CharacterCreationDialog;
import io.azraein.paperfx.ui.controls.dialog.player.PluginSelectionResult;
import io.azraein.paperfx.ui.screens.GameScreen;
import io.azraein.paperfx.ui.screens.MainMenuScreen;
import io.azraein.paperfx.ui.screens.PaperScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PaperFX extends Application {

	public static final String PAPER_VERSION = "0.0.1";

	private Map<String, PaperScreen> paperScreens;

	private Stage primaryStage;
	private Scene primaryScene;

	@Override
	public void init() throws Exception {
		super.init();

		// Check the Paper Working Directory, then instantiate the Plugin Loader.
		SaveSystem.checkFileSystem();
		Paper.PPL = new PaperPluginLoader();

		// Initialize the Ini and Create the base Database.
		Paper.INI = new PaperIni();
		Paper.DATABASE = new Database();
		Paper.DP = new DialogueParser();

		// Create the Screens HashMap and throw in the default screens
		paperScreens = new HashMap<>();
		paperScreens.put("mainMenu", new MainMenuScreen(this));
		paperScreens.put("game", new GameScreen(this));

		// Create the Default Globals
		Paper.DATABASE.addGlobal("playerName", "Phil Collins");
		Paper.DATABASE.addGlobal("playerLevel", 10);
		Paper.DATABASE.addGlobal("currentLocation", "");

		Paper.DATABASE.addGlobal("hasAmulet", false);

		// Initialize Scripting Engine last.
		Paper.SE = new ScriptEngine();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryScene = new Scene(paperScreens.get("mainMenu"), 1280, 720);
		primaryStage.setTitle("Paper Engine");
		primaryStage.setScene(primaryScene);
		primaryStage.getIcons().add(new Image(Utils.getFileFromResources("paper-icon32.png")));

		// Initialize Screens
		for (PaperScreen screen : paperScreens.values())
			screen.init();

		// Check to see if the INI contains a list of loadable plugins
		// #region Default Plugin Loading
		if (Paper.INI.getSelectedPluginsList().isEmpty()) {
			PaperPluginSelectionDialog ppsd = new PaperPluginSelectionDialog();
			Optional<PluginSelectionResult> plugins = ppsd.showAndWait();
			if (plugins.isPresent()) {
				List<String> pluginPaths = plugins.get().getSelectedPluginPaths();
				Paper.DATABASE.mergeDatabase(Paper.PPL.loadPlugins(pluginPaths));

				List<String> selectedPlugins = new ArrayList<>();
				Paper.PPL.getLoadedPlugins().values()
						.forEach(plugin -> selectedPlugins.add(plugin.getMetadata().getPluginId()));
				Paper.INI.updateSelectedPluginsList(selectedPlugins);
			}
		} else {
			List<String> pluginPaths = new ArrayList<>();
			for (String pluginId : Paper.INI.getSelectedPluginsList()) {
				String path = SaveSystem.PAPER_DATA_FOLDER + pluginId + SaveSystem.PAPER_PLUGIN_MAIN_FILE_EXTENSION;
				PaperPluginMetadata ppm = SaveSystem.readPluginMetadata(path);

				if (ppm == null) {
					path = SaveSystem.PAPER_DATA_FOLDER + pluginId + SaveSystem.PAPER_PLUGIN_ADDON_FILE_EXTENSION;
					ppm = SaveSystem.readPluginMetadata(path);
				}

				Logger.debug("Adding plugin: " + ppm.getPluginId() + " : " + ppm.getPluginName());
				pluginPaths.add(ppm.getPluginPath());
			}

			Paper.DATABASE.mergeDatabase(Paper.PPL.loadPlugins(pluginPaths));
		}
		// #endregion

		primaryStage.show();

		Paper.SE.setPaperGlobal("characterCreation", CoerceJavaToLua.coerce(new CharacterCreationDialog()));

		// Run Plugin Script Initialization
		Paper.SE.runFunction(Paper.PPL.getPluginMainScript(), "onInit");

		// If the Main Script pushed any global variables, we'll finally copy those into
		// the global observable
		for (Entry<String, Object> entry : Paper.DATABASE.getGlobalRegistry().entrySet()) {
			Logger.debug("tossing global: " + entry.getKey() + " into the observable");
			Paper.PAPER_GAME_GLOBALS.put(entry.getKey(), entry.getValue());
		}

	}

	public void swapScreens(String screenId) {
		primaryScene.setRoot(paperScreens.get(screenId));
	}

	public void setTitle(String title) {
		primaryStage.setTitle(title);
	}

	public String getTitle() {
		return primaryStage.getTitle();
	}

	public Map<String, PaperScreen> getScreens() {
		return paperScreens;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

}
