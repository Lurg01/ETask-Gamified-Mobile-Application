package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MyLeaderboard implements Serializable {

    @Exclude
    private String key;
    private String coins;

    public MyLeaderboard(){}

    public MyLeaderboard(String coins)
    {
        this.coins = coins;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
}
