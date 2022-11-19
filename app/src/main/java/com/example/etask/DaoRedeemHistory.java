package com.example.etask;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DaoRedeemHistory {

    private final DatabaseReference databaseReference;
    String uid = RedeemRewards.Global.uid_of_sk;
    String key = RedeemRewards.Global.key;

    public DaoRedeemHistory(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();

      /*  if (!user_type.equals(""))
        {*/
            databaseReference = db.getReference("Users").child("SK").child(uid).child("Reward Data").child(key).child("Redeem History");
       /* }
        else
        {
            databaseReference = db.getReference("Users").child("SK").child(uid_of_sk).child("Reward Data").child(key).child("Uploaded Task");
        }*/
    }

    public Task<Void> add(MyRedeemHistory myRdm )
    {
        return databaseReference.push().setValue(myRdm);
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
