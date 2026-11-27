package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Playlist {

    private int id;
    private int userId;
    private String title;
    private String description;
    private int duration;

    public Playlist (String title, String description, int userId) {
        this.title = title;
        this.userId = userId;
        this.description = description;
        this.duration = 0;
    }

    public Playlist (String title, String description, int id, int userId) {
        this.title = title;
        this.userId = userId;
        this.id = id;
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

    public String getDescription() {
        return this.description;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getUserId() {
        return this.userId;
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