package io.azraein.inkfx.controls.tab.locationTab;

import org.tinylog.Logger;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.PaperEditorTab;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.Actor;
import io.azraein.paperfx.system.actors.Npc;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.locations.Direction;
import io.azraein.paperfx.system.locations.Location;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

@SuppressWarnings("unchecked")
public class LocationTab extends PaperEditorTab {

    private TextField locationIdFld;
    private TextField locationNameFld;
    private TextArea locationDescriptionArea;

    private ListView<Npc> locationNpcsLV;

    private TextField[] locationNeighborIdFlds;
    private ComboBox<Location>[] locationNeighborSelectors;

    public LocationTab(InkFX inkFX) {
        super(inkFX);
        this.setText("Location Editor");

        ListView<Location> dbLocationLV = new ListView<>();
        dbLocationLV.setCellFactory(listView -> new ListCell<Location>() {

            @Override
            public void updateItem(Location item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
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
        dbLocationLV.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                locationIdFld.setText(newValue.getLocationId());
                locationNameFld.setText(newValue.getLocationState().getLocationName());
                locationDescriptionArea.setText(newValue.getLocationState().getLocationDescription());

                locationNpcsLV.getItems().clear();
                for (String npcId : newValue.getLocationState().getLocationNpcIds()) {
                    Npc npc = ((Npc) inkFX.getActorList().get(npcId));
                    locationNpcsLV.getItems().add(npc);
                }

                for (Direction dir : Direction.values()) {
                    locationNeighborIdFlds[dir.ordinal()].setText("");
                    Location neighbor = inkFX.getLocationList().get(newValue.getLocationNeighbors()[dir.ordinal()]);
                    locationNeighborSelectors[dir.ordinal()].getSelectionModel().select(dir.ordinal());
                    if (neighbor != null) {
                        locationNeighborIdFlds[dir.ordinal()].setText(neighbor.getLocationId());
                    }
                }

                // TODO Implement Buildings and Creature class, then finish this.
            }
        });
        dbLocationLV.getItems().addAll(inkFX.getLocationList().values());

        locationIdFld = new TextField();
        locationNameFld = new TextField();

        locationDescriptionArea = new TextArea();
        locationDescriptionArea.setWrapText(true);

        locationNpcsLV = new ListView<>();
        locationNpcsLV.setCellFactory(listView -> new ListCell<Npc>() {

            @Override
            public void updateItem(Npc item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
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

        ComboBox<Npc> locationNpcSelector = new ComboBox<>();
        for (Actor actor : inkFX.getActorList().values()) {
            locationNpcSelector.getItems().add((Npc) actor);
        }
        locationNpcSelector.setConverter(new StringConverter<Npc>() {

            @Override
            public String toString(Npc object) {
                if (object != null)
                    return object.getActorId();
                return null;
            }

            @Override
            public Npc fromString(String string) {
                return null;
            }
        });
        locationNpcSelector.setCellFactory((listView) -> new ListCell<Npc>() {
            @Override
            protected void updateItem(Npc item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null && !empty) {
                    setText(item.getActorId());
                } else
                    setText("");

            }

        });
        inkFX.getActorList().addListener(new MapChangeListener<String, Actor>() {

            @Override
            public void onChanged(Change<? extends String, ? extends Actor> change) {

                if (change.wasAdded()) {
                    if (inkFX.currentPluginProperty().get().getPluginDatabase().getActorList()
                            .containsKey(change.getKey())) {
                        locationNpcSelector.getItems().remove((Npc) change.getValueRemoved());
                        locationNpcSelector.getItems().add((Npc) change.getValueAdded());
                    } else {
                        locationNpcSelector.getItems().add((Npc) change.getValueAdded());
                    }
                } else if (change.wasRemoved()) {
                    locationNpcSelector.getItems().remove((Npc) change.getValueRemoved());
                }

            }

        });
        locationNpcSelector.getSelectionModel().select(0);

        Button addNpcBtn = new Button("Add Npc to Location");
        addNpcBtn.setOnAction(event -> {
            Npc npc = locationNpcSelector.getValue();
            if (npc != null) {
                if (!locationNpcsLV.getItems().contains(npc)) {
                    locationNpcsLV.getItems().add(npc);
                }
            }
        });

        Label[] locationNeighborIdLbls = new Label[4];
        locationNeighborSelectors = new ComboBox[4];
        Button[] locationNeighborSelectorBtns = new Button[4];
        locationNeighborIdFlds = new TextField[4];
        for (int i = 0; i < 4; i++) {

            int idx = i;
            String idLblStr = Utils.toNormalCase(Direction.values()[i].name() + " Neighbor");

            locationNeighborIdFlds[i] = new TextField();
            locationNeighborIdLbls[i] = new Label(idLblStr);

            locationNeighborSelectorBtns[i] = new Button("Add Location as Neighbor");
            locationNeighborSelectorBtns[i].setOnAction(event -> {
                Location neighbor = locationNeighborSelectors[idx].getValue();
                if (neighbor != null) {
                    locationNeighborIdFlds[idx].setText(neighbor.getLocationId());
                }
            });

            locationNeighborSelectors[i] = new ComboBox<Location>();
            locationNeighborSelectors[i].getItems().addAll(inkFX.getLocationList().values());
            locationNeighborSelectors[i].getSelectionModel().select(Direction.values()[i].ordinal());
            locationNeighborSelectors[i].setConverter(new StringConverter<Location>() {

                @Override
                public String toString(Location object) {
                    if (object != null)
                        return object.getLocationId();

                    return null;
                }

                @Override
                public Location fromString(String string) {
                    return null;
                }

            });
            locationNeighborSelectors[i].getSelectionModel().select(0);

            locationNeighborIdFlds[i].setEditable(false);
            locationNeighborIdFlds[i].setPromptText(Utils.toNormalCase(Direction.values()[i].name()) + " neighbor");
        }

        Button saveLocationBtn = new Button("Save Location");
        saveLocationBtn.setOnAction(event -> {
            Location selectIndex = dbLocationLV.getSelectionModel().getSelectedItem();

            Location location = new Location(locationIdFld.getText(), locationNameFld.getText(),
                    locationDescriptionArea.getText());
            locationNpcsLV.getItems()
                    .forEach(npc -> location.getLocationState().getLocationNpcIds().add(npc.getActorId()));

            Logger.debug("location to be saved: " + location.getLocationId());

            for (Direction dir : Direction.values()) {
                String locationId = locationNeighborIdFlds[dir.ordinal()].getText();

                Location neighbor;
                neighbor = inkFX.getLocationList().get(locationId);

                // if we're null, fuck it.
                if (neighbor == null)
                    continue;

                // oh wait, we're trying to add ourself to as a neighbor? Nah fuck it
                if (neighbor.getLocationId().equals(location.getLocationId()))
                    continue;

                location.setNeighborLocation(dir, neighbor);
                dbLocationLV.getSelectionModel().select(selectIndex);
            }

            // TODO: Buildings and Creatures once they're implemented

            inkFX.currentPluginProperty().get().getPluginDatabase().addLocation(location);
            inkFX.getLocationList().put(location.getLocationId(), location);
        });

        Button clearLocationFormBtn = new Button("Clear Location");
        clearLocationFormBtn.setOnAction(event -> {
            locationIdFld.clear();
            locationNameFld.clear();
            locationDescriptionArea.clear();
            locationNpcsLV.getItems().clear();

            for (Direction dir : Direction.values())
                locationNeighborIdFlds[dir.ordinal()].clear();
        });

        inkFX.currentPluginProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                dbLocationLV.getItems().clear();
            }
        });

