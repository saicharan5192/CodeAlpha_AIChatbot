package ui;

import engine.ChatHistory;
import engine.NLPEngine;
import models.ChatMessage;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dark-themed Swing GUI for CALI Chatbot.
 * Features: typing animation, scrollable chat, clean dark theme.
 */
public class ChatGUI extends JFrame {

    // ── Dark theme colors ────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(18, 18, 24);
    private static final Color BG_MSG       = new Color(28, 30, 38);
    private static final Color BG_PANEL     = new Color(22, 23, 30);
    private static final Color USER_BUBBLE  = new Color(99, 102, 241);   // indigo
    private static final Color BOT_BUBBLE   = new Color(36, 40, 56);
    private static final Color TEXT_WHITE   = new Color(230, 232, 240);
    private static final Color TEXT_MUTED   = new Color(130, 133, 155);
    private static final Color ACCENT       = new Color(99, 102, 241);
    private static final Color INPUT_BG     = new Color(32, 34, 48);
    private static final Color HEADER_BG    = new Color(20, 21, 30);

    private final NLPEngine engine = new NLPEngine();
    private final ChatHistory history = new ChatHistory();

    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private JButton sendBtn;
    private JLabel statusLabel;

    public ChatGUI() {
        setupWindow();
        setupHeader();
        setupChatArea();
        setupInputArea();
        setVisible(true);

        // Welcome message after short delay
        Timer t = new Timer(500, e -> appendBotMessage(
            "Hello! I'm CALI — CodeAlpha Language Intelligence.\n" +
            "I'm a Java-powered chatbot. Ask me anything!\n" +
            "Type 'help' to see what I can do."));
        t.setRepeats(false);
        t.start();
    }

    private void setupWindow() {
        setTitle("CALI — AI Chatbot  |  CodeAlpha Java Internship");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 620);
        setMinimumSize(new Dimension(600, 450));
        setLocationRelativeTo(null);
        setBackground(BG_DARK);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());
    }

    private void setupHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(45, 48, 68)));
        header.setPreferredSize(new Dimension(0, 64));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        left.setOpaque(false);

        // Avatar dot
        JLabel avatar = new JLabel("●");
        avatar.setForeground(new Color(52, 211, 153));
        avatar.setFont(new Font("Monospaced", Font.BOLD, 18));

        JLabel title = new JLabel("CALI — AI Chatbot");
        title.setForeground(TEXT_WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));

        JLabel version = new JLabel("  v1.0 • Java");
        version.setForeground(TEXT_MUTED);
        version.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        left.add(avatar);
        left.add(title);
        left.add(version);

        // Right: save + clear buttons
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        right.setOpaque(false);

        JButton saveBtn  = createIconBtn("Save Chat",  new Color(52, 211, 153));
        JButton clearBtn = createIconBtn("Clear Chat", new Color(239, 68, 68));

        saveBtn.addActionListener(e -> history.saveToFile("chat_log.txt"));
        clearBtn.addActionListener(e -> {
            chatPanel.removeAll();
            history.clear();
            chatPanel.revalidate();
            chatPanel.repaint();
            appendBotMessage("Chat cleared! Start a fresh conversation.");
        });

        right.add(saveBtn);
        right.add(clearBtn);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
    }

    private void setupChatArea() {
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(BG_MSG);
        chatPanel.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBackground(BG_MSG);
        scrollPane.getViewport().setBackground(BG_MSG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupInputArea() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(BG_PANEL);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(45, 48, 68)),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        inputField = new JTextField();
        inputField.setBackground(INPUT_BG);
        inputField.setForeground(TEXT_WHITE);
        inputField.setCaretColor(TEXT_WHITE);
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(55, 58, 80), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        inputField.putClientProperty("JTextField.placeholderText", "Type a message...");

        sendBtn = new JButton("Send ➤");
        sendBtn.setBackground(ACCENT);
        sendBtn.setForeground(Color.WHITE);
        sendBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sendBtn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        sendBtn.setFocusPainted(false);
        sendBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendBtn.setBorderPainted(false);
        sendBtn.setOpaque(true);

        statusLabel = new JLabel("Online");
        statusLabel.setForeground(new Color(52, 211, 153));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);
        bottomBar.add(inputField, BorderLayout.CENTER);
        bottomBar.add(sendBtn, BorderLayout.EAST);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.add(bottomBar, BorderLayout.CENTER);
        footerPanel.add(statusLabel, BorderLayout.SOUTH);

        inputPanel.add(footerPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // Actions
        ActionListener sendAction = e -> sendMessage();
        sendBtn.addActionListener(sendAction);
        inputField.addActionListener(sendAction);
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        inputField.setText("");

        appendUserMessage(text);
        history.add(new ChatMessage(text, ChatMessage.Sender.USER));

        // Show typing indicator then respond after brief delay
        statusLabel.setText("CALI is typing...");
        statusLabel.setForeground(TEXT_MUTED);
        int delay = 400 + (int)(Math.random() * 600);

        Timer t = new Timer(delay, e -> {
            String response = engine.respond(text);
            appendBotMessage(response);
            history.add(new ChatMessage(response, ChatMessage.Sender.BOT));
            statusLabel.setText("Online");
            statusLabel.setForeground(new Color(52, 211, 153));
        });
        t.setRepeats(false);
        t.start();
    }

    private void appendUserMessage(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextArea bubble = createBubble(text, USER_BUBBLE, Color.WHITE);
        row.add(bubble);
        chatPanel.add(row);
        scrollToBottom();
    }

    private void appendBotMessage(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel inner = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inner.setOpaque(false);

        JLabel icon = new JLabel("◈");
        icon.setForeground(ACCENT);
        icon.setFont(new Font("Monospaced", Font.BOLD, 16));
        icon.setAlignmentY(Component.TOP_ALIGNMENT);

        JTextArea bubble = createBubble(text, BOT_BUBBLE, TEXT_WHITE);
        inner.add(icon);
        inner.add(bubble);
        row.add(inner);
        chatPanel.add(row);
        scrollToBottom();
    }

    private JTextArea createBubble(String text, Color bg, Color fg) {
        JTextArea area = new JTextArea(text);
        area.setBackground(bg);
        area.setForeground(fg);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        FontMetrics fm = area.getFontMetrics(area.getFont());
        int maxW = Math.min(480, (int)(getWidth() * 0.65));
        area.setSize(maxW, Short.MAX_VALUE);
        int lines = Math.max(1, area.getPreferredSize().height / (fm.getHeight() + 2));
        area.setRows(lines);
        area.setColumns(0);
        area.setPreferredSize(new Dimension(maxW, area.getPreferredSize().height + 8));

        // Rounded via border trick
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.brighter(), 0, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        return area;
    }

    private JButton createIconBtn(String label, Color color) {
        JButton btn = new JButton(label);
        btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
        btn.setForeground(color);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        return btn;
    }

    private void scrollToBottom() {
        chatPanel.revalidate();
        chatPanel.repaint();
        SwingUtilities.invokeLater(() -> {
            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        });
    }
}
