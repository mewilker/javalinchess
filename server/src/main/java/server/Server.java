package server;

import dataaccess.*;
import datamodels.AuthData;
import handlers.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import response.Result;
import com.google.gson.Gson;

import java.util.Arrays;

public class Server {
    //Spark.staticFiles.location("web");
    Javalin javalin = Javalin.create(config -> config.staticFiles.add("/web"));
    UserDAO userDB = new MemUserDAO();
    AuthDAO authDB = new MemAuthDAO();
    GameDAO gameDB = new MemGameDAO();

    public Server(){
        try{
            DatabaseManager.createDatabase();
            userDB = new SQLUserDAO();
        }
        catch (DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    public int run(int desiredPort) {
        if (desiredPort == 0){
            javalin.start();
        }
        else {
            javalin.start(desiredPort);
        }


        // Register your endpoints and handle exceptions here.
        javalin.delete("/db", new ClearHandler(userDB,authDB,gameDB));
        javalin.post("/user", new RegisterHandler(userDB, authDB));
        javalin.post("/session", new LoginHandler(userDB, authDB));
        javalin.delete("/session", new LogoutHandler(authDB));
        javalin.before("/game", this::authCheck);
        javalin.get("/game", new ListGamesHandler(gameDB));
        javalin.post("/game", new CreateGameHandler(gameDB));

        javalin.exception(DataAccessException.class, (e, ctx)->{
            ctx.status(500);
            Result result = new Result();
            result.setMessage(e.getMessage());
            ctx.json(new Gson().toJson(result));
        });

        javalin.exception(UnauthorizedResponse.class, (e, ctx)->{
            ctx.status(401);
            Result result = new Result();
            result.setMessage(e.getMessage());
            ctx.json(new Gson().toJson(result));
        });
        return javalin.port();
    }

    public void stop() {
        //Spark.stop();
        javalin.stop();
        //Spark.awaitStop();
    }

    public void authCheck(Context context) throws DataAccessException{
        String token = context.header("Authorization");
        AuthData auth = authDB.getAuth(token);
        if (auth == null){
            throw new UnauthorizedResponse("Error: unauthorized access");
        }
    }
}
