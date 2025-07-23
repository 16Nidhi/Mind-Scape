package controllers;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:mindcare.db";
    
    static {
        initializeDatabase();
    }
    
    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            createTables(conn);
            System.out.println("✅ Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
        }
    }
    
    private static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        
        // Users table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT UNIQUE NOT NULL," +
            "password TEXT NOT NULL," +
            "email TEXT," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        
        // Moods table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS moods (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id INTEGER," +
            "mood TEXT NOT NULL," +
            "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (user_id) REFERENCES users (id))");
        
        // Journal entries table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS journal_entries (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id INTEGER," +
            "content TEXT NOT NULL," +
            "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (user_id) REFERENCES users (id))");
        
        // Goals table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS goals (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id INTEGER," +
            "title TEXT NOT NULL," +
            "description TEXT," +
            "target_date DATE," +
            "completed BOOLEAN DEFAULT FALSE," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (user_id) REFERENCES users (id))");
        
        // Reminders table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS reminders (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id INTEGER," +
            "title TEXT NOT NULL," +
            "description TEXT," +
            "reminder_time TIME," +
            "active BOOLEAN DEFAULT TRUE," +
            "FOREIGN KEY (user_id) REFERENCES users (id))");
        
        // Habits table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS habits (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "user_id INTEGER," +
            "name TEXT NOT NULL," +
            "target_value INTEGER DEFAULT 1," +
            "unit TEXT DEFAULT 'times'," +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "FOREIGN KEY (user_id) REFERENCES users (id))");
        
        // Habit tracking table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS habit_tracking (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "habit_id INTEGER," +
            "date DATE," +
            "value INTEGER DEFAULT 1," +
            "FOREIGN KEY (habit_id) REFERENCES habits (id))");
            
        stmt.close();
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    // User management methods
    public static boolean createUser(String username, String password, String email) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, email) VALUES (?, ?, ?)"
            );
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, hash this!
            stmt.setString(3, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static int authenticateUser(String username, String password) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id FROM users WHERE username = ? AND password = ?"
            );
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    // Mood tracking methods
    public static void saveMood(int userId, String mood) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO moods (user_id, mood) VALUES (?, ?)"
            );
            stmt.setInt(1, userId);
            stmt.setString(2, mood);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> getMoodHistory(int userId) {
        List<String> moods = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT mood, timestamp FROM moods WHERE user_id = ? ORDER BY timestamp DESC LIMIT 50"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                moods.add(rs.getTimestamp("timestamp") + " - " + rs.getString("mood"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moods;
    }
    
    // Journal methods
    public static void saveJournalEntry(int userId, String content) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO journal_entries (user_id, content) VALUES (?, ?)"
            );
            stmt.setInt(1, userId);
            stmt.setString(2, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> getJournalEntries(int userId) {
        List<String> entries = new ArrayList<>();
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT content, timestamp FROM journal_entries WHERE user_id = ? ORDER BY timestamp DESC LIMIT 20"
            );
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entries.add(rs.getTimestamp("timestamp") + "\n" + rs.getString("content") + "\n------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
}
