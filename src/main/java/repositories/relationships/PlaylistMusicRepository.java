package repositories.relationships;

import model.Music;
import model.Playlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistMusicRepository {

    private final Connection connection;

    public PlaylistMusicRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean addMusic(Playlist playlist, Music music) throws SQLException {
        String sql = "INSERT INTO playlist_music(playlist_id, music_id) VALUES(?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, playlist.getId());
        statement.setInt(2, music.getId());

        try {

            statement.executeUpdate();

        } catch (SQLException e) {

            if ("23505".equals(e.getSQLState())) {
                System.out.println("The music is already on the playlist!");
                return false;

            } else {
                throw e;
            }
        }

        return true;
    }

    public boolean removeMusic (Playlist playlist, Music music) throws SQLException {
        String sql = "DELETE FROM playlist_music WHERE playlist_id = ? AND music_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, playlist.getId());
        statement.setInt(2, music.getId());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;
    }

    public boolean exists(Playlist playlist, Music music) throws SQLException {
        String sql = "SELECT * FROM playlist_music WHERE playlist_id = ? AND music_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, playlist.getId());
        statement.setInt(2, music.getId());

        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    public Music findMusicByTitle (String title, Playlist playlist) throws SQLException {
        String sql = """
            SELECT music.*
            FROM music
            JOIN playlist_music
            ON playlist_music.music_id = music_id
            WHERE playlist_music.playlist_id = ?
            AND music.title = ?
            """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, playlist.getId());
        statement.setString(2, title);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {

            String lyrics = resultSet.getString("lyrics");
            int duration = resultSet.getInt("duration");
            int id = resultSet.getInt("id");

            return new Music(title, lyrics, duration, id);

        }

        return null;
    }

    public List<Music> findAllMusicsFromPlaylist(Playlist playlist) throws SQLException {
        String sql = """
            SELECT music.*
            FROM music
            JOIN playlist_music
            ON music.id = playlist_music.music_id
            WHERE playlist_music.playlist_id = ?
            """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, playlist.getId());

        ResultSet resultSet = statement.executeQuery();

        List<Music> result = new ArrayList<>();

        while (resultSet.next()) {
            result.add(new Music(
                    resultSet.getString("title"),
                    resultSet.getString("lyrics"),
                    resultSet.getInt("duration"),
                    resultSet.getInt("id")
            ));
        }

        return result;
    }

}
