package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MyReward implements Serializable{


    @Exclude
    private String key;
    private String image;
    private String name;
    private String description;
    private String quantity;
    private String coins;
    private String date;
    private String time;


    public MyReward(){}
    public MyReward(String image, String name, String description, String quantity, String coins, String date, String time)
    {
        this.image = image;
        this.name = name;
        this.description = description;
        this.coins = coins;
        this.date = date;
        this.time = time;
        this.quantity = quantity;



    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
