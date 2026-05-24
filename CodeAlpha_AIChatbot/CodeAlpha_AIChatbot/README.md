# CALI — AI Chatbot 🤖

A smart rule-based chatbot with NLP pattern matching, dark Swing GUI, and conversation history.

## Features
- ✅ Beautiful dark-themed Swing GUI
- ✅ NLP engine with regex pattern matching (30+ intents)
- ✅ Math calculations (add, subtract, multiply, divide)
- ✅ Java/OOP Q&A knowledge base
- ✅ Typing animation with response delay
- ✅ Conversation history with timestamps
- ✅ Save chat to file
- ✅ Console mode (fallback)
- ✅ Random response variation (not always same answer)
- ✅ Name memory (remembers your name in conversation)

## How to Run

### GUI Mode (recommended):
```bash
cd src
javac models/*.java engine/*.java ui/ChatGUI.java main/ChatMain.java
java -cp . main.ChatMain
```

### Console Mode:
```bash
java -cp . main.ChatMain --console
```

## Architecture
- `NLPEngine` — Pattern matching, intent detection, response generation
- `ChatHistory` — Stores and exports conversation
- `ChatGUI` — Swing-based dark UI
- `ChatMessage` — Message data model

## Sample Conversations
- "What is OOP?" → Java concepts
- "Tell me a joke" → Programmer humor
- "25 + 17" → Math calculation
- "My name is Alex" → Name memory
- "Motivate me" → Inspirational quotes

## Author
CodeAlpha Java Internship — Task 3
