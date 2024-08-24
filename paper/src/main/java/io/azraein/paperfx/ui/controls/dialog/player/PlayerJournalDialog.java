package io.azraein.paperfx.ui.controls.dialog.player;

import org.tinylog.Logger;

import io.azraein.inkfx.system.GameState;
import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.actors.Player;
import io.azraein.paperfx.ui.controls.tabs.InventoryTab;
import io.azraein.paperfx.ui.controls.tabs.PlayerStatsTab;
import io.azraein.paperfx.ui.controls.tabs.QuestLogTab;
import io.azraein.paperfx.ui.screens.GameScreen;
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

        String dialogueTest = """
                NPC -> "Welcome, $playerName! Are you ready to begin your adventure?"

                IF $hasAmulet THEN
                    NPC -> "I see you have the amulet. You're prepared!"
                ELSEIF ($playerLevel > 5) THEN
                    NPC -> "You're a seasoned adventurer, but you should find the amulet."
                ELSE
                    NPC -> "You need to be better prepared. Find the amulet first."
                END
                                        """;

        Logger.debug(Paper.DP.parse(dialogueTest));

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
