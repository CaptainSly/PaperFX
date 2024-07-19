package io.azraein.inkfx.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;

public class PluginSelectionDialog extends Dialog<Pair<List<String>, PaperPlugin>> {

	private ListView<PaperPluginMetadata> pluginMetadata;
	private ListView<String> pluginDeps;

	private TextArea pluginDescription;
	private TextField pluginId, pluginName, pluginAuthor;

	private List<String> selectedPlugins;

	private PaperPlugin activePlugin;

	public PluginSelectionDialog() {
		selectedPlugins = new ArrayList<>();

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

		pluginDeps = new ListView<>();
		pluginDeps.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				return new ListCell<String>() {

					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (!empty || !item.isEmpty()) {
							setText(item);
						} else
							setText(null);

					}

				};
			}
		});

		pluginMetadata = new ListView<>();
		pluginMetadata.setCellFactory(new Callback<ListView<PaperPluginMetadata>, ListCell<PaperPluginMetadata>>() {

			@Override
			public ListCell<PaperPluginMetadata> call(ListView<PaperPluginMetadata> param) {
				return new ListCell<PaperPluginMetadata>() {

					CheckBox selectedBox = new CheckBox();

					@Override
					protected void updateItem(PaperPluginMetadata item, boolean empty) {
						super.updateItem(item, empty);
						selectedBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
							if (newValue) {
								if (!selectedPlugins.contains(item.getPluginId()))
									selectedPlugins.add(item.getPluginId());
							} else {
								selectedPlugins.remove(item.getPluginId());
							}
						});

						if (item != null && !empty) {
							setGraphic(selectedBox);
							setText(item.getPluginId());

						} else {
							setGraphic(null);
							setText("");
						}

					}

				};
			}

		});

		pluginMetadata.selectionModelProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue.getSelectedItem() != null) {
				PaperPluginMetadata metadata = newValue.getSelectedItem();
				pluginId.setText(metadata.getPluginId());
				pluginName.setText(metadata.getPluginName());
				pluginAuthor.setText(metadata.getPluginAuthor());
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
						PaperPluginMetadata metadata = SaveSystem.readPluginMetadata(file.getPath());
						pluginMetadata.getItems().add(metadata);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		}

		setResultConverter(btnType -> {

			if (btnType == ButtonType.OK) {
				return new Pair<List<String>, PaperPlugin>(selectedPlugins, activePlugin);
			}

			return null;
		});

		GridPane gp = new GridPane();
		GridPane.setColumnSpan(pluginDescription, 2);
		gp.setPadding(new Insets(15));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.add(pluginId, 0, 0);
		gp.add(pluginName, 1, 0);
		gp.add(pluginAuthor, 0, 1);
		gp.add(pluginDescription, 0, 2);

		VBox pluginInfoVBox = new VBox();
		pluginInfoVBox.setPadding(new Insets(15));
		pluginInfoVBox.getChildren().addAll(pluginDeps, new Separator(Orientation.HORIZONTAL), gp);

		HBox rootContainer = new HBox();
		rootContainer.setPadding(new Insets(15));
		rootContainer.getChildren().addAll(pluginMetadata, new Separator(Orientation.VERTICAL), pluginInfoVBox);

		setTitle("Ink - Plugin Selection");
		setHeaderText("Select Plugins to Load");
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		getDialogPane().setContent(rootContainer);
	}

}
