package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MyRedeemHistory implements Serializable{

    @Exclude
    private String key;
    private String user_image;
    private String firstname;
    private String lastname;
    private String datetime;

    public MyRedeemHistory(){}
    public MyRedeemHistory(String user_image, String firstname, String lastname, String datetime)
    {
        this.user_image = user_image;
        this.firstname = firstname;
        this.lastname = lastname;
        this.datetime = datetime;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
