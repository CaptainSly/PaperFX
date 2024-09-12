package io.azraein.paperfx.ui.screens;

import java.io.File;
import java.util.Optional;

import io.azraein.inkfx.system.GameState;
import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.actors.Npc;
import io.azraein.inkfx.system.exceptions.system.IncompatibleSaveVersionException;
import io.azraein.inkfx.system.exceptions.system.SaveCorruptionException;
import io.azraein.inkfx.system.io.SaveSystem;
import io.azraein.inkfx.system.io.plugins.PaperPlugin;
import io.azraein.inkfx.system.locations.buildings.Building;
import io.azraein.inkfx.system.world.World;
import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.ui.controls.LocationView;
import io.azraein.paperfx.ui.controls.PaperClock;
import io.azraein.paperfx.ui.controls.PlayerControls;
import io.azraein.paperfx.ui.controls.dialog.OptionsDialog;
import io.azraein.paperfx.ui.controls.dialog.player.PlayerJournalDialog;
import io.azraein.paperfx.ui.controls.dialog.player.SavePlayerFileDialog;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class GameScreen extends PaperScreen {

    // Paper Nodes
    private LocationView locationView;
    private PaperClock paperClock;
    private PlayerControls playerControls;

    // Paper Dialogs
    private PlayerJournalDialog playerJournalDialog;
    private SavePlayerFileDialog spfd;

    // Paper System Stuff
    private final ObjectProperty<GameState> currentGameStateProperty = new SimpleObjectProperty<>(GameState.RUNNING);

    // Game Loop
    private final AnimationTimer gameLoop;

    // Game Loop Variables
    private long lastFrameTime;
    private float deltaTime;
    private int frameCount;
    private long lastFPSCheck;
    private int fps;

    public GameScreen(PaperFX paperFX) {
        super(paperFX);
        gameLoop = createGameLoop();
    }

    public void init() {
        locationView = new LocationView(paperFX);
        paperClock = new PaperClock();
        playerControls = new PlayerControls(this);

        // #region Listeners
        Paper.PAPER_LOCATION_PROPERTY.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                locationView.setLocation(newValue);
                World world = Paper.PAPER_WORLD_PROPERTY.get();
                world.setCurrentLocationId(newValue.getLocationId());

                newValue.onVisit();

                if (oldValue != null) {
                    oldValue.onLeave();
                    // Throw the Old Location's State into the Worlds
                    world.addLocationState(oldValue);

                    // Get the Buildings from the old location and add their states as well.
                    for (String buildingId : oldValue.getLocationBuildingIds()) {
                        Building building = Paper.DATABASE.getBuilding(buildingId);
                        world.addBuildingState(building);
                    }

                    // Finally add all the npc states that were there.
                    for (String npcId : oldValue.getLocationState().getLocationNpcIds()) {
                        Npc npc = Paper.DATABASE.getNpc(npcId);
                        world.addActorState(npc);
                    }
                }

            }
        });

        Paper.PAPER_PLAYER_PROPERTY.addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                Paper.PAPER_WORLD_PROPERTY.get().setPlayer(newValue);
                playerJournalDialog = new PlayerJournalDialog(this);
                spfd = new SavePlayerFileDialog();
            }

        });

        // #endregion

        VBox mainContainer = new VBox();
        mainContainer.setPadding(new Insets(15));
        mainContainer.getChildren().addAll(locationView, playerControls);

        // #region - Menu Bar
        MenuBar gameScreenMenuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem saveGame = new MenuItem("Save Game");
        saveGame.setOnAction(event -> {
            Optional<String> saveName = spfd.showDialog();

            if (saveName.isPresent()) {
                String filePath = SaveSystem.PAPER_SAVE_FOLDER + saveName.get() + SaveSystem.PAPER_SAVE_FILE_EXTENSION;
                SaveSystem.savePlayerFile(Paper.PAPER_WORLD_PROPERTY.get(), filePath);
            }

        });

        MenuItem loadGame = new MenuItem("Load Game");
        loadGame.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(SaveSystem.PAPER_SAVE_FOLDER));
            fc.getExtensionFilters()
                    .add(new ExtensionFilter("Paper Engine Save File", "*" + SaveSystem.PAPER_SAVE_FILE_EXTENSION));

            File saveFile = fc.showOpenDialog(paperFX.getPrimaryStage().getOwner());
            if (saveFile.exists()) {
                try {
                    World world = SaveSystem.loadPlayerFile(saveFile.getAbsolutePath());
                    Paper.CALENDAR = world.getCalendar();
                    Paper.PAPER_WORLD_PROPERTY.set(world);
                } catch (SaveCorruptionException | IncompatibleSaveVersionException e) {
                    e.printStackTrace();
                }
            }

        });

        MenuItem options = new MenuItem("Options");
        options.setOnAction(event -> {
            currentGameStateProperty.set(GameState.PAUSED);
            OptionsDialog optionDialog = new OptionsDialog(this);
            optionDialog.showAndWait();

        });

        fileMenu.getItems().addAll(saveGame, loadGame, options);
        gameScreenMenuBar.getMenus().addAll(fileMenu, paperClock);
        // #endregion

        setTop(gameScreenMenuBar);
        setCenter(mainContainer);
    }

    private AnimationTimer createGameLoop() {

        return new AnimationTimer() {

            @Override
            public void handle(long now) {
                long currentTime = now;
                deltaTime = (currentTime - lastFrameTime) / 1e9f;

                update(deltaTime);

                frameCount++;
                if (currentTime - lastFPSCheck >= 1e9) {
                    String title = "PaperFX - "
                            + ((PaperPlugin) Paper.PPL.getLoadedPlugins().values().toArray()[0]).getMetadata()
                                    .getPluginId()
                            + " FPS: " + fps + " STATE: " + currentGameStateProperty.get().name();

                    fps = frameCount;
                    frameCount = 0;
                    lastFPSCheck = currentTime;
                    paperFX.setTitle(title);
                }

                lastFrameTime = currentTime;
            }
        };
    }

    private void update(float delta) {

        switch (currentGameStateProperty.get()) {
        case RUNNING -> {
            // Update the PaperClock MenuItem to keep it up to date.
            paperClock.update(Paper.PAPER_WORLD_PROPERTY.get().getCalendar());

            Paper.PAPER_WORLD_PROPERTY.get().update(delta);
            Paper.PAPER_LOCATION_PROPERTY.get().update();
        }
        case PAUSED -> {

        }
        case COMBAT -> {
            // Update the PaperClock MenuItem to keep it up to date.
            paperClock.update(Paper.PAPER_WORLD_PROPERTY.get().getCalendar());

        }
        }
    }

    public void startGameLoop() {
        lastFrameTime = System.nanoTime();
        lastFPSCheck = lastFrameTime;
        gameLoop.start();
    }

    public void stopGameLoop() {
        gameLoop.stop();
    }

    public PlayerJournalDialog getPlayerJournalDialog() {
        return playerJournalDialog;
    }

    public ObjectProperty<GameState> currentGameStateProperty() {
        return currentGameStateProperty;
    }

}
