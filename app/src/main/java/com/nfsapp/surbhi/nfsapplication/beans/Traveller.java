package com.nfsapp.surbhi.nfsapplication.beans;

public class Traveller  {

    private String name, departure_airport, arrival_airport,date,distance;

    public Traveller() {
    }

    public Traveller(String name, String departure_airport, String arrival_airport,String date, String distance) {
        this.name = name;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.date = date;
        this.distance = distance;
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
