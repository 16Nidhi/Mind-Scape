package controllers;

import java.io.*;
import java.time.LocalDateTime;

public class MoodTracker {
    private static final String FILE = "mood_log.txt";

    public static void logMood(String mood) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            writer.write(LocalDateTime.now() + " - " + mood);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing mood: " + e.getMessage());
        }
    }

    public static String readMoodHistory() {
        StringBuilder history = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
        } catch (IOException e) {
            return "No mood history found.";
        }
        return history.toString();
    }
}
