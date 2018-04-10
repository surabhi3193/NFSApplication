package com.nfsapp.surbhi.nfsapplication.beans;

public final  class User {
    private static volatile User instance;


    private String id,name,profile_pic, location, profile_percent,email,phone,account_no,id_image;

    public User() {
    }
    public static User getInstance() {
        if (instance == null) {
            synchronized (User.class) {
                if (instance == null) {
                    instance = new User();
                }
            }
        }
        return instance;
    }


    public User(String id, String name, String profile_pic, String location, String profile_percent, String email,
                String phone, String account_no,String id_image) {
        this.id = id;
        this.name = name;
        this.profile_pic = profile_pic;
        this.location = location;
        this.profile_percent = profile_percent;
        this.email = email;
        this.phone = phone;
        this.account_no = account_no;
        this.id_image = id_image;
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

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
        System.out.println("=========== profile pic ========");
        System.out.println(profile_pic);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile_percent() {
        return profile_percent;
    }

    public void setProfile_percent(String profile_percent) {
        this.profile_percent = profile_percent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getId_image() {
        return id_image;
    }

    public void setId_image(String id_image) {
        this.id_image = id_image;
    }
}