        inkFX.getLocationList().addListener(new MapChangeListener<String, Location>() {

            @Override
            public void onChanged(Change<? extends String, ? extends Location> change) {
                if (change.wasAdded()) {
                    if (inkFX.currentPluginProperty().get().getPluginDatabase().getLocationList()
                            .containsKey(change.getKey())) {
                        dbLocationLV.getItems().remove(change.getValueRemoved());
                        dbLocationLV.getItems().add(change.getValueAdded());

                        for (Direction dir : Direction.values()) {
                            locationNeighborSelectors[dir.ordinal()].getItems().remove(change.getValueRemoved());
                            locationNeighborSelectors[dir.ordinal()].getItems().add(change.getValueAdded());
                        }

                    } else {
                        dbLocationLV.getItems().add(change.getValueAdded());
                        for (Direction dir : Direction.values()) {
                            locationNeighborSelectors[dir.ordinal()].getItems().add(change.getValueAdded());
                        }
                    }
                } else if (change.wasRemoved()) {
                    dbLocationLV.getItems().remove(change.getValueRemoved());
                    for (Direction dir : Direction.values()) {
                        locationNeighborSelectors[dir.ordinal()].getItems().remove(change.getValueRemoved());
                    }
                }
            }
        });

        Label locationIdLbl = new Label("Location Id");
        Label locationNameLbl = new Label("Location Name");
        Label locationDescriptionLbl = new Label("Location Description");
        Label locationNpcLbl = new Label("Location Npcs");

        GridPane gp = new GridPane();
        GridPane.setColumnSpan(locationDescriptionArea, 3);
        GridPane.setColumnSpan(locationNpcsLV, 2);
        GridPane.setRowSpan(locationNpcsLV, 7);
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(15));
        gp.add(locationIdLbl, 0, 0);
        gp.add(locationIdFld, 1, 0);
        gp.add(locationNameLbl, 2, 0);
        gp.add(locationNameFld, 3, 0);
        gp.add(locationDescriptionLbl, 0, 1);
        gp.add(locationDescriptionArea, 1, 1);
        gp.add(locationNpcLbl, 0, 2);
        gp.add(locationNpcSelector, 1, 2);
        gp.add(addNpcBtn, 2, 2);
        gp.add(locationNpcsLV, 0, 3);
        gp.add(saveLocationBtn, 0, 10);
        gp.add(clearLocationFormBtn, 1, 10);

        int startRow = 3;
        for (Direction dir : Direction.values()) {
            gp.add(locationNeighborIdLbls[dir.ordinal()], 2, startRow);
            gp.add(locationNeighborIdFlds[dir.ordinal()], 3, startRow);
            gp.add(locationNeighborSelectors[dir.ordinal()], 4, startRow);
            gp.add(locationNeighborSelectorBtns[dir.ordinal()], 5, startRow);

            startRow++;
        }

        content.setLeft(dbLocationLV);
        content.setCenter(gp);
    }

}
