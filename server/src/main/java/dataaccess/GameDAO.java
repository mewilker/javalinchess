package dataaccess;

import datamodels.GameData;

import java.util.ArrayList;

public interface GameDAO {
    int insertGame(String gameName)throws DataAccessException;
    ArrayList<GameData> getAllGames()throws DataAccessException;
    void updateGame(GameData game)throws DataAccessException;
    void clear()throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
}
