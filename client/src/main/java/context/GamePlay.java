package context;

import facade.ServerFacade;
import facade.WsMessageHandler;
import ui.Display;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class GamePlay implements Context, WsMessageHandler {
    ServerFacade facade;
    Display display;

    public GamePlay (ServerFacade facade, Display display){
        this.display = display;
        this.facade = facade;
    }
    @Override
    public Context eval(String command) {
        return null;
    }

    @Override
    public void handleError(ErrorMessage error) {

    }

    @Override
    public void handleLoadGame(LoadGameMessage load) {

    }

    @Override
    public void handleNotification(NotificationMessage notification) {

    }
}
