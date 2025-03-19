package dataaccess;

import chess.ChessGame;
import datamodels.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemGameDAO implements GameDAO{
    private static final HashMap<Integer, GameData> gameDB = new HashMap<>();
    private static int nextGameID = 1;

    @Override
    public int insertGame(String gameName) {
        int id = nextGameID;
        nextGameID++;
        gameDB.put(id, new GameData(id,gameName, null, null, new ChessGame()));
        return id;
    }

    @Override
    public ArrayList<GameData> getAllGames() {
        return new ArrayList<>(gameDB.values());
    }

    public GameData getGame(int gameID){
        return gameDB.get(gameID);
    }

    @Override
    public void updateGame(GameData game) {
        gameDB.put(game.gameID(), game);
    }

    @Override
    public void clear() {
        gameDB.clear();
    }
}
