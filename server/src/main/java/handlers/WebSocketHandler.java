package handlers;


import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.jetbrains.annotations.NotNull;
import serialization.TypeAdapters;
import websocket.commands.*;
import websocket.messages.ErrorMessage;

public class WebSocketHandler implements WsMessageHandler {
    private final AuthDAO authDB;
    private final GameDAO gameDB;

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
        if (authDB.getAuth(command.getAuthToken()) == null) {
            wsMessageContext.send(new ErrorMessage("Unauthorized").toString());
            return;
        }
        switch (command.getCommandType()) {
            case LEAVE -> leave((LeaveCommand) command);
            case RESIGN -> resign((ResignCommand) command);
            case CONNECT -> connect((ConnectCommand) command);
            case MAKE_MOVE -> move((MakeMoveCommand) command);
        }
    }

    private void connect(ConnectCommand command) {
    }

    private void leave(LeaveCommand command) {
    }

    private void resign(ResignCommand command) {
    }

    private void move(MakeMoveCommand command) {
    }
}
