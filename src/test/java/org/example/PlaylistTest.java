package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    private Playlist playlist;
    private Music music;
    private List<String> artists;

    @BeforeEach
    void setUp() {

        playlist = new Playlist("testPlaylist", "That's a test playlist");
        music = new Music("testMusic", artists, 100 );

    }

    @Test
    void shouldAddMusic() {
       boolean result = playlist.addMusic(music);

       assertTrue(result);
       assertTrue(playlist.getMusics().containsKey(music.getTitle()));


    }

    @Test
    void shouldRemoveMusic() {

        boolean result = playlist.removeMusic(music.getTitle());

        assertFalse(result);
        assertFalse(playlist.getMusics().containsKey(music.getTitle()));

        playlist.addMusic(music);

        boolean result2 = playlist.removeMusic(music.getTitle());

        assertTrue(result2);
        assertFalse(playlist.getMusics().containsKey(music.getTitle()));

    }

    @Test
    void shouldSetAsFavorite() {

        User user = new User();

        boolean result1 = user.getFavorites().removeMusic(music.getTitle());

        assertFalse(result1);
        assertFalse(user.getFavorites().getMusics().containsKey(music.getTitle()));

        boolean settingAsFavorite = user.getFavorites().addMusic(music);

        assertTrue(settingAsFavorite);
        assertTrue(user.getFavorites().getMusics().containsKey(music.getTitle()));

        user.getFavorites().removeMusic(music.getTitle());

        assertFalse(user.getFavorites().getMusics().containsKey(music.getTitle()));

    }

    Music music2 = new Music("testMusic", artists, 100);

    @Test
    void shouldNotAddDuplicate() {
        playlist.addMusic(music);

        boolean result = playlist.addMusic(music2);

        assertFalse(result);
        assertTrue(playlist.getMusics().containsKey(music.getTitle()));
    }

}
