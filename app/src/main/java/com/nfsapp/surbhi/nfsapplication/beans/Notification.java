package com.nfsapp.surbhi.nfsapplication.beans;

public class Notification {

    private String id,pic,name,msg, date,product_id;
    int type;

    public Notification() {
    }

    public Notification(String id,String pic, String name,String product_id, String msg, String date,int type) {
        this.id = id;
        this.pic = pic;
        this.name = name;
        this.msg = msg;
        this.date = date;
        this.product_id = product_id;
        this.type = type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
