package com.example.community_link;

import java.io.Serializable;
import com.google.gson.*;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class ServiceData implements Serializable{
    private String userName;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String eventName;

    public ServiceData(){
    }

    public void setName(String name){
        this.userName = name;
    }

    public void setDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setTime(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
