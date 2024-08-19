package io.azraein.paperfx.ui.controls.cells;

import io.azraein.paperfx.system.locations.buildings.Building;
import javafx.scene.control.ListCell;

public class BuildingListCell extends ListCell<Building> {

    @Override
    public void updateItem(Building item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            this.setText(item.getBuildingState().getBuildingName());
        } else {
            this.setText("");
        }

    }

}
