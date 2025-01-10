package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    UserDAO userDB;
    AuthDAO authDB;
    GameDAO gameDB;

    public ClearService(UserDAO user, AuthDAO auth, GameDAO game){
        userDB = user;
        authDB = auth;
        gameDB = game;
    }

    public void clear() throws DataAccessException {
        userDB.clear();
        authDB.clear();
        gameDB.clear();
    }
}
