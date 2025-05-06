package context;

import facade.ServerFacade;
import ui.Display;

public class PostLogin implements Context{
    ServerFacade server;
    Display display;

    public PostLogin(ServerFacade server, Display display){
        this.server = server;
        this.display = display;
    }

    @Override
    public Context eval(String command) {
        return this;
    }

    private Context listGames(){
        return this;
    }

    private Context createGame(){
        return this;
    }

    private Context playGame(){
        return this;
    }

    private Context observeGame(){
        return this;
    }

    private Context logout(){
        return new PreLogin(display, server);
    }
}
