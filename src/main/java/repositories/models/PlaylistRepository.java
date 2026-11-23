
package repositories.models;

import model.Playlist;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistRepository {

    private final Connection connection;

    public PlaylistRepository(Connection connection) {
        this.connection = connection;
    }

    public int save(Playlist playlist, User user) throws SQLException {
        String sql = """
                INSERT INTO playlists(title, description, user_id)
                VALUES(?, ?, ?)
                RETURNING id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, playlist.getTitle());
            statement.setString(2, playlist.getDescription());
            statement.setInt(3, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    int idPlaylist = resultSet.getInt("id");
                    playlist.setId(idPlaylist);
                    return idPlaylist;
                }
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Playlist already registered!");
                return -1;
            } else {
                throw e;
            }
        }

        return -1;
    }

    public Playlist findById(int id, User user) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE id = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setInt(2, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");

                    return new Playlist(
                            title,
                            description,
                            id,
                            resultSet.getInt("user_id")
                    );

                } else {
                    return null;
                }
            }
        }
    }

    public Playlist findByTitle(String title, User user) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE title = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setInt(2, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String description = resultSet.getString("description");
                    int id = resultSet.getInt("id");

                    return new Playlist(
                            title,
                            description,
                            id,
                            resultSet.getInt("user_id")
                    );

                } else {
                    return null;
                }
            }
        }
    }

    public boolean delete(int id, User user) throws SQLException {
        String sql = "DELETE FROM playlists WHERE id = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setInt(2, user.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public List<Playlist> findAll(User user) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {

                List<Playlist> result = new ArrayList<>();

                while (resultSet.next()) {

                    result.add(
                            new Playlist(
                                    resultSet.getString("title"),
                                    resultSet.getString("description"),
                                    resultSet.getInt("id"),
                                    resultSet.getInt("user_id")
                            )
                    );
                }

                return result;
            }
        }
    }

    public boolean update(Playlist playlist, User user) throws SQLException {
        String sql = " UPDATE playlists SET title = ?, description = ? WHERE id = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, playlist.getTitle());
            statement.setString(2, playlist.getDescription());
            statement.setInt(3, playlist.getId());
            statement.setInt(4, user.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }
}
