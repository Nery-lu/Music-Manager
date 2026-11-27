
package com.musicmanager;

import database.DatabaseConnection;
import menus.*;
import model.Music;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repositories.models.*;
import repositories.relationships.*;
import services.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public static void main(String[] args) {
        run(new Scanner(System.in));
    }

    public static void run(Scanner scanner) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            UserRepository userRepository = new UserRepository(connection);
            MusicRepository musicRepository = new MusicRepository(connection);
            ArtistRepository artistRepository = new ArtistRepository(connection);
            PlaylistRepository playlistRepository = new PlaylistRepository(connection);

            PlaylistMusicRepository playlistMusicRepository =
                    new PlaylistMusicRepository(connection);

            FavoriteMusicRepository favoriteMusicRepository =
                    new FavoriteMusicRepository(connection);

            MusicArtistRepository musicArtistRepository =
                    new MusicArtistRepository(connection);

            UserServices userServices =
                    new UserServices(userRepository, encoder);

            PlaylistServices playlistServices =
                    new PlaylistServices(
                            playlistRepository,
                            playlistMusicRepository,
                            userRepository
                    );

            MusicServices musicServices = new MusicServices(
                    connection,
                    musicRepository,
                    artistRepository,
                    musicArtistRepository
            );

            Map<Integer, Music> allMusics = musicRepository.findAll();

            PlaylistMenu playlistMenu =
                    new PlaylistMenu(userServices, playlistServices);

            FavoriteMenu favoriteMenu =
                    new FavoriteMenu(userServices, playlistServices);

            SearchMenu searchMenu =
                    new SearchMenu(playlistServices);

            UserMenu userMenu = new UserMenu(
                    userServices,
                    playlistMenu,
                    favoriteMenu,
                    searchMenu,
                    scanner
            );

            MainMenu mainMenu = new MainMenu(
                    userServices,
                    userMenu,
                    scanner
            );

            mainMenu.showMenu(
                    scanner,
                    allMusics,
                    userRepository,
                    userServices,
                    playlistRepository,
                    playlistMusicRepository,
                    favoriteMusicRepository,
                    playlistServices
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
