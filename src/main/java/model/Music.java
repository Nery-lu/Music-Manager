package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Music {

    private int id;
    private String title;
    private List<Artist> artist;
    private int duration;
    private String album;
    private String lyrics;

    public Music(String title, List<Artist> artist, String lyrics, int duration) {
        this.title = title;
        this.artist = new ArrayList<>(artist);
        this.duration = duration;
        this.lyrics = lyrics;
        this.album = "";
    }

    public Music(String title, String lyrics, int duration, int id) {
        this.title = title;
        this.artist = new ArrayList<>();
        this.duration = duration;
        this.lyrics = lyrics;
        this.album = "";
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public List<Artist> getArtist() {
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

    public void setId(int id) {
        this.id = id;
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