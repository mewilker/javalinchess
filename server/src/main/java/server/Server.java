package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.*;
import datamodels.AuthData;
import handlers.*;
import handlers.websocket.WebSocketHandler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import response.Result;
import websocket.messages.ErrorMessage;


public class Server {
    private final Javalin javalin = Javalin.create(config -> config.staticFiles.add("/web"));
    UserDAO userDB = new MemUserDAO();
    AuthDAO authDB = new MemAuthDAO();
    GameDAO gameDB = new MemGameDAO();

    public Server() {
        try {
            DatabaseManager.createDatabase();
            userDB = new SQLUserDAO();
            authDB = new SQLAuthDAO();
            gameDB = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        WebSocketHandler handler = new WebSocketHandler(authDB, gameDB);
        javalin.ws("/ws", wsConfig -> {
            wsConfig.onMessage(handler);
            wsConfig.onClose(handler);
            wsConfig.onError(ctx -> {
                if (ctx.error() != null) {
                    System.err.print(ctx.error().getMessage());
                    ctx.error().printStackTrace(System.err);
                }
                ctx.send(new ErrorMessage("There was a problem with the server. Please try again.").toString());
            });
        });
        // Register your endpoints and handle exceptions here.
        javalin.delete("/db", new ClearHandler(userDB, authDB, gameDB));
        javalin.post("/user", new RegisterHandler(userDB, authDB));
        javalin.post("/session", new LoginHandler(userDB, authDB));
        javalin.delete("/session", new LogoutHandler(authDB));
        javalin.before("/game", this::authCheck);
        javalin.get("/game", new ListGamesHandler(gameDB));
        javalin.post("/game", new CreateGameHandler(gameDB));
        javalin.put("/game", new JoinGameHandler(gameDB, authDB));

        javalin.exception(DataAccessException.class, (e, ctx) -> {
            e.printStackTrace(System.err);
            ctx.status(500);
            Result result = new Result();
            result.setMessage("There was an error with the server. Please try again later.");
            ctx.json(new Gson().toJson(result));
        });

        javalin.exception(UnauthorizedResponse.class, (e, ctx) -> {
            ctx.status(401);
            Result result = new Result();
            result.setMessage(e.getMessage());
            ctx.json(new Gson().toJson(result));
        });
        javalin.exception(JsonSyntaxException.class, (e, ctx) -> {
            ctx.status(400);
            Result result = new Result();
            result.setMessage("Error: bad request");
            ctx.json(new Gson().toJson(result));
        });

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public void authCheck(Context context) throws DataAccessException {
        String token = context.header("Authorization");
        AuthData auth = authDB.getAuth(token);
        if (auth == null) {
            throw new UnauthorizedResponse("Error: unauthorized access");
        }
    }
}
