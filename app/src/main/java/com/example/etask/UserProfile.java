package com.example.etask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    DBHelper DB;
    ImageButton back;
    TextView fname, lname, group_name,group_code, barangay, municipal, town, address, phone, hobby;
    CircleImageView profileImage;
    LinearLayout groupInfo, groupCode,participantInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_user_profile);

        participantInfo = (LinearLayout) findViewById(R.id.participant_info);
        groupInfo = (LinearLayout) findViewById(R.id.group_info_sk);
        groupCode = (LinearLayout) findViewById(R.id.group_code_sk);
        back = (ImageButton) findViewById(R.id.back_btn);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        fname = (TextView) findViewById(R.id.fname);           lname = (TextView) findViewById(R.id.lname);
        address = (TextView) findViewById(R.id.address);        phone = (TextView) findViewById(R.id.phone);
        hobby = (TextView) findViewById(R.id.hobby);
        group_name = (TextView) findViewById(R.id.group_name);   group_code= (TextView) findViewById(R.id.group_code);
        barangay = (TextView) findViewById(R.id.brgy);
        municipal = (TextView) findViewById(R.id.municipal);    town = (TextView) findViewById(R.id.town);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
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
//                                            Toast.makeText(getApplicationContext(), ""+uid_of_sk, Toast.LENGTH_SHORT).show();

                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("user_image")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String user_image = dataSnapshot.getValue(String.class);
                                                        Glide.with(UserProfile.this).load(user_image).into(profileImage);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("fname")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String firstname = dataSnapshot.getValue(String.class);
                                                        fname.setText(firstname);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("lname")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String lastname = dataSnapshot.getValue(String.class);
                                                        lname.setText(lastname);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("group_name")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String gname = dataSnapshot.getValue(String.class);
                                                        group_name.setText(gname);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("barangay")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String brgy = dataSnapshot.getValue(String.class);
                                                        barangay.setText(brgy);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("municipal")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String mun = dataSnapshot.getValue(String.class);
                                                        municipal.setText(mun);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("town")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String twn = dataSnapshot.getValue(String.class);
                                                        town.setText(twn);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });


                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("group_code")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String gcode = dataSnapshot.getValue(String.class);
                                                        group_code.setText(gcode);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                    break;

                case "Participant":
                    groupInfo.setVisibility(View.GONE);
                    groupCode.setVisibility(View.GONE);

                    databaseReference.child("Users")
                            .child("Participant")
                            .child(uid)
                            .child("User Profile")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String k = childSnapshot.getKey();
//

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("user_image")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String user_image = dataSnapshot.getValue(String.class);
                                                        Glide.with(UserProfile.this).load(user_image).into(profileImage);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                        databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("fname")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String firstname = dataSnapshot.getValue(String.class);
                                                        fname.setText(firstname);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("lname")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String lastname = dataSnapshot.getValue(String.class);
                                                        lname.setText(lastname);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("address")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String my_address = dataSnapshot.getValue(String.class);
                                                        address.setText(my_address);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("phone")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String my_phone = dataSnapshot.getValue(String.class);
                                                        phone.setText(my_phone);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("hobby")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String my_hobby = dataSnapshot.getValue(String.class);
                                                        hobby.setText(my_hobby);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                    break;
            }
        }


    }
}