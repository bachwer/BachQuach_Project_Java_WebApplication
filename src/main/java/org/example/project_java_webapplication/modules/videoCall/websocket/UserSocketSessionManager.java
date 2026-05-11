package org.example.project_java_webapplication.modules.videoCall.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserSocketSessionManager {

    // =====================================
    // ONLINE USERS
    // =====================================

    private static final Map<String, WebSocketSession>
            USERS = new ConcurrentHashMap<>();

    // =====================================
    // ADD USER
    // =====================================

    public static void addUser(
            String username,
            WebSocketSession session
    ) {

        USERS.put(username, session);

    }

    // =====================================
    // GET USER SESSION
    // =====================================

    public static WebSocketSession getUser(
            String username
    ) {

        return USERS.get(username);

    }

    // =====================================
    // REMOVE USER
    // =====================================

    public static void removeUser(
            String username
    ) {

        USERS.remove(username);

    }

    // =====================================
    // CHECK ONLINE
    // =====================================

    public static boolean isOnline(
            String username
    ) {

        return USERS.containsKey(username);

    }

    // =====================================
    // GET ONLINE USERS
    // =====================================

    public static Set<String> getOnlineUsers() {

        return USERS.keySet();

    }

    // =====================================
    // GET ALL SESSIONS
    // =====================================

    public static Map<String, WebSocketSession>
    getAllSessions() {

        return USERS;

    }
}