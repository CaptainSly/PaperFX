package io.azraein.inkfx.screens;

import java.util.ArrayList;
import java.util.List;

import io.azraein.inkfx.InkFX;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PluginMetadataScreen extends PaperEditorScreen {

	TextField pluginIdFld, pluginNameFld, pluginAuthorFld, pluginVersionFld;
	TextArea pluginDescription;
	CheckBox pluginMainFileCB;
	ListView<String> dependencyList;

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

			plugin.setPluginId(getPluginIdFld().getText());
			plugin.setPluginName(getPluginNameFld().getText());
			plugin.setPluginAuthor(getPluginAuthorFld().getText());
			plugin.setPluginDescription(getPluginDescription().getText());
			plugin.setPluginVersion(getPluginVersionFld().getText());
			plugin.setIsMainFile(getPluginMainFileCB().isSelected());

			List<String> dependencyList = new ArrayList<>();
			for (String dependency : getDependencyList().getItems())
				dependencyList.add(dependency);

			plugin.setPluginDependencies(dependencyList);
			inkFX.swapScreens(InkFX.PLUGIN_CONTENT_EDITOR_SCREEN);
		});

		dependencyList = new ListView<String>();

		pluginIdFld = new TextField();
		pluginNameFld = new TextField();
		pluginAuthorFld = new TextField();
		pluginVersionFld = new TextField();

		pluginMainFileCB = new CheckBox();

		pluginDescription = new TextArea();
		pluginDescription.setWrapText(true);
		pluginDescription.setPromptText("Plugin Description...");

		GridPane gridPane = new GridPane();
		GridPane.setColumnSpan(pluginDescription, 2);
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
		gridPane.add(dependencyList, 0, 5);
		gridPane.add(saveMetadataToPlugin, 1, 5);

		setCenter(gridPane);

	}

	public void setDependencies(List<String> deps) {
		dependencyList.getItems().clear();
		dependencyList.getItems().addAll(deps);
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

	public ListView<String> getDependencyList() {
		return dependencyList;
	}

}
