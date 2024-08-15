package io.azraein.paperfx.screens;

import java.io.File;
import java.util.Optional;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.controls.LocationMover;
import io.azraein.paperfx.controls.LocationView;
import io.azraein.paperfx.controls.PaperClock;
import io.azraein.paperfx.controls.PlayerControls;
import io.azraein.paperfx.controls.dialog.SavePlayerFileDialog;
import io.azraein.paperfx.controls.dialog.player.PlayerJournalDialog;
import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.Npc;
import io.azraein.paperfx.system.exceptions.IncompatibleSaveVersionException;
import io.azraein.paperfx.system.exceptions.SaveCorruptionException;
import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import io.azraein.paperfx.system.locations.buildings.Building;
import io.azraein.paperfx.system.world.World;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

// TODO: Implement Game Screen State Machine

public class GameScreen extends PaperScreen {

    // Paper Nodes
    private LocationView locationView;
    private LocationMover locationMover;
    private PaperClock paperClock;
    private PlayerControls playerControls;

    // Paper Dialogs
    private PlayerJournalDialog playerJournalDialog;
    private SavePlayerFileDialog spfd;

    // Paper System Stuff

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
        locationMover = new LocationMover();
        paperClock = new PaperClock();
        playerControls = new PlayerControls(this);

        // #region Listeners
        Paper.PAPER_LOCATION_PROPERTY.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                World world = Paper.PAPER_WORLD_PROPERTY.get();
                locationView.setLocation(newValue);
                world.setCurrentLocationId(newValue.getLocationId());

                if (oldValue != null) {
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
                playerJournalDialog = new PlayerJournalDialog();
                spfd = new SavePlayerFileDialog();
            }

        });
        // #endregion

        HBox playerControlsContainer = new HBox();
        playerControlsContainer.setPadding(new Insets(15));
        playerControlsContainer.getChildren().addAll(locationMover, playerControls);

        VBox mainContainer = new VBox();
        mainContainer.setPadding(new Insets(15));
        mainContainer.getChildren().addAll(locationView, playerControlsContainer);

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
        fileMenu.getItems().addAll(saveGame, loadGame);
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
                    fps = frameCount;
                    frameCount = 0;
                    lastFPSCheck = currentTime;
                    paperFX.setTitle("PaperFX - " + ((PaperPlugin) Paper.PPL.getLoadedPlugins().values().toArray()[0])
                            .getMetadata().getPluginId() + " FPS: " + fps);
                }

                lastFrameTime = currentTime;
            }
        };
    }

    private void update(float delta) {
        // Update the PaperClock MenuItem to keep it up to date.
        paperClock.update(Paper.PAPER_WORLD_PROPERTY.get().getCalendar());

        Paper.PAPER_WORLD_PROPERTY.get().update(delta);
        Paper.PAPER_LOCATION_PROPERTY.get().update();

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

}
