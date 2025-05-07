package services;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datamodels.GameData;
import response.ListGamesResult;
import response.Result;

import java.util.ArrayList;

public class ListGamesService {
    private final GameDAO gameDB;

    public ListGamesService(GameDAO gameDAO) {
        gameDB = gameDAO;
    }

    public Result list() throws DataAccessException {
        ArrayList<GameData> gameList = gameDB.getAllGames();
        return new ListGamesResult(gameList);
    }
}
