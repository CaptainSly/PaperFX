package io.azraein.paperfx.ui.controls.dialog;

import java.io.IOException;

import io.azraein.inkfx.system.GameState;
import io.azraein.inkfx.system.Options;
import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.io.PaperIni;
import io.azraein.paperfx.ui.screens.GameScreen;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class OptionsDialog extends Dialog<String> {

    public OptionsDialog(GameScreen gameScreen) {

        Label musicAudioLbl = new Label("Music Audio");
        Label musicAudioVolumeLbl = new Label(
                "Music Volume: " + (int) Utils.formatToDecimalPlace(0, Options.defaultMusicVolume * 100) + "%");
        Slider musicAudioSlider = new Slider(0.0, 1.0, Options.defaultMusicVolume);
        musicAudioSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Options.defaultMusicVolume = newValue.doubleValue();
                musicAudioVolumeLbl.setText(
                        "Music Volume: " + (int) Utils.formatToDecimalPlace(0, Options.defaultMusicVolume * 100) + "%");

                Paper.AUDIO.setMusicVolume(newValue.doubleValue());
            }
        });

        Label ambienceAudioLbl = new Label("Ambience Audio");
        Label ambienceAudioVolumeLbl = new Label(
                "Ambience Volume: " + (int) Utils.formatToDecimalPlace(0, Options.defaultAmbienceVolume * 100) + "%");
        Slider ambienceAudioSlider = new Slider(0.0, 1.0, Options.defaultAmbienceVolume);
        ambienceAudioSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Options.defaultAmbienceVolume = newValue.doubleValue();

                ambienceAudioVolumeLbl.setText("Music Volume: "
                        + (int) Utils.formatToDecimalPlace(0, Options.defaultAmbienceVolume * 100) + "%");

                Paper.AUDIO.setAmbienceVolume(newValue.doubleValue());
            }
        });

        Label sfxAudioLbl = new Label("Sound Effect Audio");
        Label sfxAudioVolumeLbl = new Label(
                "Sound Effect Volume: " + (int) Utils.formatToDecimalPlace(0, Options.defaultSFXVolume * 100) + "%");
        Slider sfxAudioSlider = new Slider(0.0, 1.0, Options.defaultSFXVolume);
        sfxAudioSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Options.defaultSFXVolume = newValue.doubleValue();
                sfxAudioVolumeLbl.setText(
                        "Music Volume: " + (int) Utils.formatToDecimalPlace(0, Options.defaultSFXVolume * 100) + "%");
            }
        });

        GridPane gp = new GridPane(10, 10);
        gp.setPadding(new Insets(15));
        gp.add(musicAudioLbl, 0, 0);
        gp.add(musicAudioSlider, 1, 0);
        gp.add(musicAudioVolumeLbl, 2, 0);
        gp.add(ambienceAudioLbl, 0, 1);
        gp.add(ambienceAudioSlider, 1, 1);
        gp.add(ambienceAudioVolumeLbl, 2, 1);
        gp.add(sfxAudioLbl, 0, 2);
        gp.add(sfxAudioSlider, 1, 2);
        gp.add(sfxAudioVolumeLbl, 2, 2);

        setResultConverter(new Callback<ButtonType, String>() {

            @Override
            public String call(ButtonType param) {

                if (param.equals(ButtonType.APPLY)) {
                    try {
                        Paper.INI.put(PaperIni.SYSTEM_SECTION, PaperIni.SYSTEM_DEFAULT_MUSIC_VOLUME,
                                Utils.formatToDecimalPlace(2, musicAudioSlider.getValue()));
                        Paper.INI.put(PaperIni.SYSTEM_SECTION, PaperIni.SYSTEM_DEFAULT_AMBIENCE_VOLUME,
                                Utils.formatToDecimalPlace(2, ambienceAudioSlider.getValue()));
                        Paper.INI.put(PaperIni.SYSTEM_SECTION, PaperIni.SYSTEM_DEFAULT_SFX_VOLUME,
                                Utils.formatToDecimalPlace(2, sfxAudioSlider.getValue()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    gameScreen.currentGameStateProperty().set(GameState.RUNNING);
                }

                return "";

            }
        });

        getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
        getDialogPane().setContent(gp);
    }

}
