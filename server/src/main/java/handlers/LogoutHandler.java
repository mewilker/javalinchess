package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.LogoutRequest;
import response.Result;
import services.LogoutService;

public class LogoutHandler implements Handler {
    private final LogoutService service;
    public LogoutHandler(AuthDAO authDAO){
        service = new LogoutService(authDAO);
    }
    
    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        LogoutRequest request = new LogoutRequest(context.header("authorization"));
        Result result = service.logout(request);
        if (result.getMessage() != null){
            context.status(401);
        }
        else {
            context.status(200);
        }
        context.json(new Gson().toJson(result));
    }
}
