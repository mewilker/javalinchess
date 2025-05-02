package facade;

import chess.ChessGame;
import datamodels.GameData;
import requests.*;
import response.*;

import java.util.ArrayList;

public class ServerFacade {
    private final String url;
    private final HttpCommunicator http = new HttpCommunicator();

    private String authToken = "";

    public ServerFacade(String url){
        this.url = url;
    }

    public void register(String username, String password, String email) throws ServerErrorException {
        RegisterRequest request = new RegisterRequest(username, password, email);
        Result result = http.doPost(url + "/user", authToken, request);
        if (result instanceof LoginResult login){
            authToken = login.getAuthToken();
        }
        else{
            //TODO: log to standard err
            if (http.getLastStatusCode() == 403){
                throw new ServerErrorException("Username already taken");
            }
            throw new ServerErrorException(result.getMessage());
        }
    }

    public void login(String username, String password) throws ServerErrorException{
        LoginRequest request = new LoginRequest(username, password);
        Result result = http.doPost(url + "/session", authToken, request);
        if (result instanceof LoginResult login){
            authToken = login.getAuthToken();
        }
        else{
            //TODO: log to standard err
            if (http.getLastStatusCode() == 401){
                throw new ServerErrorException("Username or password was incorrect");
            }
            throw new ServerErrorException(result.getMessage());
        }
    }

    public void logout() throws ServerErrorException {
        http.doDelete(url + "/session", authToken);
        authToken = "";
        if (http.getLastStatusCode() != 200){
            //TODO: log to standard err
            throw new ServerErrorException("Logout was unsuccessful");
        }
    }

    public int createGame(String name) throws ServerErrorException{
        CreateGameRequest request = new CreateGameRequest(name, "");
        Result result = http.doPost(url + "/game", authToken, request);
        if (result instanceof CreateGameResult create){
            return create.getGameID();
        }
        //TODO: log to standard err
        if(http.getLastStatusCode() == 401){
            authToken = "";
            throw new ServerErrorException("Authorization expired");
        }
        throw new ServerErrorException(result.getMessage());
    }

    public ArrayList<GameData> listGames() throws ServerErrorException{
        Result result = http.doGet(url + "/game", authToken);
        if (result instanceof ListGamesResult list){
            return list.getGames();
        }
        if (http.getLastStatusCode() == 401){
            authToken = "";
            throw new ServerErrorException("Authorization expired");
        }
        throw new ServerErrorException(result.getMessage());
    }

    public void joinGame(ChessGame.TeamColor color, int gameID) throws ServerErrorException{
        JoinGameRequest request = new JoinGameRequest(color, gameID, "");
        Result result = http.doPut(url + "/game", authToken, request);
        switch (http.getLastStatusCode()){
            case 200 : return;
            case 403 : throw new ServerErrorException("Color already taken");
            case 401 : authToken = "";
                throw new ServerErrorException("Authorization expired");
            default: throw new ServerErrorException(result.getMessage());
        }
    }
}
