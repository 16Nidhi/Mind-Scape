package controllers;

import java.util.*;

public class Assistant {

    private static Map<String, List<String>> conversationHistory = new HashMap<>();

    public static String getResponse(String question, String sessionId, String currentMood) {
        // Store conversation history
        conversationHistory.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(question);
        
        question = question.toLowerCase();

        // Context-aware responses based on mood
        if (currentMood != null) {
            String moodResponse = getMoodSpecificResponse(question, currentMood);
            if (moodResponse != null) return moodResponse;
        }

        // Enhanced responses with more categories
        if (question.contains("anxiety") || question.contains("anxious") || question.contains("worried")) {
            return "🌱 For anxiety, try: " + MeditationExercises.getBreathingExercise() + 
                   "\n\nAlso consider: grounding techniques, progressive muscle relaxation, or talking to someone you trust.";
        }
        
        if (question.contains("sleep") || question.contains("insomnia") || question.contains("tired")) {
            return "😴 Sleep tips: Avoid screens 1 hour before bed, keep room cool & dark, try relaxation techniques. " +
                   "Consider a bedtime routine with calming activities like reading or gentle stretching.";
        }
        
        if (question.contains("sad") || question.contains("depressed") || question.contains("down")) {
            return "💙 You are not alone. Try: journaling your feelings, gentle exercise, connecting with friends, " +
                   "or engaging in activities you usually enjoy. If feelings persist, consider professional support.";
        }
        
        if (question.contains("stress") || question.contains("overwhelmed")) {
            return "🧘 Stress relief: " + MeditationExercises.getQuickRelaxation() + 
                   "\n\nBreak tasks into smaller steps, practice saying 'no' to additional commitments, and prioritize self-care.";
        }
        
        if (question.contains("meditation") || question.contains("mindfulness")) {
            return "🧘‍♀️ " + MeditationExercises.getMeditationPrompt() + 
                   "\n\nStart with just 5 minutes daily. Consistency is more important than duration.";
        }
        
        if (question.contains("exercise") || question.contains("workout") || question.contains("fitness")) {
            return "💪 Exercise boosts mood! Start small: 10-minute walks, stretching, dancing, or yoga. " +
                   "Even light movement can improve mental wellbeing.";
        }
        
        if (question.contains("eating") || question.contains("nutrition") || question.contains("food")) {
            return "🥗 Nutrition affects mood: Stay hydrated, eat regular meals, include omega-3s, limit caffeine. " +
                   "Mindful eating can also reduce stress.";
        }
        
        if (question.contains("social") || question.contains("friends") || question.contains("lonely")) {
            return "👥 Social connection is vital: Reach out to one person today, join community activities, " +
                   "or consider online communities with shared interests.";
        }
        
        if (question.contains("work") || question.contains("job") || question.contains("career")) {
            return "💼 Work-life balance: Set boundaries, take breaks, communicate needs with supervisors, " +
                   "and remember that your worth isn't defined by productivity.";
        }
        
        if (question.contains("goal") || question.contains("motivation") || question.contains("purpose")) {
            return "🎯 Goal setting: Start with small, achievable goals. Celebrate progress, not just outcomes. " +
                   "Break big goals into daily actions and track your progress.";
        }
        
        if (question.contains("habit") || question.contains("routine")) {
            return "🔄 Building habits: Start with 2-minute versions, be consistent, stack new habits onto existing ones. " +
                   "Focus on one habit at a time for better success.";
        }

        // Conversational responses
        if (question.contains("hello") || question.contains("hi") || question.contains("hey")) {
            return "👋 Hello! I'm here to support your mental wellness. How are you feeling today? " +
                   "You can ask me about anxiety, sleep, stress, meditation, or any wellness topic.";
        }
        
        if (question.contains("thank") || question.contains("thanks")) {
            return "💚 You're welcome! Remember, taking care of your mental health is a journey. " +
                   "I'm here whenever you need guidance or support.";
        }
        
        if (question.contains("how are you")) {
            return "🤖 I'm here and ready to help! More importantly, how are YOU feeling? " +
                   "I'm designed to support your mental wellness journey.";
        }

        // Default response with personalized suggestion
        List<String> history = conversationHistory.get(sessionId);
        if (history != null && history.size() > 1) {
            return "💭 I understand you're looking for support. Based on our conversation, you might find " +
                   "journaling, breathing exercises, or talking to someone helpful. " +
                   "What specific area would you like guidance on? (anxiety, sleep, stress, motivation, etc.)";
        }
        
        return "🌟 I'm here to help with your mental wellness! Try asking about:\n" +
               "• Anxiety or stress management\n• Sleep improvement\n• Meditation & mindfulness\n" +
               "• Building healthy habits\n• Mood support\n• Goal setting\n\nWhat would you like to explore?";
    }
    
    private static String getMoodSpecificResponse(String question, String mood) {
        if (mood.contains("Angry") || mood.contains("😡")) {
            if (question.contains("calm") || question.contains("relax")) {
                return "🔥➡️😌 For anger management: " + MeditationExercises.getBreathingExercise() + 
                       "\n\nTry counting to 10, physical exercise, or removing yourself from the trigger temporarily.";
            }
        } else if (mood.contains("Sad") || mood.contains("😔")) {
            if (question.contains("better") || question.contains("help")) {
                return "💙 I see you're feeling sad. Gentle activities can help: listen to uplifting music, " +
                       "reach out to a friend, do something creative, or practice self-compassion.";
            }
        } else if (mood.contains("Happy") || mood.contains("😄")) {
            return "✨ I love that you're feeling good! To maintain this positive energy: practice gratitude, " +
                   "share your joy with others, or engage in activities that bring you fulfillment.";
        }
        return null;
    }
    
    public static void clearHistory(String sessionId) {
        conversationHistory.remove(sessionId);
    }
}
