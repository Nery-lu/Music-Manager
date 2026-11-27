package services;

import model.Playlist;
import model.User;
import repositories.models.PlaylistRepository;
import repositories.models.UserRepository;
import repositories.relationships.PlaylistMusicRepository;

import java.sql.SQLException;

public class PlaylistServices {

    private final PlaylistRepository playlistRepository;
    private final PlaylistMusicRepository playlistMusicRepository;
    private final UserRepository userRepository;

    public PlaylistServices (PlaylistRepository playlistRepository,
                             PlaylistMusicRepository playlistMusicRepository,
                             UserRepository userRepository) {

        this.playlistRepository = playlistRepository;
        this.playlistMusicRepository = playlistMusicRepository;
        this.userRepository = userRepository;

    }

    public boolean createPlaylist(String title, String description, User user) throws SQLException {

        if (playlistRepository.findByTitle(title, user) != null) {
            return false;
        }

        Playlist playlist = new Playlist(title, description, user.getId());

        int createdId = playlistRepository.save(playlist, user);

        return createdId != -1;
    }

    public boolean deletePlaylist(String title, User user) throws SQLException {

        Playlist playlist = playlistRepository.findByTitle(title, user);

        if (playlist == null) {
            return false;
        }

        return playlistRepository.delete(playlist.getId(), user);
    }
}
