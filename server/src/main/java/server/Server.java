package server;

import io.javalin.Javalin;

public class Server {
    Javalin javalin = Javalin.create(config ->{config.staticFiles.add("/web");});

    public int run(int desiredPort) {
        //Spark.port(desiredPort);
        if (desiredPort == 0){
            javalin.start();
        }
        else {
            javalin.start(desiredPort);
        }

        //Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        javalin.delete("/db", context -> {
            context.body();
        });

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        //Spark.awaitInitialization();
        //return Spark.port();
        return javalin.port();
    }

    public void stop() {
        //Spark.stop();
        javalin.stop();
        //Spark.awaitStop();
    }
}
