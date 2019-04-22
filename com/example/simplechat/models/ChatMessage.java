package com.example.simplechat.models;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String groupType;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String message;

    

    public ChatMessage(String groupType, String senderId, String senderName, String receiverId, String receiverName, String message) {
        this.groupType = groupType;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.message = message;
    }

    public String getGroupType() {
        return groupType;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessage() {
        return message;
    }
}
