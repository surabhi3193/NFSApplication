package com.nfsapp.surbhi.nfsapplication.beans;

public class Traveller  {

    private String id,name,product_pic, departure_airport, arrival_airport,date,distance;

    public Traveller() {
    }

    public Traveller(String id,String name,String product_pic, String departure_airport, String arrival_airport,String date, String distance) {
        this.id = id;
        this.name = name;
        this.product_pic = product_pic;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.date = date;
        this.distance = distance;
    }

    public String getProduct_pic() {
        return product_pic;
    }

    public void setProduct_pic(String product_pic) {
        this.product_pic = product_pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeparture_airport() {
        return departure_airport;
    }

    public void setDeparture_airport(String departure_airport) {
        this.departure_airport = departure_airport;
    }

    public String getArrival_airport() {
        return arrival_airport;
    }

    public void setArrival_airport(String arrival_airport) {
        this.arrival_airport = arrival_airport;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
