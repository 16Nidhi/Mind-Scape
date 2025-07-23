package controllers;

import java.util.Random;

public class QuoteProvider {

    private static final String[] quotes = {
        "🌟 You are stronger than you think.",
        "💖 Every day is a second chance.",
        "🧠 Your mental health matters.",
        "🌈 Storms don’t last forever.",
        "✨ Believe in yourself and your journey."
    };

    public static String getQuoteOfTheDay() {
        return quotes[new Random().nextInt(quotes.length)];
    }
}
