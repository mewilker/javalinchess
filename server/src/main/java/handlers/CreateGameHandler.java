package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import requests.CreateGameRequest;
import response.Result;
import services.CreateGameService;

public class CreateGameHandler implements Handler {
    private final CreateGameService service;
    public CreateGameHandler(GameDAO gameDAO){
        service = new CreateGameService(gameDAO);
    }


    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        CreateGameRequest request = new Gson().fromJson(context.body(), CreateGameRequest.class);
        Result result = new Result();
        if (request.getGameName() == null){
            context.status(400);
            result.setMessage("Error: bad request");
            context.json(new Gson().toJson(result));
            return;
        }
        result = service.createGame(request);
        context.status(200);
        context.json(new Gson().toJson(result));
    }
}
