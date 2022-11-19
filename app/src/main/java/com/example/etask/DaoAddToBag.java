package com.example.etask;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
public class DaoAddToBag {

    private DatabaseReference databaseReference;

    String uid = RedeemRewards.Global.uid_of_participant;

    public DaoAddToBag()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        databaseReference = db.getReference("Users").child("Participant").child(uid).child("My Bag");

    }
    public Task<Void> add(MyAddToBag myAdd)
    {
        // if( myt == null ) throw exception
        return databaseReference.push().setValue(myAdd);
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
