package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.RegisterRequest;
import response.Result;
import services.RegisterService;

import java.util.Objects;

public class RegisterHandler implements Handler {
    RegisterService service;
    public RegisterHandler(UserDAO user, AuthDAO auth){
        service = new RegisterService(user, auth);
    }

    @Override
    public void handle(@NotNull Context context) throws DataAccessException{
        RegisterRequest request = new Gson().fromJson(context.body(), RegisterRequest.class);
        Result result = new Result();
        if (request.getEmail() == null || request.getPassword() == null || request.getUsername()==null){
            context.status(400);
            result.setMessage("Error: Bad Request");
            context.json(new Gson().toJson(result));
            return;
        }
        result = service.register(request);
        if(result.getMessage() != null){
            context.status(403);
        }
        else{
            context.status(200);
        }
        context.json(new Gson().toJson(result));
    }
}
