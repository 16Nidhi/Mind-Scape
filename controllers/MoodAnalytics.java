package controllers;

import java.sql.*;
import java.util.*;

public class MoodAnalytics {
    
    public static Map<String, Object> getMoodStatistics(int userId) {
        Map<String, Object> stats = new HashMap<>();
        
        try (Connection conn = DatabaseManager.getConnection()) {
            // Mood frequency
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT mood, COUNT(*) as count FROM moods WHERE user_id = ? GROUP BY mood ORDER BY count DESC"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            Map<String, Integer> moodCounts = new HashMap<>();
            while (rs.next()) {
                moodCounts.put(rs.getString("mood"), rs.getInt("count"));
            }
            stats.put("moodCounts", moodCounts);
            
            // Recent trend (last 7 days)
            stmt = conn.prepareStatement(
                "SELECT mood, timestamp FROM moods WHERE user_id = ? AND timestamp >= datetime('now', '-7 days') ORDER BY timestamp"
            );
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            List<Map<String, String>> recentMoods = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> entry = new HashMap<>();
                entry.put("mood", rs.getString("mood"));
                entry.put("timestamp", rs.getString("timestamp"));
                recentMoods.add(entry);
            }
            stats.put("recentTrend", recentMoods);
            
            // Average mood score (simplified scoring)
            stmt = conn.prepareStatement(
                "SELECT mood FROM moods WHERE user_id = ?"
            );
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            int totalScore = 0;
            int count = 0;
            while (rs.next()) {
                String mood = rs.getString("mood");
                totalScore += getMoodScore(mood);
                count++;
            }
            
            if (count > 0) {
                stats.put("averageScore", (double) totalScore / count);
            } else {
                stats.put("averageScore", 0.0);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return stats;
    }
    
    private static int getMoodScore(String mood) {
        if (mood.contains("Happy") || mood.contains("😄")) return 5;
        if (mood.contains("Calm") || mood.contains("😌")) return 4;
        if (mood.contains("Neutral") || mood.contains("😐")) return 3;
        if (mood.contains("Sad") || mood.contains("😔")) return 2;
        if (mood.contains("Angry") || mood.contains("😡")) return 1;
        return 3; // default neutral
    }
    
    public static String generateMoodInsights(int userId) {
        Map<String, Object> stats = getMoodStatistics(userId);
        StringBuilder insights = new StringBuilder();
        
        Map<String, Integer> moodCounts = (Map<String, Integer>) stats.get("moodCounts");
        if (moodCounts != null && !moodCounts.isEmpty()) {
            String mostCommon = moodCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
            
            insights.append("📊 Your most common mood: ").append(mostCommon).append("\n");
        }
        
        double avgScore = (Double) stats.get("averageScore");
        if (avgScore >= 4) {
            insights.append("✨ You're doing great! Your overall mood is positive.\n");
        } else if (avgScore >= 3) {
            insights.append("😊 Your mood is generally balanced.\n");
        } else {
            insights.append("💙 Consider some self-care activities. Your mood could use a boost.\n");
        }
        
        return insights.toString();
    }
}
