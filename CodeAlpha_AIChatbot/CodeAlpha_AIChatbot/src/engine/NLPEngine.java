package engine;

import java.util.*;
import java.util.regex.*;

/**
 * Rule-based NLP engine using pattern matching and keyword detection.
 * Handles greetings, FAQs, math, weather queries, and general conversation.
 */
public class NLPEngine {

    /** Intent pattern: ordered list of (regex pattern, response template). */
    private final List<Intent> intents = new ArrayList<>();
    private final Map<String, String> memory = new HashMap<>();
    private final Random rand = new Random();
    private String userName = "Friend";

    public NLPEngine() {
        loadIntents();
    }

    private void loadIntents() {
        // Greetings
        addIntent("(hi|hello|hey|greetings|good morning|good evening|what's up|sup)",
            "Hello there! How can I help you today?",
            "Hey! Great to see you. What's on your mind?",
            "Hi! I'm CALI (CodeAlpha Language Intelligence). How can I assist?");

        // Name introduction
        addIntent("my name is ([a-zA-Z]+)",
            "Nice to meet you, {1}! I'll remember that.",
            "Hello {1}! What can I help you with today?");

        // Bot identity
        addIntent("what is your name|who are you|what are you",
            "I'm CALI — CodeAlpha Language Intelligence, built with Java!",
            "My name is CALI, your smart Java-powered assistant.");

        // How are you
        addIntent("how are you|how do you feel|are you ok",
            "I'm running perfectly! All systems operational. How about you?",
            "As an AI, I don't feel emotions — but my logic is sharp today!");

        // Help
        addIntent("help|what can you do|commands|features",
            "I can help with: greetings, Java questions, math calculations, " +
            "jokes, general knowledge, and more! Just ask anything.");

        // Java questions
        addIntent("what is java|tell me about java",
            "Java is a high-level, object-oriented programming language created by Sun Microsystems in 1995. " +
            "It follows 'Write Once, Run Anywhere' (WORA) principle via the JVM.");

        addIntent("what is oop|object oriented",
            "OOP stands for Object-Oriented Programming. It has 4 pillars: " +
            "1) Encapsulation, 2) Abstraction, 3) Inheritance, 4) Polymorphism.");

        addIntent("what is inheritance",
            "Inheritance lets a class (child) acquire properties and methods from another class (parent). " +
            "In Java: class Dog extends Animal { }");

        addIntent("what is polymorphism",
            "Polymorphism means 'many forms'. In Java, a method can behave differently based on the object " +
            "calling it — achieved via method overriding and overloading.");

        addIntent("what is an interface|what are interfaces",
            "An interface in Java is a blueprint with abstract methods. " +
            "Classes implement interfaces using the 'implements' keyword.");

        addIntent("what is a thread|multithreading",
            "A thread is the smallest unit of execution. Java supports multithreading " +
            "via Thread class or Runnable interface, allowing parallel execution.");

        addIntent("what is exception handling",
            "Exception handling in Java uses try-catch-finally blocks to manage runtime errors gracefully, " +
            "preventing program crashes.");

        // Math
        addIntent("(\\d+)\\s*[+]\\s*(\\d+)",
            "The result is: {result:add}");
        addIntent("(\\d+)\\s*[-]\\s*(\\d+)",
            "The result is: {result:sub}");
        addIntent("(\\d+)\\s*[*x×]\\s*(\\d+)",
            "The result is: {result:mul}");
        addIntent("(\\d+)\\s*[/÷]\\s*(\\d+)",
            "The result is: {result:div}");

        // Time / date
        addIntent("what time is it|current time|what's the time",
            "Current time: " + new java.util.Date().toString().substring(11, 19));
        addIntent("what is today|what day is it|today's date",
            "Today is: " + new java.util.Date());

        // Jokes
        addIntent("tell me a joke|joke|funny",
            "Why do Java developers wear glasses? Because they don't C#!",
            "Why was the Java developer broke? Because he used up all his cache!",
            "A SQL query walks into a bar, walks up to two tables and asks... 'Can I join you?'",
            "Why do programmers prefer dark mode? Because light attracts bugs!",
            "What's a programmer's favourite place? The foo bar!");

        // Motivational
        addIntent("motivate me|i feel sad|i'm sad|inspire me|encouragement",
            "Every expert was once a beginner. Keep coding and learning!",
            "You are capable of amazing things. One bug at a time!",
            "The best error message is the one that never shows up. Keep going!",
            "Rome wasn't built in a day, and neither is great software. Stay strong!");

        // Weather (rule-based placeholder)
        addIntent("weather|will it rain|is it hot|temperature",
            "I don't have real-time weather access, but you can check weather.com " +
            "or simply step outside for a moment!");

        // Thanks
        addIntent("thank you|thanks|ty|thx|appreciate",
            "You're welcome! Happy to help anytime.",
            "My pleasure! That's what I'm here for.",
            "Anytime! Feel free to ask more.");

        // Goodbye
        addIntent("bye|goodbye|see you|exit|quit",
            "Goodbye! It was great chatting with you. Come back anytime!",
            "See you soon! Take care.",
            "Farewell! Keep coding and stay curious!");

        // Age
        addIntent("how old are you|your age",
            "I was born when this project was created. I'm just a few bytes old!");

        // Favourite
        addIntent("favourite (language|programming language)",
            "My favourite is obviously Java — I'm written in it! Though Python is popular too.");

        // Default / fallback
        addIntent(".*",
            "Hmm, that's interesting! I'm still learning. Could you rephrase that?",
            "I'm not sure I understand. Try asking about Java, math, or just chat with me!",
            "Good question! I'm a rule-based bot, so I may not have all answers yet.",
            "Interesting! You can ask me about Java, OOP, math, jokes, or just say hello.");
    }

