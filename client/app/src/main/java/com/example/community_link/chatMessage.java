package com.example.community_link;

import com.google.gson.Gson;

//a simple strut to hold message info
//TODO: security private guards?
public class chatMessage {
    public String sender;
    public String receiver;
    public String timeStamp;
    public String content;

    public chatMessage(String sender, String receiver, String timeStamp, String content){
        this.sender = sender;
        this.receiver = receiver;
        this.timeStamp = timeStamp;
        this.content = content;
    }

    @Override
    public String toString() {
        return "chatMessage{" +
                "sender=" + sender + '\n' +
                "receiver=" + receiver + '\n' +
                "timeStamp=" + timeStamp + '\n' +
                "content=" + content + '\n' +
                '}';
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }




}
