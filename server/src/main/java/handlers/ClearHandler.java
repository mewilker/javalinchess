package handlers;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import services.ClearService;

public class ClearHandler implements Handler {
    private final ClearService service;

    public ClearHandler(UserDAO user, AuthDAO auth, GameDAO game) {
        service = new ClearService(user, auth, game);
    }

    @Override
    public void handle(@NotNull Context context) throws DataAccessException {
        service.clear();
        context.status(200);
        context.json("{}");
    }
}
