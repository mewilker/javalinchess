package server;

import dataaccess.*;
import handlers.ClearHandler;
import handlers.LoginHandler;
import handlers.LogoutHandler;
import handlers.RegisterHandler;
import io.javalin.Javalin;
import response.Result;
import com.google.gson.Gson;

public class Server {
    //Spark.staticFiles.location("web");
    Javalin javalin = Javalin.create(config ->{config.staticFiles.add("/web");});
    UserDAO userDB = new MemUserDAO();
    AuthDAO authDB = new MemAuthDAO();
    GameDAO gameDB = new MemGameDAO();

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

        javalin.exception(DataAccessException.class, (e, ctx)->{
            ctx.status(500);
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
}
