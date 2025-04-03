package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.JoinGameRequest;
import response.Result;
import services.JoinGameService;

public class JoinGameHandler implements Handler {

    private final JoinGameService service;
    public JoinGameHandler (GameDAO game, AuthDAO auth){
        service = new JoinGameService(auth, game);
    }

    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        JoinGameRequest request = new Gson().fromJson(context.body(), JoinGameRequest.class);
        request.setAuthToken(context.header("Authorization"));
        Result result = new Result();
        if (request.getGameID() < 1 || request.getPlayerColor() == null){
            context.status(400);
            result.setMessage("Error: bad request");
            context.json(new Gson().toJson(result));
            return;
        }
        result = service.joinGame(request);
        String message = result.getMessage();
        if (message == null){
            context.status(200);
        }
        else if (message.contains("already taken")){
            context.status(403);
        }
        else if(message.contains("bad request")){
            context.status(400);
        }
        context.json(new Gson().toJson(result));
    }
}
