package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single chat message (user or bot).
 */
public class ChatMessage {
    public enum Sender { USER, BOT }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String text;
    private final Sender sender;
    private final String timestamp;

    public ChatMessage(String text, Sender sender) {
        this.text      = text;
        this.sender    = sender;
        this.timestamp = LocalDateTime.now().format(FMT);
    }

    public String getText()      { return text; }
    public Sender getSender()    { return sender; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        String label = sender == Sender.USER ? "You" : "Bot";
        return "[" + timestamp + "] " + label + ": " + text;
    }
}
