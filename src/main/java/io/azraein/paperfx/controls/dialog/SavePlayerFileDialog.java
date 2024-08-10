package io.azraein.paperfx.controls.dialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class SavePlayerFileDialog extends Dialog<String> {

    private TextField saveNameFld;

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
                    return saveNameFld.getText();
                }

                return "";
            }

        });

    }

}
