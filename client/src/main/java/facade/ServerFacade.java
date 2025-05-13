package facade;

import chess.ChessGame;
import chess.ChessMove;
import datamodels.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import response.CreateGameResult;
import response.ListGamesResult;
import response.LoginResult;
import response.Result;
import websocket.commands.*;

import java.io.IOException;
import java.util.ArrayList;

public class ServerFacade {
    private final String url;
    private final HttpCommunicator http = new HttpCommunicator();
    private WSCommunicator ws;

    private String authToken = "";

    public ServerFacade(String url) {
        this.url = url;
    }

    public void register(String username, String password, String email) throws ServerErrorException {
        RegisterRequest request = new RegisterRequest(username, password, email);
        Result result = http.doPost(url + "/user", authToken, request);
        if (result instanceof LoginResult login) {
            authToken = login.getAuthToken();
        } else {
            logToError(result);
            if (http.getLastStatusCode() == 403) {
                throw new ServerErrorException("Username already taken");
            }
            throw new ServerErrorException(result.getMessage());
        }
    }

    public void login(String username, String password) throws ServerErrorException {
        LoginRequest request = new LoginRequest(username, password);
        Result result = http.doPost(url + "/session", authToken, request);
        if (result instanceof LoginResult login) {
            authToken = login.getAuthToken();
        } else {
            logToError(result);
            if (http.getLastStatusCode() == 401) {
                throw new ServerErrorException("Username or password was incorrect");
            }
            throw new ServerErrorException(result.getMessage());
        }
    }

    public void logout() throws ServerErrorException {
        Result result = http.doDelete(url + "/session", authToken);
        authToken = "";
        if (http.getLastStatusCode() != 200) {
            logToError(result);
            throw new ServerErrorException("Logout was unsuccessful");
        }
    }

    public int createGame(String name) throws ServerErrorException {
        CreateGameRequest request = new CreateGameRequest(name, "");
        Result result = http.doPost(url + "/game", authToken, request);
        if (result instanceof CreateGameResult create) {
            return create.getGameID();
        }
        logToError(result);
        if (http.getLastStatusCode() == 401) {
            authToken = "";
            throw new ServerErrorException("Authorization expired");
        }
        throw new ServerErrorException(result.getMessage());
    }

    public ArrayList<GameData> listGames() throws ServerErrorException {
        Result result = http.doGet(url + "/game", authToken);
        if (result instanceof ListGamesResult list) {
            return list.getGames();
        }
        logToError(result);
        if (http.getLastStatusCode() == 401) {
            authToken = "";
            throw new ServerErrorException("Authorization expired");
        }
        throw new ServerErrorException(result.getMessage());
    }

    public void joinGame(ChessGame.TeamColor color, int gameID) throws ServerErrorException {
        JoinGameRequest request = new JoinGameRequest(color, gameID, "");
        Result result = http.doPut(url + "/game", authToken, request);
        if (http.getLastStatusCode() != 200) {
            logToError(result);
        }
        switch (http.getLastStatusCode()) {
            case 200:
                return;
            case 403:
                throw new ServerErrorException("Color already taken");
            case 401:
                authToken = "";
                throw new ServerErrorException("Authorization expired");
            default:
                throw new ServerErrorException(result.getMessage());
        }
    }

    public boolean isNotAuthorized() {
        return authToken.isEmpty();
    }

    private void logToError(Result result) {
        System.err.println("Server Error: " + http.getLastStatusCode() + " " + result.getMessage());
    }
    private void logToError(Exception e){
        System.err.print(e.getMessage());
        e.printStackTrace(System.err);
    }

    public void connect(int gameID, WsMessageHandler handler) throws ServerErrorException{
        try {
            ws = new WSCommunicator(url.replace("http", "ws"), handler);
            String message = new ConnectCommand(authToken, gameID).toString();
            ws.send(message);
        }
        catch (Exception e){
            logToError(e);
            ws = null;
            throw new ServerErrorException("Could not connect to game", e);
        }
    }

    public void leave(int gameID) throws ServerErrorException {
        sendCommand(new LeaveCommand(authToken, gameID));
    }

    public void resign(int gameID) throws ServerErrorException {
        sendCommand(new ResignCommand(authToken, gameID));
    }

    public void makeMove(int gameID, ChessMove move) throws ServerErrorException {
        sendCommand(new MakeMoveCommand(authToken, gameID, move));
    }

    private void sendCommand(UserGameCommand command)throws ServerErrorException{
        if (ws == null){
            throw new ServerErrorException("Could not connect to game");
        }
        try{
            ws.send(command.toString());
        } catch (IOException e) {
            throw new ServerErrorException("Could not connect to game", e);
        }
        finally {
            ws = null;
        }
    }
}
