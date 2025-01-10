package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import response.Result;
import services.ClearService;

public class ClearHandler implements Handler {
    ClearService service;

    public ClearHandler(UserDAO user, AuthDAO auth, GameDAO game){
        service = new ClearService(user, auth, game);
    }

    @Override
    public void handle(@NotNull Context context){
        try{
            service.clear();
            context.status(200);
            context.json("{}");
        } catch (DataAccessException e){
            context.status(500);
            Result result = new Result();
            result.setMessage(e.getMessage());
            context.json(new Gson().toJson(result));
        }
    }
}
