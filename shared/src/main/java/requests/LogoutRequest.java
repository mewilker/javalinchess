package requests;

public class LogoutRequest {
    private final String authToken;

    public LogoutRequest(String authorization) {
        authToken = authorization;
    }

    public String getAuthToken() {
        return authToken;
    }
}
