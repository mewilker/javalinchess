package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import datamodels.GameData;

import java.sql.*;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws DataAccessException{
        String statement = """
                CREATE TABLE IF NOT EXISTS games (
                gameID INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                whiteUser VARCHAR(255),
                blackUser VARCHAR(255),
                game TEXT NOT NULL,
                PRIMARY KEY (gameID)
                )
                """;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)){
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException("Could not initialize auth table", e);
        }
    }

    @Override
    public int insertGame(String gameName) throws DataAccessException {
        String statement = "INSERT INTO games (name, game) VALUES (?, ?)";
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, gameName);
            preparedStatement.setString(2, new Gson().toJson(new ChessGame()));

            preparedStatement.executeUpdate();

            try(ResultSet resultSet = preparedStatement.getGeneratedKeys();) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        catch (SQLException e){
            throw new DataAccessException("Error inserting game", e);
        }
        throw new DataAccessException("Could not get an ID");
    }

    @Override
    public ArrayList<GameData> getAllGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        String statement = "SELECT * FROM games";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement);
             ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                GameData data = new GameData(
                        resultSet.getInt("gameID"),
                        resultSet.getString("name"),
                        resultSet.getString("whiteUser"),
                        resultSet.getString("blackUser"),
                        new Gson().fromJson(resultSet.getString("game"), ChessGame.class)
                );
                games.add(data);
            }
        }
        catch (SQLException e){
            throw new DataAccessException("Error getting games", e);
        }
        return games;
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {
        String statement = "UPDATE games SET whiteUser = ?, blackUser = ?, game = ? WHERE gameID = ?";
        try(Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)){
            preparedStatement.setString(1, data.whiteUsername());
            preparedStatement.setString(2, data.blackUsername());
            preparedStatement.setString(3, new Gson().toJson(data.game()));
            preparedStatement.setInt(4, data.gameID());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException("Could not update game", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE games";
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new DataAccessException("Could not clear games", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT * FROM games WHERE gameID = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement);){
            preparedStatement.setInt(1, gameID);
             try(ResultSet resultSet = preparedStatement.executeQuery()) {
                 if (resultSet.next()) {
                     return new GameData(
                             resultSet.getInt("gameID"),
                             resultSet.getString("name"),
                             resultSet.getString("whiteUser"),
                             resultSet.getString("blackUser"),
                             new Gson().fromJson(resultSet.getString("game"), ChessGame.class)
                     );
                 }
             }
        }
        catch (SQLException e){
            throw new DataAccessException("Error getting game", e);
        }
        return null;
    }
}
