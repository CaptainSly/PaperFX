package io.azraein.paperfx.controls.dialog.player;

import io.azraein.paperfx.controls.tabs.InventoryTab;
import io.azraein.paperfx.controls.tabs.PlayerStatsTab;
import io.azraein.paperfx.controls.tabs.QuestLogTab;
import io.azraein.paperfx.screens.GameScreen;
import io.azraein.paperfx.system.GameState;
import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.Player;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PlayerJournalDialog extends Dialog<String> {

    private PlayerStatsTab playerStatsTab = new PlayerStatsTab();
    private QuestLogTab playerQuestLog = new QuestLogTab();
    private InventoryTab playerInventoryTab = new InventoryTab();

    private GameScreen gameScreen;

    public PlayerJournalDialog(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        Player player = Paper.PAPER_WORLD_PROPERTY.get().getPlayer();

        String title = player.getActorState().getActorName() + "'s Journal";
        setTitle(title);
        setResizable(true);

        TabPane journalTabs = new TabPane();
        journalTabs.getTabs().add(playerStatsTab);
        journalTabs.getTabs().add(playerQuestLog);
        journalTabs.getTabs().add(playerInventoryTab);

        getDialogPane().setContent(journalTabs);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        setOnCloseRequest(event -> {
            gameScreen.currentGameStateProperty().set(GameState.RUNNING);
        });

        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(new Image(Utils.getFileFromResources("journal.png")));
    }

    public void showJournal() {
        playerStatsTab.updateTab();
        playerQuestLog.updateTab();
        playerInventoryTab.updateTab();

        gameScreen.currentGameStateProperty().set(GameState.PAUSED);
        show();
    }

}
