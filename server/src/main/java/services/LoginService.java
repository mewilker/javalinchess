package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import datamodels.AuthData;
import datamodels.UserData;
import requests.LoginRequest;
import response.RegisterResult;
import response.Result;

public class LoginService {
    UserDAO userDB;
    AuthDAO authDB;

    public LoginService (UserDAO user, AuthDAO auth){
        userDB = user;
        authDB = auth;
    }

    public Result login(LoginRequest request) throws DataAccessException {
        UserData user = userDB.getUser(request.getUsername());
        Result unauthorized = new Result();
        unauthorized.setMessage("Error: unauthorized access");
        if (user == null || !request.getPassword().equals(user.password())){
            return unauthorized;
        }
        AuthData newAuth = authDB.insertAuth(request.getUsername());
        return new RegisterResult(newAuth.authToken(), newAuth.username());
    }
}
