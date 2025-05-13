package context;

import chess.ChessGame;
import chess.ChessPosition;
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
        display.printGame(color, game);
        return this;
    }

    private Context highlight() {
        return this;
    }

    private Context move() {
        if (color == null){
            return menu();
        }
        return this;
    }

    private Context resign() {
        if (color == null){
            return menu();
        }
        return this;
    }

    private Context leave() {
        return new PostLogin(server, display);
    }

    private Context menu() {
        String menu;
        if (color == null){
            menu = """
                
                ****OPTIONS****
                Type "help" for options
                Type "redraw" or "r" to redraw the board
                Type "highlight" or "h" to see a piece's moves
                Type "leave" or "l" to leave the game
                Type "pieces" or "p" to toggle pieces or letters
                
                """;
        }
        else {
            menu = """
                
                ****OPTIONS****
                Type "help" for options
                Type "redraw" or "r" to redraw the board
                Type "move" or "m" to move a piece
                Type "resign" to resign
                Type "highlight" or "h" to see a piece's moves
                Type "leave" or "l" to leave the game
                Type "pieces" or "p" to toggle pieces or letters
                
                """;
        }
        display.printText(menu);
        return this;
    }

    @Override
    public void handleError(ErrorMessage error) {
        display.printError(error.getErrorMessage());
    }

    @Override
    public void handleLoadGame(LoadGameMessage load) {
        game = load.getGame();
        display.printText("");
        display.printGame(color, game);
        display.printText("Chess:");
    }

    @Override
    public void handleNotification(NotificationMessage notification) {
        display.printNotification(notification.getMessage());
    }
}
