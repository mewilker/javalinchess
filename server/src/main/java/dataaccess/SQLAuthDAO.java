package dataaccess;

import datamodels.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    public SQLAuthDAO() throws DataAccessException {
        String statement = """ 
                CREATE TABLE IF NOT EXISTS auths (
                    username VARCHAR (255) NOT NULL,
                    token VARCHAR (255) NOT NULL,
                    PRIMARY KEY (token)
                )
                """;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not initialize auth table", e);
        }
    }

    @Override
    public AuthData insertAuth(String username) throws DataAccessException {
        String statement = "INSERT INTO auths (username, token) VALUES (?, ?)";
        String token = AuthDAO.generateToken();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not insert into auths", e);
        }
        return new AuthData(username, token);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String statement = "SELECT * FROM auths WHERE token = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new AuthData(resultSet.getString("username"),
                            resultSet.getString("token"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting user", e);
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = "DELETE FROM auths WHERE token = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete from auths", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE auths";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not clear tokens", e);
        }
    }
}
