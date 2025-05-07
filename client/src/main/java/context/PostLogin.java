package context;

import chess.ChessGame;
import datamodels.GameData;
import facade.ServerErrorException;
import facade.ServerFacade;
import ui.Display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class PostLogin implements Context {
    private final ServerFacade server;
    private final Display display;
    private final HashMap<Integer, GameData> gameStorage = new HashMap<>();
    private int nextID = 1;

    public PostLogin(ServerFacade server, Display display) {
        this.server = server;
        this.display = display;
        menu();
        try {
            populateGames();
        } catch (ServerErrorException e) {
            display.printError(e.getMessage());
        }
    }

    @Override
    public Context eval(String command) {
        return switch (command) {
            case "list", "refresh", "r" -> listGames();
            case "create", "c" -> createGame();
            case "join", "j", "play", "p" -> playGame();
            case "observe", "o" -> observeGame();
            case "logout", "l" -> logout();
            default -> menu();
        };
    }

    private Context listGames() {
        try {
            populateGames();
            display.printGamesTable(gameStorage);
        } catch (ServerErrorException e) {
            display.printError(e.getMessage());
            if (server.isNotAuthorized()) {
                return new PreLogin(display, server);
            }
        }
        return this;
    }

    private Context createGame() {
        String name = display.stringField("Game Name");
        try {
            int gameID = server.createGame(name);
            gameStorage.put(nextID, new GameData(gameID, name, null, null, new ChessGame()));
            display.printNotification("Created game " + name + "! Join using #" + nextID + "!");
            nextID++;
        } catch (ServerErrorException e) {
            display.printError(e.getMessage());
            if (server.isNotAuthorized()) {
                return new PreLogin(display, server);
            }
        }
        return this;
    }

    private Context playGame() {
        try {
            int number = validateGameNumber();
            ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(
                    display.stringField("Side Color").toUpperCase(Locale.ROOT));
            server.joinGame(color, number);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage().contains("Could not find game") ? e.getMessage() : "Not a valid color";
            display.printError(errorMessage);
            return this;
        } catch (ServerErrorException e) {
            display.printError(e.getMessage());
            if (server.isNotAuthorized()) {
                return new PreLogin(display, server);
            }
            return this;
        }
        //TODO: WSFACADE AND CHANGE CONTEXT TO PLAYGAME
        return this;
    }

    private Context observeGame() {
        try {
            int number = validateGameNumber();
        } catch (IllegalArgumentException e) {
            display.printError(e.getMessage());
            //return this;
        }
        //TODO: WS FACADE AND CHANGE CONTEXT TO PLAYGAME
        return this;
    }

    private int validateGameNumber() {
        int number = display.numberEntryField("Game Number");
        if (gameStorage.get(number) == null) {
            throw new IllegalArgumentException("Could not find game");
        }
        return number;
    }

    private Context logout() {
        try {
            server.logout();
            display.printNotification("Logout successful");
        } catch (ServerErrorException e) {
            display.printError(e.getMessage());
        }
        return new PreLogin(display, server);
    }

    private Context menu() {
        String menu = """
                
                ****OPTIONS****
                Type "help" for options
                Type "logout" or "l" to logout
                Type "create" or "c" to make a new game
                Type "refresh", "r" or "list" to see the list of existing games
                Type "join", "j", "play" or "p" to play a game
                Type "observe" or "o" to watch a game
                Type "quit" to exit
                
                """;
        display.printText(menu);
        return this;
    }

    private void populateGames() throws ServerErrorException {
        gameStorage.clear();
        ArrayList<GameData> games = server.listGames();
        int index = 1;
        for (GameData game : games) {
            gameStorage.put(index, game);
            index++;
        }
        nextID = index;
    }
}
