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
    private int id;
    private int maxCapacity;
    private int numPeople;

    public ServiceData(){
        longi = -123.116226;
        lat = 49.246292;
        numPeople = 0;
        maxCapacity = 1;
    }

    public void setName(String name){
        this.owner = name;
    }

    public void setDate(int year, int month, int day){
        this.date = year+"-"+(month + 1)+"-"+day;
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

    public void setLongi(double longi){ this.longi = longi;}

    public void setLat(double lat){ this.lat = lat;}

    public void setMaxCapacity(int maxCapacity){this.maxCapacity = maxCapacity;}

    public boolean isFull(){return numPeople >= maxCapacity;}

    public void addPeople(){
        if(!this.isFull()){
            numPeople++;
        }
    }

    public String toString(){
        return  "ID: " + id + "\n" +
                "Owner: " + this.owner + "\n" +
                "Date: " + this.date + "\n" +
                "Time: " + this.time  + "\n" +
                "Event Name: " + this.name + "\n" +
                "Day of Week: " + this.dow + "\n" +
                "Type: " + this.type + "\n" +
                "Description: " + this.description + "\n" +
                "Coordinate: (" + this.lat + "," + this.longi + ")\n" +
                "maxCapacity: " + this.maxCapacity + "\n" +
                "numPeople: " + this.numPeople + "\n";
    }

    public double getLongi () {
        return this.longi;
    }

    public double getLat() {
        return this.lat;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getDow() {
        return dow;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public int getNumPeople() { return numPeople; }

    public int getMaxCapacity() { return maxCapacity; }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
