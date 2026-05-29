package facade;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface WsMessageHandler {
    public void handleError(ErrorMessage error);
    public void handleLoadGame(LoadGameMessage load);
    public void handleNotification(NotificationMessage notification);
}
