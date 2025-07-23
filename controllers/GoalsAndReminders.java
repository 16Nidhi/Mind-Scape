package controllers;

import java.sql.*;
import java.util.*;

public class GoalsAndReminders {
    
    // Goals management
    public static boolean addGoal(int userId, String title, String description, String targetDate) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO goals (user_id, title, description, target_date) VALUES (?, ?, ?, ?)"
            );
            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, targetDate);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Map<String, Object>> getGoals(int userId) {
        List<Map<String, Object>> goals = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM goals WHERE user_id = ? ORDER BY created_at DESC"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> goal = new HashMap<>();
                goal.put("id", rs.getInt("id"));
                goal.put("title", rs.getString("title"));
                goal.put("description", rs.getString("description"));
                goal.put("targetDate", rs.getString("target_date"));
                goal.put("completed", rs.getBoolean("completed"));
                goal.put("createdAt", rs.getString("created_at"));
                goals.add(goal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goals;
    }
    
    public static boolean completeGoal(int goalId, int userId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE goals SET completed = TRUE WHERE id = ? AND user_id = ?"
            );
            stmt.setInt(1, goalId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Reminders management
    public static boolean addReminder(int userId, String title, String description, String reminderTime) {
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO reminders (user_id, title, description, reminder_time) VALUES (?, ?, ?, ?)"
            );
            stmt.setInt(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, reminderTime);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Map<String, Object>> getReminders(int userId) {
        List<Map<String, Object>> reminders = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM reminders WHERE user_id = ? AND active = TRUE ORDER BY reminder_time"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> reminder = new HashMap<>();
                reminder.put("id", rs.getInt("id"));
                reminder.put("title", rs.getString("title"));
                reminder.put("description", rs.getString("description"));
                reminder.put("reminderTime", rs.getString("reminder_time"));
                reminders.add(reminder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reminders;
    }
}
