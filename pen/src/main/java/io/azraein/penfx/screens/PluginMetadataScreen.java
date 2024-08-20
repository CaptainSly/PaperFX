package io.azraein.penfx.screens;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.azraein.inkfx.system.io.Database;
import io.azraein.inkfx.system.io.SaveSystem;
import io.azraein.inkfx.system.io.plugins.PaperPlugin;
import io.azraein.inkfx.system.io.plugins.PaperPluginMetadata;
import io.azraein.penfx.PenFX;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;

public class PluginMetadataScreen extends PaperEditorScreen {

	TextField pluginIdFld, pluginNameFld, pluginAuthorFld, pluginVersionFld;
	TextArea pluginDescription;
	CheckBox pluginMainFileCB;
	TextField pluginMainScriptFld;
	ListView<PaperPluginMetadata> dependencyListView;

	public PluginMetadataScreen(PenFX inkFX) {
		super(inkFX);

		Label pluginIdLbl = new Label("Plugin ID");
		Label pluginNameLbl = new Label("Plugin Name");
		Label pluginAuthorLbl = new Label("Plugin Author");
		Label pluginVersionLbl = new Label("Plugin Version");
		Label pluginMainfileLbl = new Label("Is Plugin Main File?");
		Label pluginDeps = new Label("Plugin Dependencies");
		Label pluginMainScriptLbl = new Label("Plugin Script Main File: ");

		Button saveMetadataToPlugin = new Button("Save Plugin Metadata");
		saveMetadataToPlugin.setTooltip(new Tooltip("Saving the Metadata will also save the plugin."));
		saveMetadataToPlugin.setOnAction(event -> {
			PaperPlugin plugin = inkFX.currentPluginProperty().get();
			if (plugin == null) {
				plugin = new PaperPlugin();
				inkFX.currentPluginProperty().set(plugin);
			}

			plugin.getMetadata().setPluginId(getPluginIdFld().getText());
			plugin.getMetadata().setPluginName(getPluginNameFld().getText());
			plugin.getMetadata().setPluginAuthor(getPluginAuthorFld().getText());
			plugin.getMetadata().setPluginDescription(getPluginDescription().getText());
			plugin.getMetadata().setPluginVersion(getPluginVersionFld().getText());
			plugin.getMetadata().setPluginMainFile(getPluginMainFileCB().isSelected());
			plugin.getMetadata().setPluginMainScript(pluginMainScriptFld.getText());

			String ext;
			if (plugin.getMetadata().isPluginMainFile())
				ext = SaveSystem.PAPER_PLUGIN_MAIN_FILE_EXTENSION;
			else
				ext = SaveSystem.PAPER_PLUGIN_ADDON_FILE_EXTENSION;

			String filePath = SaveSystem.PAPER_DATA_FOLDER + plugin.getMetadata().getPluginId() + ext;
			plugin.getMetadata().setPluginPath(filePath);

			List<String> dependencyList = new ArrayList<>();
			for (PaperPluginMetadata dependency : getDependencyListView().getItems())
				dependencyList.add(dependency.getPluginId());

			plugin.getMetadata().setPluginDependencies(dependencyList);

			// Clear the databases and load everything again
			inkFX.clearDatabase();
			inkFX.getPluginLoader().clearLoadedPlugins();

			List<String> dependencyPaths = new ArrayList<>();
			for (PaperPluginMetadata metadata : dependencyListView.getItems()) {
				dependencyPaths.add(metadata.getPluginPath());
			}

			// Get the Combined Plugin Databases, merge it with the current plugin, then
			// finally merge it into the Editor's database.
			Database combinedDatabase = inkFX.getPluginLoader().loadPlugins(dependencyPaths);
			combinedDatabase.mergeDatabase(plugin.getPluginDatabase());
			inkFX.mergeDatabase(combinedDatabase);

			SaveSystem.savePlugin(plugin, filePath);
		});

		ComboBox<PaperPluginMetadata> dependencyChooser = new ComboBox<>();
		dependencyChooser.setCellFactory(listView -> new ListCell<PaperPluginMetadata>() {

			@Override
			public void updateItem(PaperPluginMetadata item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null) {
					String pluginType;
					if (item.isPluginMainFile())
						pluginType = "M";
					else
						pluginType = "A";

					setText(item.getPluginId() + " " + pluginType);
				} else {
					setText("");
				}

			}

		});
		dependencyChooser.setConverter(new StringConverter<PaperPluginMetadata>() {

			@Override
			public String toString(PaperPluginMetadata object) {
				if (object != null) {
					String pluginType;
					if (object.isPluginMainFile())
						pluginType = "M";
					else
						pluginType = "A";

					return object.getPluginId() + " " + pluginType;
				}

				return "";
			}

			@Override
			public PaperPluginMetadata fromString(String string) {
				throw null;
			}

		});
		File file = new File(SaveSystem.PAPER_DATA_FOLDER);
		for (File pluginFile : file.listFiles()) {
			if (pluginFile.exists()) {
				if (pluginFile.getPath().endsWith(SaveSystem.PAPER_PLUGIN_MAIN_FILE_EXTENSION)
						|| pluginFile.getPath().endsWith(SaveSystem.PAPER_PLUGIN_ADDON_FILE_EXTENSION)) {
					PaperPluginMetadata metadata;
					try {
						metadata = SaveSystem.readPluginMetadata(pluginFile.getPath());
						dependencyChooser.getItems().add(metadata);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		dependencyChooser.getSelectionModel().select(0);

		Button chooseDepdencyBtn = new Button("Choose Dependency");
		chooseDepdencyBtn.setOnAction(event -> {
			PaperPluginMetadata metadata = dependencyChooser.getSelectionModel().getSelectedItem();
			dependencyListView.getItems().add(metadata);
		});

		dependencyListView = new ListView<>();
		dependencyListView.setCellFactory(listView -> new ListCell<PaperPluginMetadata>() {

			@Override
			public void updateItem(PaperPluginMetadata item, boolean empty) {
				if (item != null && !empty) {
					this.setText(item.getPluginId());
					ContextMenu cm = new ContextMenu();
					MenuItem removeDependency = new MenuItem("Remove Dependency");
					removeDependency.setOnAction(event -> {
						int currentIdx = dependencyListView.getItems()
								.indexOf(dependencyListView.getSelectionModel().getSelectedItem());
						dependencyListView.getItems().remove(currentIdx);
					});
					cm.getItems().add(removeDependency);
					this.setContextMenu(cm);
				} else {
					this.setText("");
					this.setContextMenu(null);
				}
			}

		});

		pluginIdFld = new TextField();
		pluginNameFld = new TextField();
		pluginAuthorFld = new TextField();
		pluginVersionFld = new TextField();

		Button choosePluginScriptBtn = new Button("Select Main Script");
		choosePluginScriptBtn.setDisable(true);
		choosePluginScriptBtn.setOnAction(event -> {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(SaveSystem.PAPER_SCRIPT_FOLDER));
			fc.getExtensionFilters().add(new ExtensionFilter("Lua Script", "*.lua"));

			File selectedScript = fc.showOpenDialog(inkFX.getPrimaryStage().getOwner());
			if (selectedScript != null) {
				pluginMainScriptFld.setText(selectedScript.getName());
			}

		});

		Button clearPluginScriptBtn = new Button("Clear Selected Plugin");
		clearPluginScriptBtn.setOnAction(event -> {
			pluginMainScriptFld.setText("");
		});

		pluginMainFileCB = new CheckBox();
		pluginMainFileCB.selectedProperty()
				.addListener((observableValue, oldValue, newValue) -> choosePluginScriptBtn.setDisable(!newValue));

		pluginMainScriptFld = new TextField();
		pluginMainScriptFld.setEditable(false);

		pluginDescription = new TextArea();
		pluginDescription.setWrapText(true);
		pluginDescription.setPromptText("Plugin Description...");

		inkFX.currentPluginProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue == null) {
				dependencyListView.getItems().clear();
				pluginIdFld.setText("");
				pluginNameFld.setText("");
				pluginAuthorFld.setText("");
				pluginDescription.setText("");
				pluginVersionFld.setText("");
				pluginMainScriptFld.setText("");
				pluginMainFileCB.setSelected(false);

			}
		});

