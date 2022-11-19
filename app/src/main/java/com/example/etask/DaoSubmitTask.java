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

public class DaoSubmitTask {

    private final DatabaseReference databaseReference;
    String user_type = HomeActivity.Global.user_type;
    String uid = HomeActivity.Global.uid;
    String uid_of_sk = ParticipantSubmitTask.Global.uid_of_sk;
    String key = ParticipantSubmitTask.Global.key;

    public DaoSubmitTask(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        if (!user_type.equals(""))
        {
            databaseReference = db.getReference("Users").child("SK").child(uid).child("Task Data").child(key).child("Uploaded Task");
        }
        else
        {
            databaseReference = db.getReference("Users").child("SK").child(uid_of_sk).child("Task Data").child(key).child("Uploaded Task");
        }
    }

    public Task<Void> add(MySubmitTask mySub )
    {
        return databaseReference.push().setValue(mySub);
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
