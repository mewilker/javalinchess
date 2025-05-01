package response;

public class LoginResult extends Result{
    String username;
    String authToken;

    public LoginResult(String authToken, String username){
        this.authToken = authToken;
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public String getAuthToken(){
        return authToken;
    }
}
