package com.example.etask;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DaoAnnouncement {

    private  DatabaseReference databaseReference;
    String user_type = HomeActivity.Global.user_type; // can be move to task activity
    String uid = HomeActivity.Global.uid;
    String uid_of_sk = AnnouncementActivity.Global.uid_of_sk;
    public DaoAnnouncement()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //databaseReference = db.getReference(MyAns.class.getSimpleName());


        if (!user_type.equals(""))
        {
            databaseReference = db.getReference("Users").child("SK").child(uid).child("Announcement Data");
        }
        else
        {
            databaseReference = db.getReference("Users").child("SK").child(uid_of_sk).child("Announcement Data");
        }


    }
    public Task<Void> add(MyAnnouncement myAns)
    {

        return databaseReference.push().setValue(myAns);
    }

    public Task<Void> update(String key1, HashMap<String, Object> hashMap)
    {
        return databaseReference.child(key1).updateChildren(hashMap);
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
