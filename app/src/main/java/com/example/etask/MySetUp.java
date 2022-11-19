package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MySetUp implements Serializable{

    @Exclude
    private String key, user_image, fname, lname, group_name, group_code, barangay, municipal, town;

    public MySetUp(){}

    public MySetUp(String user_image, String fname, String lname, String group_name,String group_code, String barangay, String municipal, String town)
    {
        this.user_image = user_image;
        this.fname = fname;             this.lname = lname;
        this.group_name = group_name;   this.group_code = group_code;
        this.barangay = barangay;       this.municipal = municipal;
        this.town = town;

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

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getMunicipal() {
        return municipal;
    }

    public void setMunicipal(String municipal) {
        this.municipal = municipal;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