		// #region pluginMetadataGrid
		GridPane pluginMetadataGrid = new GridPane();
		GridPane.setColumnSpan(pluginDescription, 2);
		GridPane.setRowSpan(dependencyListView, 7);
		pluginMetadataGrid.setPadding(new Insets(15));
		pluginMetadataGrid.setHgap(10);
		pluginMetadataGrid.setVgap(10);
		pluginMetadataGrid.add(pluginIdLbl, 0, 0);
		pluginMetadataGrid.add(pluginIdFld, 1, 0);
		pluginMetadataGrid.add(pluginNameLbl, 2, 0);
		pluginMetadataGrid.add(pluginNameFld, 3, 0);
		pluginMetadataGrid.add(pluginAuthorLbl, 0, 1);
		pluginMetadataGrid.add(pluginAuthorFld, 1, 1);
		pluginMetadataGrid.add(pluginVersionLbl, 2, 1);
		pluginMetadataGrid.add(pluginVersionFld, 3, 1);
		pluginMetadataGrid.add(new Label("Plugin Description"), 0, 2);
		pluginMetadataGrid.add(pluginMainfileLbl, 2, 2);
		pluginMetadataGrid.add(pluginMainFileCB, 3, 2);
		pluginMetadataGrid.add(pluginDescription, 0, 3);
		pluginMetadataGrid.add(pluginDeps, 0, 4);
		pluginMetadataGrid.add(dependencyListView, 0, 5);
		pluginMetadataGrid.add(dependencyChooser, 1, 7);
		pluginMetadataGrid.add(chooseDepdencyBtn, 2, 7);
		pluginMetadataGrid.add(saveMetadataToPlugin, 1, 8);
		pluginMetadataGrid.add(pluginMainScriptLbl, 1, 9);
		pluginMetadataGrid.add(pluginMainScriptFld, 2, 9);
		pluginMetadataGrid.add(choosePluginScriptBtn, 3, 9);
		pluginMetadataGrid.add(clearPluginScriptBtn, 4, 9);
		// #endregion

		setCenter(pluginMetadataGrid);

	}

	public void setDependencies(List<PaperPluginMetadata> deps) {
		dependencyListView.getItems().clear();
		dependencyListView.getItems().addAll(deps);
	}

	public TextField getPluginIdFld() {
		return pluginIdFld;
	}

	public TextField getPluginNameFld() {
		return pluginNameFld;
	}

	public TextField getPluginAuthorFld() {
		return pluginAuthorFld;
	}

	public TextField getPluginVersionFld() {
		return pluginVersionFld;
	}

	public TextArea getPluginDescription() {
		return pluginDescription;
	}

	public CheckBox getPluginMainFileCB() {
		return pluginMainFileCB;
	}

	public TextField getPluginMainScriptFld() {
		return pluginMainScriptFld;
	}

	public ListView<PaperPluginMetadata> getDependencyListView() {
		return dependencyListView;
	}

}
