package facade;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WSCommunicator extends Endpoint {
    private Session session;
    public WSCommunicator(String url) throws Exception {
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String s) {

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
