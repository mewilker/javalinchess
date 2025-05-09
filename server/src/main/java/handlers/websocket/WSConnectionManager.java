package handlers.websocket;

import io.javalin.websocket.WsContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WSConnectionManager {
    //there are sessions in the WsContext object
    private final HashMap<Integer, Set<WsContext>> sessionMap = new HashMap<>();

    public void addConnection(int id, WsContext context) {
        Set<WsContext> set = sessionMap.get(id);
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(context);
        sessionMap.put(id, set);
    }

    public void removeConnection(int id, WsContext context) {
        Set<WsContext> sessions = sessionMap.get(id);
        if (sessions != null) {
            sessions.remove(context);
        }
    }

    public void broadcast(String message, int id) {
        broadcastWithExclusion(message, id, null);
    }

    public void broadcastWithExclusion(String message, int id, WsContext context) {
        Set<WsContext> sessions = sessionMap.get(id);
        sessions = Set.copyOf(sessions);
        for (WsContext session : sessions) {
            if (session.session.isOpen() && session.equals(context)) {
                session.send(message);
            } else if (!session.session.isOpen()) {
                removeConnection(id, session);
            }
        }
    }
}
