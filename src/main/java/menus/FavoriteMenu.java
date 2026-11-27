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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FavoriteMenu {

    private final UserServices userServices;
    private final PlaylistServices playlistServices;

    public FavoriteMenu (UserServices userServices, PlaylistServices playlistServices) throws SQLException {
        this.userServices = userServices;
        this.playlistServices = playlistServices;
    }

    public void showMenu (User user,
                          Scanner scanner,
                          UserRepository userRepository,
                          PlaylistRepository playlistRepository,
                          PlaylistMusicRepository playlistMusicRepository,
                          FavoriteMusicRepository favoriteMusicRepository,
                          Map<Integer, Music> allMusics) throws SQLException {

        boolean runningPlaylistMenu = true;

        while (runningPlaylistMenu) {
            System.out.println("\n\n\n=== Favorites ===");
            System.out.println("\nWhat Would you like to do?");
            System.out.println("1 - Play music");
            System.out.println("2 - Add music");
            System.out.println("3 - Remove music");
            System.out.println("4 - Return");

            if (!favoriteMusicRepository.findAll(user).isEmpty()) {
                for (Music music : favoriteMusicRepository.findAll(user)) {
                    System.out.println(music.getTitle());
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

                    List<Music> musicFound = MusicSearch.searchMusic(search, allMusics);

                    ArrayList<Music> foundList = new ArrayList<>(musicFound);

                    if (!foundList.isEmpty()) {

                        System.out.println("\nChoose the music you want to add");

                        for (int index = 0; index < foundList.size(); index++) {
                            Music music = foundList.get(index);

                            System.out.println(
                                    (index + 1) + " - " + music.getTitle()
                                            + " - " + music.getArtist().stream()
                                            .map(Artist::getName)
                                            .collect(Collectors.joining(", "))
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


                            if (favoriteMusicRepository.exists(selectedMusic, user)) {

                                System.out.println(
                                        "This music is already in the playlist"
                                );

                            } else {

                                favoriteMusicRepository.addFavorite(selectedMusic, user);

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

                    Music music = favoriteMusicRepository.findMusicByTitle(removedMusic, user);
                    int attempts = 1;
                    boolean cancelled = false;
                    boolean removed = false;

                    if (music != null) {

                        removed = favoriteMusicRepository.removeFavorite(music, user);

                    } else {


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

                                    Music musicConfirmation = favoriteMusicRepository.findMusicByTitle(removedMusic, user);

                                    if (musicConfirmation != null) {

                                        removed = favoriteMusicRepository.removeFavorite(favoriteMusicRepository.findMusicByTitle(removedMusic, user),
                                                user);

                                    }
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
                    }

                    if (removed) {
                        System.out.println(
                                removedMusic + " was removed successfully!"
                        );

                    } else if (!cancelled) {
                        System.out.println("Maximum number of attempts reached.");
                    }

                    break;
                }

                // Return
                case 4: {
                    runningPlaylistMenu = false;
                    break;
                }
            }
        }
    }
}
