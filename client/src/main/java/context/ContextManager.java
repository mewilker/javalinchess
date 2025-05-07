package context;

import facade.ServerFacade;
import ui.Display;

import java.util.Locale;

public class ContextManager {

    private final Display display;
    private Context context;

    public ContextManager(Display display, ServerFacade server){
        this.display = display;
        context = new PreLogin(display, server);
    }

    public void run(){
        String command = "";
        while(!command.contains("quit")){
            context = context.eval(command);
            command = display.stringField("Chess");
            command = command.toLowerCase(Locale.ROOT);
        }
    }
}
