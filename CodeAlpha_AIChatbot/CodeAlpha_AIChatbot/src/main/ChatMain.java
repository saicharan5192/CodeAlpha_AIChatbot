package main;

import engine.ChatHistory;
import engine.NLPEngine;
import models.ChatMessage;
import ui.ChatGUI;

import javax.swing.*;
import java.util.Scanner;

/**
 * Main entry — CALI AI Chatbot
 * CodeAlpha Java Internship — Task 3
 * Launches GUI mode by default; falls back to console with --console flag.
 */
public class ChatMain {

    public static void main(String[] args) {
        boolean consoleMode = args.length > 0 && args[0].equals("--console");

        if (consoleMode) {
            runConsoleMode();
        } else {
            // Try GUI mode
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            SwingUtilities.invokeLater(ChatGUI::new);
        }
    }

    /** Console fallback mode — fully functional without GUI. */
    static void runConsoleMode() {
        NLPEngine engine = new NLPEngine();
        ChatHistory history = new ChatHistory();
        Scanner sc = new Scanner(System.in);

        String CYAN   = "\u001B[36m";
        String GREEN  = "\u001B[32m";
        String YELLOW = "\u001B[33m";
        String RESET  = "\u001B[0m";
        String BOLD   = "\u001B[1m";

        System.out.println(CYAN + BOLD);
        System.out.println("  ╔══════════════════════════════════════════════╗");
        System.out.println("  ║     CALI — AI Chatbot (Console Mode)        ║");
        System.out.println("  ║     CodeAlpha Java Internship — Task 3      ║");
        System.out.println("  ╚══════════════════════════════════════════════╝" + RESET);
        System.out.println(GREEN + "\n  CALI: Hello! I'm CALI — your Java-powered AI assistant.");
        System.out.println("  CALI: Type 'help' for commands, 'save' to save chat, 'quit' to exit.\n" + RESET);

        while (true) {
            System.out.print(YELLOW + "  You: " + RESET);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;

            history.add(new ChatMessage(input, ChatMessage.Sender.USER));

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                history.saveToFile("chat_log.txt");
                System.out.println(GREEN + "  CALI: Goodbye! Chat saved. Take care!" + RESET);
                break;
            }

            if (input.equalsIgnoreCase("save")) {
                history.saveToFile("chat_log.txt");
                continue;
            }

            if (input.equalsIgnoreCase("clear")) {
                history.clear();
                System.out.println(GREEN + "  CALI: Chat cleared!" + RESET);
                continue;
            }

            // Simulate typing delay
            System.out.print("  ...");
            try { Thread.sleep(300 + (long)(Math.random() * 400)); } catch (InterruptedException ignored) {}
            System.out.print("\r");

            String response = engine.respond(input);
            history.add(new ChatMessage(response, ChatMessage.Sender.BOT));
            System.out.println(GREEN + "  CALI: " + response + RESET + "\n");
        }
    }
}
