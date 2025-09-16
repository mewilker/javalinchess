package context;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import facade.ServerErrorException;
import facade.ServerFacade;
import facade.WsMessageHandler;
import ui.Display;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Locale;

public class GamePlay implements Context, WsMessageHandler {
    ServerFacade server;
    Display display;
    ChessGame.TeamColor color;
    ChessGame game;
    int gameID;

    public GamePlay(ServerFacade facade, Display display, ChessGame.TeamColor color, int id) {
        this.display = display;
        this.server = facade;
        this.color = color;
        this.gameID = id;
    }

    @Override
    public Context eval(String command) {
        return switch (command) {
            case "redraw", "r" -> redraw();
            case "highlight", "h" -> highlight();
            case "move", "m" -> move();
            case "resign" -> resign();
            case "leave", "l" -> leave();
            case "pieces", "p" -> pieces();
            default -> menu();
        };
    }

    private Context redraw() {
        display.printGame(color, game);
        return this;
    }

    private Context highlight() {
        //TODO: testme
        ChessPosition highlight = new ChessPosition(display.stringField("Where to highlight? (format: a1)"));
        display.printGame(color, game, highlight);
        return this;
    }

    private Context move() {
        if (color == null){
            return menu();
        }
        try{
            ChessPosition startPosition = new ChessPosition(display.stringField("Start Position (format: a1)"));
            ChessPosition endPosition = new ChessPosition(display.stringField("End position"));
            ChessMove move = new ChessMove(startPosition, endPosition, null);
            ChessGame copy = new ChessGame(game);
            copy.makeMove(move);
            server.makeMove(gameID, move);
        } catch (NumberFormatException e){
            display.printError("Not a valid position");
        } catch (IllegalArgumentException | ServerErrorException e){
            display.printError(e.getMessage());
        } catch (InvalidMoveException e) {
            display.printError("Move is invalid: " + e.getMessage());
        }
        return this;
    }

    private Context resign() {
        if (color == null){
            return menu();
        }
        display.printError("Are you sure you want to resign?");
        if (!display.stringField("Type \"yes\" to confirm").toLowerCase(Locale.ROOT).equals("yes")){
            return this;
        }
        try{
            server.resign(gameID);
        } catch (ServerErrorException e) {
            display.printError(e.getMessage());
        }
        return this;
    }

    private Context leave() {
        try{
            server.leave(gameID);
        }
        catch (ServerErrorException e){
            display.printError(e.getMessage());
        }
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

    private Context pieces(){
        display.changeDisplayOptions();
        return this;
    }

    @Override
    public void handleError(ErrorMessage error) {
        display.printText("");
        display.printError(error.getErrorMessage());
        display.printText("Chess:");
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
        display.printText("Chess:");
    }
}
