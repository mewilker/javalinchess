package facade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import serialization.TypeAdapters;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WSCommunicator extends Endpoint {
    private final Session session;
    WsMessageHandler handler;
    public WSCommunicator(String url, WsMessageHandler handler) throws Exception {
        this.handler = handler;
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String s) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(ServerMessage.class, TypeAdapters.messageDeserializer())
                        .create();
                ServerMessage message = gson.fromJson(s, ServerMessage.class);
                switch (message.getServerMessageType()){
                    case ERROR -> handler.handleError((ErrorMessage) message);
                    case NOTIFICATION -> handler.handleNotification((NotificationMessage) message);
                    case LOAD_GAME -> handler.handleLoadGame((LoadGameMessage) message);
                }
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void send(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}
