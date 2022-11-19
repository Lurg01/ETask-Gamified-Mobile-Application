package com.example.etask;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DaoReward {


    private DatabaseReference databaseReference;
    String user_type = HomeActivity.Global.user_type; // can be move to task activity
    String uid = HomeActivity.Global.uid;
    String uid_of_sk = RewardActivity.Global.uid_of_sk;
    public DaoReward()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        // databaseReference = db.getReference(MyRewards.class.getSimpleName());

        if (!user_type.equals(""))
        {
            databaseReference = db.getReference("Users").child("SK").child(uid).child("Reward Data");
        }
        else
        {
            databaseReference = db.getReference("Users").child("SK").child(uid_of_sk).child("Reward Data");
        }

    }
    public Task<Void> add(MyReward myR)
    {
        // if( myt == null ) throw exception
        return databaseReference.push().setValue(myR);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key)
    {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key)
    {
        if(key == null)
        {
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.orderByKey().startAfter(key).limitToFirst(8);

    }

    public Query get()
    {
        return databaseReference;
    }

}
