package response;

public class CreateGameResult extends Result{
    private final int gameID;

    public CreateGameResult (int gameID){
        this.gameID = gameID;
    }

    public int getGameID(){
        return gameID;
    }
}
