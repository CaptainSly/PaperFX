package io.azraein.paperfx.controls.cells;

import io.azraein.paperfx.system.Action;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

public class ActionListCell extends ListCell<Action> {

    @Override
    protected void updateItem(Action item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            Button actionBtn = new Button(item.getActionName());
            actionBtn.setOnAction(event -> item.onAction());

            setGraphic(actionBtn);
        } else {
            setGraphic(null);
        }

    }

}
