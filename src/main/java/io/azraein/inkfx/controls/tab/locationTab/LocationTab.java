package io.azraein.inkfx.controls.tab.locationTab;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.PaperEditorTab;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.Npc;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.locations.Direction;
import io.azraein.paperfx.system.locations.Location;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LocationTab extends PaperEditorTab {

    private TextField locationIdFld;
    private TextField locationNameFld;
    private TextArea locationDescriptionArea;

    private ListView<Npc> locationNpcsLV;

    private TextField[] locationNeighborIdFlds;

    public LocationTab(InkFX inkFX) {
        super(inkFX);
        this.setText("Location Editor");

        ListView<Location> dbLocationLV = new ListView<>();
        dbLocationLV.setCellFactory(listView -> new ListCell<Location>() {

            @Override
            public void updateItem(Location item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null || !empty) {
                    setText(item.getLocationId());

                    ContextMenu cm = new ContextMenu();
                    MenuItem removeLocationItem = new MenuItem("Remove Location");
                    removeLocationItem.setOnAction(event -> {
                        Location location = dbLocationLV.getSelectionModel().getSelectedItem();

                        if (inkFX.currentPluginProperty().get() != null) {
                            Database curPluginDb = inkFX.currentPluginProperty().get().getPluginDatabase();
                            if (curPluginDb.getLocationList().containsKey(location.getLocationId())) {
                                curPluginDb.getLocationList().remove(location.getLocationId());
                                inkFX.getLocationList().remove(location.getLocationId());
                            }
                        }
                    });

                    cm.getItems().add(removeLocationItem);
                    setContextMenu(cm);
                } else {
                    setText("");
                    setContextMenu(null);
                }

            }

        });
        dbLocationLV.getItems().addAll(inkFX.getLocationList().values());

        locationIdFld = new TextField();
        locationNameFld = new TextField();

        locationDescriptionArea = new TextArea();
        locationDescriptionArea.setEditable(false);

        locationNpcsLV = new ListView<>();
        locationNpcsLV.setCellFactory(listView -> new ListCell<Npc>() {

            @Override
            public void updateItem(Npc item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    setText(item.getActorId());
                    ContextMenu cm = new ContextMenu();
                    MenuItem removeNpcItem = new MenuItem();
                    removeNpcItem.setOnAction(event -> {
                        Npc npc = locationNpcsLV.getSelectionModel().getSelectedItem();

                        if (inkFX.currentPluginProperty().get() != null) {
                            Database curPluginDb = inkFX.currentPluginProperty().get().getPluginDatabase();
                            if (curPluginDb.getActorList().containsKey(npc.getActorId())) {
                                curPluginDb.getActorList().remove(npc.getActorId());
                                inkFX.getActorList().remove(npc.getActorId());
                            }
                        }

                    });

                    cm.getItems().add(removeNpcItem);
                    setContextMenu(cm);
                } else {
                    setText("");
                    setContextMenu(null);
                }
            }

        });

        Label[] locationNeighborIdLbls = new Label[4];
        locationNeighborIdFlds = new TextField[4];
        for (int i = 0; i < 4; i++) {
            locationNeighborIdFlds[i] = new TextField();
            locationNeighborIdFlds[i].setEditable(false);
            String idLblStr = Utils.toNormalCase(Direction.values()[i].name() + " Neighbor");
            locationNeighborIdLbls[i] = new Label(idLblStr);
        }

        Button saveLocation = new Button("Save Location");
        saveLocation.setOnAction(event -> {
            Location location = new Location(locationIdFld.getText(), locationNameFld.getText(),
                    locationDescriptionArea.getText());
            locationNpcsLV.getItems()
                    .forEach(npc -> location.getLocationState().getLocationNpcIds().add(npc.getActorId()));

            for (Direction dir : Direction.values()) {
                Database curPluginDb = inkFX.currentPluginProperty().get().getPluginDatabase();
                String locationId = locationNeighborIdFlds[dir.ordinal()].getText();

                Location neighbor;
                if (curPluginDb.getLocationList().containsKey(locationId)) {
                    neighbor = curPluginDb.getLocationList().get(locationId);
                } else {
                    neighbor = inkFX.getLocationList().get(locationId);
                }

                location.setNeighborLocation(dir, neighbor);
            }

            // TODO: Buildings and Creatures once they're implemented

        });

        content.setLeft(dbLocationLV);
    }

}
