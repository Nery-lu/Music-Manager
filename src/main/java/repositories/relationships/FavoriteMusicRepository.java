package repositories.relationships;

import model.Artist;
import model.Playlist;
import model.User;
import model.Music;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteMusicRepository {

    private final Connection connection;

    public FavoriteMusicRepository(Connection connection) {
        this.connection = connection;
    }


    public boolean addFavorite (Music music, User user) throws SQLException {
        String sql = "INSERT INTO favorite_music(user_id, music_id) VALUES(?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, user.getId());
        statement.setInt(2, music.getId());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;
    }

    public boolean removeFavorite (Music music, User user) throws SQLException {
        String sql = "DELETE FROM favorite_music WHERE user_id = ? AND music_id = ?";


        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, user.getId());
        statement.setInt(2, music.getId());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;

    }

    public Music findMusicByTitle (String title, User user) throws SQLException {
        String sql = """
            SELECT music.*
            FROM music
            JOIN favorite_music
            ON favorite_music.music_id = music_id
            WHERE favorite_music.user_id = ?
            AND music.title = ?
            """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, user.getId());
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

    public boolean exists(Music music, User user) throws SQLException {
        String sql = "SELECT * FROM favorite_music WHERE user_id = ? AND music_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, user.getId());
        statement.setInt(2, music.getId());

        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    public List<Music> findAll(User user) throws SQLException {
        String sql = """
            SELECT music.*
            FROM music
            JOIN favorite_music
            ON music.id = favorite_music.music_id
            WHERE favorite_music.user_id = ?
            """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, user.getId());

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
