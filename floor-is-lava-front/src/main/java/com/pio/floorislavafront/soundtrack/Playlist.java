package com.pio.floorislavafront.soundtrack;

import java.io.File;
import java.util.ArrayList;

public class Playlist {
    private ArrayList<String> songs;
    private int songIndex;

    public Playlist() {
        this.songs = new ArrayList<>();
        this.songIndex = 0;
    };

    public Playlist(ArrayList<String> songs) {
        this.songs = songs;
        this.songIndex = 0;
    }

    public void add(String song) {
        this.songs.add(song);
    }

    public String getNextSong() {
        if (songIndex + 1 < this.songs.size()) {
            this.songIndex = this.songIndex + 1;
        } else {
            this.songIndex = 0;
        }

        return this.songs.get(songIndex);
    }

    public Playlist fillWithSongsFromDirectory(String directory) {
        File[] files = new File(directory).listFiles();

        if (files == null) {
            return this;
        }

        for (File file : files) {
            if (file.isFile()) {
                this.add(file.getParent().concat("\\").concat(file.getName()));
            }
        }

        return this;
    }
}
