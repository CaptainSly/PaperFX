package io.azraein.paperfx.controls;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.controls.cells.ActionListCell;
import io.azraein.paperfx.controls.cells.BuildingListCell;
import io.azraein.paperfx.controls.cells.NpcListCell;
import io.azraein.paperfx.system.Action;
import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.Npc;
import io.azraein.paperfx.system.locations.Location;
import io.azraein.paperfx.system.locations.buildings.Building;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LocationView extends Region {

    private TextArea locationDescriptionArea;
    private Label locationNameLbl;

    private LocationMover locationMover;

    private ListView<Npc> locationNpcs;
    private ListView<Building> locationBuildings;
    private ListView<Action> locationActions;

    public LocationView(PaperFX paperFX) {
        locationDescriptionArea = new TextArea();
        locationDescriptionArea.setEditable(false);
        locationDescriptionArea.setWrapText(true);

        locationNameLbl = new Label();

        locationMover = new LocationMover();

        locationNpcs = new ListView<>();
        locationNpcs.setMaxHeight(300);
        locationNpcs.setCellFactory(listView -> new NpcListCell());

        locationBuildings = new ListView<>();
        locationBuildings.setMaxHeight(250);
        locationBuildings.setCellFactory(listView -> new BuildingListCell());

        locationActions = new ListView<>();
        locationActions.setMaxHeight(250);
        locationActions.setCellFactory(listView -> new ActionListCell());

        HBox rootContainer = new HBox();
        rootContainer.setPadding(new Insets(15));

        VBox primaryListContainer = new VBox();
        primaryListContainer.setPadding(new Insets(15));

        VBox secondaryListContainer = new VBox();
        secondaryListContainer.setPadding(new Insets(15));

        GridPane locationInfoGrid = new GridPane(10, 10);
        locationInfoGrid.setPadding(new Insets(15));
        locationInfoGrid.add(locationNpcs, 0, 0, 1, 5);
        locationInfoGrid.add(locationNameLbl, 1, 0);
        locationInfoGrid.add(locationDescriptionArea, 1, 1, 4, 5);

        primaryListContainer.getChildren().addAll(locationNpcs, locationMover);
        secondaryListContainer.getChildren().addAll(locationBuildings, locationActions);
        rootContainer.getChildren().addAll(primaryListContainer, locationInfoGrid, secondaryListContainer);
        getChildren().add(rootContainer);
    }

    public void setLocation(Location location) {
        locationNameLbl.setText(location.getLocationState().getLocationName());
        locationDescriptionArea.setText(location.getLocationState().getLocationDescription());

        locationNpcs.getItems().clear();
        for (String str : location.getLocationState().getLocationNpcIds()) {
            Npc npc = Paper.DATABASE.getNpc(str);
            locationNpcs.getItems().add(npc);
        }

        locationBuildings.getItems().clear();
        for (String buildingId : location.getLocationBuildingIds()) {
            Building building = Paper.DATABASE.getBuilding(buildingId);
            locationBuildings.getItems().add(building);
        }

        locationActions.getItems().clear();
        for (String actionId : location.getLocationActionIds()) {
            Action action = Paper.DATABASE.getAction(actionId);
            locationActions.getItems().add(action);
        }

    }

}
