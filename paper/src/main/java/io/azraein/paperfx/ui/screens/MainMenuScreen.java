
package io.azraein.paperfx.ui.screens;

import java.io.File;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.exceptions.system.IncompatibleSaveVersionException;
import io.azraein.inkfx.system.exceptions.system.SaveCorruptionException;
import io.azraein.inkfx.system.io.SaveSystem;
import io.azraein.inkfx.system.world.World;
import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.ui.controls.dialog.EasterEggDialog;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainMenuScreen extends PaperScreen {

    public MainMenuScreen(PaperFX paperFX) {
        super(paperFX);
    }

    @Override
    public void init() {
        GridPane rootContainer = new GridPane(10, 10);

        Button startNewGameBtn = new Button("Start New Game");
        Button loadGameBtn = new Button("Load Game");
        Button exitBtn = new Button("Exit");

        startNewGameBtn.setOnAction(event -> startNewGame());
        loadGameBtn.setOnAction(event -> loadGame());
        exitBtn.setOnAction(event -> System.exit(0));

        rootContainer.add(startNewGameBtn, 0, 0);
        rootContainer.add(loadGameBtn, 0, 1);
        rootContainer.add(exitBtn, 0, 2);

        container.setPadding(new Insets(15));
        setLeft(rootContainer);
    }

    public void startNewGame() {
        // Setup the World
        World world = new World();
        Paper.PAPER_WORLD_PROPERTY.set(world);
        Paper.CALENDAR = world.getCalendar();
        Paper.SE.setPaperGlobal("calendar", CoerceJavaToLua.coerce(Paper.CALENDAR));
        Paper.SE.runFunction(Paper.PPL.getPluginMainScript(), "onNewGame");

        if (Paper.PAPER_PLAYER_PROPERTY.get().getActorState().getActorName().contains("Cage")
                || Paper.PAPER_PLAYER_PROPERTY.get().getActorState().getActorName().contains("cage")) {
            new EasterEggDialog().show();
        }

        // Set any default system globals
        Paper.DATABASE.addGlobal("playerName", world.getPlayer().getActorState().getActorName());

        ((GameScreen) paperFX.getScreens().get("game")).startGameLoop();
        paperFX.swapScreens("game");
    }

    public void loadGame() {
        // Load a Save File, set the gamescreens world and start it's loop and swap to
        // it.
        try {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(SaveSystem.PAPER_SAVE_FOLDER));
            fc.getExtensionFilters()
                    .add(new ExtensionFilter("Paper Engine Save File", "*" + SaveSystem.PAPER_SAVE_FILE_EXTENSION));

            File f = fc.showOpenDialog(paperFX.getPrimaryStage().getOwner());
            if (f != null) {
                SaveSystem.loadPlayerFile(f.getPath());
                ((GameScreen) paperFX.getScreens().get("game")).startGameLoop();
                paperFX.swapScreens("game");
            }
        } catch (SaveCorruptionException | IncompatibleSaveVersionException e) {
            // TODO: Figure out what to do if the save game is corrupted or incompatible
            e.printStackTrace();
        }

    }

}
