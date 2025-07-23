package controllers;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MoodPredictor {
    
    public static String predictMood(int userId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            // Get recent mood patterns
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT mood, timestamp, " +
                "strftime('%H', timestamp) as hour, " +
                "strftime('%w', timestamp) as day_of_week " +
                "FROM moods WHERE user_id = ? " +
                "ORDER BY timestamp DESC LIMIT 50"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            Map<String, Integer> hourlyMoods = new HashMap<>();
            Map<String, Integer> dailyMoods = new HashMap<>();
            List<String> recentMoods = new ArrayList<>();
            
            while (rs.next()) {
                String mood = rs.getString("mood");
                String hour = rs.getString("hour");
                String dayOfWeek = rs.getString("day_of_week");
                
                recentMoods.add(mood);
                hourlyMoods.put(hour, hourlyMoods.getOrDefault(hour, 0) + getMoodScore(mood));
                dailyMoods.put(dayOfWeek, dailyMoods.getOrDefault(dayOfWeek, 0) + getMoodScore(mood));
            }
            
            // Current time analysis
            LocalDateTime now = LocalDateTime.now();
            int currentHour = now.getHour();
            int currentDayOfWeek = now.getDayOfWeek().getValue() % 7; // Convert to 0-6
            
            // Predict based on patterns
            StringBuilder prediction = new StringBuilder();
            prediction.append("🔮 Mood Prediction:\n");
            
            // Time-based prediction
            String currentHourStr = String.valueOf(currentHour);
            if (hourlyMoods.containsKey(currentHourStr)) {
                int avgScore = hourlyMoods.get(currentHourStr);
                prediction.append("⏰ At this time, you typically feel: ")
                          .append(getScoreMood(avgScore)).append("\n");
            }
            
            // Day-based prediction
            String currentDayStr = String.valueOf(currentDayOfWeek);
            if (dailyMoods.containsKey(currentDayStr)) {
                int avgScore = dailyMoods.get(currentDayStr);
                prediction.append("📅 On this day of week, you usually feel: ")
                          .append(getScoreMood(avgScore)).append("\n");
            }
            
            // Recent trend analysis
            if (recentMoods.size() >= 3) {
                int recentAvg = recentMoods.stream()
                    .mapToInt(MoodPredictor::getMoodScore)
                    .limit(3)
                    .sum() / 3;
                
                prediction.append("📈 Recent trend suggests: ")
                          .append(getScoreMood(recentAvg)).append("\n");
            }
            
            // Recommendations based on prediction
            prediction.append("\n💡 Recommendations:\n");
            if (currentHour < 12) {
                prediction.append("🌅 Morning: Start with positive affirmations and light exercise\n");
            } else if (currentHour < 18) {
                prediction.append("☀️ Afternoon: Take breaks and stay hydrated\n");
            } else {
                prediction.append("🌙 Evening: Practice relaxation and prepare for rest\n");
            }
            
            return prediction.toString();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unable to generate prediction. Log more moods for better insights!";
        }
    }
    
    private static int getMoodScore(String mood) {
        if (mood.contains("Happy") || mood.contains("😄")) return 5;
        if (mood.contains("Calm") || mood.contains("😌")) return 4;
        if (mood.contains("Neutral") || mood.contains("😐")) return 3;
        if (mood.contains("Sad") || mood.contains("😔")) return 2;
        if (mood.contains("Angry") || mood.contains("😡")) return 1;
        return 3;
    }
    
    private static String getScoreMood(int score) {
        if (score >= 5) return "😄 Happy";
        if (score >= 4) return "😌 Calm";
        if (score >= 3) return "😐 Neutral";
        if (score >= 2) return "😔 Sad";
        return "😡 Angry";
    }
    
    public static String getPersonalizedTips(int userId) {
        Map<String, Object> stats = MoodAnalytics.getMoodStatistics(userId);
        double avgScore = (Double) stats.get("averageScore");
        
        StringBuilder tips = new StringBuilder();
        tips.append("🎯 Personalized Tips:\n\n");
        
        if (avgScore < 3) {
            tips.append("💙 Focus on mood boosting activities:\n")
                .append("• Daily gratitude practice\n")
                .append("• Regular exercise\n")
                .append("• Social connections\n")
                .append("• Professional support if needed\n");
        } else if (avgScore < 4) {
            tips.append("⚖️ Maintain balance:\n")
                .append("• Consistent sleep schedule\n")
                .append("• Stress management techniques\n")
                .append("• Mindfulness practice\n")
                .append("• Regular self-check-ins\n");
        } else {
            tips.append("✨ Keep up the great work:\n")
                .append("• Continue current positive habits\n")
                .append("• Share your strategies with others\n")
                .append("• Set new wellness goals\n")
                .append("• Practice gratitude regularly\n");
        }
        
        return tips.toString();
    }
}
