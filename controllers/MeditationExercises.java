package controllers;

import java.util.Random;

public class MeditationExercises {
    
    private static final String[] breathingExercises = {
        "🫁 4-7-8 Breathing: Inhale for 4, hold for 7, exhale for 8",
        "🌬️ Box Breathing: Inhale 4, hold 4, exhale 4, hold 4",
        "💨 Equal Breathing: Inhale for 4 counts, exhale for 4 counts",
        "🌊 Ocean Breathing: Deep inhales and long, slow exhales",
        "⭐ Star Breathing: 5-count inhale, 5-count exhale"
    };
    
    private static final String[] meditationPrompts = {
        "🧘‍♀️ Body Scan: Focus on each part of your body from toes to head",
        "🌅 Mindful Observation: Notice 5 things you can see, 4 you can hear, 3 you can touch",
        "💭 Thought Watching: Observe your thoughts without judgment, like clouds passing",
        "❤️ Loving Kindness: Send positive thoughts to yourself and others",
        "🌿 Nature Connection: Imagine yourself in a peaceful natural setting",
        "🕯️ Candle Meditation: Focus on the gentle flicker of a candle flame",
        "📿 Mantra Meditation: Repeat a calming phrase like 'I am at peace'",
        "🎵 Sound Meditation: Focus on calming sounds around you"
    };
    
    private static final String[] quickRelaxation = {
        "😌 Progressive Muscle Relaxation: Tense and release each muscle group",
        "🌸 Guided Imagery: Visualize your happy place in detail",
        "🤲 Grounding Technique: Feel your feet on the ground, hands on your lap",
        "💙 Color Breathing: Imagine breathing in calming blue, exhaling stress",
        "🎯 Focus Point: Concentrate on one object for 2 minutes",
        "🌙 Moon Meditation: Imagine soft moonlight washing over you"
    };
    
    public static String getBreathingExercise() {
        return breathingExercises[new Random().nextInt(breathingExercises.length)];
    }
    
    public static String getMeditationPrompt() {
        return meditationPrompts[new Random().nextInt(meditationPrompts.length)];
    }
    
    public static String getQuickRelaxation() {
        return quickRelaxation[new Random().nextInt(quickRelaxation.length)];
    }
    
    public static String getTimedSession(int minutes) {
        String exercise = getMeditationPrompt();
        return String.format("🕰️ %d-Minute Session: %s\n\n" +
            "Set a timer for %d minutes and focus on this practice. " +
            "When your mind wanders, gently bring it back to the exercise.", 
            minutes, exercise, minutes);
    }
    
    public static String getPersonalizedRecommendation(String currentMood) {
        if (currentMood.contains("Angry") || currentMood.contains("😡")) {
            return "🔥 For anger: Try box breathing and progressive muscle relaxation";
        } else if (currentMood.contains("Sad") || currentMood.contains("😔")) {
            return "💙 For sadness: Loving kindness meditation and gentle breathing";
        } else if (currentMood.contains("Happy") || currentMood.contains("😄")) {
            return "✨ For happiness: Gratitude meditation to maintain positive energy";
        } else {
            return getBreathingExercise();
        }
    }
}
