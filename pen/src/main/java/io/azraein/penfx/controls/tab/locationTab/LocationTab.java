package io.azraein.penfx.controls.tab.locationTab;

import org.tinylog.Logger;

import io.azraein.inkfx.system.Action;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.actors.Npc;
import io.azraein.inkfx.system.io.Database;
import io.azraein.inkfx.system.locations.Direction;
import io.azraein.inkfx.system.locations.Location;
import io.azraein.inkfx.system.locations.buildings.Building;
import io.azraein.penfx.PenFX;
import io.azraein.penfx.controls.tab.PaperEditorTab;
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
    private ListView<Building> locationBuildingsLV;
    private ListView<Action> locationActionsLV;

    private ComboBox<Location>[] locationNeighborSelectors;

    public LocationTab(PenFX penFX) {
        super(penFX);
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

                        if (penFX.currentPluginProperty().get() != null) {
                            Database curPluginDb = penFX.currentPluginProperty().get().getPluginDatabase();
                            if (curPluginDb.getLocationRegistry().containsKey(location.getLocationId())) {
                                curPluginDb.getLocationRegistry().remove(location.getLocationId());
                                penFX.getObservableLocationRegistry().remove(location.getLocationId());
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
                    Npc npc = penFX.getObservableNpcRegistry().get(npcId);
                    locationNpcsLV.getItems().add(npc);
                }

                for (Direction dir : Direction.values()) {
                    locationNeighborSelectors[dir.ordinal()].setValue(
                            penFX.getObservableLocationRegistry().get(newValue.getLocationNeighbors()[dir.ordinal()]));
                }

                for (String buildingId : newValue.getLocationBuildingIds()) {
                    locationBuildingsLV.getItems().add(penFX.getObservableBuildingRegistry().get(buildingId));
                }

                for (String actionId : newValue.getLocationActionIds()) {
                    locationActionsLV.getItems().add(penFX.getObservableActionRegistry().get(actionId));
                }

                // TODO: Creatures need to be added.
            }
        });
        dbLocationLV.getItems().addAll(penFX.getObservableLocationRegistry().values());

        locationIdFld = new TextField();
        locationNameFld = new TextField();

        locationDescriptionArea = new TextArea();
        locationDescriptionArea.setWrapText(true);

        locationActionsLV = new ListView<>();
        locationActionsLV.setCellFactory(listView -> new ListCell<Action>() {

            @Override
            public void updateItem(Action item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(item.getActionId());
                    ContextMenu cm = new ContextMenu();
                    MenuItem removeActionItem = new MenuItem();
                    removeActionItem.setOnAction(event -> {
                        locationActionsLV.getItems().remove(item);
                    });

                    cm.getItems().add(removeActionItem);
                    this.setContextMenu(cm);
                } else {
                    this.setText("");
                    this.setContextMenu(null);
                }

            }

        });

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
                        locationNpcsLV.getItems().remove(npc);
                    });

                    cm.getItems().add(removeNpcItem);
                    setContextMenu(cm);
                } else {
                    setText("");
                    setContextMenu(null);
                }
            }

        });

        locationBuildingsLV = new ListView<>();
        locationBuildingsLV.setCellFactory(listView -> new ListCell<Building>() {

            @Override
            public void updateItem(Building item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(item.getBuildingId());
                    ContextMenu cm = new ContextMenu();
                    MenuItem removeBuildingItem = new MenuItem("Remove " + item.getBuildingId());
                    removeBuildingItem.setOnAction(event -> {
                        locationBuildingsLV.getItems().remove(item);
                    });

                    cm.getItems().addAll(removeBuildingItem);
                    this.setContextMenu(cm);
                } else {
                    this.setText("");
                    this.setContextMenu(null);
                }
            }

        });

        ComboBox<Action> locationActionSelector = new ComboBox<>();
        locationActionSelector.getItems().addAll(penFX.getObservableActionRegistry().values());
        locationActionSelector.setConverter(new StringConverter<Action>() {

            @Override
            public String toString(Action object) {
                if (object != null)
                    return object.getActionId();

                return null;
            }

            @Override
            public Action fromString(String string) {
                return penFX.getObservableActionRegistry().get(string);
            }

        });
        locationActionSelector.setCellFactory(listView -> new ListCell<Action>() {

            @Override
            public void updateItem(Action item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getActionId());
                } else
                    setText("");
            }

        });
        penFX.getObservableActionRegistry().addListener((MapChangeListener<String, Action>) change -> {
            if (change.wasAdded()) {
                if (penFX.currentPluginProperty().get().getPluginDatabase().getActionRegistry()
                        .containsKey(change.getKey())) {
                    locationActionSelector.getItems().remove(change.getValueRemoved());
                    locationActionSelector.getItems().add(change.getValueAdded());
                } else {
                    locationActionSelector.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                locationActionSelector.getItems().remove(change.getValueRemoved());
            }
        });

        ComboBox<Building> locationBuildingSelector = new ComboBox<>();
        locationBuildingSelector.getItems().addAll(penFX.getObservableBuildingRegistry().values());
        locationBuildingSelector.setConverter(new StringConverter<Building>() {

            @Override
            public String toString(Building object) {
                if (object != null)
                    return object.getBuildingId();

                return null;
            }

            @Override
            public Building fromString(String string) {
                return penFX.getObservableBuildingRegistry().get(string);
            }

        });
        locationBuildingSelector.setCellFactory(listView -> new ListCell<Building>() {

            @Override
            public void updateItem(Building item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(item.getBuildingId());
                } else
                    setText("");

            }

        });
        penFX.getObservableBuildingRegistry().addListener((MapChangeListener<String, Building>) change -> {

            if (change.wasAdded()) {
                if (penFX.currentPluginProperty().get().getPluginDatabase().getNpcRegistry()
                        .containsKey(change.getKey())) {
                    locationBuildingSelector.getItems().remove(change.getValueRemoved());
                    locationBuildingSelector.getItems().add(change.getValueAdded());
                } else {
                    locationBuildingSelector.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                locationBuildingSelector.getItems().remove(change.getValueRemoved());
            }

        });

        ComboBox<Npc> locationNpcSelector = new ComboBox<>();
        locationNpcSelector.getItems().addAll(penFX.getObservableNpcRegistry().values());
        locationNpcSelector.setConverter(new StringConverter<Npc>() {

            @Override
            public String toString(Npc object) {
                if (object != null)
                    return object.getActorId();
                return null;
            }

            @Override
            public Npc fromString(String string) {
                return penFX.getObservableNpcRegistry().get(string);
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
        penFX.getObservableNpcRegistry().addListener((MapChangeListener<String, Npc>) change -> {

            if (change.wasAdded()) {
                if (penFX.currentPluginProperty().get().getPluginDatabase().getNpcRegistry()
                        .containsKey(change.getKey())) {
                    locationNpcSelector.getItems().remove(change.getValueRemoved());
                    locationNpcSelector.getItems().add(change.getValueAdded());
                } else {
                    locationNpcSelector.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                locationNpcSelector.getItems().remove(change.getValueRemoved());
            }

        });
        locationNpcSelector.getSelectionModel().select(0);

        Button addBuildingBtn = new Button("Add Building");
        addBuildingBtn.setOnAction(event -> {
            Building building = locationBuildingSelector.getValue();
            if (building != null) {
                if (!locationBuildingsLV.getItems().contains(building))
                    locationBuildingsLV.getItems().add(building);
            }
        });

        Button addNpcBtn = new Button("Add Npc");
        addNpcBtn.setOnAction(event -> {
            Npc npc = locationNpcSelector.getValue();
            if (npc != null) {
                if (!locationNpcsLV.getItems().contains(npc)) {
                    locationNpcsLV.getItems().add(npc);
                }
            }
        });

        Button addActionBtn = new Button("Add Action");
        addActionBtn.setOnAction(event -> {
            locationActionsLV.getItems().add(locationActionSelector.getValue());
        });

        Label[] locationNeighborIdLbls = new Label[4];
        locationNeighborSelectors = new ComboBox[4];
        Button[] locationNeighborSelectorClearBtns = new Button[4];
        for (int i = 0; i < 4; i++) {

            int idx = i;
            String idLblStr = Utils.toNormalCase(Direction.values()[i].name() + " Neighbor");

            locationNeighborIdLbls[i] = new Label(idLblStr);
            locationNeighborSelectorClearBtns[i] = new Button("Clear");

            locationNeighborSelectorClearBtns[i].setOnAction(event -> {
                locationNeighborSelectors[idx].getSelectionModel().select(null);
            });

            locationNeighborSelectors[i] = new ComboBox<Location>();
            locationNeighborSelectors[i].getItems().addAll(penFX.getObservableLocationRegistry().values());
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
                    return penFX.getObservableLocationRegistry().get(string);
                }

            });
            locationNeighborSelectors[i].getSelectionModel().select(0);
        }

        Button saveLocationBtn = new Button("Save Location");
        saveLocationBtn.setOnAction(event -> {
            Location location = new Location(locationIdFld.getText(), locationNameFld.getText(),
                    locationDescriptionArea.getText());
            locationNpcsLV.getItems()
                    .forEach(npc -> location.getLocationState().getLocationNpcIds().add(npc.getActorId()));

            Logger.debug("location to be saved: " + location.getLocationId());

            for (Direction dir : Direction.values()) {
                if (locationNeighborSelectors[dir.ordinal()].getValue() != null) {

                    Location neighbor = locationNeighborSelectors[dir.ordinal()].getValue();

                    // if we're null, fuck it.
                    if (neighbor == null)
                        continue;

                    // oh wait, we're trying to add ourself to as a neighbor? Nah fuck it
                    if (neighbor.getLocationId().equals(location.getLocationId()))
                        continue;

                    location.setNeighborLocation(dir, neighbor);
                }
            }

            // TODO: Creatures once they're implemented

            for (Building building : locationBuildingsLV.getItems()) {
                location.getLocationBuildingIds().add(building.getBuildingId());
            }

            for (Action action : locationActionsLV.getItems()) {
                location.getLocationActionIds().add(action.getActionId());
            }

            penFX.currentPluginProperty().get().getPluginDatabase().addLocation(location);
            penFX.getObservableLocationRegistry().put(location.getLocationId(), location);
        });

        Button clearLocationFormBtn = new Button("Clear Location");
        clearLocationFormBtn.setOnAction(event -> {
            locationIdFld.clear();
            locationNameFld.clear();
            locationDescriptionArea.clear();
            locationNpcsLV.getItems().clear();
            locationBuildingsLV.getItems().clear();
        });

        penFX.currentPluginProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                dbLocationLV.getItems().clear();
            }
        });

        penFX.getObservableLocationRegistry().addListener(new MapChangeListener<String, Location>() {

            @Override
            public void onChanged(Change<? extends String, ? extends Location> change) {
                if (change.wasAdded()) {
                    if (penFX.currentPluginProperty().get().getPluginDatabase().getLocationRegistry()
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
        Label locationBuildingLbl = new Label("Location Buildings");
        Label locationActionLbl = new Label("Location Actions");

        GridPane gp = new GridPane(10, 10);
        gp.setPadding(new Insets(15));
        gp.add(locationIdLbl, 0, 0);
        gp.add(locationIdFld, 1, 0);
        gp.add(locationNameLbl, 2, 0);
        gp.add(locationNameFld, 3, 0);
        gp.add(locationDescriptionLbl, 0, 1);
        gp.add(locationDescriptionArea, 1, 1, 6, 4);

        gp.add(locationNpcLbl, 0, 6);
        gp.add(locationNpcSelector, 1, 6);
        gp.add(addNpcBtn, 2, 6);
        gp.add(locationNpcsLV, 0, 7, 3, 7);

        gp.add(locationBuildingLbl, 3, 6);
        gp.add(locationBuildingSelector, 4, 6);
        gp.add(addBuildingBtn, 5, 6);
        gp.add(locationBuildingsLV, 3, 7, 3, 7);

        gp.add(locationActionLbl, 6, 6);
        gp.add(locationActionSelector, 7, 6);
        gp.add(addActionBtn, 8, 6);
        gp.add(locationActionsLV, 6, 7, 3, 7);

        gp.add(saveLocationBtn, 0, 14);
        gp.add(clearLocationFormBtn, 1, 14);

        int startRow = 0;
        int col = 7;
        for (Direction dir : Direction.values()) {
            gp.add(locationNeighborIdLbls[dir.ordinal()], col, startRow);
            gp.add(locationNeighborSelectors[dir.ordinal()], col + 1, startRow);
            gp.add(locationNeighborSelectorClearBtns[dir.ordinal()], col + 2, startRow);

            startRow++;
        }

        content.setLeft(dbLocationLV);
        content.setCenter(gp);
    }

}
