package io.azraein.inkfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.tinylog.Logger;

import io.azraein.inkfx.dialog.AboutAlert;
import io.azraein.inkfx.dialog.PluginSelectionDialog;
import io.azraein.inkfx.dialog.PluginSelectionResult;
import io.azraein.inkfx.screens.PaperEditorScreen;
import io.azraein.inkfx.screens.PluginContentEditorScreen;
import io.azraein.inkfx.screens.PluginMetadataScreen;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.Actor;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.exceptions.IncompatiblePluginVersionException;
import io.azraein.paperfx.system.exceptions.PluginCorruptionException;
import io.azraein.paperfx.system.inventory.items.Item;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import io.azraein.paperfx.system.io.plugins.PaperPluginLoader;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import io.azraein.paperfx.system.locations.Location;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class InkFX extends Application {

	private Stage primaryStage;
	private BorderPane rootPane;
	private MenuBar menuBar;
	private Map<String, PaperEditorScreen> editorScreens;

	private PaperPluginLoader ppl;

	public static final String INK_VERSION = "0.0.8";

	// Everything gets saved to this database in here, avoiding duplicate entries
	private final ObjectProperty<PaperPlugin> currentPluginProperty = new SimpleObjectProperty<>(null);

	// Everything gets loaded here, but nothing gets saved.
	private final ObservableMap<String, Object> globalList = FXCollections.observableHashMap();
	private final ObservableMap<String, Item> itemList = FXCollections.observableHashMap();
	private final ObservableMap<String, ActorRace> raceList = FXCollections.observableHashMap();
	private final ObservableMap<String, ActorClass> actorClassList = FXCollections.observableHashMap();
	private final ObservableMap<String, Actor> actorList = FXCollections.observableHashMap();
	private final ObservableMap<String, Location> locationList = FXCollections.observableHashMap();

	public static final String PLUGIN_METADATA_SCREEN = "pluginMetadata";
	public static final String PLUGIN_CONTENT_EDITOR_SCREEN = "pluginContent";

	@Override
	public void init() throws Exception {
		super.init();
		ppl = new PaperPluginLoader();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		rootPane = new BorderPane();

		SaveSystem.checkFileSystem();

		editorScreens = new HashMap<>();
		editorScreens.put(PLUGIN_METADATA_SCREEN, new PluginMetadataScreen(this));
		editorScreens.put(PLUGIN_CONTENT_EDITOR_SCREEN, new PluginContentEditorScreen(this));

		// MenuBar
		menuBar = new MenuBar();
		createFileMenu();
		createScreenMenu();
		createAboutMenu();

		rootPane.setTop(menuBar);
		rootPane.setCenter(new Label("Open or Create a new Plugin to get started"));

		primaryStage.setScene(new Scene(rootPane, 1366, 768));
		primaryStage.setTitle("Ink");
		primaryStage.show();
		primaryStage.getIcons().add(new Image(Utils.getFileFromResources("ink-icon32.png")));

	}

	private void createFileMenu() {
		Menu fileMenu = new Menu("File");

		MenuItem newPluginItem = new MenuItem("New Plugin");
		newPluginItem.setOnAction(event -> {
			swapScreens(PLUGIN_METADATA_SCREEN);
			currentPluginProperty.set(new PaperPlugin());
		});

		MenuItem openPluginItem = new MenuItem("Open Plugin");
		openPluginItem.setOnAction(event -> {
			openPlugin();
		});

		MenuItem savePluginItem = new MenuItem("Save Plugin");
		savePluginItem.setDisable(true);
		savePluginItem.setOnAction(event -> {
			savePlugin();
		});

		MenuItem closePluginItem = new MenuItem("Close Plugin");
		closePluginItem.setDisable(true);
		closePluginItem.setOnAction(event -> {
			swapScreens(PLUGIN_METADATA_SCREEN);
			clearDatabase();
			ppl.clearLoadedPlugins();
			currentPluginProperty.set(null);
		});

		currentPluginProperty.addListener((observable, oldValue, newValue) -> {
			savePluginItem.setDisable(newValue == null);
			closePluginItem.setDisable(newValue == null);
		});

		fileMenu.getItems().addAll(newPluginItem, openPluginItem, savePluginItem, closePluginItem);
		menuBar.getMenus().add(fileMenu);
	}

	private void createScreenMenu() {
		Menu screenMenu = new Menu("Screen");
		screenMenu.setDisable(true);

		currentPluginProperty.addListener((observable, oldValue, newValue) -> {
			screenMenu.setDisable(newValue == null);
		});

		MenuItem metadataScreen = new MenuItem("Swap to Plugin Metadata");
		metadataScreen.setOnAction(event -> {
			swapScreens(PLUGIN_METADATA_SCREEN);
		});

		MenuItem contentEditorScreen = new MenuItem("Swap to Plugin Content Editor");
		contentEditorScreen.setOnAction(event -> {
			swapScreens(PLUGIN_CONTENT_EDITOR_SCREEN);
		});

		screenMenu.getItems().addAll(metadataScreen, contentEditorScreen);
		menuBar.getMenus().add(screenMenu);
	}

	private void createAboutMenu() {
		Menu aboutMenu = new Menu("About");

		MenuItem aboutItem = new MenuItem("About Ink");
		aboutItem.setOnAction(event -> {
			AboutAlert alert = new AboutAlert();
			alert.show();
		});

		aboutMenu.getItems().add(aboutItem);
		menuBar.getMenus().add(aboutMenu);
	}

	private void openPlugin() {
		PluginSelectionDialog psd = new PluginSelectionDialog();
		Optional<PluginSelectionResult> depsAndMetadata = psd.showAndWait();
		if (depsAndMetadata.isPresent()) {
			if (depsAndMetadata.get().getSelectedPlugins() != null) {
				List<String> pluginDependencyPaths = depsAndMetadata.get().getSelectedPluginPaths();
				List<PaperPluginMetadata> pluginDependencies = depsAndMetadata.get().getSelectedPlugins();
				PluginMetadataScreen pmd = ((PluginMetadataScreen) editorScreens.get(PLUGIN_METADATA_SCREEN));

				for (String str : ppl.getLoadedPlugins().keySet()) {
					Logger.debug("Loaded Plugin: " + str);
				}

				if (depsAndMetadata.get().getActivePlugin() != null) {
					PaperPluginMetadata pluginMetadata = depsAndMetadata.get().getActivePlugin();
					try {
						currentPluginProperty.set(SaveSystem.loadPlugin(pluginMetadata.getPluginPath()));
						mergeDatabase(ppl.loadPlugins(pluginDependencyPaths));
						pmd.setDependencies(pluginDependencies);

						// Remove the active plugin from the list, considering we don't want to add
						// ourselves. Talk about recursive
						pmd.getDependencyListView().getItems().remove(pluginMetadata);

						// Add the rest of the information to the plugin
						pmd.getPluginIdFld().setText(pluginMetadata.getPluginId());
						pmd.getPluginNameFld().setText(pluginMetadata.getPluginName());
						pmd.getPluginAuthorFld().setText(pluginMetadata.getPluginAuthor());
						pmd.getPluginDescription().setText(pluginMetadata.getPluginDescription());
						pmd.getPluginMainFileCB().setSelected(pluginMetadata.isPluginMainFile());
						pmd.getPluginVersionFld().setText(pluginMetadata.getPluginVersion());
						pmd.getPluginMainScriptFld().setText(pluginMetadata.getPluginMainScript());

					} catch (IncompatiblePluginVersionException | PluginCorruptionException e) {
						Logger.error(e);
					}
				} else {
					currentPluginProperty.set(new PaperPlugin());
					mergeDatabase(ppl.loadPlugins(pluginDependencyPaths));
					pmd.setDependencies(pluginDependencies);
				}

				swapScreens(PLUGIN_METADATA_SCREEN);
			}
		}
	}

	private void savePlugin() {

		final ReentrantLock lock = new ReentrantLock();

		lock.lock();
		try {
			// Create a Plugin object set it to current and give it data to save to the
			// list.
			if (currentPluginProperty.get() == null) {
				currentPluginProperty.set(new PaperPlugin());
				Logger.debug("Created new plugin");
			}
			PaperPlugin plugin = currentPluginProperty.get();

			// Get the Metadata Screen data
			PluginMetadataScreen pmd = (PluginMetadataScreen) editorScreens.get(PLUGIN_METADATA_SCREEN);

			if (pmd.getPluginIdFld() != null && pmd.getPluginNameFld() != null && pmd.getPluginAuthorFld() != null
					&& pmd.getPluginDescription() != null && pmd.getPluginMainFileCB() != null
					&& pmd.getDependencyListView() != null) {

				plugin.getMetadata().setPluginId(pmd.getPluginIdFld().getText());
				plugin.getMetadata().setPluginName(pmd.getPluginNameFld().getText());
				plugin.getMetadata().setPluginAuthor(pmd.getPluginAuthorFld().getText());
				plugin.getMetadata().setPluginVersion(pmd.getPluginVersionFld().getText());
				plugin.getMetadata().setPluginDescription(pmd.getPluginDescription().getText());
				plugin.getMetadata().setPluginMainFile(pmd.getPluginMainFileCB().isSelected());

				List<String> dependencyList = new ArrayList<>();
				for (PaperPluginMetadata dependency : pmd.getDependencyListView().getItems())
					dependencyList.add(dependency.getPluginId());

				plugin.getMetadata().setPluginDependencies(dependencyList);

				String fileName = plugin.getMetadata().isPluginMainFile()
						? plugin.getMetadata().getPluginId() + SaveSystem.PAPER_PLUGIN_MAIN_FILE_EXTENSION
						: plugin.getMetadata().getPluginId() + SaveSystem.PAPER_PLUGIN_ADDON_FILE_EXTENSION;

				String filePath = SaveSystem.PAPER_DATA_FOLDER + fileName;
				SaveSystem.savePlugin(plugin, filePath);
			}
		} finally {
			lock.unlock();
		}
	}

	public void mergeDatabase(Database database) {
		this.getGlobalList().putAll(database.getGlobalList());
		this.getItemList().putAll(database.getItemList());
		this.getActorClassList().putAll(database.getActorClassList());
		this.getRaceList().putAll(database.getRaceList());
		this.getActorList().putAll(database.getActorList());
		this.getLocationList().putAll(database.getLocationList());
	}

	public void clearDatabase() {
		this.getGlobalList().clear();
		this.getItemList().clear();
		this.getActorClassList().clear();
		this.getRaceList().clear();
		this.getActorList().clear();
		this.getLocationList().clear();
	}

	public void swapScreens(String screenId) {
		rootPane.setCenter(editorScreens.get(screenId));
	}

	public PaperPluginLoader getPluginLoader() {
		return ppl;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public ObjectProperty<PaperPlugin> currentPluginProperty() {
		return currentPluginProperty;
	}

	public ObservableMap<String, Object> getGlobalList() {
		return globalList;
	}

	public ObservableMap<String, Item> getItemList() {
		return itemList;
	}

	public ObservableMap<String, ActorRace> getRaceList() {
		return raceList;
	}

	public ObservableMap<String, ActorClass> getActorClassList() {
		return actorClassList;
	}

	public ObservableMap<String, Actor> getActorList() {
		return actorList;
	}

	public ObservableMap<String, Location> getLocationList() {
		return locationList;
	}

}
