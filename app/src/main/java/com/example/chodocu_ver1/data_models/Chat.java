package com.example.chodocu_ver1.data_models;

public class Chat {

    private String chatID;
    private  String sender;
    private String receiver;
    private String messega;

    public Chat() {
    }

    public Chat(String chatID, String sender, String receiver, String messega) {
        this.chatID = chatID;
        this.sender = sender;
        this.receiver = receiver;
        this.messega = messega;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessega() {
        return messega;
    }

    public void setMessega(String messega) {
        this.messega = messega;
    }
}
