package io.azraein.paperfx.controls.cells;

import io.azraein.paperfx.controls.dialog.PaperPluginSelectionDialog;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;

public class PaperPluginMetadataCell extends ListCell<PaperPluginMetadata> {

	private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false);

	private final CheckBox pmcCB;
	public PaperPluginMetadataCell(PaperPluginSelectionDialog dialog) {
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

		MenuItem setActive = new MenuItem("Set Active Plugin");
		setActive.setOnAction(e -> {
			pmcCB.setSelected(true);
		});
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
			setText(item.getPluginId() + " " + pluginType + " : " + getIndex());
		} else {
			setGraphic(null);
			setContextMenu(null);
			setText("");
		}
	}

}
