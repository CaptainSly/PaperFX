package io.azraein.paperfx.controls.dialog;

import java.util.Optional;

import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.ActorState;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class SavePlayerFileDialog extends Dialog<String> {

    private TextField saveNameFld;

    private String defaultSaveName = "";

    private int internalCounter = 1;

    public SavePlayerFileDialog() {
        setTitle("Choose Save File Name");
        setHeaderText("Choose your save name, then click okay!");
        saveNameFld = new TextField();
        getDialogPane().setContent(saveNameFld);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        setResultConverter(new Callback<ButtonType, String>() {

            @Override
            public String call(ButtonType param) {
                if (param.equals(ButtonType.OK)) {

                    if (saveNameFld.getText().equals(defaultSaveName)) {
                        internalCounter++;
                    }

                    return saveNameFld.getText();
                }

                return "";
            }

        });

    }

    public Optional<String> showDialog() {
        ActorState playerState = Paper.PAPER_PLAYER_PROPERTY.get().getActorState();
        defaultSaveName = String.format("Save %d %s %s", internalCounter, playerState.getActorName(),
                Paper.PAPER_LOCATION_PROPERTY.get().getLocationState().getLocationName());
        saveNameFld.setText(defaultSaveName);

        return this.showAndWait();
    }

}
