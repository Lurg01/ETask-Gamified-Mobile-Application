package com.example.etask;
import com.google.firebase.database.Exclude;
import java.io.Serializable;
public class MyCoins implements Serializable {
    @Exclude
    private String key;
    private String coins;

    public MyCoins(){}

    public MyCoins(String coins)
    {
        this.coins = coins;

    }

}
