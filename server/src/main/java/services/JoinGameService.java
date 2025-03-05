package services;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import requests.JoinGameRequest;
import response.Result;

public class JoinGameService {
    private final GameDAO gameDB;
    private final AuthDAO authDB;
    private final UserDAO userDB;

    public JoinGameService(UserDAO user, AuthDAO auth, GameDAO game) {
        userDB = user;
        authDB = auth;
        gameDB = game;
    }

    public Result joinGame(JoinGameRequest request){
        gameDB.getGame(request.getGameID());
        return null;
    }
}
