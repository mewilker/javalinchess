package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.LoginRequest;
import requests.RegisterRequest;
import response.Result;
import services.LoginService;

public class LoginHandler implements Handler {
    private final LoginService service;
    public LoginHandler(UserDAO userDAO, AuthDAO authDAO){
        service = new LoginService(userDAO, authDAO);
    }

    @Override
    public void handle(@NotNull Context context) throws DataAccessException{
        LoginRequest request = new Gson().fromJson(context.body(), LoginRequest.class);
        Result result = new Result();
        if (request.getPassword()==null || request.getUsername() == null) {
            context.status(400);
            result.setMessage("Error: bad request");
            context.json(new Gson().toJson(result));
            return;
        }
        result = service.login(request);
        if (result.getMessage() != null) {
            context.status(401);
            result.setMessage("Error: unauthorized access");
        }
        else{
            context.status(200);
        }
        context.json(new Gson().toJson(result));
    }
}
