package io.azraein.paperfx.controls.cells;

import io.azraein.paperfx.system.actors.ActorState;
import io.azraein.paperfx.system.actors.Npc;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;

public class NpcListCell extends ListCell<Npc> {

    @Override
    protected void updateItem(Npc item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            ActorState npc = item.getActorState();
            setText(npc.getActorName());
            ContextMenu npcContextMenu = new ContextMenu();
            MenuItem talkItem = new MenuItem("Talk to " + npc.getActorName());
            MenuItem inspectItem = new MenuItem("Inspect " + npc.getActorName());

            npcContextMenu.getItems().addAll(talkItem, inspectItem);
            setContextMenu(npcContextMenu);
        } else {
            setText("");
            setContextMenu(null);
        }
    }

}