    private void addIntent(String pattern, String... responses) {
        intents.add(new Intent(pattern, responses));
    }

    /**
     * Process user input and return a response.
     */
    public String respond(String input) {
        if (input == null || input.isBlank()) return "Please type something!";
        String lower = input.trim().toLowerCase();

        // Check name memory
        Matcher nameMatcher = Pattern.compile("my name is ([a-zA-Z]+)", Pattern.CASE_INSENSITIVE).matcher(lower);
        if (nameMatcher.find()) {
            userName = capitalize(nameMatcher.group(1));
            memory.put("name", userName);
        }

        // Match intents in order
        for (Intent intent : intents) {
            Matcher m = intent.pattern.matcher(lower);
            if (m.find()) {
                String response = intent.getRandomResponse();
                response = fillGroups(response, m, lower);
                return insertName(response);
            }
        }

        return "I'm not sure about that. Try asking me about Java or say 'help'!";
    }

    /** Replace {1}, {2} group references and {result:op} math placeholders. */
    private String fillGroups(String response, Matcher m, String input) {
        // Math evaluation
        if (response.contains("{result:")) {
            try {
                double a = Double.parseDouble(m.group(1));
                double b = Double.parseDouble(m.group(2));
                double result = switch (response.replaceAll(".*\\{result:(\\w+)\\}.*", "$1")) {
                    case "add" -> a + b;
                    case "sub" -> a - b;
                    case "mul" -> a * b;
                    case "div" -> b != 0 ? a / b : Double.NaN;
                    default    -> 0;
                };
                String res = result % 1 == 0 ? String.valueOf((long)result) : String.format("%.4f", result);
                return response.replaceAll("\\{result:\\w+\\}", res);
            } catch (Exception e) {
                return "I couldn't compute that. Check the numbers!";
            }
        }

        // Named group: {1}
        for (int i = 1; i <= m.groupCount(); i++) {
            if (m.group(i) != null) {
                response = response.replace("{" + i + "}", capitalize(m.group(i)));
            }
        }
        return response;
    }

    private String insertName(String response) {
        return response.replace("{name}", userName);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    // ── Inner Classes ─────────────────────────────────────────────────────────
    private class Intent {
        final Pattern pattern;
        final String[] responses;

        Intent(String regex, String... responses) {
            this.pattern   = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.responses = responses;
        }

        String getRandomResponse() {
            return responses[rand.nextInt(responses.length)];
        }
    }
}
