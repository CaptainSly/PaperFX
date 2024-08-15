package io.azraein.paperfx.controls.tabs;

import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.Player;
import io.azraein.paperfx.system.quest.Quest;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class QuestLogTab extends JournalTab {

    private ListView<Quest> questLogLV;

    public QuestLogTab() {
        super("Quest Log");

        Label questNameLbl = new Label("Quest: ");
        Label questDescriptionLbl = new Label("Description:");

        TextArea questDescription = new TextArea();
        questDescription.setEditable(false);

        Button questSetAsActive = new Button("Set as Active Quest");
        questSetAsActive.setDisable(true);
        questSetAsActive.setOnAction(event -> {
            Paper.DATABASE.addGlobal("currentQuestId", questLogLV.getSelectionModel().getSelectedItem().getQuestId());
        });

        questLogLV = new ListView<>();
        questLogLV.setCellFactory(listView -> new ListCell<Quest>() {

            @Override
            protected void updateItem(Quest item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {

                    if (Paper.DATABASE.getGlobal("currentQuestId").equals(item.getQuestId()))
                        setText(item.getQuestName() + " - Active");
                    else
                        setText(item.getQuestName());
                } else
                    setText("");

            }

        });
        questLogLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                questNameLbl.setText("Quest: " + newValue.getQuestName());
                questDescription.setText(newValue.getQuestDescription());
                questSetAsActive.setDisable(false);
            } else {
                questSetAsActive.setDisable(true);
            }

        });

        GridPane gp = new GridPane(10, 10);
        gp.setPadding(new Insets(15));
        gp.add(questNameLbl, 0, 0);
        gp.add(questDescriptionLbl, 0, 1);
        gp.add(questDescription, 0, 2);
        gp.add(questSetAsActive, 0, 3);

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(15));
        bp.setLeft(questLogLV);
        bp.setCenter(gp);

        setContent(bp);
    }

    @Override
    public void updateTab() {
        Player player = Paper.PAPER_PLAYER_PROPERTY.get();
        questLogLV.getItems().clear();

        for (String questId : player.getPlayerQuestLog()) {
            Quest quest = Paper.DATABASE.getQuest(questId);
            questLogLV.getItems().add(quest);
        }

    }

}
