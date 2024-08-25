package io.azraein.paperfx.ui.controls;

import org.tinylog.Logger;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.locations.Direction;
import io.azraein.inkfx.system.locations.Location;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class LocationMover extends Region {

    Button[] locationBtns = new Button[4];

    public LocationMover() {
        setPadding(new Insets(15));

        for (Direction dir : Direction.values()) {
            String dirLetter = dir.name().substring(0, 1);
            locationBtns[dir.ordinal()] = new Button();
            locationBtns[dir.ordinal()].setDisable(true);
            Logger.debug(dirLetter + "Arrow.png");
            locationBtns[dir.ordinal()]
                    .setGraphic(new ImageView(new Image(Utils.getFileFromResources(dirLetter + "Arrow.png"))));
            locationBtns[dir.ordinal()].setOnAction(event -> {
                String neighborId = Paper.PAPER_LOCATION_PROPERTY.get().getLocationNeighbors()[dir.ordinal()];
                Location neighbor = Paper.DATABASE.getLocation(neighborId);
                Paper.AUDIO.playSoundEffect("location_transition_sfx.mp3");
                Paper.PAPER_LOCATION_PROPERTY.set(neighbor);
                Paper.DATABASE.addGlobal("currentLocationName", neighbor.getLocationState().getLocationName());
            });
        }

        Paper.PAPER_LOCATION_PROPERTY.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                for (Direction dir : Direction.values()) {
                    String dirName = Utils.toNormalCase(dir.name());
                    String neighborId = Paper.PAPER_LOCATION_PROPERTY.get().getLocationNeighbors()[dir.ordinal()];

                    if (neighborId != null) {

                        Location neighbor = Paper.DATABASE.getLocation(neighborId);
                        locationBtns[dir.ordinal()].setTooltip(
                                new Tooltip("Go " + dirName + " to " + neighbor.getLocationState().getLocationName()));
                        locationBtns[dir.ordinal()].setDisable(false);
                    } else {
                        locationBtns[dir.ordinal()].setDisable(true);
                    }
                }

            }
        });

        GridPane locationBtnGrid = new GridPane(10, 10);
        locationBtnGrid.add(locationBtns[Direction.NORTH.ordinal()], 1, 0);
        locationBtnGrid.add(locationBtns[Direction.WEST.ordinal()], 0, 1);
        locationBtnGrid.add(locationBtns[Direction.EAST.ordinal()], 2, 1);
        locationBtnGrid.add(locationBtns[Direction.SOUTH.ordinal()], 1, 2);
        getChildren().add(locationBtnGrid);
    }

}
