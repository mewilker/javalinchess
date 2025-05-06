package context;

import facade.ServerErrorException;
import facade.ServerFacade;
import ui.Display;

public class PreLogin implements Context{
    Display display;
    ServerFacade server;
    PreLogin(Display display, ServerFacade server){
        this.display = display;
        this.server = server;
    }

    @Override
    public Context eval(String command) {
        return switch (command) {
            case "register", "r" -> register();
            case "login", "l" -> login();
            default -> menu();
        };
    }

    public Context register(){
        String username = display.stringField("Username");
        String password = display.stringField("Password");
        String email = display.stringField("Email");
        try{
            server.register(username, password, email);
            return new PostLogin(server, display);
        } catch (ServerErrorException e) {
            display.printError(e.getMessage());
        }
        return this;
    }

    public Context login(){
        String username = display.stringField("Username");
        String password = display.stringField("Password");
        try{
            server.login(username, password);
            return new PostLogin(server, display);
        } catch (ServerErrorException e){
            display.printError(e.getMessage());
        }
        return this;
    }

    public Context menu(){
        String menu = """
                ****OPTIONS****
                Type "help" for options
                Type "quit" to exit
                Type "register" or "r" to create an account
                Type "login" or "l" to access an existing account
                """;

        return this;
    }
}
