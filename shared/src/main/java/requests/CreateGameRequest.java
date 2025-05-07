package requests;

public class CreateGameRequest {

    private final String gameName;

    public CreateGameRequest(String gameName, String authToken) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

}
