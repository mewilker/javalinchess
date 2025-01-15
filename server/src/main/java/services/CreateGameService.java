package services;

import dataaccess.GameDAO;
import requests.CreateGameRequest;
import response.CreateGameResult;
import response.Result;

public class CreateGameService {
    private final GameDAO gameDB;
    public CreateGameService (GameDAO gameDAO){
        gameDB = gameDAO;
    }

    public Result createGame (CreateGameRequest request){
        int gameID = gameDB.insertGame(request.getGameName());
        return new CreateGameResult(gameID);
    }
}
