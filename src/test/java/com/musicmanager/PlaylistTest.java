
package com.musicmanager;

import database.DatabaseConnection;
import model.Music;
import model.Playlist;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import repositories.models.PlaylistRepository;
import repositories.relationships.PlaylistMusicRepository;
import repositories.models.UserRepository;

import services.PlaylistServices;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    private Connection connection;

    private UserRepository userRepository;
    private PlaylistRepository playlistRepository;
    private PlaylistMusicRepository playlistMusicRepository;

    private PlaylistServices playlistServices;

    private User user;

    @BeforeEach
    void setUp() throws SQLException {

        connection = DatabaseConnection.getConnection();

        userRepository = new UserRepository(connection);
        playlistRepository = new PlaylistRepository(connection);
        playlistMusicRepository = new PlaylistMusicRepository(connection);

        playlistServices = new PlaylistServices(
                playlistRepository,
                playlistMusicRepository,
                userRepository
        );

        String email = "playlist_test_" + UUID.randomUUID() + "@teste.com";
        String password = "Testando1";
        String username = "PlaylistTest";

        user = new User(email, password, username);

        int userId = userRepository.save(user);

        assertTrue(userId > 0, "The test user must be saved successfully.");
        assertEquals(userId, user.getId());
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection == null || connection.isClosed()) {
            return;
        }

        try {
            if (user != null && user.getId() > 0) {

                List<Playlist> playlists = playlistRepository.findAll(user);

                for (Playlist playlist : playlists) {

                    List<Music> musics =
                            playlistMusicRepository.findAllMusicsFromPlaylist(playlist);

                    for (Music music : musics) {
                        playlistMusicRepository.removeMusic(playlist, music);
                    }

                    playlistRepository.delete(playlist.getId(), user);
                }

                userRepository.delete(user.getId());
            }

        } finally {
            connection.close();
        }
    }

    @Test
    void shouldCreatePlaylist() throws SQLException {

        boolean result = playlistServices.createPlaylist(
                "testPlaylist",
                "That's a test playlist",
                user
        );

        assertTrue(result);
    }

    @Test
    void shouldNotCreateDuplicatePlaylist() throws SQLException {

        playlistServices.createPlaylist(
                "testPlaylist",
                "That's a test playlist",
                user
        );

        boolean result = playlistServices.createPlaylist(
                "testPlaylist",
                "Another description",
                user
        );

        assertFalse(result);
    }

    @Test
    void shouldDeletePlaylist() throws SQLException {

        playlistServices.createPlaylist(
                "testPlaylist",
                "That's a test playlist",
                user
        );

        boolean result = playlistServices.deletePlaylist(
                "testPlaylist",
                user
        );

        assertTrue(result);
    }
}
