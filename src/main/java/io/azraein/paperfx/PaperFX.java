package io.azraein.paperfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.tinylog.Logger;

import io.azraein.inkfx.dialog.PluginSelectionResult;
import io.azraein.paperfx.controls.dialog.PaperPluginSelectionDialog;
import io.azraein.paperfx.screens.PaperGameScreen;
import io.azraein.paperfx.screens.PaperScreen;
import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.PaperIni;
import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPluginLoader;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import io.azraein.paperfx.system.io.scripting.ScriptEngine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaperFX extends Application {

	public static final String PAPER_VERSION = "0.0.1";

	private Map<String, PaperScreen> paperScreens;
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

		// Initialize Scripting Engine last.
		Paper.SE = new ScriptEngine();

		paperScreens = new HashMap<>();
		paperScreens.put("game", new PaperGameScreen(this));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryScene = new Scene(paperScreens.get("game"), 1280, 720);
		primaryStage.setTitle("Paper Engine");
		primaryStage.setScene(primaryScene);
		primaryStage.show();

		// Check to see if the INI contains a list of loadable plugins
		// #region Default Plugin Loading
		if (Paper.INI.getSelectedPluginsList().isEmpty()) {
			PaperPluginSelectionDialog ppsd = new PaperPluginSelectionDialog();
			Optional<PluginSelectionResult> plugins = ppsd.showAndWait();
			if (plugins.isPresent()) {
				List<String> pluginPaths = plugins.get().getSelectedPluginPaths();
				Paper.DATABASE.mergeDatabase(Paper.PPL.loadPlugins(pluginPaths));

				List<String> selectedPlugins = new ArrayList<>();
				for (PaperPluginMetadata metadata : plugins.get().getSelectedPlugins()) {
					selectedPlugins.add(metadata.getPluginId());
				}

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
	
	}

	public void swapScreens(String screenId) {
		primaryScene.setRoot(paperScreens.get(screenId));
	}

}
