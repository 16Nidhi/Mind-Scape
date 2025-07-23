package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.util.Random;
import java.util.Map;
import java.util.List;

public class HomeController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        
        // Get session from cookies
        String sessionId = getSessionFromCookies(exchange);
        Integer userId = null;
        if (sessionId != null) {
            userId = AuthController.getUserId(sessionId);
        }

        switch (path) {
            case "/":
            case "/index":
                serveFile(exchange, "templates/index.html", "text/html");
                break;

            case "/static/style.css":
                serveFile(exchange, "static/style.css", "text/css");
                break;

            case "/static/script.js":
                serveFile(exchange, "static/script.js", "application/javascript");
                break;

            // Authentication endpoints
            case "/login":
                if (method.equalsIgnoreCase("POST")) {
                    handleLogin(exchange);
                } else {
                    sendText(exchange, "Login page", 200);
                }
                break;

            case "/register":
                if (method.equalsIgnoreCase("POST")) {
                    handleRegister(exchange);
                } else {
                    sendText(exchange, "Register page", 200);
                }
                break;

            case "/logout":
                handleLogout(exchange, sessionId);
                break;

            // Existing endpoints with user context
            case "/mood":
                handleMood(exchange, userId);
                break;

            case "/quote":
                handleQuote(exchange);
                break;

            case "/journal":
                if (method.equalsIgnoreCase("POST")) {
                    saveJournalEntry(exchange, userId);
                } else if (method.equalsIgnoreCase("GET")) {
                    getJournalEntries(exchange, userId);
                } else {
                    sendText(exchange, "Unsupported method", 405);
                }
                break;

            case "/mood-log":
                showMoodHistory(exchange, userId);
                break;

            case "/chat":
                handleChat(exchange, sessionId, userId);
                break;

            // New feature endpoints
            case "/mood-analytics":
                handleMoodAnalytics(exchange, userId);
                break;

            case "/mood-insights":
                handleMoodInsights(exchange, userId);
                break;

            case "/mood-prediction":
                handleMoodPrediction(exchange, userId);
                break;

            case "/goals":
                if (method.equalsIgnoreCase("POST")) {
                    addGoal(exchange, userId);
                } else {
                    getGoals(exchange, userId);
                }
                break;

            case "/complete-goal":
                if (method.equalsIgnoreCase("POST")) {
                    completeGoal(exchange, userId);
                } else {
                    sendText(exchange, "POST required", 405);
                }
                break;

            case "/reminders":
                if (method.equalsIgnoreCase("POST")) {
                    addReminder(exchange, userId);
                } else {
                    getReminders(exchange, userId);
                }
                break;

            case "/meditation":
                handleMeditation(exchange);
                break;

            case "/breathing-exercise":
                handleBreathingExercise(exchange);
                break;

            case "/meditation-session":
                handleMeditationSession(exchange);
                break;

            case "/habits":
                if (method.equalsIgnoreCase("POST")) {
                    addHabit(exchange, userId);
                } else {
                    getHabits(exchange, userId);
                }
                break;

            case "/track-habit":
                if (method.equalsIgnoreCase("POST")) {
                    trackHabit(exchange);
                } else {
                    sendText(exchange, "POST required", 405);
                }
                break;

            case "/habit-stats":
                getHabitStats(exchange, userId);
                break;

            case "/personalized-tips":
                getPersonalizedTips(exchange, userId);
                break;

            case "/calming-activity":
                sendText(exchange, CalmingActivityProvider.getRandomActivity(), 200);
                break;

            default:
                sendText(exchange, "404 Not Found", 404);
        }
    }

    private String getSessionFromCookies(HttpExchange exchange) {
        String cookies = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookies != null) {
            for (String cookie : cookies.split(";")) {
                String[] parts = cookie.trim().split("=");
                if (parts.length == 2 && "sessionId".equals(parts[0])) {
                    return parts[1];
                }
            }
        }
        return null;
    }

    private void serveFile(HttpExchange exchange, String filePath, String contentType) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            sendText(exchange, "File not found", 404);
            return;
        }

        byte[] response = Files.readAllBytes(file.toPath());
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        byte[] bytes = text.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void sendJson(HttpExchange exchange, String json, int statusCode) throws IOException {
        byte[] bytes = json.getBytes();
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    // Authentication handlers
    private void handleLogin(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        String[] parts = body.split("&");
        String username = null, password = null;
        
        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                if ("username".equals(kv[0])) username = kv[1];
                if ("password".equals(kv[0])) password = kv[1];
            }
        }
        
        String sessionId = AuthController.login(username, password);
        if (sessionId != null) {
            exchange.getResponseHeaders().set("Set-Cookie", "sessionId=" + sessionId);
            sendText(exchange, "Login successful", 200);
        } else {
            sendText(exchange, "Invalid credentials", 401);
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        String[] parts = body.split("&");
        String username = null, password = null, email = null;
        
        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                if ("username".equals(kv[0])) username = kv[1];
                if ("password".equals(kv[0])) password = kv[1];
                if ("email".equals(kv[0])) email = kv[1];
            }
        }
        
        boolean success = AuthController.register(username, password, email);
        sendText(exchange, success ? "Registration successful" : "Registration failed", 
                success ? 200 : 400);
    }

    private void handleLogout(HttpExchange exchange, String sessionId) throws IOException {
        if (sessionId != null) {
            AuthController.logout(sessionId);
        }
        exchange.getResponseHeaders().set("Set-Cookie", "sessionId=; Max-Age=0");
        sendText(exchange, "Logged out", 200);
    }

    // Enhanced existing handlers
    private void handleMood(HttpExchange exchange, Integer userId) throws IOException {
        String[] moods = {"😄 Happy", "😔 Sad", "😡 Angry", "😌 Calm", "😐 Neutral"};
        String mood = moods[new Random().nextInt(moods.length)];
        
        if (userId != null) {
            DatabaseManager.saveMood(userId, mood);
        } else {
            MoodTracker.logMood(mood);
        }
        
        sendText(exchange, mood, 200);
    }

    private void handleQuote(HttpExchange exchange) throws IOException {
        String quote = QuoteProvider.getQuoteOfTheDay();
        sendText(exchange, quote, 200);
    }

    private void saveJournalEntry(HttpExchange exchange, Integer userId) throws IOException {
        String entry = readRequestBody(exchange);
        
        if (userId != null) {
            DatabaseManager.saveJournalEntry(userId, entry);
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("journal.txt", true))) {
                writer.write(entry);
                writer.write("\n------\n");
            }
        }
        
        sendText(exchange, "Journal saved!", 200);
    }

    private void getJournalEntries(HttpExchange exchange, Integer userId) throws IOException {
        if (userId != null) {
            List<String> entries = DatabaseManager.getJournalEntries(userId);
            sendText(exchange, String.join("\n", entries), 200);
        } else {
            sendText(exchange, "Please log in to view entries", 401);
        }
    }

    private void showMoodHistory(HttpExchange exchange, Integer userId) throws IOException {
        if (userId != null) {
            List<String> history = DatabaseManager.getMoodHistory(userId);
            sendText(exchange, String.join("\n", history), 200);
        } else {
            String history = MoodTracker.readMoodHistory();
            sendText(exchange, history, 200);
        }
    }

    private void handleChat(HttpExchange exchange, String sessionId, Integer userId) throws IOException {
        URI uri = exchange.getRequestURI();
        String query = uri.getQuery();
        String message = "";
        String currentMood = null;

        if (query != null && query.startsWith("msg=")) {
            message = query.substring(4).replace("+", " ");
        }

        if (userId != null) {
            // Get current mood for context
            List<String> recentMoods = DatabaseManager.getMoodHistory(userId);
            if (!recentMoods.isEmpty()) {
                currentMood = recentMoods.get(0);
            }
        }

        String reply = Assistant.getResponse(message, sessionId, currentMood);
        sendText(exchange, reply, 200);
    }

    // New feature handlers
    private void handleMoodAnalytics(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        Map<String, Object> analytics = MoodAnalytics.getMoodStatistics(userId);
        sendJson(exchange, analytics.toString(), 200);
    }

    private void handleMoodInsights(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        String insights = MoodAnalytics.generateMoodInsights(userId);
        sendText(exchange, insights, 200);
    }

    private void handleMoodPrediction(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        String prediction = MoodPredictor.predictMood(userId);
        sendText(exchange, prediction, 200);
    }

    // Goals and reminders handlers
    private void addGoal(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        String body = readRequestBody(exchange);
        String[] parts = body.split("&");
        String title = null, description = null, targetDate = null;
        
        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                if ("title".equals(kv[0])) title = kv[1];
                if ("description".equals(kv[0])) description = kv[1];
                if ("targetDate".equals(kv[0])) targetDate = kv[1];
            }
        }
        
        boolean success = GoalsAndReminders.addGoal(userId, title, description, targetDate);
        sendText(exchange, success ? "Goal added!" : "Failed to add goal", success ? 200 : 400);
    }

    private void getGoals(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        List<Map<String, Object>> goals = GoalsAndReminders.getGoals(userId);
        sendJson(exchange, goals.toString(), 200);
    }

    private void completeGoal(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        String body = readRequestBody(exchange);
        int goalId = Integer.parseInt(body.split("=")[1]);
        
        boolean success = GoalsAndReminders.completeGoal(goalId, userId);
        sendText(exchange, success ? "Goal completed!" : "Failed to complete goal", success ? 200 : 400);
    }

    private void addReminder(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        String body = readRequestBody(exchange);
        String[] parts = body.split("&");
        String title = null, description = null, reminderTime = null;
        
        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                if ("title".equals(kv[0])) title = kv[1];
                if ("description".equals(kv[0])) description = kv[1];
                if ("reminderTime".equals(kv[0])) reminderTime = kv[1];
            }
        }
        
        boolean success = GoalsAndReminders.addReminder(userId, title, description, reminderTime);
        sendText(exchange, success ? "Reminder added!" : "Failed to add reminder", success ? 200 : 400);
    }

    private void getReminders(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        List<Map<String, Object>> reminders = GoalsAndReminders.getReminders(userId);
        sendJson(exchange, reminders.toString(), 200);
    }

    // Meditation handlers
    private void handleMeditation(HttpExchange exchange) throws IOException {
        String prompt = MeditationExercises.getMeditationPrompt();
        sendText(exchange, prompt, 200);
    }

    private void handleBreathingExercise(HttpExchange exchange) throws IOException {
        String exercise = MeditationExercises.getBreathingExercise();
        sendText(exchange, exercise, 200);
    }

    private void handleMeditationSession(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String query = uri.getQuery();
        int minutes = 5; // default
        
        if (query != null && query.startsWith("minutes=")) {
            minutes = Integer.parseInt(query.substring(8));
        }
        
        String session = MeditationExercises.getTimedSession(minutes);
        sendText(exchange, session, 200);
    }

    // Habit tracking handlers
    private void addHabit(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        String body = readRequestBody(exchange);
        String[] parts = body.split("&");
        String name = null, unit = "times";
        int targetValue = 1;
        
        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                if ("name".equals(kv[0])) name = kv[1];
                if ("targetValue".equals(kv[0])) targetValue = Integer.parseInt(kv[1]);
                if ("unit".equals(kv[0])) unit = kv[1];
            }
        }
        
        boolean success = HabitTracker.addHabit(userId, name, targetValue, unit);
        sendText(exchange, success ? "Habit added!" : "Failed to add habit", success ? 200 : 400);
    }

    private void getHabits(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        List<Map<String, Object>> habits = HabitTracker.getHabits(userId);
        sendJson(exchange, habits.toString(), 200);
    }

    private void trackHabit(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        String[] parts = body.split("&");
        int habitId = 0, value = 1;
        
        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length == 2) {
                if ("habitId".equals(kv[0])) habitId = Integer.parseInt(kv[1]);
                if ("value".equals(kv[0])) value = Integer.parseInt(kv[1]);
            }
        }
        
        boolean success = HabitTracker.trackHabit(habitId, value);
        sendText(exchange, success ? "Habit tracked!" : "Failed to track habit", success ? 200 : 400);
    }

    private void getHabitStats(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        Map<String, Object> stats = HabitTracker.getHabitStatistics(userId);
        sendJson(exchange, stats.toString(), 200);
    }

    private void getPersonalizedTips(HttpExchange exchange, Integer userId) throws IOException {
        if (userId == null) {
            sendText(exchange, "Please log in", 401);
            return;
        }
        
        String tips = MoodPredictor.getPersonalizedTips(userId);
        sendText(exchange, tips, 200);
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }
}
