package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MyParticipantSetup implements Serializable {

    @Exclude
    private String key, user_image, fname, lname, address, phone, hobby;

    public MyParticipantSetup(){}

    public MyParticipantSetup(String user_image, String fname, String lname, String address, String phone, String hobby)
    {
        this.user_image = user_image;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phone = phone;
        this.hobby = hobby;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
}
