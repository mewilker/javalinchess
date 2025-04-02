package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import datamodels.AuthData;
import requests.LogoutRequest;
import response.Result;

public class LogoutService {

    private final AuthDAO authDB;

    public LogoutService(AuthDAO authDAO){
        authDB = authDAO;
    }

    public Result logout (LogoutRequest request) throws DataAccessException {
        Result result = new Result();
        AuthData auth = authDB.getAuth(request.getAuthToken());
        if (auth != null){
            authDB.deleteAuth(request.getAuthToken());
            return result;
        }
        result.setMessage("Error: unauthorized");
        return result;
    }
}
