package io.azraein.paperfx.screens;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.controls.LocationView;
import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import javafx.animation.AnimationTimer;

public class GameScreen extends PaperScreen {

    // Paper Nodes
    private LocationView locationView;

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

        locationView = new LocationView(paperFX);

        Paper.PAPER_LOCATION_PROPERTY.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                locationView.setLocation(newValue);
            }
        });


        getChildren().add(locationView);
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

    }

    public void startGameLoop() {
        lastFrameTime = System.nanoTime();
        lastFPSCheck = lastFrameTime;
        gameLoop.start();
    }

    public void stopGameLoop() {
        gameLoop.stop();
    }

}
