package com.nfsapp.surbhi.nfsapplication.beans;

public class Confirmpackage {

    private String id="",
            sender_id="",name="",product_pic="", departure_airport="", arrival_airport="",date="",status="",flight_no="",user_type="";

    public Confirmpackage() {
    }

    public Confirmpackage(String id, String name, String product_pic, String departure_airport, String arrival_airport
            ,String flight_no, String date,String user_type ,String status) {
        this.id = id;
        this.sender_id = sender_id;
        this.name = name;
        this.product_pic = product_pic;
        this.departure_airport = departure_airport;
        this.arrival_airport = arrival_airport;
        this.flight_no = flight_no;
        this.user_type = user_type;
        this.date = date;
        this.status = status;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getFlight_no() {
        return flight_no;
    }

    public void setFlight_no(String flight_no) {
        this.flight_no = flight_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
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

}
