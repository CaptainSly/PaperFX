package io.azraein.paperfx.ui.controls.dialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import io.azraein.inkfx.system.Utils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class EasterEggDialog {

    public void show() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("You've done goofed up!");
        alert.setContentText("Now time for your punishment");
        alert.setGraphic(new ImageView(new Image(Utils.getFileFromResources("bbb.bee"))));

        Label label = new Label("OH, NO, NOT THE BEES! NOT THE BEES! AAAAAHHHHH! OH, THEY'RE IN MY EYES!");

        String exception = "";
        try {
            URI url = new URI("https://tinyurl.com/squizzmak");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.toURL().openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                exception += line + "\n";
            }
            in.close();

        } catch (Exception e) {
        }

        TextArea textArea = new TextArea(exception);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setContent(expContent);

        alert.showAndWait();
    }

}
