package io.azraein.paperfx.controls.dialog.player;

import io.azraein.paperfx.controls.tabs.PlayerStatsTab;
import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.Player;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TabPane;

public class PlayerJournalDialog extends Dialog<String> {

    private PlayerStatsTab playerStatsTab = new PlayerStatsTab();

    public PlayerJournalDialog() {
        Player player = Paper.PAPER_WORLD_PROPERTY.get().getPlayer();

        String title = player.getActorState().getActorName() + "'s Journal";
        setTitle(title);
        setResizable(true);

        TabPane journalTabs = new TabPane();
        journalTabs.getTabs().add(playerStatsTab);

        getDialogPane().setContent(journalTabs);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        getDialogPane().setMaxHeight(800);
    }

    public void showJournal() {
        playerStatsTab.updateTab();

        show();
    }

}
