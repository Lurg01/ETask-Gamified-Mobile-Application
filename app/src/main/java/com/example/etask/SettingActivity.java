package com.example.etask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingActivity extends AppCompatActivity {
    DBHelper DB;
    ImageButton back;
    Button updatePersonalInfoBtn;
    TextInputEditText firstname, lastname, pass;
    CircleImageView profileImageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_setting);

         back = findViewById(R.id.back_set);
         firstname = (TextInputEditText) findViewById(R.id.edit_firstname);
         lastname = (TextInputEditText) findViewById(R.id.editTxt_lastname);
         pass = (TextInputEditText) findViewById(R.id.edit_pass );
         profileImageUser = (CircleImageView) findViewById(R.id.profileImageUser);
         updatePersonalInfoBtn = (Button) findViewById(R.id.save_update);

         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent backIntent = new Intent(SettingActivity.this, HomeActivity.class);
                 startActivity(backIntent);
                 finish();
             }
         });


        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        DB = new DBHelper(this);
        Cursor res = DB.getUserTypeData();
        if (res.moveToFirst()) {
            switch (res.getString(0))
            {
                case "Leader":
                    databaseReference.child("Users")
                            .child("SK")
                            .child(uid)
                            .child("User Profile")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String k = childSnapshot.getKey();

                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("user_image").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String user_image = dataSnapshot.getValue(String.class);
                                                        Glide.with(SettingActivity.this).load(user_image).into(profileImageUser);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });


                    databaseReference.child("Users")
                            .child("SK")
                            .child(uid)
                            .child("User Private Info")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String k = childSnapshot.getKey();

                                        databaseReference.child("Users").child("SK").child(uid).child("User Private Info").child(k).child("firstname").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String fName = dataSnapshot.getValue(String.class);
                                                        firstname.setText(fName);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });

                                        databaseReference.child("Users").child("SK").child(uid).child("User Private Info").child(k).child("lastname").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String lName = dataSnapshot.getValue(String.class);
                                                        lastname.setText(lName);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });
                                        databaseReference.child("Users").child("SK").child(uid).child("User Private Info").child(k).child("password").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String password = dataSnapshot.getValue(String.class);
                                                        pass.setText(password);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });


                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });



                    updatePersonalInfoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            databaseReference.child("Users")
                                    .child("SK")
                                    .child(uid)
                                    .child("User Private Info")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                String k = childSnapshot.getKey();

                                                databaseReference.child("Users").child("SK").child(uid).child("User Private Info").child(k).child("firstname")
                                                        .setValue(firstname.getText().toString());


                                                databaseReference.child("Users").child("SK").child(uid).child("User Private Info").child(k).child("lastname")
                                                        .setValue(lastname.getText().toString());


                                                databaseReference.child("Users").child("SK").child(uid).child("User Private Info").child(k).child("password")
                                                        .setValue(pass.getText().toString()).addOnSuccessListener(suc ->
                                                        {
                                                            Toast.makeText(SettingActivity.this, "Security Updated.", Toast.LENGTH_SHORT).show();
                                                        });
                                            }


                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { }
                                    });

                        }
                    });

                    break;

                case "Participant":
                    databaseReference.child("Users")
                            .child("Participant")
                            .child(uid)
                            .child("User Profile")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String k = childSnapshot.getKey();
                                        databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("user_image").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String user_image = dataSnapshot.getValue(String.class);
                                                        Glide.with(SettingActivity.this).load(user_image).into(profileImageUser);


                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });

                    databaseReference.child("Users")
                            .child("Participant")
                            .child(uid)
                            .child("User Private Info")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String k = childSnapshot.getKey();

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(k).child("firstname").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String fName = dataSnapshot.getValue(String.class);
                                                        firstname.setText(fName);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(k).child("lastname").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String lName = dataSnapshot.getValue(String.class);
                                                        lastname.setText(lName);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(k).child("password").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String password = dataSnapshot.getValue(String.class);
                                                        pass.setText(password);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });
                                    }


                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });


                    updatePersonalInfoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            databaseReference.child("Users")
                                    .child("Participant")
                                    .child(uid)
                                    .child("User Private Info")
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                String k = childSnapshot.getKey();

                                                databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(k).child("firstname")
                                                                .setValue(firstname.getText().toString());


                                                databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(k).child("lastname")
                                                        .setValue(lastname.getText().toString());


                                                databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(k).child("password")
                                                        .setValue(pass.getText().toString()).addOnSuccessListener(suc ->
                                                                {
                                                                    Toast.makeText(SettingActivity.this, "Security Updated.", Toast.LENGTH_SHORT).show();
                                                                });
                                            }


                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { }
                                    });

                        }
                    });

                    break;
            }
        }

  }
}