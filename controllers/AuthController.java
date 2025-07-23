package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthController {
    private static Map<String, Integer> sessions = new HashMap<>();
    
    public static String login(String username, String password) {
        int userId = DatabaseManager.authenticateUser(username, password);
        if (userId != -1) {
            String sessionId = UUID.randomUUID().toString();
            sessions.put(sessionId, userId);
            return sessionId;
        }
        return null;
    }
    
    public static boolean register(String username, String password, String email) {
        return DatabaseManager.createUser(username, password, email);
    }
    
    public static Integer getUserId(String sessionId) {
        return sessions.get(sessionId);
    }
    
    public static void logout(String sessionId) {
        sessions.remove(sessionId);
    }
    
    public static boolean isAuthenticated(String sessionId) {
        return sessionId != null && sessions.containsKey(sessionId);
    }
}
