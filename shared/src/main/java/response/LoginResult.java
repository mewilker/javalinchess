package response;

public class LoginResult extends Result {
    private final String username;
    private final String authToken;

    public LoginResult(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
