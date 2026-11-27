package com.musicmanager;

import database.DatabaseConnection;
import menus.*;
import model.Music;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repositories.models.ArtistRepository;
import repositories.models.MusicRepository;
import repositories.models.PlaylistRepository;
import repositories.models.UserRepository;
import repositories.relationships.FavoriteMusicRepository;
import repositories.relationships.MusicArtistRepository;
import repositories.relationships.PlaylistMusicRepository;
import services.MusicServices;
import services.PlaylistServices;
import services.UserServices;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/*    TODO: Play music     */

/*    Where I'm at now: Just finished integrating the database, maybe will do the frontend  */

public class Main {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            UserRepository userRepository = new UserRepository(connection);
            MusicRepository musicRepository = new MusicRepository(connection);
            ArtistRepository artistRepository = new ArtistRepository(connection);
            PlaylistRepository playlistRepository = new PlaylistRepository(connection);

            PlaylistMusicRepository playlistMusicRepository = new PlaylistMusicRepository(connection);
            FavoriteMusicRepository favoriteMusicRepository = new FavoriteMusicRepository(connection);
            MusicArtistRepository musicArtistRepository = new MusicArtistRepository(connection);

            UserServices userServices = new UserServices(userRepository, encoder);
            PlaylistServices playlistServices = new PlaylistServices(playlistRepository,
                    playlistMusicRepository, userRepository);

            MusicServices musicServices = new MusicServices(
                    connection,
                    musicRepository,
                    artistRepository,
                    musicArtistRepository
            );

            Map<Integer, Music> allMusics = musicRepository.findAll();

            Scanner scanner = new Scanner(System.in);


            PlaylistMenu playlistMenu = new PlaylistMenu(userServices, playlistServices);
            FavoriteMenu favoriteMenu = new FavoriteMenu(userServices, playlistServices);
            SearchMenu searchMenu = new SearchMenu(playlistServices);

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
                    playlistServices );

        } catch (SQLException e) {
            e.printStackTrace();
        }
      }
    }








