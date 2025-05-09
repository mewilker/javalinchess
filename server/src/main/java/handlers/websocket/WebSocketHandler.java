package handlers.websocket;


import chess.ChessPiece;
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
    }
}
