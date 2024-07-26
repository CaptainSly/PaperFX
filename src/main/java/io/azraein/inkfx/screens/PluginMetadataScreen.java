package io.azraein.inkfx.screens;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.azraein.inkfx.InkFX;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
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
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class PluginMetadataScreen extends PaperEditorScreen {

	TextField pluginIdFld, pluginNameFld, pluginAuthorFld, pluginVersionFld;
	TextArea pluginDescription;
	CheckBox pluginMainFileCB;
	ListView<PaperPluginMetadata> dependencyListView;

	public PluginMetadataScreen(InkFX inkFX) {
		super(inkFX);

		Label pluginIdLbl = new Label("Plugin ID");
		Label pluginNameLbl = new Label("Plugin Name");
		Label pluginAuthorLbl = new Label("Plugin Author");
		Label pluginVersionLbl = new Label("Plugin Version");
		Label pluginMainfileLbl = new Label("Is Plugin Main File?");
		Label pluginDeps = new Label("Plugin Dependencies");

		Button saveMetadataToPlugin = new Button("Save Plugin Metadata");
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

			Database combinedDatabase = inkFX.getPluginLoader().loadPlugins(dependencyPaths);
			inkFX.mergeDatabase(combinedDatabase);

			inkFX.swapScreens(InkFX.PLUGIN_CONTENT_EDITOR_SCREEN);
		});

		ComboBox<PaperPluginMetadata> dependencyChooser = new ComboBox<>();
		dependencyChooser.setCellFactory(listView -> new ListCell<PaperPluginMetadata>() {

			@Override
			public void updateItem(PaperPluginMetadata item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null || !empty) {
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

		pluginMainFileCB = new CheckBox();

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
				pluginMainFileCB.setSelected(false);
			}
		});

		GridPane gridPane = new GridPane();
		GridPane.setColumnSpan(pluginDescription, 2);
		GridPane.setRowSpan(dependencyListView, 4);
		gridPane.setPadding(new Insets(15));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.add(pluginIdLbl, 0, 0);
		gridPane.add(pluginIdFld, 1, 0);
		gridPane.add(pluginNameLbl, 2, 0);
		gridPane.add(pluginNameFld, 3, 0);
		gridPane.add(pluginAuthorLbl, 0, 1);
		gridPane.add(pluginAuthorFld, 1, 1);
		gridPane.add(pluginVersionLbl, 2, 1);
		gridPane.add(pluginVersionFld, 3, 1);
		gridPane.add(new Label("Plugin Description"), 0, 2);
		gridPane.add(pluginMainfileLbl, 2, 2);
		gridPane.add(pluginMainFileCB, 3, 2);
		gridPane.add(pluginDescription, 0, 3);
		gridPane.add(pluginDeps, 0, 4);
		gridPane.add(dependencyListView, 0, 5);
		gridPane.add(dependencyChooser, 1, 7);
		gridPane.add(chooseDepdencyBtn, 2, 7);
		gridPane.add(saveMetadataToPlugin, 1, 8);

		setCenter(gridPane);

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

	public ListView<PaperPluginMetadata> getDependencyListView() {
		return dependencyListView;
	}

}
