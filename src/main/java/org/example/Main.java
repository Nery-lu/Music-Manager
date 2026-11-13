package org.example;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

/*    There is a problem that I still have to correct. Right now,                   */
/*    as the parameter that is being used to find a music in a playlist is the      */
/*    name of the music, it is not possible, yet, to have two musics with the same  */
/*    name but different artists in the same playlist, which is something I have    */
/*    change later but, at the moment, this is not that important.Anyways, this     */
/*    has to be corrected later.                                                    */


/*    TODO: create the repositories with the methods I will use to modify the database from here   */
/*    TODO: Play music       */

/*    Where I'm at now: Developing the methods I will use to communicate with the database, I'm about to start   */

public class Main {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {

        Map<String, ArrayList<Music>> allMusics =
                MusicCatalog.loadMusicCatalog();

        Map<String, User> users = UserStorage.loadUsers();
        Scanner scanner = new Scanner(System.in);

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
                    createAccount(users, scanner);
                    break;

                case 2: {
                    User loggedUser = login(users, scanner);

                    if (loggedUser != null) {
                        userMenu(loggedUser, scanner, users, allMusics);
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

        scanner.close();
    }

    private static void createAccount(Map<String, User> users, Scanner scanner) {

        String email;

        System.out.println("\nChoose an Email:");
        System.out.print("Email: ");
        email = scanner.nextLine();

        while (!isValidEmail(email) || users.containsKey(email)) {

            if (users.containsKey(email)) {
                System.out.println(
                        "An account is already registered under this email, please try another one");
            } else {
                System.out.println(
                        "Invalid email, please try another one:");
            }

            System.out.print("Email: ");
            email = scanner.nextLine();
        }

        System.out.println("\nChoose a password");
        System.out.println("Your password must have:");
        System.out.println("- 8 characters or more;");
        System.out.println("- letters and numbers;");
        System.out.println("- At least 1 uppercase letter;");
        System.out.print("\nPassword: ");

        String password = scanner.nextLine();

        while (!isValidPassword(password)) {
            System.out.println("Invalid password, please try another one");
            System.out.print("Password: ");
            password = scanner.nextLine();
        }

        System.out.println("\nConfirm password");
        System.out.print("Password: ");

        String passwordConf = scanner.nextLine();

        while (!passwordConf.equals(password)) {
            System.out.println(
                    "Something went wrong, please confirm if both passwords are the same");
            System.out.print("Password: ");
            passwordConf = scanner.nextLine();
        }

        System.out.println("Now, choose a username for your account");
        System.out.print("Name: ");
        String userName = scanner.nextLine();

        User user = new User();
        user.setEmail(email);

        String passwordHash = encoder.encode(password);

        user.setPasswordHash(passwordHash);

        user.setName(userName);

        users.put(email, user);

        UserStorage.saveUsers(users);

        System.out.println("\nAccount created successfully!");
    }

    private static User login(Map<String, User> users, Scanner scanner) {

        System.out.println("\nEnter your Email");
        System.out.print("Email: ");

        String email = scanner.nextLine();
        int i = 0;

        while (!users.containsKey(email)) {

            i++;

            System.out.println("Email not found, please try again.");

            if (i >= 3) {

                boolean choosingAction = true;

                while (choosingAction) {

                    System.out.println("\nNew here? Create your account now!");
                    System.out.println("1 - Create Account");
                    System.out.println("2 - Continue trying");
                    System.out.print("Option: ");

                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1":
                            createAccount(users, scanner);
                            return null;

                        case "2":
                            i = 0;
                            choosingAction = false;
                            break;

                        default:
                            System.out.println("Invalid choice, try another one");
                            break;
                    }
                }
            }

            System.out.print("Email: ");
            email = scanner.nextLine();
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();

        while (!encoder.matches(password, users.get(email).getPasswordHash())) {

            System.out.println("Wrong password, try again");
            System.out.print("Password: ");
            password = scanner.nextLine();
        }

        System.out.println("\nLogin successful!");
        return users.get(email);
    }

    private static boolean isValidPassword(String password) {
        return password != null
                && password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    }

    private static boolean isValidEmail(String email) {
        return email != null
                && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    private static void userMenu(User user, Scanner scanner, Map<String, User> users, Map<String, ArrayList<Music>> allMusics) {

        boolean runningUserMenu = true;

        while (runningUserMenu) {

            System.out.println("\n\n=== Welcome " + user.getName() + ", what would you like to do today? ===");

            System.out.println("1 - Playlists");
            System.out.println("2 - Create Playlist");
            System.out.println("3 - Delete Playlist");
            System.out.println("4 - Favorite musics");
            System.out.println("5 - Search");
            System.out.println("6 - Logout");
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

                            for (Playlist playlist : user.listPlaylists()) {
                                System.out.println(playlist.getTitle());
                            }

                            System.out.print("\nPlaylist Name: ");
                            String playlistName = scanner.nextLine();

                            while (!(users.get(user.getEmail()).getPlaylists().containsKey(playlistName))) {
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

                            if (users.get(user.getEmail()).getPlaylists().containsKey(playlistName)) {
                                playlistMenu(user, users.get(user.getEmail()).getPlaylists().get(playlistName), scanner, users, allMusics, false);
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

                    if (!user.createPlaylist(playlistName, description)) {

                        while (!(user.createPlaylist(playlistName, description))) {

                            System.out.println(
                                    "There is already a playlist created with this name, please try another one");

                            System.out.print("Playlist name: ");
                            playlistName = scanner.nextLine();
                        }
                    }

                    UserStorage.saveUsers(users);

                    System.out.println("The playlist was created successfully");
                    break;
                }

                case 3: {

                    System.out.println("Your playlists:");

                    for (Playlist playlist : user.getPlaylists().values()) {
                        System.out.println(playlist.getTitle());
                    }

                    System.out.println("\n\nPlaylist Name:");
                    String playlistName = scanner.nextLine();

                    if (!user.deletePlaylist(playlistName)) {

                        while (!(user.deletePlaylist(playlistName))) {

                            System.out.println(
                                    "There is no playlist with this name, please try another one");

                            System.out.print("Playlist name: ");
                            playlistName = scanner.nextLine();
                        }
                    }

                    UserStorage.saveUsers(users);

                    System.out.println("The playlist was deleted successfully");
                    break;
                }

                //option: access favorite musics
                case 4:
                    playlistMenu(user, user.getFavoriteMusics(), scanner, users, allMusics, true);
                    break;

                //option: Search
                case 5: {
                    searchMenu(user, scanner, users, allMusics);
                   break;
                }

                // Return
                case 6:
                    runningUserMenu = false;
                    break;

                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private static void playlistMenu(User user, Playlist playlist, Scanner scanner,
                                     Map<String, User> users, Map<String, ArrayList<Music>> allMusics,
                                     boolean isFavoriteMenu) {

        boolean runningPlaylistMenu = true;

        while (runningPlaylistMenu) {
            System.out.println("\n\n\n===" + playlist.getTitle() + "===");
            System.out.println("\nWhat Would you like to do?");
            System.out.println("1 - Play music");
            System.out.println("2 - Add music");
            System.out.println("3 - Remove music");
            if (!isFavoriteMenu) {
                System.out.println("4 - Favorite music");
                System.out.println("5 - Return");

            } else {

                System.out.println("4 - Return");
            }

            if (!(playlist.getMusics().isEmpty())) {
                for (String title : playlist.getMusics().keySet()) {
                    System.out.println(title);
                }
            } else {
                System.out.println("The playlist is empty");
            }

            int optionPlaylist = scanner.nextInt();
            scanner.nextLine();

            switch (optionPlaylist) {

                //Play music "dont know yet how the fuck i'm going to do this to be honest :)"
                case 1: {
                    /*TO DO: play music */
                    break;
                }

                // Add music
                case 2: {
                    System.out.print("Search for a music: ");
                    String search = scanner.nextLine();

                    Set<Music> musicFound = MusicSearch.searchMusic(search, allMusics);

                    ArrayList<Music> foundList = new ArrayList<>(musicFound);

                    if (!foundList.isEmpty()) {

                        System.out.println("\nChoose the music you want to add");

                        for (int index = 0; index < foundList.size(); index++) {
                            Music music = foundList.get(index);

                            System.out.println(
                                    (index + 1) + " - " + music.getTitle()
                                            + " - " + String.join(", ", music.getArtist())
                            );
                        }

                        System.out.print("Music: ");
                        int indexMusic = scanner.nextInt();
                        scanner.nextLine();

                        // Validate the user's choice
                        if (indexMusic < 1 || indexMusic > foundList.size()) {

                            System.out.println("Invalid music option.");

                        } else {

                            Music selectedMusic = foundList.get(indexMusic - 1);


                            if (playlist.getMusics()
                                    .containsKey(selectedMusic.getTitle())) {

                                System.out.println(
                                        "This music is already in the playlist"
                                );

                            } else {

                                playlist.addMusic(selectedMusic);

                                UserStorage.saveUsers(users);

                                System.out.println(
                                        selectedMusic.getTitle()
                                                + " added successfully!"
                                );
                            }
                        }

                    } else {
                        System.out.println("No music found.");
                    }

                    break;
                }


                // Remove music
                case 3: {
                    System.out.println("Which music do you want to remove?");
                    System.out.print("Music: ");

                    String removedMusic = scanner.nextLine();

                    boolean removed = playlist
                            .removeMusic(removedMusic);

                    int attempts = 1;
                    boolean cancelled = false;

                    while (!removed && attempts < 3 && !cancelled) {

                        System.out.println("Sorry, but this music was not found in the playlist.");
                        System.out.println("Please try again or return.");
                        System.out.println("1 - Try again");
                        System.out.println("2 - Return");
                        System.out.print("Option: ");

                        String option = scanner.nextLine();

                        switch (option) {

                            case "1": {
                                System.out.print("\nMusic: ");
                                removedMusic = scanner.nextLine();

                                attempts++;

                                removed = playlist.removeMusic(removedMusic);

                                break;
                            }

                            case "2": {
                                cancelled = true;
                                break;
                            }

                            default: {
                                System.out.println("Invalid option. Please try again.");
                                break;
                            }
                        }
                    }

                    if (removed) {
                        System.out.println(
                                removedMusic + " was removed successfully!"
                        );

                        UserStorage.saveUsers(users);

                    } else if (!cancelled) {
                        System.out.println("Maximum number of attempts reached.");
                    }

                    break;
                }


                // Favorite music
                case 4: {
                    if (!isFavoriteMenu) {
                        System.out.println("\nWhich music do you want to set as favorite?");
                        System.out.print("Music: ");
                        String musicFavorite = scanner.nextLine();

                        Set<Music> musicFoundToFavorite = MusicSearch.searchMusic(musicFavorite, allMusics);
                        ArrayList<Music> foundListToFavorite = new ArrayList<>(musicFoundToFavorite);

                        if (!foundListToFavorite.isEmpty()) {

                            System.out.println("\nChoose the music you want to add");

                            for (int index = 0; index < foundListToFavorite.size(); index++) {
                                Music music = foundListToFavorite.get(index);

                                System.out.println(
                                        (index + 1) + " - " + music.getTitle()
                                                + " - " + String.join(", ", music.getArtist())
                                );
                            }

                            System.out.print("Music: ");
                            int indexMusic = scanner.nextInt();
                            scanner.nextLine();

                            // Validate the user's choice
                            if (indexMusic < 1 || indexMusic > foundListToFavorite.size()) {

                                System.out.println("Invalid music option.");

                            } else {

                                Music selectedMusic = foundListToFavorite.get(indexMusic - 1);

                                if (user.getFavorites().getMusics()
                                        .containsKey(selectedMusic.getTitle())) {

                                    System.out.println(
                                            "This music is already in the playlist"
                                    );

                                } else {

                                    user.getFavorites().addMusic(selectedMusic);

                                    UserStorage.saveUsers(users);

                                    System.out.println(selectedMusic.getTitle() + " favorited successfully!");
                                }
                            }

                        } else {

                            System.out.println("No music found.");
                        }

                        break;

                    } else {

                        runningPlaylistMenu = false;
                        break;
                    }

                }


                // Return
                case 5: {
                    runningPlaylistMenu = false;
                    break;
                }
            }
        }
    }


    private static void searchMenu(User user, Scanner scanner, Map<String,
            User> users, Map<String, ArrayList<Music>> allMusics) {
        System.out.println("\n\n===Search===");
        System.out.print("Type here your search: ");
        String search = scanner.nextLine();
        Set<Music> musicFound = new HashSet<>(MusicSearch.searchMusic(search, allMusics));
        List<Music> searchResult = new ArrayList<>(musicFound);

        if (!searchResult.isEmpty()) {

            for (int index = 0; index < searchResult.size(); index++) {
                System.out.println((index + 1) + " - " +
                        searchResult.get(index).getTitle() + " - "
                        + String.join(", ", searchResult.get(index).getArtist()));
            }

            System.out.println("\nPlease, select a music");
            System.out.print("Music: ");
            int indexMusicFound = scanner.nextInt();
            scanner.nextLine();

            if (indexMusicFound < 1 || indexMusicFound > searchResult.size()) {

                System.out.println("Invalid music option.");

            } else {

                Music selectedMusic = searchResult.get(indexMusicFound - 1);

                System.out.println("\n\n===" + selectedMusic.getTitle() + "===" );
                System.out.println("What would you like to do?");
                System.out.println("\n1 - Play music");
                System.out.println("2 - Add to playlist");
                System.out.println("3 - Remove from playlist");
                System.out.println("4 - Add to favorites");
                System.out.println("5 - Add to queue");
                System.out.println("6 - Return");

                String optionSelectedMusic = scanner.nextLine();
                boolean runningSearchMenu = true;

                while (runningSearchMenu) {
                    switch (optionSelectedMusic) {

                        /* Play music */
                        case "1": {
                            /* Still don't know how the fuck I'm going to do this, but this will be the last thing */
                            break;
                        }

                        /* Add music */
                        case "2": {
                            boolean addingToPlaylist = true;

                            while (addingToPlaylist) {
                                System.out.println("\n\n===Your Playlists===");

                                if (user.getPlaylists().isEmpty()) {
                                    System.out.println("Seems like you haven't created any playlist yet,\n" +
                                            "would you like to create one?");

                                    System.out.println("1 - Create a Playlist");
                                    System.out.println("2 - Return");
                                    System.out.print("Option: ");

                                    String optionNoPlaylists = scanner.nextLine();

                                    switch (optionNoPlaylists) {
                                        case "1": {
                                            System.out.println("\nWhat would be the name of your playlist?");
                                            System.out.print("Name: ");
                                            String title = scanner.nextLine();

                                            System.out.println("Now, set a description for your playlist");
                                            System.out.println("Description: ");
                                            String description = scanner.nextLine();

                                            user.createPlaylist(title, description);
                                            break;
                                        }

                                        case "2": {
                                            addingToPlaylist = false;
                                            break;
                                        }

                                        default: {
                                            System.out.println("Invalid option, please try again");
                                            break;
                                        }
                                    }

                                } else {

                                    for (Playlist playlist : user.getPlaylists().values()) {
                                        System.out.println(playlist.getTitle());
                                    }

                                    System.out.println("On which playlist would you like to add this music?");
                                    String chosenPlaylist = scanner.nextLine();

                                    if (!(user.getPlaylists().containsKey(chosenPlaylist))) {
                                        System.out.println("There isn't any playlist under this name," +
                                                " please try another one");

                                    } else {

                                        boolean wasAdded = user.getPlaylists().get(chosenPlaylist).addMusic(selectedMusic);

                                        if (!wasAdded) {
                                            System.out.println("Looks like this music is already on this playlist");

                                        } else {

                                            System.out.println("The music was added successfully to " + chosenPlaylist);
                                        }

                                        addingToPlaylist = false;

                                    }
                                }
                            }
                            runningSearchMenu = false;
                            break;
                        }

                        /* Remove music */
                        case "3": {
                            boolean removingFromPlaylist = true;

                            while (removingFromPlaylist) {
                                System.out.println("\n\n===Your Playlists===");

                                if (user.getPlaylists().isEmpty()) {
                                    System.out.println("Seems like you haven't created any playlist yet,\n" +
                                            "would you like to create one?");

                                    System.out.println("1 - Create a Playlist");
                                    System.out.println("2 - Return");
                                    System.out.print("Option: ");

                                    String optionNoPlaylists = scanner.nextLine();

                                    switch (optionNoPlaylists) {
                                        case "1": {
                                            System.out.println("\nWhat would be the name of your playlist?");
                                            System.out.print("Name: ");
                                            String title = scanner.nextLine();

                                            System.out.println("Now, set a description for your playlist");
                                            System.out.println("Description: ");
                                            String description = scanner.nextLine();

                                            user.createPlaylist(title, description);
                                            break;
                                        }

                                        case "2": {
                                            removingFromPlaylist = false;
                                            break;
                                        }

                                        default: {
                                            System.out.println("Invalid option, please try again");
                                            break;
                                        }
                                    }

                                } else {

                                    for (Playlist playlist : user.getPlaylists().values()) {
                                        System.out.println(playlist.getTitle());
                                    }

                                    System.out.println("On which playlist would you like to remove this music?");
                                    String chosenPlaylist = scanner.nextLine();

                                    if (!(user.getPlaylists().containsKey(chosenPlaylist))) {
                                        System.out.println("There isn't any playlist under this name," +
                                                " please try another one");

                                    } else {

                                        boolean wasRemoved = user.getPlaylists().get(chosenPlaylist).removeMusic(selectedMusic.getTitle());

                                        if (!wasRemoved) {
                                            System.out.println("Looks like this music already not in this playlist");

                                        } else {

                                            System.out.println("The music was removed successfully to " + chosenPlaylist);
                                        }

                                        removingFromPlaylist = false;

                                    }
                                }
                            }
                            runningSearchMenu = false;
                            break;

                        }

                        /* Add to favorites */
                        case "4": {
                            if (user.getFavorites().getMusics()
                                    .containsKey(selectedMusic.getTitle())) {

                                System.out.println("This music is already in the playlist");

                            } else {

                                user.getFavorites().addMusic(selectedMusic);

                                UserStorage.saveUsers(users);

                                System.out.println(selectedMusic.getTitle() + " favorited successfully!");
                            }
                            runningSearchMenu = false;
                            break;
                        }

                        /* Add to queue */
                        case "5": {
                            if(user.getQueue().contains(selectedMusic)) {
                                System.out.println("Looks like this music is already on the queue");

                            } else {
                                user.getQueue().offer(selectedMusic);

                            }
                            runningSearchMenu = false;
                            break;
                        }

                        case "6": {
                            runningSearchMenu = false;
                            break;
                        }

                        default: {
                            System.out.println("Invalid option, please try another one");
                            System.out.print("Option: ");
                            optionSelectedMusic = scanner.nextLine();
                        }
                    }
                }


                UserStorage.saveUsers(users);

            }

        } else {

        System.out.println("No music found.");

           }

            }
        }







