
package repositories.models;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public int save(User user) throws SQLException {
        String sql = " INSERT INTO users(email, password_hash, username) VALUES(?, ?, ?)RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getName());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int idUser = resultSet.getInt("id");
                    user.setId(idUser);
                    return idUser;
                }
            }

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("User already registered!");
                return -1;
            } else {
                throw e;
            }
        }

        return -1;
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password_hash");
                    String userName = resultSet.getString("username");

                    return new User(email, password, userName, id);

                } else {
                    return null;
                }
            }
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String password = resultSet.getString("password_hash");
                    String userName = resultSet.getString("username");
                    int id = resultSet.getInt("id");

                    return new User(email, password, userName, id);

                } else {
                    return null;
                }
            }
        }
    }

    public User findByName(String name) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password_hash");
                    int id = resultSet.getInt("id");

                    return new User(email, password, name, id);

                } else {
                    return null;
                }
            }
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<User> result = new ArrayList<>();

            while (resultSet.next()) {

                result.add(
                        new User(
                                resultSet.getString("email"),
                                resultSet.getString("password_hash"),
                                resultSet.getString("username"),
                                resultSet.getInt("id")
                        )
                );
            }

            return result;
        }
    }

    public boolean update(User user) throws SQLException {
        String sql = " UPDATE users SET username = ?, email = ?, password_hash = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setInt(4, user.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }
}
