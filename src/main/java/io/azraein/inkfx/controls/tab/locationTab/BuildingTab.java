package io.azraein.inkfx.controls.tab.locationTab;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.PaperEditorTab;
import io.azraein.paperfx.system.Action;
import io.azraein.paperfx.system.actors.Npc;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.locations.buildings.Building;
import io.azraein.paperfx.system.locations.buildings.BuildingType;
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

public class BuildingTab extends PaperEditorTab {

    private TextField buildingIdFld, buildingNameFld;
    private ComboBox<BuildingType> buildingTypeBox;
    private TextArea buildingDescriptionArea;
    private ListView<Npc> buildingNpcsLV;
    private ListView<Action> buildingActionsLV;

    public BuildingTab(InkFX inkFX) {
        super(inkFX);
        setText("Building Editor");

        buildingIdFld = new TextField();
        buildingNameFld = new TextField();

        buildingTypeBox = new ComboBox<>();
        buildingTypeBox.getItems().addAll(BuildingType.values());
        buildingTypeBox.getSelectionModel().select(0);
        buildingTypeBox.setConverter(new StringConverter<BuildingType>() {

            @Override
            public String toString(BuildingType object) {
                if (object != null) {
                    return object.name();
                }

                return null;
            }

            @Override
            public BuildingType fromString(String string) {
                for (BuildingType bt : BuildingType.values()) {
                    if (bt.name().equals(string))
                        return bt;
                }

                return null;
            }

        });
        buildingTypeBox.setCellFactory(listView -> new ListCell<BuildingType>() {

            @Override
            public void updateItem(BuildingType item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(item.name());
                } else {
                    this.setText("");
                }
            }

        });

        buildingDescriptionArea = new TextArea();
        buildingDescriptionArea.setEditable(true);

