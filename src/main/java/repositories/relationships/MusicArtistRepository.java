package repositories.relationships;

import model.Artist;
import model.Music;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MusicArtistRepository {

    private final Connection connection;

    public MusicArtistRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Artist> findArtistsFromMusic(Music music) throws SQLException {
        String sql = """
                  SELECT artists.*
                  FROM artists
                  JOIN music_artists
                  ON artists.id = music_artists.artist_id
                  WHERE music_artists.music_id = ?
                  """;

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, music.getId());

        ResultSet resultSet = statement.executeQuery();
        List<Artist> artistsFound = new ArrayList<>();

        while(resultSet.next()) {
            artistsFound.add(new Artist(resultSet.getString("name"),
                    resultSet.getInt("id")));
        }

        return artistsFound;
    }

    public boolean exists(Artist artist, Music music) throws SQLException {
        String sql = "SELECT * FROM music_artists WHERE music_id = ? AND artist_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, music.getId());
        statement.setInt(2, artist.getId());

        ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

    public boolean addArtistToMusic (Artist artist, Music music) throws SQLException {
        String sql = "INSERT INTO music_artists(music_id, artist_id) VALUES(?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, music.getId());
        statement.setInt(2, artist.getId());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;
    }

    public boolean removeArtistToMusic (Artist artist, Music music) throws SQLException {
        String sql = "DELETE FROM music_artists WHERE music_id = ? AND artist_id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, music.getId());
        statement.setInt(2, artist.getId());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;
    }
}
