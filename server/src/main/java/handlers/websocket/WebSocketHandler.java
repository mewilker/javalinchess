package handlers.websocket;


import chess.ChessGame;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import datamodels.AuthData;
import datamodels.GameData;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.jetbrains.annotations.NotNull;
import serialization.TypeAdapters;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessGame.TeamColor.BLACK;

public class WebSocketHandler implements WsMessageHandler {
    private final AuthDAO authDB;
    private final GameDAO gameDB;
    private final WSConnectionManager connectionManager = new WSConnectionManager();

    public WebSocketHandler(AuthDAO auth, GameDAO game) {
        authDB = auth;
        gameDB = game;
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(UserGameCommand.class, TypeAdapters.commandDeserializer())
                .registerTypeAdapter(ChessPiece.class, TypeAdapters.pieceDeserializer())
                .create();
        UserGameCommand command = gson.fromJson(wsMessageContext.message(), UserGameCommand.class);
        AuthData auth = authDB.getAuth(command.getAuthToken());
        if (auth == null) {
            wsMessageContext.send(new ErrorMessage("Unauthorized").toString());
            return;
        }
        GameData lobby = gameDB.getGame(command.getGameID());
        if (lobby == null){
            wsMessageContext.send(new ErrorMessage("Game does not exist").toString());
            return;
        }
        switch (command.getCommandType()) {
            case LEAVE -> leave((LeaveCommand) command, wsMessageContext, lobby, auth);
            case RESIGN -> resign((ResignCommand) command, wsMessageContext, lobby, auth);
            case CONNECT -> connect((ConnectCommand) command, wsMessageContext, lobby, auth);
            case MAKE_MOVE -> move((MakeMoveCommand) command, wsMessageContext, lobby, auth);
        }
    }

    private void connect(ConnectCommand command, WsMessageContext context, GameData lobby, AuthData auth)
            throws DataAccessException {
        connectionManager.addConnection(lobby.gameID(), context);
        String user = auth.username();
        String message = user + " has entered as ";
        if (user.equals(lobby.whiteUsername())){
            message = message + "the white player!";
        }
        else if(user.equals(lobby.blackUsername())){
            message = message + "the black player!";
        }
        else {
            message = message + "an observer!";
        }
        context.send(new LoadGameMessage(lobby.game()).toString());
        connectionManager.broadcastWithExclusion(new NotificationMessage(message).toString(),
                lobby.gameID(), context);
    }

    private void leave(LeaveCommand command, WsMessageContext context, GameData lobby, AuthData auth)
            throws DataAccessException{
        if (auth.username().equals(lobby.blackUsername())){
            gameDB.updateGame(new GameData(lobby.gameID(),
                    lobby.gameName(),
                    lobby.whiteUsername(),
                    null,
                    lobby.game()));
        }
        if (auth.username().equals(lobby.whiteUsername())){
            gameDB.updateGame(new GameData(lobby.gameID(),
                    lobby.gameName(),
                    null,
                    lobby.blackUsername(),
                    lobby.game()));
        }
        connectionManager.removeConnection(lobby.gameID(), context);
        connectionManager.broadcast(new NotificationMessage(auth.username() + " has left").toString(), lobby.gameID());

    }

    private void resign(ResignCommand command, WsMessageContext context, GameData lobby, AuthData auth)
            throws DataAccessException{
    }

    private void move(MakeMoveCommand command, WsMessageContext context, GameData lobby, AuthData auth)
            throws DataAccessException{
        ChessGame game = lobby.game();
        ChessGame.TeamColor turn = game.getTeamTurn();
        if (turn == WHITE && !auth.username().equals(lobby.whiteUsername()) ||
                turn == BLACK && !auth.username().equals(lobby.blackUsername())){
            if (!auth.username().equals(lobby.blackUsername()) && !auth.username().equals(lobby.whiteUsername())){
                context.send(new ErrorMessage("Observers cannot move pieces").toString());
                return;
            }

            context.send(new ErrorMessage("Cannot make move out of turn").toString());
            return;
        }
        try{
            game.makeMove(command.getMove());
            gameDB.updateGame(new GameData(lobby.gameID(),
                    lobby.gameName(),
                    lobby.whiteUsername(),
                    lobby.blackUsername(),
                    game));
            connectionManager.broadcast(new LoadGameMessage(game).toString(), lobby.gameID());
            String notification = auth.username() + " moved " + command.getMove().toString();
            connectionManager.broadcastWithExclusion(new NotificationMessage(notification).toString(),
                    lobby.gameID(), context);
            if (game.isInCheck(game.getTeamTurn())){
                if (game.isInCheckmate(game.getTeamTurn())){
                    notification = "Checkmate! " + getUsernameOfColor(game.getTeamTurn(), lobby) + " loses, " +
                            getUsernameOfColor(game.getEnemyColor(game.getTeamTurn()), lobby) + " wins!";
                }
                else{
                    notification = getUsernameOfColor(game.getTeamTurn(), lobby) + " is in Check!";
                }
                connectionManager.broadcast(new NotificationMessage(notification).toString(), lobby.gameID());
            }
            else if (game.isInStalemate(game.getTeamTurn())){
                notification = "Stalemate! No one wins!";
                connectionManager.broadcast(notification, lobby.gameID());
            }
        } catch (InvalidMoveException e) {
            context.send(new ErrorMessage("Given move was invalid").toString());
        }
    }

    private String getUsernameOfColor(ChessGame.TeamColor color, GameData data){
        return switch (color){
            case BLACK -> data.blackUsername();
            case WHITE -> data.whiteUsername();
        };
    }
}
