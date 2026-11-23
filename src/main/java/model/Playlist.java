package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Playlist {

    private int id;
    private int userId;
    private String title;
    private Map<String, Music> musics;
    private String description;
    private int duration;

    public Playlist (String title, String description) {
        this.title = title;
        this.musics = new HashMap<>();
        this.description = description;
        this.duration = 0;
    }

    public Playlist (String title, String description, int id, int userId) {
        this.userId = userId;
        this.id = id;
        this.musics = new HashMap<>();
        this.description = description;
        this.duration = 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public Map<String, Music> getMusics() {
        return this.musics;
    }

    public String getDescription() {
        return this.description;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean addMusic(Music music) {
        if (!(musics.containsKey(music.getTitle()))) {
            musics.put(music.getTitle(), music);
            duration += music.getDuration();
            return true;
        }

        return false;
    }

    public boolean removeMusic(String nameMusic) {
        if (!musics.containsKey(nameMusic)) {
            return false;
        }

        Music removedMusic = musics.get(nameMusic);
        musics.remove(nameMusic);
        duration -= removedMusic.getDuration();
            return true;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Playlist)) {
            return false;
        }

        Playlist other = (Playlist) obj;

        return title.equals(other.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}