package dataaccess;

import datamodels.GameData;

public interface GameDAO {
    public int insertGame(String gameName);
    public GameData getGame(int gameID)throws DataAccessException;
    public void updateGame(GameData game)throws DataAccessException;
    public void clear()throws DataAccessException;
}
