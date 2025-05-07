package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import response.Result;
import services.ListGamesService;

public class ListGamesHandler implements Handler {
    private final ListGamesService service;

    public ListGamesHandler(GameDAO gameDAO) {
        service = new ListGamesService(gameDAO);
    }


    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        Result result = service.list();
        context.status(200);
        context.json(new Gson().toJson(result));
    }
}
