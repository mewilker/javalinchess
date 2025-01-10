package response;

public class RegisterResult extends Result{
    String username;
    String authToken;

    public RegisterResult(String authToken, String username){
        this.authToken = authToken;
        this.username = username;
    }
}
