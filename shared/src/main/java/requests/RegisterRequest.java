package requests;

public class RegisterRequest extends LoginRequest{
    private final String email;

    public RegisterRequest(String username, String password, String email) {
        super(username, password);
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
}
