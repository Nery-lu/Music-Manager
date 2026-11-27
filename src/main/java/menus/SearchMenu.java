package menus;

import com.musicmanager.MusicSearch;
import model.Artist;
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
import java.util.*;
import java.util.stream.Collectors;

public class SearchMenu {

    private final PlaylistServices playlistServices;

    public SearchMenu (PlaylistServices playlistServices) {
        this.playlistServices = playlistServices;
    }

    public void showMenu (User user,
                                    Scanner scanner,
                                    UserRepository userRepository,
                                    UserServices userServices,
                                    PlaylistRepository playlistRepository,
                                    PlaylistServices playlistServices,
                                    PlaylistMusicRepository playlistMusicRepository,
                                    FavoriteMusicRepository favoriteMusicRepository,
                                    Map< Integer, Music> allMusics) throws SQLException {

        System.out.println("\n\n===Search===");
        System.out.print("Type here your search: ");
        String search = scanner.nextLine();
        List<Music> searchResult = new ArrayList<>(MusicSearch.searchMusic(search, allMusics));

        if (!searchResult.isEmpty()) {

            for (int index = 0; index < searchResult.size(); index++) {
                System.out.println((index + 1) + " - " +
                        searchResult.get(index).getTitle() + " - "
                        + searchResult.get(index).getArtist().stream()
                        .map(Artist::getName)
                        .collect(Collectors.joining(", ")));
            }

            System.out.println("\nPlease, select a music");
            System.out.print("Music: ");
            int indexMusicFound = scanner.nextInt();
            scanner.nextLine();

            if (indexMusicFound < 1 || indexMusicFound > searchResult.size()) {

                System.out.println("Invalid music option.");

            } else {

                Music selectedMusic = searchResult.get(indexMusicFound - 1);

                System.out.println("\n\n===" + selectedMusic.getTitle() + "===");
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

                                if (playlistRepository.findAll(user).isEmpty()) {
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

                                            playlistServices.createPlaylist(title, description, user);
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

                                    for (Playlist playlist : playlistRepository.findAll(user)) {
                                        System.out.println(playlist.getTitle());
                                    }

                                    System.out.println("On which playlist would you like to add this music?");
                                    String chosenPlaylist = scanner.nextLine();

                                    if (playlistRepository.findByTitle(chosenPlaylist, user) == null) {
                                        System.out.println("There isn't any playlist under this name," +
                                                " please try another one");

                                    } else {

                                        boolean wasAdded = playlistMusicRepository.addMusic(playlistRepository.findByTitle(chosenPlaylist, user), selectedMusic);

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

                                if (playlistRepository.findAll(user).isEmpty()) {
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

                                            playlistServices.createPlaylist(title, description, user);
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

                                    for (Playlist playlist : playlistRepository.findAll(user)) {
                                        System.out.println(playlist.getTitle());
                                    }

                                    System.out.println("On which playlist would you like to remove this music?");
                                    String chosenPlaylist = scanner.nextLine();

                                    if (playlistRepository.findByTitle(chosenPlaylist, user) == null) {
                                        System.out.println("There isn't any playlist under this name," +
                                                " please try another one");

                                    } else {

                                        boolean wasRemoved = playlistMusicRepository.removeMusic(playlistRepository.findByTitle(chosenPlaylist, user),
                                                selectedMusic);

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
                            if (favoriteMusicRepository.exists(selectedMusic, user)) {

                                System.out.println("This music is already in the playlist");

                            } else {

                                favoriteMusicRepository.addFavorite(selectedMusic, user);

                                System.out.println(selectedMusic.getTitle() + " favorited successfully!");
                            }
                            runningSearchMenu = false;
                            break;
                        }

                        /* Add to queue */
                        case "5": {
                            if (user.getQueue().contains(selectedMusic)) {
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

            }

        } else {

            System.out.println("No music found.");

        }

    }
}
