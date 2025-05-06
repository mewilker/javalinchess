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
}
