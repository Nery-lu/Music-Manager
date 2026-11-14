package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {

    private User user;
    private Playlist playlist;
    private Map<String, ArrayList<Music>> allMusics;
    private String search;
    private ArrayList<Music> musics;

    @BeforeEach
    void setUp() {

        user = new User();
        allMusics = new HashMap<>(MusicCatalog.loadMusicCatalog());

    }

    @Test
    void shouldFindMusicsByTitle() {

        search = "Raindance";
        musics = allMusics.get(search);
        Set<Music> expected = new HashSet<>(musics);
        Set<Music> musicsFound = MusicSearch.searchMusic(search, allMusics);

        assertEquals(expected, musicsFound);
    }

    @Test
    void shouldFindMusicsByArtists() {

        search = "Dave";
        //We can do this because we don't have musics with the same name
        Set<Music> expected = Set.of(allMusics.get("Sprinter").get(0), allMusics.get("Raindance").get(0));
        Set<Music> musicsFound = MusicSearch.searchMusic(search, allMusics);

        assertEquals(expected, musicsFound);
    }

    @Test
    void shouldFindMusicByLyrics() {

        search = "rain";
        Set<Music> expected = Set.of(allMusics.get("Raindance").get(0));
        Set<Music> musicsFound = MusicSearch.searchMusic(search, allMusics);

        assertEquals(expected, musicsFound);
    }

    @Test
    void shouldFindNothingWhenSearchIsNull() {
        search = "";
        Set<Music> expected = new HashSet<>();
        Set<Music> musicsFound = MusicSearch.searchMusic(search, allMusics);

        assertEquals(expected, musicsFound);
    }


}
