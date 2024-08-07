package io.azraein.paperfx.screens;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.system.Paper;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MainMenuScreen extends PaperScreen {

    public MainMenuScreen(PaperFX paperFX) {
        super(paperFX);

        GridPane rootContainer = new GridPane(10, 10);

        Button startNewGameBtn = new Button("Start New Game");
        Button loadGameBtn = new Button("Load Game");
        Button optionsBtn = new Button("Options");
        Button exitBtn = new Button("Exit");

        startNewGameBtn.setOnAction(event -> startNewGame());
        loadGameBtn.setOnAction(event -> loadGame());
        optionsBtn.setOnAction(event -> options());
        exitBtn.setOnAction(event -> System.exit(0));

        rootContainer.add(startNewGameBtn, 0, 0);
        rootContainer.add(loadGameBtn, 0, 1);
        rootContainer.add(optionsBtn, 0, 2);
        rootContainer.add(exitBtn, 0, 3);

        setLeft(rootContainer);
    }

    public void startNewGame() {
        Paper.SE.runFunction(Paper.PPL.getPluginMainScript(), "onNewGame");
        ((GameScreen) paperFX.getScreens().get("game")).startGameLoop();
        paperFX.swapScreens("game");
    }

    public void loadGame() {
        // Load a Save File, set the gamescreens world and start it's loop and swap to it.
        
    }

    public void options() {

    }

}
