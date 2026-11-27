package menus;

import model.Music;
import model.Playlist;
import model.User;
import repositories.models.PlaylistRepository;
import repositories.models.UserRepository;
import repositories.relationships.FavoriteMusicRepository;
import repositories.relationships.PlaylistMusicRepository;
import services.PlaylistServices;
import services.UserServices;

import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class UserMenu {

    private final UserServices userServices;
    private final FavoriteMenu favoriteMenu;
    private final PlaylistMenu playlistMenu;
    private final SearchMenu searchMenu;

    public UserMenu (UserServices userServices, PlaylistMenu playlistMenu, FavoriteMenu favoriteMenu, SearchMenu searchMenu, Scanner scanner) {
        this.userServices = userServices;
        this.playlistMenu = playlistMenu;
        this.favoriteMenu = favoriteMenu;
        this.searchMenu = searchMenu;
    }

    public void showMenu (User user,
                          Scanner scanner,
                          Map<Integer, Music> allMusics,
                          UserRepository userRepository,
                          UserServices userServices,
                          PlaylistRepository playlistRepository,
                          PlaylistMusicRepository playlistMusicRepository,
                          FavoriteMusicRepository favoriteMusicRepository,
                          PlaylistServices playlistServices) throws SQLException {

        boolean runningUserMenu = true;

        while (runningUserMenu) {

            System.out.println("\n\n=== Welcome " + user.getName() + ", what would you like to do today? ===");

            System.out.println("1 - Playlists");
            System.out.println("2 - Create Playlist");
            System.out.println("3 - Delete Playlist");
            System.out.println("4 - Favorite musics");
            System.out.println("5 - Search");
            System.out.println("6 - Logout");
            System.out.println("7 - Delete user");
            System.out.print("Option: ");

            int optionMenu = scanner.nextInt();
            scanner.nextLine();

            switch (optionMenu) {

                //option: Playlists
                case 1: {
                    System.out.println("\n\n=== Playlists ===");

                    System.out.println("What do you want to do?");
                    System.out.println("1 - Access a playlist");
                    System.out.println("2 - Return to menu");
                    System.out.print("\nOption: ");

                    int optionPlaylist = scanner.nextInt();
                    scanner.nextLine();

                    switch (optionPlaylist) {
                        case 1: {
                            System.out.println("\n\nWhich playlist do you want to access?");

                            for (Playlist playlist : playlistRepository.findAll(user)) {
                                System.out.println(playlist.getTitle());
                            }

                            System.out.print("\nPlaylist Name: ");
                            String playlistName = scanner.nextLine();

                            while (playlistRepository.findByTitle(playlistName, userRepository.findByEmail(user.getEmail())) == null) {
                                System.out.println("\nSeems that you don't have any playlist with that name");
                                System.out.println("Please, try another one or return");
                                System.out.println("1 - Try another one");
                                System.out.println("2 - Return");
                                int option = scanner.nextInt();
                                scanner.nextLine();

                                if (option == 1) {
                                    System.out.print("Playlist Name: ");
                                    playlistName = scanner.nextLine();

                                } else if (option == 2) {
                                    break;

                                } else {
                                    while (option != 1 && option != 2) {
                                        System.out.println("Invalid answer, please try again");
                                        System.out.println("1 - Try another one");
                                        System.out.println("2 - Return");
                                        option = scanner.nextInt();
                                        scanner.nextLine();

                                    }
                                }
                            }

                            if (playlistRepository.findByTitle(playlistName, user) != null) {
                                playlistMenu.showMenu(user,
                                        playlistRepository.findByTitle(playlistName, user),
                                        scanner, userRepository,
                                        playlistRepository,
                                        playlistMusicRepository,
                                        favoriteMusicRepository,
                                        allMusics);
                            }

                            break;
                        }

                        case 2:
                            break;

                        default:
                            System.out.println("Invalid option!");
                            break;
                    }

                    break;
                }

                //option: create playlist
                case 2: {
                    System.out.println("\n\nPlaylist Name:");
                    String playlistName = scanner.nextLine();

                    System.out.println(
                            "Now, set a description for your playlist:");
                    String description = scanner.nextLine();

                    while (!playlistServices.createPlaylist(playlistName, description, user)) {

                        System.out.println("There is already a playlist with this name.");

                        System.out.print("Playlist name: ");
                        playlistName = scanner.nextLine();
                    }

                    System.out.println("The playlist was created successfully");
                    break;
                }

                case 3: {

                    System.out.println("Your playlists:");

                    for (Playlist playlist : playlistRepository.findAll(user)) {
                        System.out.println(playlist.getTitle());
                    }

                    System.out.println("\n\nPlaylist Name:");
                    String playlistName = scanner.nextLine();

                    Playlist playlist = playlistRepository.findByTitle(playlistName, user);

                    while (playlist == null) {

                        System.out.println("It seems that the playlist you are trying to access doesn't exist, please try another one");
                        System.out.print("Playlist: ");
                        playlistName = scanner.nextLine();
                        playlist = playlistRepository.findByTitle(playlistName, user);
                    }

                    while (!(playlistRepository.delete(playlist.getId(), user))) {

                        System.out.println(
                                "There is no playlist with this name, please try another one");

                        System.out.print("Playlist name: ");
                        playlistName = scanner.nextLine();
                    }


                    System.out.println("The playlist was deleted successfully");
                    break;

                }

                //option: access favorite musics
                case 4:
                    favoriteMenu.showMenu(user,
                            scanner,userRepository,
                            playlistRepository,
                            playlistMusicRepository,
                            favoriteMusicRepository,
                            allMusics);
                    break;

                //option: Search
                case 5: {
                    searchMenu.showMenu(user,
                            scanner,
                            userRepository,
                            userServices,
                            playlistRepository,
                            playlistServices,
                            playlistMusicRepository,
                            favoriteMusicRepository,
                            allMusics);
                    break;
                }

                // Return
                case 6:
                    runningUserMenu = false;
                    break;

                case 7: {
                    System.out.println("Are you sure you want to delete you account?");
                    System.out.println("All the information related to this user will be deleted permanently.");
                    System.out.println("1 - Yes");
                    System.out.println("2 - Return");
                    System.out.print("Option: ");

                    String optionDelete = scanner.nextLine();

                    switch(optionDelete) {

                        case "1": {
                            userRepository.delete(user.getId());
                            runningUserMenu = false;
                            break;
                        }

                        case "2": {
                            break;
                        }

                        default:
                            System.out.println("Invalid option!");
                            break;
                    }
                }
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }
}
