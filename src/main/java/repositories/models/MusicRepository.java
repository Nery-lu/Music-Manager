package repositories.models;

import model.Artist;
import model.Music;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicRepository {

    private final Connection connection;

    public MusicRepository(Connection connection) {
        this.connection = connection;
    }

    public int save(Music music) throws SQLException {
        String sql = """
                INSERT INTO music(title, lyrics, duration)
                VALUES(?, ?, ?)
                RETURNING id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, music.getTitle());
            statement.setString(2, music.getLyrics());
            statement.setInt(3, music.getDuration());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    int idMusic = resultSet.getInt("id");
                    music.setId(idMusic);
                    return idMusic;
                }
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Music already registered!");
                return -1;
            } else {
                throw e;
            }
        }

        return -1;
    }

    public Music findById(int id) throws SQLException {
        String sql = "SELECT * FROM music WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String title = resultSet.getString("title");
                    String lyrics = resultSet.getString("lyrics");
                    int duration = resultSet.getInt("duration");

                    return new Music(title, lyrics, duration, id);

                } else {
                    return null;
                }
            }
        }
    }

    public Music findByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM music WHERE title = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String lyrics = resultSet.getString("lyrics");
                    int duration = resultSet.getInt("duration");
                    int id = resultSet.getInt("id");

                    return new Music(title, lyrics, duration, id);

                } else {
                    return null;
                }
            }
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM music WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public Map<Integer, Music> findAll() throws SQLException {

        String sql = """
                SELECT
                    m.id AS music_id,
                    m.title,
                    m.lyrics,
                    m.duration,
                    a.id AS artist_id,
                    a.name AS artist_name
                FROM music m
                LEFT JOIN music_artists ma
                    ON m.id = ma.music_id
                LEFT JOIN artists a
                    ON ma.artist_id = a.id
                ORDER BY m.id
                """;

        Map<Integer, Music> allMusics = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int musicId = resultSet.getInt("music_id");

                Music music = allMusics.get(musicId);

                if (music == null) {

                    music = new Music(
                            resultSet.getString("title"),
                            resultSet.getString("lyrics"),
                            resultSet.getInt("duration"),
                            musicId
                    );

                    music.setId(musicId);

                    allMusics.put(musicId, music);
                }

                int artistId = resultSet.getInt("artist_id");

                if (!resultSet.wasNull()) {

                    Artist artist = new Artist(
                            resultSet.getString("artist_name")
                    );

                    artist.setId(artistId);

                    music.getArtist().add(artist);

                }
            }
        }

        return allMusics;
    }

public boolean update(Music music) throws SQLException {
    String sql = "UPDATE music SET title = ?, lyrics = ?, duration = ? WHERE id = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, music.getTitle());
        statement.setString(2, music.getLyrics());
        statement.setInt(3, music.getDuration());
        statement.setInt(4, music.getId());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;
    }
}
}
