package io.azraein.paperfx.ui.controls.tabs;

import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.ActorState;
import io.azraein.paperfx.system.inventory.ItemSlot;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

// TODO: Continue working on the implementation

public class InventoryTab extends JournalTab {

    private ListView<ItemSlot> inventoryLV;
    private Label playerCarryWeight;

    public InventoryTab() {
        super("Inventory");

        Label itemNameLbl = new Label("Item: ");
        playerCarryWeight = new Label("Carry Weight: ");

        TextArea itemDescriptionArea = new TextArea();
        itemDescriptionArea.setEditable(false);

        inventoryLV = new ListView<>();
        inventoryLV.setCellFactory(listView -> new ListCell<ItemSlot>() {

            @Override
            public void updateItem(ItemSlot item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(item.getItem().getItemName() + " | " + item.getItemCount());
                    ContextMenu invCM = new ContextMenu();
                    MenuItem useItemMenuItem = new MenuItem();
                    MenuItem removeItemMenuItem = new MenuItem();

                    invCM.getItems().addAll(useItemMenuItem, removeItemMenuItem);
                    setContextMenu(invCM);
                } else {
                    this.setText("");
                    this.setContextMenu(null);
                }

            };

        });
        inventoryLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                itemNameLbl.setText("Item: ");
                itemDescriptionArea.setText(newValue.getItem().getItemDescription());
            }

        });

        GridPane gp = new GridPane(10, 10);
        gp.setPadding(new Insets(15));
        gp.add(playerCarryWeight, 0, 0);
        gp.add(itemNameLbl, 0, 1);
        gp.add(new Label("Description:"), 0, 2);
        gp.add(itemDescriptionArea, 0, 3);

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(15));
        bp.setLeft(inventoryLV);
        bp.setCenter(gp);

        setContent(bp);
    }

    @Override
    public void updateTab() {
        ActorState playerState = Paper.PAPER_PLAYER_PROPERTY.get().getActorState();

        double currentInvWeight = playerState.getActorInventory().getInventoryWeight();
        playerCarryWeight.setText("Carry Weight: " + currentInvWeight + "/" + playerState.getActorCarryWeight());
        
        inventoryLV.getItems().clear();
        inventoryLV.getItems().addAll(playerState.getActorInventory().getItemSlots());
    }

}
