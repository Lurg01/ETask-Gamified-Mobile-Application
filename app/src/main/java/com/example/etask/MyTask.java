package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MyTask implements Serializable{

    @Exclude
    private String key;
    private String title;
    private String description;
    private String coins;
    private String date;
    private String time;


    public MyTask(){}

    public MyTask(String title, String description, String coins, String date, String time)
    {
        this.title = title;
        this.description = description;
        this.coins = coins;
        this.date = date;
        this.time = time;


    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
