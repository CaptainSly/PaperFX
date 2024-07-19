package io.azraein.inkfx.controls.tab;

import java.util.List;

import io.azraein.inkfx.InkFX;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PluginMetadataTab extends PaperEditorTab {

	TextField pluginIdFld, pluginNameFld, pluginAuthorFld;
	TextArea pluginDescription;
	CheckBox pluginMainFileCB;
	ListView<String> dependencyList;

	public PluginMetadataTab(InkFX inkFX) {
		super(inkFX);
		this.setClosable(false);
		this.setText("Plugin Metadata");

		Label pluginIdLbl = new Label("Plugin ID");
		Label pluginNameLbl = new Label("Plugin Name");
		Label pluginAuthorLbl = new Label("Plugin Author");
		Label pluginMainfileLbl = new Label("Is Plugin Main File?");
		Label pluginDeps = new Label("Plugin Dependencies");

		dependencyList = new ListView<String>();

		pluginIdFld = new TextField();
		pluginNameFld = new TextField();
		pluginAuthorFld = new TextField();

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
		gridPane.add(pluginMainfileLbl, 2, 1);
		gridPane.add(pluginMainFileCB, 3, 1);
		gridPane.add(new Label("Plugin Description"), 0, 2);
		gridPane.add(pluginDescription, 0, 3);
		gridPane.add(pluginDeps, 0, 4);
		gridPane.add(dependencyList, 0, 5);

		setContent(gridPane);
	}

	public void setDependencies(List<String> deps) {
		dependencyList.getItems().clear();
		dependencyList.getItems().addAll(deps);
	}

}
