package com.example.etask;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.security.Signature;
import java.util.HashMap;


public class DaoUsername {
    private DatabaseReference databaseReference;

    String user_type = UserSignup.Global.user;
    String uid = UserSignup.Global.uid;
    public DaoUsername(){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Users").child(user_type).child(uid).child("User Private Info");

/*
        databaseReference.child(last_name).get().addOnSuccessListener(dataSnapshot -> {
            databaseReference.child(new_user_name).setValue(dataSnapshot.getValue());
            databaseReference.child(last_name).removeValue();

        });

 */

    }
    public Task<Void> add(GetUsername getU )
    {
        return databaseReference.push().setValue(getU);
    }


    public Query get()
    {
        return databaseReference;
    }
}
