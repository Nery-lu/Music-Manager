package org.example;
import java.util.*;

public class User {
    private static int nextId = 1;

    private int id;
    private String name;
    private Map<String, Playlist> playlists;
    private String email;
    private String passwordHash;
    private Playlist favoriteMusics;
    private transient Queue<Music> queue;

    public User() {
        this.id = nextId++;
        this.playlists = new HashMap<>();
        this.favoriteMusics = new Playlist("Favorites", "Your favorite musics!");
        this.name = "";
        this.email = "";
        this.passwordHash = "";
        this.queue = new ArrayDeque<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean createPlaylist(String title, String description) {
        Playlist playlist = new Playlist(title, description);

        if (!(playlists.containsKey(title))) {
            playlists.put(title, playlist);
            return true;
        }

        return false;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Playlist> getPlaylists() {
        return this.playlists;
    }

    public Collection<Playlist> listPlaylists() {
        return this.playlists.values();
    }

    public String getEmail() {
        return this.email;
    }

    public Playlist getFavoriteMusics() {
        return this.favoriteMusics;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public Playlist getFavorites() {
        return this.favoriteMusics;
    }

    public Queue<Music> getQueue() {
        return this.queue;
    }


}
