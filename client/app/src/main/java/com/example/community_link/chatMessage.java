package com.example.community_link;

import com.google.gson.Gson;

//a simple strut to hold message info
//TODO: security private guards?
public class chatMessage {
    public String sender;
    public String recipient;
    public String timestamp;
    public String content;

    public chatMessage(String sender, String recipient, String timestamp, String content){
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.content = content;
    }

    @Override
    public String toString() {
        return "chatMessage{" +
                "sender=" + sender + '\n' +
                "recipient=" + recipient + '\n' +
                "timestamp=" + timestamp + '\n' +
                "content=" + content + '\n' +
                '}';
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }




}
