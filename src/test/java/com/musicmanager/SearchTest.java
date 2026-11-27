
package com.musicmanager;

import model.Artist;
import model.Music;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import repositories.models.MusicRepository;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTest {

    private Map<Integer, Music> allMusics;
    private List<Music> expected;

    private MusicRepository musicRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {

        connection = DatabaseConnection.getConnection();

        musicRepository = new MusicRepository(connection);

        expected = new ArrayList<>();

        allMusics = new HashMap<>(musicRepository.findAll());
    }

    @AfterEach
    void tearDown() throws SQLException {

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void shouldFindMusicsByTitle() {

        String search = "Raindance";

        for (Music music : allMusics.values()) {
            if (music.getTitle().equals(search)) {
                expected.add(music);
            }
        }

        List<Music> musicsFound = MusicSearch.searchMusic(search, allMusics);

        assertEquals(expected, musicsFound);
    }

    @Test
    void shouldFindMusicsByArtists() throws SQLException {

        String search = "Dave";

        Set<Integer> expectedIds = new HashSet<>();

        expectedIds.add(musicRepository.findByTitle("Sprinter").getId());
        expectedIds.add(musicRepository.findByTitle("Raindance").getId());

        Set<Integer> foundIds = new HashSet<>();

        List<Music> musicsFound = MusicSearch.searchMusic(search, allMusics);

        for (Music music : musicsFound) {
            foundIds.add(music.getId());
        }

        assertEquals(expectedIds, foundIds);
    }

    @Test
    void shouldFindMusicByLyrics() throws SQLException {
        String search = "storm";

        Music expectedMusic = musicRepository.findByTitle("Raindance");

        List<Music> musicsFound =
                MusicSearch.searchMusic(search, allMusics);

        assertEquals(1, musicsFound.size());
        assertEquals(expectedMusic.getId(), musicsFound.get(0).getId());
    }

    @Test
    void shouldFindNothingWhenSearchIsNull() {

        String search = "";

        List<Music> musicsFound = MusicSearch.searchMusic(search, allMusics);

        assertEquals(expected, musicsFound);
    }
}
