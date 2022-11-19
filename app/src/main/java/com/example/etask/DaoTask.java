package com.example.etask;

import android.database.Cursor;
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

public class DaoTask {

    private final DatabaseReference databaseReference;
    //DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    //String uName = NavHomeActivity.Global.user_name;
    // Initialize variable

    String user_type = HomeActivity.Global.user_type; // can be move to task activity
    String user_id = HomeActivity.Global.uid;
    String uid_of_sk = TaskActivity.Global.uid_of_sk;
    public DaoTask(){

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        if (!user_type.equals(""))
        {
            databaseReference = db.getReference("Users").child("SK").child(user_id).child("Task Data");
        }
        else
        {
            databaseReference = db.getReference("Users").child("SK").child(uid_of_sk).child("Task Data");
        }

    }

    public Task<Void> add(MyTask myt )
    {
        return databaseReference.push().setValue(myt);
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
