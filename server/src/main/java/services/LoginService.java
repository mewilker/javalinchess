package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import datamodels.AuthData;
import datamodels.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requests.LoginRequest;
import response.RegisterResult;
import response.Result;

public class LoginService {
    private final UserDAO userDB;
    private final AuthDAO authDB;

    public LoginService (UserDAO user, AuthDAO auth){
        userDB = user;
        authDB = auth;
    }

    public Result login(LoginRequest request) throws DataAccessException {
        UserData user = userDB.getUser(request.getUsername());
        Result unauthorized = new Result();
        unauthorized.setMessage("Error: unauthorized access");
        if (user == null || !BCrypt.checkpw(request.getPassword(), user.password())){
            return unauthorized;
        }
        AuthData newAuth = authDB.insertAuth(request.getUsername());
        return new RegisterResult(newAuth.authToken(), newAuth.username());
    }
}
