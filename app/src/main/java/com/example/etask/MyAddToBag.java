package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MyAddToBag implements Serializable {


    @Exclude
    private String key;
    private String image;
    private String name;
    private String coins;


    public MyAddToBag(){}
    public MyAddToBag(String image, String name, String coins)
    {
        this.image = image;
        this.name = name;
        this.coins = coins;

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

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
}
