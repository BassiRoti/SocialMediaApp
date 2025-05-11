package com.example.socialmediaapp;

public class Message {
    String sender;
    String text;
    long timestamp;

    public Message() {} // ✅ required for Firebase

    // ✅ This constructor must exist
    public Message(String sender, String text, long timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}