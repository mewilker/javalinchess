package dataaccess;

import datamodels.GameData;

import java.util.ArrayList;

public interface GameDAO {
    public int insertGame(String gameName)throws DataAccessException;
    public ArrayList<GameData> getAllGames()throws DataAccessException;
    public void updateGame(GameData game)throws DataAccessException;
    public void clear()throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
}
