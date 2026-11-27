
package com.musicmanager;

import model.User;
import model.Playlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import repositories.models.UserRepository;
import repositories.models.PlaylistRepository;
import repositories.relationships.PlaylistMusicRepository;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

import services.PlaylistServices;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

   private User user;
   private UserRepository userRepository;
   private PlaylistRepository playlistRepository;
   private PlaylistMusicRepository playlistMusicRepository;
   private Connection connection;
   private PlaylistServices playlistServices;

   @BeforeEach
   void setUp() throws SQLException {

      connection = DatabaseConnection.getConnection();

      String email = "teste@teste.com";
      String password = "Testando1";
      String userName = "UsuarioTeste";

      user = new User(email, password, userName);

      userRepository = new UserRepository(connection);
      playlistRepository = new PlaylistRepository(connection);
      playlistMusicRepository = new PlaylistMusicRepository(connection);

      userRepository.save(user);

      playlistServices = new PlaylistServices(
              playlistRepository,
              playlistMusicRepository,
              userRepository
      );
   }

   @AfterEach
   void tearDown() throws SQLException {

      if (connection != null && !connection.isClosed()) {

         if (user != null && user.getId() != 0) {

            Playlist playlist = playlistRepository.findByTitle("test", user);

            if (playlist != null) {
               playlistRepository.delete(playlist.getId(), user);
            }

            userRepository.delete(user.getId());
         }

         connection.close();
      }
   }

   @Test
   void shouldCreatePlaylist() throws SQLException {

      boolean result = playlistServices.createPlaylist(
              "test",
              "test playlist",
              user
      );

      assertTrue(result);
   }
}
