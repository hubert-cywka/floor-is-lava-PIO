package com.pio.floorislavafront.soundtrack;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class SoundtrackManager {
    private final String BASE_SOUNDTRACK_PATH = "src/main/resources/com/pio/floorislavafront/music/";
    public final String SOUNDTRACK_AMBIENT = BASE_SOUNDTRACK_PATH.concat("ambient");
    public final String SOUNDTRACK_INSPIRING = BASE_SOUNDTRACK_PATH.concat("inspiring");

    private Playlist ambientPlaylist;
    private Playlist inspiringPlaylist;
    private Playlist currentPlaylist;
    private Media media;
    private MediaPlayer mediaPlayer;

    public SoundtrackManager() {
        this.ambientPlaylist = new Playlist();
        this.ambientPlaylist.fillWithSongsFromDirectory(SOUNDTRACK_AMBIENT);

        this.inspiringPlaylist = new Playlist();
        this.inspiringPlaylist.fillWithSongsFromDirectory(SOUNDTRACK_INSPIRING);

        this.playInspiringPlaylist();
    }

    public void playNextSong() {
        Media nextSong = new Media(new File(this.currentPlaylist.getNextSong()).toURI().toString());
        this.mediaPlayer = new MediaPlayer(nextSong);
        this.mediaPlayer.setOnEndOfMedia(this::playNextSong);
        this.mediaPlayer.setVolume(1);
        this.mediaPlayer.play();
    }

    public void playAmbientPlaylist() {
        this.currentPlaylist = ambientPlaylist;
        this.playNextSong();
    }

    public void playInspiringPlaylist() {
        this.currentPlaylist = inspiringPlaylist;
        this.playNextSong();
    }

    public void pause() {
        this.mediaPlayer.pause();
    }

    public void play() {
        this.mediaPlayer.play();
    }

    public void setVolume(double volume) {
        this.mediaPlayer.setVolume(volume);
    }
}