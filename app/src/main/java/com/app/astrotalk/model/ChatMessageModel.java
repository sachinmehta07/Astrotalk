package com.app.astrotalk.model;

public class ChatMessageModel {
    private String messageId;
    private String message;
    private String senderId;

    public ChatMessageModel() {

    }

    public ChatMessageModel(String messageId, String message, String senderId) {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }
}
