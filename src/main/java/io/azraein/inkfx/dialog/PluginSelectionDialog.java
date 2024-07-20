package io.azraein.inkfx.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tinylog.Logger;

import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;

public class PluginSelectionDialog extends Dialog<Pair<List<String>, Pair<List<String>, PaperPluginMetadata>>> {

	private ListView<PaperPluginMetadata> pluginMetadata;
	private ListView<String> pluginDeps;

	private TextArea pluginDescription;
	private TextField pluginId, pluginName, pluginAuthor, pluginVersion;

	private List<String> selectedPlugins;
	private List<String> selectedPluginPaths;

	private ObjectProperty<PaperPluginMetadata> activePluginMetadataProperty = new SimpleObjectProperty<>();

	public PluginSelectionDialog() {
		selectedPlugins = new ArrayList<>();
		selectedPluginPaths = new ArrayList<>();

		pluginDescription = new TextArea();
		pluginDescription.setPromptText("Plugin Description");
		pluginDescription.setWrapText(true);
		pluginDescription.setEditable(false);

		pluginId = new TextField();
		pluginId.setPromptText("Plugin ID");
		pluginId.setEditable(false);

		pluginName = new TextField();
		pluginName.setPromptText("PluginName");
		pluginName.setEditable(false);

		pluginAuthor = new TextField();
		pluginAuthor.setPromptText("Plugin Author");
		pluginAuthor.setEditable(false);

		pluginVersion = new TextField();
		pluginVersion.setPromptText("Plugin Version");
		pluginVersion.setEditable(false);

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

		Button setActivePluginBtn = new Button("Set Active Plugin");
		setActivePluginBtn.setOnAction(event -> {
			activePluginMetadataProperty.set(pluginMetadata.getSelectionModel().getSelectedItem());
		});

		Button clearActivePluginBtn = new Button("Clear Active Plugin");
		clearActivePluginBtn.setOnAction(event -> {
			activePluginMetadataProperty.set(null);
		});

		pluginDeps = new ListView<>();
		pluginMetadata = new ListView<>();
		pluginMetadata.setCellFactory(new Callback<ListView<PaperPluginMetadata>, ListCell<PaperPluginMetadata>>() {

			@Override
			public ListCell<PaperPluginMetadata> call(ListView<PaperPluginMetadata> param) {
				return new ListCell<PaperPluginMetadata>() {

					CheckBox selectedBox = new CheckBox();

					@Override
					protected void updateItem(PaperPluginMetadata item, boolean empty) {
						super.updateItem(item, empty);

						if (item != null && !empty) {
							setGraphic(selectedBox);
							setText(item.getPluginId());

							ContextMenu cm = new ContextMenu();
							MenuItem setAsActiveItem = new MenuItem("Set as active plugin");
							cm.getItems().add(setAsActiveItem);

							setAsActiveItem.setOnAction(e -> {
								activePluginMetadataProperty.set(item);
							});

							selectedBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
								if (newValue) {
									if (!selectedPlugins.contains(item.getPluginId())) {
										selectedPlugins.add(item.getPluginId());
										selectedPluginPaths.add(item.getPluginPath());
									}
								} else {
									selectedPlugins.remove(item.getPluginId());
									selectedPluginPaths.remove(item.getPluginPath());
								}
							});

							setContextMenu(cm);

						} else {
							setGraphic(null);
							setText("");
							setContextMenu(null);
						}

					}

				};
			}

		});

		pluginMetadata.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue != null) {
				PaperPluginMetadata metadata = newValue;
				pluginId.setText(metadata.getPluginId());
				pluginName.setText(metadata.getPluginName());
				pluginAuthor.setText(metadata.getPluginAuthor());
				pluginVersion.setText(metadata.getPluginVersion());
				pluginDescription.setText(metadata.getPluginDescription());

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
						pluginMetadata.getItems().add(metadata);

						Logger.debug(metadata.getPluginId());

					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}

		setResultConverter(btnType -> {

			if (btnType == ButtonType.OK) {
				return new Pair<List<String>, Pair<List<String>, PaperPluginMetadata>>(selectedPlugins,
						new Pair<List<String>, PaperPluginMetadata>(selectedPluginPaths,
								activePluginMetadataProperty.get()));
			}

			return null;
		});

		GridPane gp = new GridPane();
		GridPane.setColumnSpan(pluginDescription, 2);
		GridPane.setColumnSpan(activePluginFld, 2);
		gp.setPadding(new Insets(15));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.add(pluginId, 0, 0);
		gp.add(pluginName, 1, 0);
		gp.add(pluginAuthor, 0, 1);
		gp.add(pluginVersion, 1, 1);
		gp.add(pluginDescription, 0, 2);
		gp.add(activePluginFld, 0, 3);
		gp.add(clearActivePluginBtn, 0, 4);
		gp.add(setActivePluginBtn, 1, 4);

		VBox pluginInfoVBox = new VBox();
		pluginInfoVBox.setPadding(new Insets(15));
		pluginInfoVBox.getChildren().addAll(pluginDeps, new Separator(Orientation.HORIZONTAL), gp);

		HBox rootContainer = new HBox();
		rootContainer.setPadding(new Insets(15));
		rootContainer.getChildren().addAll(pluginMetadata, new Separator(Orientation.VERTICAL), pluginInfoVBox);

		setTitle("Ink - Plugin Selection");
		setHeaderText(
				"Select Plugins to Load, to set a plugin as active, Right Click it and select active plugin from the context menu");
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		getDialogPane().setContent(rootContainer);
	}

}
