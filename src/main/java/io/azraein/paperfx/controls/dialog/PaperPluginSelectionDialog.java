package io.azraein.paperfx.controls.dialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tinylog.Logger;

import io.azraein.inkfx.dialog.PluginSelectionResult;
import io.azraein.paperfx.controls.cells.PaperPluginMetadataCell;
import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PaperPluginSelectionDialog extends Dialog<PluginSelectionResult> {

    private ListView<PaperPluginMetadata> loadablePluginList;
    private TextArea pluginDescriptionArea;
    private TextField pluginIdFld, pluginNameFld, pluginAuthorFld, pluginVersionFld;

    private ObservableList<PaperPluginMetadata> selectedPlugins;
    private List<String> selectedPluginPaths;

    public PaperPluginSelectionDialog() {
        PaperPluginSelectionDialog ppsd = this;
        selectedPlugins = FXCollections.observableArrayList();
        selectedPluginPaths = new ArrayList<>();

        pluginDescriptionArea = new TextArea();
        pluginDescriptionArea.setPromptText("Plugin Description");
        pluginDescriptionArea.setWrapText(true);
        pluginDescriptionArea.setEditable(false);

        pluginIdFld = new TextField();
        pluginIdFld.setPromptText("Plugin ID");
        pluginIdFld.setEditable(false);

        pluginNameFld = new TextField();
        pluginNameFld.setPromptText("PluginName");
        pluginNameFld.setEditable(false);

        pluginAuthorFld = new TextField();
        pluginAuthorFld.setPromptText("Plugin Author");
        pluginAuthorFld.setEditable(false);

        pluginVersionFld = new TextField();
        pluginVersionFld.setPromptText("Plugin Version");
        pluginVersionFld.setEditable(false);

        loadablePluginList = new ListView<>();
        loadablePluginList.setCellFactory((ListView<PaperPluginMetadata> param) -> new PaperPluginMetadataCell(ppsd));

        loadablePluginList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                PaperPluginMetadata metadata = newValue;

                String pluginType;
                if (metadata.isPluginMainFile())
                    pluginType = "MAIN";
                else
                    pluginType = "ADDON";

                pluginIdFld.setText(metadata.getPluginId() + " " + pluginType);
                pluginNameFld.setText(metadata.getPluginName());
                pluginAuthorFld.setText(metadata.getPluginAuthor());
                pluginVersionFld.setText(metadata.getPluginVersion());
                pluginDescriptionArea.setText(metadata.getPluginDescription());
            }

        });

        for (File file : new File(SaveSystem.PAPER_DATA_FOLDER).listFiles()) {
            if (file.exists()) {
                if (file.getPath().endsWith(SaveSystem.PAPER_PLUGIN_MAIN_FILE_EXTENSION)
                        || file.getPath().endsWith(SaveSystem.PAPER_PLUGIN_ADDON_FILE_EXTENSION)) {

                    // These files are going to be plugins and what nots.
                    try {
                        Logger.debug("Found Plugin reading metadata");
                        PaperPluginMetadata metadata = SaveSystem.readPluginMetadata(file.getPath());
                        loadablePluginList.getItems().add(metadata);

                        Logger.debug(metadata.getPluginId());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        setResultConverter(btnType -> {

            if (btnType == ButtonType.OK) {
                return new PluginSelectionResult(selectedPlugins, selectedPluginPaths, null);
            }

            return null;
        });

        GridPane gp = new GridPane();
        GridPane.setColumnSpan(pluginDescriptionArea, 2);
        gp.setPadding(new Insets(15));
        gp.setHgap(10);
        gp.setVgap(10);
        gp.add(pluginIdFld, 0, 0);
        gp.add(pluginNameFld, 1, 0);
        gp.add(pluginAuthorFld, 0, 1);
        gp.add(pluginVersionFld, 1, 1);
        gp.add(pluginDescriptionArea, 0, 2);

        HBox rootContainer = new HBox();
        rootContainer.setPadding(new Insets(5));
        rootContainer.getChildren().addAll(loadablePluginList, new Separator(Orientation.VERTICAL), gp);

        setTitle("Paper - Plugin Selection");
        setHeaderText("We couldn't find any default plugins to load, select them with the check box and the click okay.");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        getDialogPane().setContent(rootContainer);

    }

    public ListView<PaperPluginMetadata> getLoadablePluginList() {
        return loadablePluginList;
    }

    public TextArea getPluginDescriptionArea() {
        return pluginDescriptionArea;
    }

    public TextField getPluginIdFld() {
        return pluginIdFld;
    }

    public TextField getPluginNameFld() {
        return pluginNameFld;
    }

    public TextField getPluginAuthorFld() {
        return pluginAuthorFld;
    }

    public TextField getPluginVersionFld() {
        return pluginVersionFld;
    }

    public ObservableList<PaperPluginMetadata> getSelectedPlugins() {
        return selectedPlugins;
    }

    public List<String> getSelectedPluginPaths() {
        return selectedPluginPaths;
    }

}
