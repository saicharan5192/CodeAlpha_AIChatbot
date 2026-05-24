package engine;

import models.ChatMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores conversation history in memory and saves to file.
 */
public class ChatHistory {
    private final List<ChatMessage> messages = new ArrayList<>();
    private static final String DATA_DIR = "data/";

    public void add(ChatMessage msg) {
        messages.add(msg);
    }

    public List<ChatMessage> getAll() { return messages; }

    public void saveToFile(String filename) {
        new File(DATA_DIR).mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + filename))) {
            pw.println("=".repeat(60));
            pw.println("           CALI CHATBOT — CONVERSATION LOG");
            pw.println("=".repeat(60));
            for (ChatMessage msg : messages) {
                pw.println(msg.toString());
            }
            pw.println("=".repeat(60));
            pw.printf("Total messages: %d%n", messages.size());
            System.out.println("  ✔ Chat saved to " + DATA_DIR + filename);
        } catch (IOException e) {
            System.out.println("  ✘ Could not save chat: " + e.getMessage());
        }
    }

    public void clear() { messages.clear(); }
    public int size()   { return messages.size(); }
}
