package com.example.etask;

import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Objects;

public class DaoLeaderboard {


    private DatabaseReference databaseReference;

    String uid_of_sk = MemberActivity.Global.uid_of_sk_from_memberActivity;
    public DaoLeaderboard()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        // databaseReference = db.getReference(MyRewards.class.getSimpleName());

            databaseReference = db.getReference("Users").child("SK").child(uid_of_sk).child("Leaderboard");


    }
    public Task<Void> add(MyLeaderboard myL)
    {
        // if( myt == null ) throw exception
        return databaseReference.push().setValue(myL);
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
