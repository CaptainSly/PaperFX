package io.azraein.penfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.tinylog.Logger;

import io.azraein.inkfx.system.Action;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.actors.Npc;
import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.inkfx.system.actors.classes.ActorRace;
import io.azraein.inkfx.system.actors.dialogue.Topic;
import io.azraein.inkfx.system.exceptions.IncompatiblePluginVersionException;
import io.azraein.inkfx.system.exceptions.PluginCorruptionException;
import io.azraein.inkfx.system.inventory.items.Item;
import io.azraein.inkfx.system.inventory.items.Lootlist;
import io.azraein.inkfx.system.inventory.items.equipment.Equipment;
import io.azraein.inkfx.system.io.Database;
import io.azraein.inkfx.system.io.ObservableDatabase;
import io.azraein.inkfx.system.io.SaveSystem;
import io.azraein.inkfx.system.io.plugins.PaperPlugin;
import io.azraein.inkfx.system.io.plugins.PaperPluginLoader;
import io.azraein.inkfx.system.io.plugins.PaperPluginMetadata;
import io.azraein.inkfx.system.locations.Location;
import io.azraein.inkfx.system.locations.buildings.Building;
import io.azraein.inkfx.system.quest.Quest;
import io.azraein.penfx.controls.cells.PluginSelectionResult;
import io.azraein.penfx.controls.dialog.AboutAlert;
import io.azraein.penfx.controls.dialog.PluginSelectionDialog;
import io.azraein.penfx.screens.PaperEditorScreen;
import io.azraein.penfx.screens.PluginContentEditorScreen;
import io.azraein.penfx.screens.PluginMetadataScreen;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PenFX extends Application {

	private Stage primaryStage;
	private BorderPane rootPane;
	private MenuBar menuBar;
	private Map<String, PaperEditorScreen> editorScreens;

	private PaperPluginLoader ppl;

	public static final String INK_VERSION = "0.0.8";

	// Everything gets saved to this database in here, avoiding duplicate entries
	private final ObjectProperty<PaperPlugin> currentPluginProperty = new SimpleObjectProperty<>(null);

	// Everything gets loaded here, but nothing gets saved.
	private ObservableDatabase observableDatabase;

	public static final String PLUGIN_METADATA_SCREEN = "pluginMetadata";
	public static final String PLUGIN_CONTENT_EDITOR_SCREEN = "pluginContent";

	@Override
	public void init() throws Exception {
		super.init();
		ppl = new PaperPluginLoader();
		observableDatabase = new ObservableDatabase();
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
		observableDatabase.mergeDatabase(database);
	}

	public void clearDatabase() {
		observableDatabase.clearDatabase();
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

	public ObservableMap<String, Equipment> getObservableEquipmentRegistry() {
		return observableDatabase.getObservableEquipmentRegistry();
	}

	public ObservableMap<String, Action> getObservableActionRegistry() {
		return observableDatabase.getObservableActionRegistry();
	}

	public ObservableMap<String, Topic> getObservableTopicRegistry() {
		return observableDatabase.getObservableTopicRegistry();
	}

	public ObservableMap<String, Object> getObservableGlobalRegistry() {
		return observableDatabase.getObservableGlobalRegistry();
	}

	public ObservableMap<String, Quest> getObservableQuestRegistry() {
		return observableDatabase.getObservableQuestRegistry();
	}

	public ObservableMap<String, Building> getObservableBuildingRegistry() {
		return observableDatabase.getObservableBuildingRegistry();
	}

	public ObservableMap<String, Item> getObservableItemRegistry() {
		return observableDatabase.getObservableItemRegistry();
	}

	public ObservableMap<String, Lootlist> getObservableLootlistRegistry() {
		return observableDatabase.getObservableLootlistRegistry();
	}

	public ObservableMap<String, ActorRace> getObservableActorRaceRegistry() {
		return observableDatabase.getObservableActorRaceRegistry();
	}

	public ObservableMap<String, ActorClass> getObservableActorClassRegistry() {
		return observableDatabase.getObservableActorClassRegistry();
	}

	public ObservableMap<String, Npc> getObservableNpcRegistry() {
		return observableDatabase.getObservableNpcRegistry();
	}

	public ObservableMap<String, Location> getObservableLocationRegistry() {
		return observableDatabase.getObservableLocationRegistry();
	}

}
