package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datamodels.AuthData;
import datamodels.GameData;
import requests.JoinGameRequest;
import response.Result;

public class JoinGameService {
    private final GameDAO gameDB;
    private final AuthDAO authDB;

    public JoinGameService(AuthDAO auth, GameDAO game) {
        authDB = auth;
        gameDB = game;
    }

    public Result joinGame(JoinGameRequest request) throws DataAccessException {
        GameData game = gameDB.getGame(request.getGameID());
        if (game == null){
            //bad request
        }
        AuthData auth = authDB.getAuth(request.getAuthToken());
        return null;
    }
}
