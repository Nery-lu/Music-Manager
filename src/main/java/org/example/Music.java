package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Music {

    private static int nextId = 1;

    private int id;
    private String title;
    private List<String> artist;
    private int duration;
    private String album;
    private String lyrics;

    public Music(String title, List<String> artist, int duration) {
        this.id = nextId++;
        this.title = title;
        this.artist = new ArrayList<>(artist);
        this.duration = duration;
        this.album = "";
        this.lyrics = "";
    }

    public Music() {
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getArtist() {
        return this.artist;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getAlbum() {
        return this.album;
    }

    public String getLyrics() {
        return this.lyrics;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setLyrics (String lyrics) {
        this.lyrics = lyrics;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Music)) {
            return false;
        }

        Music other = (Music) obj;

        return title.equals(other.title)
                && artist.equals(other.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }

}