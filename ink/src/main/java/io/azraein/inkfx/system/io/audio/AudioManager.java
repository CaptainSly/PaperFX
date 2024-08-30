package io.azraein.inkfx.system.io.audio;

import java.io.File;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;

import io.azraein.inkfx.system.Options;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.io.SaveSystem;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioManager {

    private MediaPlayer musicPlayer;
    private MediaPlayer ambiencePlayer;

    private Queue<Media> musicQueue;

    public static final int FADEOUT_TIMER = 10; // Seconds

    public AudioManager() {
        musicQueue = new LinkedList<>();
    }

    public void addMusicTrack(String mediaName) {
        Media media = new Media(new File(SaveSystem.PAPER_AUDIO_FOLDER + "music/" + mediaName).toURI().toString());
        musicQueue.add(media);
    }

    public void addMusicTrackInternal(String mediaName) {
        try {
            Media media = new Media(Utils.getFileURIFromResources(mediaName).toString());
            musicQueue.add(media);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setMusicVolume(double volume) {
        if (musicPlayer != null) {
            musicPlayer.setVolume(volume);
        }
    }

    public void playNextMusicTrack() {
        Media nextTrack = musicQueue.poll();
        if (nextTrack != null) {
            if (musicPlayer != null)
                musicPlayer.dispose();

            musicPlayer = new MediaPlayer(nextTrack);
            musicPlayer.setVolume(Options.defaultMusicVolume);
            musicPlayer.setOnEndOfMedia(this::playNextMusicTrack);
            musicPlayer.play();
        }
    }

    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
        }
    }

    public void fadeOutAudio(MediaPlayer player) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(FADEOUT_TIMER), new KeyValue(player.volumeProperty(), 0)));
        timeline.setOnFinished(event -> {
            player.stop();
            player.dispose();
        });
        timeline.play();
    }

    // Ambience Control
    public void playAmbience(String mediaName) {
        if (ambiencePlayer != null) {
            ambiencePlayer.stop();
            ambiencePlayer.dispose();
        }
        Media media = new Media(new File(SaveSystem.PAPER_AUDIO_FOLDER + "music/" + mediaName).toURI().toString());
        ambiencePlayer = new MediaPlayer(media);
        ambiencePlayer.setVolume(Options.defaultAmbienceVolume);
        ambiencePlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop ambiance
        ambiencePlayer.play();
    }

    public void playAmbienceInternal(String mediaName) {
        if (ambiencePlayer != null) {
            ambiencePlayer.stop();
            ambiencePlayer.dispose();
        }
        try {
            Media media = new Media(Utils.getFileURIFromResources(mediaName).toString());
            ambiencePlayer = new MediaPlayer(media);
            ambiencePlayer.setVolume(Options.defaultAmbienceVolume);
            ambiencePlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop ambiance
            ambiencePlayer.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void setAmbienceVolume(double volume) {
        if (ambiencePlayer != null) {
            ambiencePlayer.setVolume(volume);
        }
    }

    public void stopAmbience() {
        if (ambiencePlayer != null) {
            ambiencePlayer.stop();
            ambiencePlayer.dispose();
        }
    }

    // Sound Effect Stuff
    public void playSoundEffect(String mediaName) {
        Media sfx = new Media(new File(SaveSystem.PAPER_AUDIO_FOLDER + "sfx/" + mediaName).toURI().toString());
        MediaPlayer sfxPlayer = new MediaPlayer(sfx);
        sfxPlayer.setVolume(Options.defaultSFXVolume);
        sfxPlayer.play();

        sfxPlayer.setOnEndOfMedia(() -> {
            sfxPlayer.stop();
            sfxPlayer.dispose();
        });
    }

    public void playSoundEffectInternal(String mediaName) {
        try {
            Media sfx = new Media(Utils.getFileURIFromResources(mediaName).toString());
            MediaPlayer sfxPlayer = new MediaPlayer(sfx);
            sfxPlayer.setVolume(Options.defaultSFXVolume);
            sfxPlayer.play();

            sfxPlayer.setOnEndOfMedia(() -> {
                sfxPlayer.stop();
                sfxPlayer.dispose();
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // General Control
    public void pauseAll() {
        if (musicPlayer != null)
            musicPlayer.pause();

        if (ambiencePlayer != null)
            ambiencePlayer.pause();
    }

    public void resumeAll() {
        if (musicPlayer != null)
            musicPlayer.play();

        if (ambiencePlayer != null)
            ambiencePlayer.play();
    }

    public void stopAll() {
        stopMusic();
        stopAmbience();
    }
}
