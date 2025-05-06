package context;

import facade.ServerFacade;
import ui.Display;

import java.util.Collection;
import java.util.Locale;

public class ContextManager {

    ServerFacade server;
    Display display;
    Context context;

    public ContextManager(Display display, ServerFacade server){
        this.display = display;
        this.server = server;
    }

    public void run(){
        String command = "";
        while(!command.contains("quit")){
            context = context.eval(command);
            command = display.stringField("");
            command = command.toLowerCase(Locale.ROOT);
        }
    }
}
