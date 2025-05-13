package context;

import chess.ChessGame;
import facade.ServerFacade;
import facade.WsMessageHandler;
import ui.Display;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class GamePlay implements Context, WsMessageHandler {
    ServerFacade server;
    Display display;
    ChessGame.TeamColor color;
    ChessGame game;

    public GamePlay(ServerFacade facade, Display display, ChessGame.TeamColor color) {
        this.display = display;
        this.server = facade;
        this.color = color;
    }

    @Override
    public Context eval(String command) {
        return switch (command) {
            case "redraw", "r" -> redraw();
            case "highlight", "h" -> highlight();
            case "move", "m" -> move();
            case "resign" -> resign();
            case "leave", "l" -> leave();
            default -> menu();
        };
    }

    private Context redraw() {
        return this;
    }

    private Context highlight() {
        return this;
    }

    private Context move() {
        return this;
    }

    private Context resign() {
        return this;
    }

    private Context leave() {
        return new PostLogin(server, display);
    }

    private Context menu() {
        return this;
    }

    @Override
    public void handleError(ErrorMessage error) {
        display.printError(error.getErrorMessage());
    }

    @Override
    public void handleLoadGame(LoadGameMessage load) {

    }

    @Override
    public void handleNotification(NotificationMessage notification) {

    }
}
