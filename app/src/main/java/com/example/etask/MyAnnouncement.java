package com.example.etask;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class MyAnnouncement implements Serializable {


    @Exclude
    private String key1;
    private String title;
    private String description;
    public MyAnnouncement(){}
    public MyAnnouncement(String title, String description)
    {
        this.title = title;
        this.description = description;

    }

    public String getKey() {
        return key1;
    }

    public void setKey(String key1) {
        this.key1 = key1;
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




}
