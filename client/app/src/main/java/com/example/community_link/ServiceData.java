package com.example.community_link;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.*;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class ServiceData implements Serializable{
    private String owner;
    private String date;
    private String time;
    private String name;
    private String dow;
    private String type;
    private String description;
    private double longi;
    private double lat;

    public ServiceData(){
        longi = -123.116226;
        lat = 49.246292;
    }

    public void setName(String name){
        this.owner = name;
    }

    public void setDate(int year, int month, int day){
        this.date = year+"-"+month+"-"+day;
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(year, month, day-1);
        String dow = simpledateformat.format(date);
        this.dow = dow;
    }

    public void setTime(int hour, int minute){
        this.time = hour+":"+minute;
    }

    public void setEventName(String eventName){
        this.name = eventName;
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
