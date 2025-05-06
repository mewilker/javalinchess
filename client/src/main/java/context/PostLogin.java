package context;

import datamodels.GameData;
import facade.ServerErrorException;
import facade.ServerFacade;
import ui.Display;

import java.util.ArrayList;
import java.util.HashMap;

public class PostLogin implements Context{
    ServerFacade server;
    Display display;
    int nextID = 1;
    HashMap<Integer, GameData> gameStorage;

    public PostLogin(ServerFacade server, Display display){
        this.server = server;
        this.display = display;
    }

    @Override
    public Context eval(String command) {
        return switch (command){
            case "list", "refresh", "r" -> listGames();
            case "create", "c" -> createGame();
            case "join", "j", "play", "p" -> playGame();
            case "observe", "o" -> observeGame();
            case "logout", "l" -> logout();
            default -> menu();
        };
    }

    private Context listGames(){
        return this;
    }

    private Context createGame(){
        return this;
    }

    private Context playGame(){
        return this;
    }

    private Context observeGame(){
        return this;
    }

    private Context logout(){
        try {
            server.logout();
        }
        catch (ServerErrorException e){
            display.printError(e.getMessage());
        }
        return new PreLogin(display, server);
    }

    private Context menu(){
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

    private void populateGames() throws ServerErrorException{
        ArrayList<GameData> games = server.listGames();
        int index = 1;
        for(GameData game : games){
            gameStorage.put(index, game);
            index++;
        }
        nextID = index;
    }
}