        ListView<Building> dbBuildingView = new ListView<>();
        dbBuildingView.getItems().addAll(inkFX.getObservableBuildingRegistry().values());
        dbBuildingView.setCellFactory(listView -> new ListCell<Building>() {

            @Override
            public void updateItem(Building item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(item.getBuildingId());
                    ContextMenu cm = new ContextMenu();
                    MenuItem removeBuildingItem = new MenuItem("Remove " + item.getBuildingId());
                    removeBuildingItem.setOnAction(event -> {
                        Database pluginDb = inkFX.currentPluginProperty().get().getPluginDatabase();
                        if (pluginDb.getBuildingRegistry().containsKey(item.getBuildingId())) {
                            pluginDb.getBuildingRegistry().remove(item.getBuildingId());
                            inkFX.getObservableBuildingRegistry().remove(item.getBuildingId());
                        }

                    });

                    MenuItem renameBuildingItem = new MenuItem("Rename " + item.getBuildingId());

                    cm.getItems().addAll(removeBuildingItem, renameBuildingItem);
                    this.setContextMenu(cm);
                } else {
                    this.setText("");
                    this.setContextMenu(null);
                }
            };

        });
        dbBuildingView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                buildingIdFld.setText(newValue.getBuildingId());
                buildingNameFld.setText(newValue.getBuildingState().getBuildingName());
                buildingDescriptionArea.setText(newValue.getBuildingState().getBuildingDescription());
                buildingTypeBox.setValue(newValue.getBuildingType());
                buildingNpcsLV.getItems().clear();

                for (String npcId : newValue.getBuildingState().getBuildingNpcs()) {
                    Npc npc = inkFX.getObservableNpcRegistry().get(npcId);
                    buildingNpcsLV.getItems().add(npc);
                }

                for (String actionId : newValue.getBuildingActionIds()) {
                    Action action = inkFX.getObservableActionRegistry().get(actionId);
                    buildingActionsLV.getItems().add(action);
                }

            }

        });
        inkFX.getObservableBuildingRegistry().addListener((MapChangeListener<String, Building>) change -> {

            if (change.wasAdded()) {
                if (inkFX.currentPluginProperty().get().getPluginDatabase().getBuildingRegistry()
                        .containsKey(change.getKey())) {
                    dbBuildingView.getItems().remove(change.getValueRemoved());
                    dbBuildingView.getItems().add(change.getValueAdded());
                } else {
                    dbBuildingView.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                dbBuildingView.getItems().remove(change.getValueRemoved());
            }

        });

        buildingNpcsLV = new ListView<>();
        buildingNpcsLV.setCellFactory(listView -> new ListCell<Npc>() {

            @Override
            public void updateItem(Npc item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(item.getActorId());
                    ContextMenu cm = new ContextMenu();
                    MenuItem removeNpcItem = new MenuItem("Remove " + item.getActorId());
                    removeNpcItem.setOnAction(event -> {
                        buildingNpcsLV.getItems().remove(item);
                    });

                    this.setContextMenu(cm);
                } else {
                    this.setText("");
                    this.setContextMenu(null);
                }
            }

        });

        buildingActionsLV = new ListView<>();
        buildingActionsLV.setCellFactory(listView -> new ListCell<Action>() {

            @Override
            public void updateItem(Action item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getActionId());
                } else {
                    setText("");
                }

            }

        });

        ComboBox<Action> buildingActionSelector = new ComboBox<>();
        buildingActionSelector.getItems().addAll(inkFX.getObservableActionRegistry().values());
        buildingActionSelector.setConverter(new StringConverter<Action>() {

            @Override
            public String toString(Action object) {
                if (object != null)
                    return object.getActionId();

                return null;
            }

            @Override
            public Action fromString(String string) {
                return inkFX.getObservableActionRegistry().get(string);
            }

        });
        buildingActionSelector.setCellFactory(listView -> new ListCell<Action>() {

            @Override
            public void updateItem(Action item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getActionId());
                } else
                    setText("");
            }

        });
        inkFX.getObservableActionRegistry().addListener((MapChangeListener<String, Action>) change -> {
            if (change.wasAdded()) {
                if (inkFX.currentPluginProperty().get().getPluginDatabase().getActionRegistry()
                        .containsKey(change.getKey())) {
                    buildingActionSelector.getItems().remove(change.getValueRemoved());
                    buildingActionSelector.getItems().add(change.getValueAdded());
                } else {
                    buildingActionSelector.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                buildingActionSelector.getItems().remove(change.getValueRemoved());
            }
        });

        ComboBox<Npc> buildingNpcSelectorBox = new ComboBox<>();
        buildingNpcSelectorBox.getItems().addAll(inkFX.getObservableNpcRegistry().values());
        buildingNpcSelectorBox.getSelectionModel().select(0);
        buildingNpcSelectorBox.setCellFactory(listView -> new ListCell<Npc>() {

            @Override
            public void updateItem(Npc item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(item.getActorId());
                } else
                    this.setText("");
            }

        });
        buildingNpcSelectorBox.setConverter(new StringConverter<Npc>() {

            @Override
            public String toString(Npc object) {
                if (object != null)
                    return object.getActorId();

                return null;
            }

            @Override
            public Npc fromString(String string) {
                return inkFX.getObservableNpcRegistry().get(string);
            }

        });

        inkFX.getObservableNpcRegistry().addListener((MapChangeListener<String, Npc>) change -> {
            Database pluginDb = inkFX.currentPluginProperty().get().getPluginDatabase();

            if (change.wasAdded()) {
                if (pluginDb.getNpcRegistry().containsKey(change.getKey())) {
                    buildingNpcsLV.getItems().remove(change.getValueRemoved());
                    buildingNpcSelectorBox.getItems().remove(change.getValueRemoved());

                    buildingNpcsLV.getItems().add(change.getValueAdded());
                    buildingNpcSelectorBox.getItems().add(change.getValueAdded());
                }
            } else {
                buildingNpcsLV.getItems().remove(change.getValueRemoved());
                buildingNpcSelectorBox.getItems().remove(change.getValueRemoved());

            }
        });

        Button saveBuildingBtn = new Button("Save Building");
        saveBuildingBtn.setOnAction(event -> {
            Building building = new Building(buildingIdFld.getText(), buildingNameFld.getText(),
                    buildingDescriptionArea.getText());

            building.setBuildingType(buildingTypeBox.getValue());

            for (Npc npc : buildingNpcsLV.getItems())
                building.getBuildingState().addNpc(npc);

            inkFX.currentPluginProperty().get().getPluginDatabase().addBuilding(building);
            inkFX.getObservableBuildingRegistry().put(building.getBuildingId(), building);
        });

        Button clearFormBtn = new Button("Clear Building Form");
        clearFormBtn.setOnAction(event -> {
            buildingIdFld.setText("");
            buildingNameFld.setText("");
            buildingDescriptionArea.setText("");
            buildingTypeBox.getSelectionModel().select(0);
            buildingNpcsLV.getItems().clear();
        });

        Button addNpcBtn = new Button("Add Npc");
        addNpcBtn.setOnAction(event -> buildingNpcsLV.getItems().add(buildingNpcSelectorBox.getValue()));

        Button addActionBtn = new Button("Add Action");
        addActionBtn.setOnAction(event -> buildingActionsLV.getItems().add(buildingActionSelector.getValue()));

        GridPane gp = new GridPane(10, 10);
        gp.setPadding(new Insets(15));
        gp.add(new Label("Building Id"), 0, 0);
        gp.add(buildingIdFld, 1, 0);
        gp.add(new Label("Building Name"), 2, 0);
        gp.add(buildingNameFld, 3, 0);
        gp.add(new Label("Building Type"), 4, 0);
        gp.add(buildingTypeBox, 5, 0);
        gp.add(new Label("Building Description"), 0, 1);
        gp.add(buildingDescriptionArea, 0, 2, 4, 3);

        gp.add(new Label("Building Npcs"), 0, 5);
        gp.add(buildingNpcsLV, 0, 6, 2, 6);

        gp.add(new Label("Building Actions"), 3, 5);
        gp.add(buildingActionsLV, 3, 6, 2, 6);

        gp.add(buildingNpcSelectorBox, 0, 12);
        gp.add(addNpcBtn, 1, 12);

        gp.add(buildingActionSelector, 3, 12);
        gp.add(addActionBtn, 4, 12);

        gp.add(saveBuildingBtn, 0, 13);
        gp.add(clearFormBtn, 1, 13);

        content.setLeft(dbBuildingView);
        content.setCenter(gp);
    }

}
