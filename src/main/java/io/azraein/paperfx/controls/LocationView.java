package io.azraein.paperfx.controls;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.controls.cells.NpcListCell;
import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.Npc;
import io.azraein.paperfx.system.locations.Location;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class LocationView extends Region {

    private TextArea locationDescriptionArea;
    private Label locationNameLbl;
    private ListView<Npc> locationNpcs;

    public LocationView(PaperFX paperFX) {
        locationDescriptionArea = new TextArea();
        locationDescriptionArea.setEditable(false);
        locationDescriptionArea.setWrapText(true);

        locationNameLbl = new Label();

        locationNpcs = new ListView<>();
        locationNpcs.setCellFactory(listView -> new NpcListCell());

        HBox rootContainer = new HBox();
        rootContainer.setPadding(new Insets(15));

        GridPane locationInfoGrid = new GridPane(10, 10);
        locationInfoGrid.add(locationNpcs, 0, 0, 1, 5);
        locationInfoGrid.add(locationNameLbl, 1, 0);
        locationInfoGrid.add(locationDescriptionArea, 1, 1, 4, 5);

        rootContainer.getChildren().addAll(locationNpcs, new Separator(Orientation.VERTICAL), locationInfoGrid);
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

    }

}
