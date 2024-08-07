package io.azraein.paperfx.screens;

import io.azraein.paperfx.PaperFX;
import javafx.animation.AnimationTimer;

public class GameScreen extends PaperScreen {

    private final AnimationTimer gameLoop;

    public GameScreen(PaperFX paperFX) {
        super(paperFX);
        gameLoop = createGameLoop();

    }

    private AnimationTimer createGameLoop() {
        return new AnimationTimer() {

            long previousTime = System.nanoTime();

            @Override
            public void handle(long now) {
                long currentTime = now;
                long elapsedNanos = currentTime - previousTime;
                float deltaTime = (float) (elapsedNanos / 1e9);
                u(deltaTime);
                // Update Game World

                previousTime = currentTime;
            }
        };
    }

    private void u(float d) {}

    public void startGameLoop() {
        gameLoop.start();
    }

    public void stopGameLoop() {
        gameLoop.stop();
    }

}
