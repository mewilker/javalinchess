package requests;

import chess.ChessGame;

public class JoinGameRequest {
    private final ChessGame.TeamColor playerColor;
    private final int gameID;
    private String authToken;

    public JoinGameRequest (ChessGame.TeamColor color, int id, String token){
        playerColor = color;
        gameID = id;
        authToken = token;
    }

    public ChessGame.TeamColor getPlayerColor(){
        return playerColor;
    }

    public int getGameID(){
        return gameID;
    }

    public String getAuthToken(){
        return authToken;
    }

}
