package response;

import datamodels.GameData;

import java.util.ArrayList;

public class ListGamesResult extends Result{
    private final ArrayList<GameData> games;
    public ListGamesResult(ArrayList<GameData> games){
        this.games = games;
    }

    public ArrayList<GameData> getGames(){
        return games;
    }
}
