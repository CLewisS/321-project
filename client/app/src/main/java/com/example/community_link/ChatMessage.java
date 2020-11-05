package com.example.community_link;

import com.google.gson.Gson;

//a simple strut to hold message info
public class ChatMessage {
    public String sender;
    public String recipient;
    public String timestamp;
    public String content;

    public ChatMessage(String sender, String recipient, String timestamp, String content){
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "sender=" + sender + '\n' +
                "recipient=" + recipient + '\n' +
                "timestamp=" + timestamp + '\n' +
                "content=" + content + '\n' +
                '}';
    }

    //this is optimized for logical comparision, not for human reading.
    public String compactString(){
        return timestamp+"_"+content+"_"+recipient+"_"+sender;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }




}
