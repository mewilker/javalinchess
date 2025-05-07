package dataaccess;

import chess.ChessGame;
import datamodels.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemGameDAO implements GameDAO {
    private static final HashMap<Integer, GameData> GAME_DB = new HashMap<>();
    private static int nextGameID = 1;

    @Override
    public int insertGame(String gameName) {
        int id = nextGameID;
        nextGameID++;
        GAME_DB.put(id, new GameData(id, gameName, null, null, new ChessGame()));
        return id;
    }

    @Override
    public ArrayList<GameData> getAllGames() {
        return new ArrayList<>(GAME_DB.values());
    }

    public GameData getGame(int gameID) {
        return GAME_DB.get(gameID);
    }

    @Override
    public void updateGame(GameData game) {
        GAME_DB.put(game.gameID(), game);
    }

    @Override
    public void clear() {
        GAME_DB.clear();
    }
}
