package com.example.etask;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Objects;

public class DaoSetUp {


    private DatabaseReference databaseReference;
    //DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    //String uName = NavHomeActivity.Global.user_name;
    String user_type = SetUpActivity.Global.user_type;
    String uid = SetUpActivity.Global.uid;

    public DaoSetUp(){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        if (!user_type.equals(""))
        {
            databaseReference = db.getReference("Users").child("SK").child(uid).child("User Profile");
        }
        else
        {
            databaseReference = db.getReference("Users").child("Participant").child(uid).child("User Profile");
        }

    }

    public Task<Void> add(MyParticipantSetup myPS )
    {
        return databaseReference.push().setValue(myPS);
    }

    public Task<Void> add(MySetUp mys )
    {
        return databaseReference.push().setValue(mys);
    }
    public Task<Void> update(String key, HashMap<String, Object> hashMap) { return databaseReference.child(key).updateChildren(hashMap); }
    public Task<Void> remove(String key)
    {
        return databaseReference.child(key).removeValue();
    }
    public Query get(String key)
    { if(key == null) { return databaseReference.orderByKey().limitToFirst(8); } return databaseReference.orderByKey().startAfter(key).limitToFirst(8); }

    public Query get()
    {
        return databaseReference;
    }

}
