package importer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MusicJson {

    private int id;
    private String title;
    private List<String> artist;
    private int duration;
    private String album;
    private String lyrics;

    public MusicJson(String title, List<String> artist, int duration) {
        this.title = title;
        this.artist = new ArrayList<>(artist);
        this.duration = duration;
        this.album = "";
        this.lyrics = "";
    }

    public MusicJson() {
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
        if (!(obj instanceof MusicJson)) {
            return false;
        }

        MusicJson other = (MusicJson) obj;

        return title.equals(other.title)
                && artist.equals(other.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }

}
