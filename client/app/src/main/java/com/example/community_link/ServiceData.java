package com.example.community_link;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String dow;
    private String type;
    private String description;

    public ServiceData(){
    }

    public void setName(String name){
        this.userName = name;
    }

    public void setDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(year, month, day-1);
        String dow = simpledateformat.format(date);
        this.dow = dow;
    }

    public void setTime(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
