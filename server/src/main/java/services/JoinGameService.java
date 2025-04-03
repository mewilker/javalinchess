package services;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datamodels.AuthData;
import datamodels.GameData;
import requests.JoinGameRequest;
import response.Result;

public class JoinGameService {
    private final GameDAO gameDB;
    private final AuthDAO authDB;

    public JoinGameService(AuthDAO auth, GameDAO game) {
        authDB = auth;
        gameDB = game;
    }

    public Result joinGame(JoinGameRequest request) throws DataAccessException {
        Result result = new Result();
        GameData game = gameDB.getGame(request.getGameID());
        if (game == null){
            result.setMessage("Error: bad request");
            return result;
        }
        AuthData auth = authDB.getAuth(request.getAuthToken());
        if (request.getPlayerColor() == ChessGame.TeamColor.WHITE){
            if (game.whiteUsername() == null || game.whiteUsername().equals(auth.username())){
                gameDB.updateGame(new GameData(game.gameID(), game.gameName(), auth.username(), game.blackUsername(),
                        game.game()));
            }
            else {
                result.setMessage("Error: white side already taken");
            }
        }
        else if(game.blackUsername() == null || game.blackUsername().equals(auth.username())){
            gameDB.updateGame(new GameData(game.gameID(), game.gameName(), game.whiteUsername(), auth.username(),
                    game.game()));
        }
        else {
            result.setMessage("Error: black side already taken");
        }
        return result;
    }
}
