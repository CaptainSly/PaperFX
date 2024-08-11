package io.azraein.paperfx.controls;

import io.azraein.paperfx.screens.GameScreen;
import io.azraein.paperfx.system.Utils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class PlayerControls extends Region {

    public PlayerControls(GameScreen gameScreen) {
        GridPane playerControlGrid = new GridPane(10, 10);
        playerControlGrid.setPadding(new Insets(15));

        Button playerJournalBtn = new Button();
        playerJournalBtn.setTooltip(new Tooltip("Journal"));
        playerJournalBtn.setGraphic(new ImageView(new Image(Utils.getFileFromResources("journal.png"))));
        playerJournalBtn.setOnAction(event -> {
            gameScreen.getPlayerJournalDialog().showJournal();
        });

        playerControlGrid.add(playerJournalBtn, 0, 0);
        getChildren().add(playerControlGrid);
    }

}
