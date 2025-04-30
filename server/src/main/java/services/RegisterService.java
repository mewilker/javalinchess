package services;

import dataaccess.*;
import datamodels.AuthData;
import datamodels.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requests.RegisterRequest;
import response.RegisterResult;
import response.Result;

public class RegisterService {
    private final UserDAO userDB;
    private final AuthDAO authDB;

    public RegisterService(UserDAO user, AuthDAO auth){
        userDB = user;
        authDB = auth;
    }

    public Result register (RegisterRequest request) throws DataAccessException {
        if (userDB.getUser(request.getUsername()) == null){
            userDB.insertUser(new UserData(
                    request.getUsername(),
                    BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()),
                    request.getEmail()));
            AuthData auth = authDB.insertAuth(request.getUsername());
            return new RegisterResult(auth.authToken(), auth.username());
        }
        Result result = new Result();
        result.setMessage("Error: Username already taken");
        return result;
    }
}
