package io.azraein.inkfx.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tinylog.Logger;

import io.azraein.inkfx.controls.cells.EditorPluginMetadataCell;
import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// TODO: If Player selects a plugin that requires dependencies, disable the OK Button until they're selected, currently you can load the databases without them, and that would most likely break a few things.

/**
 * Pair<List<PaperPluginMetadata>, Pair<List<String>, PaperPluginMetadata>> The
 * returns are as followed in order selectedPlugins selectedPluginPaths
 * activePluginMetadata
 */
public class PluginSelectionDialog extends Dialog<PluginSelectionResult> {

	private ListView<PaperPluginMetadata> loadablePluginList;
	private ListView<String> pluginDeps;

	private TextArea pluginDescriptionArea;
	private TextField pluginIdFld, pluginNameFld, pluginAuthorFld, pluginVersionFld;

	private ObservableList<PaperPluginMetadata> selectedPlugins;
	private List<String> selectedPluginPaths;

	private ObjectProperty<PaperPluginMetadata> activePluginMetadataProperty = new SimpleObjectProperty<>();

	public PluginSelectionDialog() {
		PluginSelectionDialog pluginSelectionDialog = this;
		selectedPlugins = FXCollections.observableArrayList();
		selectedPluginPaths = new ArrayList<>();

		pluginDescriptionArea = new TextArea();
		pluginDescriptionArea.setPromptText("Plugin Description");
		pluginDescriptionArea.setWrapText(true);
		pluginDescriptionArea.setEditable(false);

		pluginIdFld = new TextField();
		pluginIdFld.setPromptText("Plugin ID");
		pluginIdFld.setEditable(false);

		pluginNameFld = new TextField();
		pluginNameFld.setPromptText("PluginName");
		pluginNameFld.setEditable(false);

		pluginAuthorFld = new TextField();
		pluginAuthorFld.setPromptText("Plugin Author");
		pluginAuthorFld.setEditable(false);

		pluginVersionFld = new TextField();
		pluginVersionFld.setPromptText("Plugin Version");
		pluginVersionFld.setEditable(false);

		TextField activePluginFld = new TextField();
		activePluginFld.setPromptText("Active Plugin: None");
		activePluginFld.setEditable(false);

		activePluginMetadataProperty.addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				activePluginFld.setText("Active Plugin: " + newValue.getPluginId());
			} else {
				activePluginFld.setText("Active Plugin: None");
			}
		});

		CheckBox isMainPlugin = new CheckBox();
		isMainPlugin.setDisable(true);
		isMainPlugin.setOpacity(1);

		Button setActivePluginBtn = new Button("Set Active Plugin");
		setActivePluginBtn.setOnAction(event -> {
			activePluginMetadataProperty.set(loadablePluginList.getSelectionModel().getSelectedItem());
		});

		Button clearActivePluginBtn = new Button("Clear Active Plugin");
		clearActivePluginBtn.setOnAction(event -> {
			activePluginMetadataProperty.set(null);
		});

		pluginDeps = new ListView<>();
		loadablePluginList = new ListView<>();
		loadablePluginList
				.setCellFactory((ListView<PaperPluginMetadata> param) -> new EditorPluginMetadataCell(pluginSelectionDialog));

		loadablePluginList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue != null) {
				PaperPluginMetadata metadata = newValue;

				String pluginType;
				if (metadata.isPluginMainFile())
					pluginType = "MAIN";
				else
					pluginType = "ADDON";

				pluginIdFld.setText(metadata.getPluginId() + " " + pluginType);
				pluginNameFld.setText(metadata.getPluginName());
				pluginAuthorFld.setText(metadata.getPluginAuthor());
				pluginVersionFld.setText(metadata.getPluginVersion());
				pluginDescriptionArea.setText(metadata.getPluginDescription());
				isMainPlugin.setSelected(metadata.isPluginMainFile());

				pluginDeps.getItems().clear();
				for (String pluginId : metadata.getPluginDependencies())
					pluginDeps.getItems().add(pluginId);

			}

		});

		for (File file : new File(SaveSystem.PAPER_DATA_FOLDER).listFiles()) {
			if (file.exists()) {
				if (file.getPath().endsWith(SaveSystem.PAPER_PLUGIN_MAIN_FILE_EXTENSION)
						|| file.getPath().endsWith(SaveSystem.PAPER_PLUGIN_ADDON_FILE_EXTENSION)) {

					// These files are going to be plugins and what nots.
					try {
						Logger.debug("Found Plugin reading metadata");
						PaperPluginMetadata metadata = SaveSystem.readPluginMetadata(file.getPath());
						loadablePluginList.getItems().add(metadata);

						Logger.debug(metadata.getPluginId());

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}

		setResultConverter(btnType -> {

			if (btnType == ButtonType.OK) {
				return new PluginSelectionResult(selectedPlugins, selectedPluginPaths,
						activePluginMetadataProperty.get());
			}

			return null;
		});

		GridPane gp = new GridPane();
		GridPane.setColumnSpan(pluginDescriptionArea, 2);
		GridPane.setColumnSpan(activePluginFld, 1);
		gp.setPadding(new Insets(15));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.add(pluginIdFld, 0, 0);
		gp.add(pluginNameFld, 1, 0);
		gp.add(pluginAuthorFld, 0, 1);
		gp.add(pluginVersionFld, 1, 1);
		gp.add(pluginDescriptionArea, 0, 2);
		gp.add(activePluginFld, 0, 3);
		gp.add(isMainPlugin, 1, 3);
		gp.add(clearActivePluginBtn, 0, 4);
		gp.add(setActivePluginBtn, 1, 4);

		VBox pluginInfoVBox = new VBox();
		pluginInfoVBox.setPadding(new Insets(5));
		pluginInfoVBox.getChildren().addAll(pluginDeps, new Separator(Orientation.HORIZONTAL), gp);

		ScrollPane pluginInfoScroll = new ScrollPane(pluginInfoVBox);
		pluginInfoScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		pluginInfoScroll.setMaxHeight(600);

		HBox rootContainer = new HBox();
		rootContainer.setPadding(new Insets(5));
		rootContainer.getChildren().addAll(loadablePluginList, new Separator(Orientation.VERTICAL), pluginInfoScroll);

		setTitle("Ink - Plugin Selection");
		setHeaderText(
				"Select Plugins to Load, to set a plugin as active, Right Click it and select active plugin from the context menu");
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		getDialogPane().setContent(rootContainer);
	}

	public ListView<PaperPluginMetadata> getLoadablePluginList() {
		return loadablePluginList;
	}

	public ListView<String> getPluginDeps() {
		return pluginDeps;
	}

	public TextArea getPluginDescriptionArea() {
		return pluginDescriptionArea;
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

	public List<PaperPluginMetadata> getSelectedPlugins() {
		return selectedPlugins;
	}

	public List<String> getSelectedPluginPaths() {
		return selectedPluginPaths;
	}

	public ObjectProperty<PaperPluginMetadata> activePluginMetadataProperty() {
		return activePluginMetadataProperty;
	}

}
