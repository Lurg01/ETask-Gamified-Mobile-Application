package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MySubmitTask implements Serializable{

    @Exclude
    private String key;
    private String uid;
    private String full_name;
    private String imageUrl_start;
    private String imageUrl_done;
    private String my_location;
    private String statement;

    public MySubmitTask(){}
    public MySubmitTask(String uid, String full_name, String imageUrl_start, String imageUrl_done, String my_location, String statement)
    {
        this.uid = uid;
        this.full_name = full_name;
        this.imageUrl_start = imageUrl_start;
        this.imageUrl_done = imageUrl_done;
        this.my_location = my_location;
        this.statement = statement;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getImageUrl_start() {
        return imageUrl_start;
    }

    public void setImageUrl_start(String imageUrl_start) {
        this.imageUrl_start = imageUrl_start;
    }

    public String getImageUrl_done() {
        return imageUrl_done;
    }

    public void setImageUrl_done(String imageUrl_done) {
        this.imageUrl_done = imageUrl_done;
    }

    public String getMy_location() {
        return my_location;
    }

    public void setMy_location(String my_location) {
        this.my_location = my_location;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}
