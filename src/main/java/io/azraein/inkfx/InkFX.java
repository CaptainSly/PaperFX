package io.azraein.inkfx;

import java.util.*;

import org.tinylog.Logger;

import io.azraein.inkfx.dialog.AboutAlert;
import io.azraein.inkfx.dialog.PluginSelectionDialog;
import io.azraein.inkfx.screens.PaperEditorScreen;
import io.azraein.inkfx.screens.PluginContentEditorScreen;
import io.azraein.inkfx.screens.PluginMetadataScreen;
import io.azraein.paperfx.system.actors.Actor;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.actors.creatures.Creature;
import io.azraein.paperfx.system.exceptions.IncompatiblePluginVersionException;
import io.azraein.paperfx.system.exceptions.PluginCorruptionException;
import io.azraein.paperfx.system.inventory.items.Item;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import io.azraein.paperfx.system.io.plugins.PaperPluginLoader;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import io.azraein.paperfx.system.locations.Location;
import io.azraein.paperfx.system.locations.SubLocation;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class InkFX extends Application {

	private BorderPane rootPane;
	private MenuBar menuBar;
	private Map<String, PaperEditorScreen> editorScreens;

	private PaperPluginLoader ppl;

	public static final String INK_VERSION = "0.0.3";

	// Everything gets saved to this database in here, avoiding duplicate entries
	private ObjectProperty<PaperPlugin> currentPluginProperty = new SimpleObjectProperty<>(null);

	// Everything gets loaded here, but nothing gets saved.
	private ObservableMap<String, Object> globalList = FXCollections.observableHashMap();
	private ObservableMap<String, Item> itemList = FXCollections.observableHashMap();
	private ObservableMap<String, ActorRace> raceList = FXCollections.observableHashMap();
	private ObservableMap<String, ActorClass> charClassList = FXCollections.observableHashMap();
	private ObservableMap<String, Actor> actorList = FXCollections.observableHashMap();
	private ObservableMap<String, Creature> creatureList = FXCollections.observableHashMap();
	private ObservableMap<String, Location> locationList = FXCollections.observableHashMap();
	private ObservableMap<String, SubLocation> subLocationList = FXCollections.observableHashMap();

	public static final String PLUGIN_METADATA_SCREEN = "pluginMetadata";
	public static final String PLUGIN_CONTENT_EDITOR_SCREEN = "pluginContent";

	@Override
	public void init() throws Exception {
		super.init();
		ppl = new PaperPluginLoader();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		rootPane = new BorderPane();

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

		primaryStage.setScene(new Scene(rootPane, 1280, 720));
		primaryStage.setTitle("Ink");
		primaryStage.show();
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
		savePluginItem.setOnAction(event -> {
			savePlugin();
		});

		fileMenu.getItems().addAll(newPluginItem, openPluginItem, savePluginItem);
		menuBar.getMenus().add(fileMenu);
	}

	private void createScreenMenu() {
		Menu screenMenu = new Menu("Screen");
		screenMenu.setDisable(true);

		currentPluginProperty.addListener((observable, oldValue, newValue) -> {
			screenMenu.setDisable(newValue == null ? true : false);
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
			new AboutAlert();
		});

		aboutMenu.getItems().add(aboutItem);
		menuBar.getMenus().add(aboutMenu);
	}

	private void openPlugin() {
		PluginSelectionDialog psd = new PluginSelectionDialog();
		Optional<Pair<List<String>, Pair<List<String>, PaperPluginMetadata>>> depsAndMetadata = psd.showAndWait();
		if (depsAndMetadata.isPresent()) {
			if (depsAndMetadata.get().getKey() != null) {
				List<String> pluginDependencyPaths = depsAndMetadata.get().getValue().getKey();
				List<String> pluginDependencies = depsAndMetadata.get().getKey();
				PluginMetadataScreen pmd = ((PluginMetadataScreen) editorScreens.get(PLUGIN_METADATA_SCREEN));
				mergeDatabase(ppl.loadPlugins(pluginDependencyPaths));

				for (String str : ppl.getLoadedPlugins().keySet()) {
					Logger.debug("Loaded Plugin: " + str);
				}

				if (depsAndMetadata.get().getValue().getValue() != null) {
					PaperPluginMetadata pluginMetadata = depsAndMetadata.get().getValue().getValue();

					Logger.debug(pluginMetadata.getPluginPath());
					try {
						currentPluginProperty.set(SaveSystem.loadPlugin(pluginMetadata.getPluginPath()));
						pmd.setDependencies(pluginDependencies);

						// Remove the active plugin from the list, considering we don't want to add
						// ourselves. Talk about recursive
						pmd.getDependencyList().getItems().remove(pluginMetadata.getPluginId());

						// Add the rest of the information to the plugin
						pmd.getPluginIdFld().setText(pluginMetadata.getPluginId());
						pmd.getPluginNameFld().setText(pluginMetadata.getPluginName());
						pmd.getPluginAuthorFld().setText(pluginMetadata.getPluginAuthor());
						pmd.getPluginDescription().setText(pluginMetadata.getPluginDescription());
						pmd.getPluginMainFileCB().setSelected(pluginMetadata.isPluginMainFile());
						pmd.getPluginVersionFld().setText(pluginMetadata.getPluginVersion());

					} catch (IncompatiblePluginVersionException e) {
						Logger.error(e);
					} catch (PluginCorruptionException e) {
						Logger.error(e);
					}
				} else {
					currentPluginProperty.set(new PaperPlugin());
					pmd.setDependencies(pluginDependencies);
				}

				swapScreens(PLUGIN_METADATA_SCREEN);
			}
		}
	}

	private void savePlugin() {
		if (currentPluginProperty.get() == null) {
			currentPluginProperty.set(new PaperPlugin());
			Logger.debug("Created new plugin");
		}
		// Create a Plugin object set it to current and give it data to save to the
		// list.
		PaperPlugin plugin = currentPluginProperty.get();

		// Get the Metadata Screen data
		PluginMetadataScreen pmd = (PluginMetadataScreen) editorScreens.get(PLUGIN_METADATA_SCREEN);

		if (pmd.getPluginIdFld() != null && pmd.getPluginNameFld() != null && pmd.getPluginAuthorFld() != null
				&& pmd.getPluginDescription() != null && pmd.getPluginMainFileCB() != null
				&& pmd.getDependencyList() != null) {

			plugin.setPluginId(pmd.getPluginIdFld().getText());
			plugin.setPluginName(pmd.getPluginNameFld().getText());
			plugin.setPluginAuthor(pmd.getPluginAuthorFld().getText());
			plugin.setPluginVersion(pmd.getPluginVersionFld().getText());
			plugin.setPluginDescription(pmd.getPluginDescription().getText());
			plugin.setIsMainFile(pmd.getPluginMainFileCB().isSelected());

			List<String> dependencyList = new ArrayList<>();
			for (String dependency : pmd.getDependencyList().getItems())
				dependencyList.add(dependency);

			plugin.setPluginDependencies(dependencyList);

			String fileName = plugin.isMainFile() ? plugin.getPluginId() + SaveSystem.PAPER_PLUGIN_MAIN_FILE_EXTENSION
					: plugin.getPluginId() + SaveSystem.PAPER_PLUGIN_ADDON_FILE_EXTENSION;

			String filePath = SaveSystem.PAPER_DATA_FOLDER + fileName;
			SaveSystem.savePlugin(plugin, filePath);
		}
	}

	public void mergeDatabase(Database database) {
		this.getGlobalList().putAll(database.getGlobalList());
		this.getItemList().putAll(database.getItemList());
		this.getCharClassList().putAll(database.getCharClassList());
		this.getRaceList().putAll(database.getRaceList());
		this.getActorList().putAll(database.getActorList());
		this.getCreatureList().putAll(database.getCreatureList());
		this.getLocationList().putAll(database.getLocationList());
		this.getSubLocationList().putAll(database.getSubLocationList());
	}

	public void swapScreens(String screenId) {
		rootPane.setCenter(editorScreens.get(screenId));
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

	public ObservableMap<String, ActorClass> getCharClassList() {
		return charClassList;
	}

	public ObservableMap<String, Actor> getActorList() {
		return actorList;
	}

	public ObservableMap<String, Creature> getCreatureList() {
		return creatureList;
	}

	public ObservableMap<String, Location> getLocationList() {
		return locationList;
	}

	public ObservableMap<String, SubLocation> getSubLocationList() {
		return subLocationList;
	}

	public void setRaceList(ObservableMap<String, ActorRace> raceList) {
		this.raceList = raceList;
	}

	public void setLocationList(ObservableMap<String, Location> locationList) {
		this.locationList = locationList;
	}

}
