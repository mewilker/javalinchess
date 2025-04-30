package facade;

import requests.*;
import response.*;

public class ServerFacade {
    private final String url;
    public ServerFacade(String url){
        this.url = url;
    }

    public Result register(RegisterRequest request){
        return null;
    }

    public Result login(LoginRequest request){
        return null;
    }

    public Result logout(LogoutRequest request){
        return null;
    }

    public Result createGame(CreateGameRequest request){
        return null;
    }

    public Result listGames(){
        return null;
    }

    public Result joinGame(JoinGameRequest request){
        return null;
    }
}
