package menus;

import model.Music;
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

public class MainMenu {

    private final UserServices userServices;
    private final UserMenu userMenu;
    private final Scanner scanner;

    public MainMenu (UserServices userServices, UserMenu userMenu, Scanner scanner) {
        this.userServices = userServices;
        this.userMenu = userMenu;
        this.scanner = scanner;
    }

    public void showMenu (Scanner scanner,
                          Map<Integer, Music> allMusics,
                          UserRepository userRepository,
                          UserServices userServices,
                          PlaylistRepository playlistRepository,
                          PlaylistMusicRepository playlistMusicRepository,
                          FavoriteMusicRepository favoriteMusicRepository,
                          PlaylistServices playlistServices) throws SQLException {

        boolean running = true;

        while (running) {

            System.out.println("=== MusicManager ===");
            System.out.println("\nWhat would you like to do today?");
            System.out.println("1 - Create account");
            System.out.println("2 - Login");
            System.out.println("3 - Exit");
            System.out.print("Option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    int createdUserID = userServices.createAccount(scanner);
                    break;

                case 2: {
                    User loggedUser = userServices.login(scanner);

                    if (loggedUser != null) {
                        userMenu.showMenu(loggedUser,
                                          scanner,
                                          allMusics,
                                          userRepository,
                                          userServices,
                                          playlistRepository,
                                          playlistMusicRepository,
                                          favoriteMusicRepository,
                                          playlistServices);
                    }
                    break;
                }

                case 3:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }


    }


}
