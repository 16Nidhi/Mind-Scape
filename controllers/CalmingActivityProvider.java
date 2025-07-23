package controllers;

import java.util.Random;

public class CalmingActivityProvider {

    private static final String[] activities = {
        "🎧 Listen to soothing music",
        "🧘 Practice deep breathing for 2 minutes",
        "📓 Write in your journal",
        "🌿 Take a walk in nature",
        "🌅 Watch a calming video or meditation"
    };

    public static String getRandomActivity() {
        return activities[new Random().nextInt(activities.length)];
    }
}
