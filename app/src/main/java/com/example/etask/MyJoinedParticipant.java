package com.example.etask;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Comparator;

public class MyJoinedParticipant  implements Serializable{

    @Exclude
    private String key;
    private String user_image;
    private String firstname;
    private String lastname;
    private String email;
    private int coins;

    public MyJoinedParticipant(){}

    public MyJoinedParticipant(String user_image, String firstname, String lastname, String email, int coins)
    {
        this.user_image = user_image;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.coins = coins;
    }

    public static Comparator<MyJoinedParticipant>  CoinsLToHComparator = new Comparator<MyJoinedParticipant>() {
        @Override
        public int compare(MyJoinedParticipant myJ1, MyJoinedParticipant myJ2) {
            return myJ1.getCoins() - myJ2.getCoins();
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "MyJoinedParticipant{" +
                "key='" + key + '\'' +
                ", user_image='" + user_image + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", coins='" + coins + '\'' +
                '}';
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
