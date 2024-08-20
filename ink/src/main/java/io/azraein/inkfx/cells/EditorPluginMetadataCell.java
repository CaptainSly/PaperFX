package io.azraein.inkfx.cells;

import io.azraein.inkfx.dialog.PluginSelectionDialog;
import io.azraein.inkfx.system.io.plugins.PaperPluginMetadata;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;

public class EditorPluginMetadataCell extends ListCell<PaperPluginMetadata> {

	private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

	private final CheckBox pmcCB;
	private final ContextMenu pmcCM;

	public EditorPluginMetadataCell(PluginSelectionDialog dialog) {
		pmcCB = new CheckBox();
		pmcCB.selectedProperty().bindBidirectional(selectedProperty);
		pmcCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				if (!dialog.getSelectedPlugins().contains(getItem())) {
					dialog.getSelectedPlugins().add(getItem());
					dialog.getSelectedPluginPaths().add(getItem().getPluginPath());
				} else {
					dialog.getSelectedPlugins().remove(getItem());
					dialog.getSelectedPluginPaths().remove(getItem().getPluginPath());
				}
			}
		});

		pmcCM = new ContextMenu();
		MenuItem setActive = new MenuItem("Set Active Plugin");
		setActive.setOnAction(e -> {
			dialog.activePluginMetadataProperty().set(getItem());
			pmcCB.setSelected(true);
		});

		pmcCM.getItems().add(setActive);
	}

	@Override
	public void updateItem(PaperPluginMetadata item, boolean empty) {
		super.updateItem(item, empty);

		if (item != null && !empty) {

			String pluginType;
			if (item.isPluginMainFile())
				pluginType = "MAIN";
			else
				pluginType = "ADDON";

			setGraphic(pmcCB);
			setContextMenu(pmcCM);
			setText(item.getPluginId() + " " + pluginType + " : " + getIndex());
		} else {
			setGraphic(null);
			setContextMenu(null);
			setText("");
		}
	}

}
