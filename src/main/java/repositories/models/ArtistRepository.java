
package repositories.models;

import model.Artist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistRepository {

    private final Connection connection;

    public ArtistRepository(Connection connection) {
        this.connection = connection;
    }

    public int save(Artist artist) throws SQLException {
        String sql = """
                INSERT INTO artists(name)
                VALUES(?)
                RETURNING id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, artist.getName());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    int idArtist = resultSet.getInt("id");
                    artist.setId(idArtist);
                    return idArtist;
                }
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Artist already registered!");
                return -1;
            } else {
                throw e;
            }
        }

        return -1;
    }

    public Artist findById(int id) throws SQLException {
        String sql = "SELECT * FROM artists WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String name = resultSet.getString("name");

                    return new Artist(name, id);

                } else {
                    return null;
                }
            }
        }
    }

    public Artist findByName(String name) throws SQLException {
        String sql = "SELECT * FROM artists WHERE name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    int id = resultSet.getInt("id");

                    return new Artist(name, id);

                } else {
                    return null;
                }
            }
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM artists WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public List<Artist> findAll() throws SQLException {
        String sql = "SELECT * FROM artists";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<Artist> result = new ArrayList<>();

            while (resultSet.next()) {

                result.add(
                        new Artist(
                                resultSet.getString("name"),
                                resultSet.getInt("id")
                        )
                );
            }

            return result;
        }
    }

    public boolean update(Artist artist) throws SQLException {
        String sql = "UPDATE artists SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, artist.getName());
            statement.setInt(2, artist.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }
}
