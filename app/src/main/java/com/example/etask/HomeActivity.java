package com.example.etask;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.ui.email.RecoverPasswordActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity {


    // PARTICIPANT USES
    Button joinBtn, clearBtn;
    EditText code;
    LinearLayout joinGroupContent, taskNavHide, announcementsNavHide, groupNavHide, rewardsNavHide;
    TextInputLayout txtField;

    // SK USES
    DBHelper DB;
    Handler h = new Handler();
    ScrollView skContent;
    // INITIALIZE VARIABLE
    DrawerLayout drawerLayout;
    CircleImageView profileImage;
    ImageButton qr;
    ImageButton totalCoinsBtn;
    FirebaseAuth auth;
    TextView user_email, firstname, lastname, groupName,
            nav_home, navDrawer_participants, totalCoins, wfName;

    public static class Global { public static String uid, user_type, key; }
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;
    ActionBar actionBar;
    CardView trees,waste,recycle;
    String uid_of_sk_holder = "", generated_code_holder = "", total_coins = "", email_from_sk = "", email_from_participant = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull (getSupportActionBar ()).hide ();
        setContentView(R.layout.activity_home);

        // INIT FIREBASE AUTH
        auth = FirebaseAuth.getInstance();
        // CONFIGURE ACTION BAR
        actionBar = getSupportActionBar();
        actionBar.setTitle("Join Group");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // CONFIGURE PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Joining Group . . .");
        progressDialog.setCanceledOnTouchOutside(false);

        code = (EditText) findViewById(R.id.code);
        joinBtn = (Button) findViewById(R.id.join_btn);
        clearBtn = (Button) findViewById(R.id.clear_button);
        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        qr = (ImageButton) findViewById(R.id.qr);
        totalCoins = (TextView) findViewById(R.id.total_coins);
        totalCoinsBtn = (ImageButton) findViewById(R.id.total_coins_btn);
        firstname = (TextView) findViewById(R.id.fName_txt);
        lastname = (TextView) findViewById(R.id.lName_txt);
        user_email = (TextView) findViewById(R.id.email_txt);
        groupName = (TextView) findViewById(R.id.group_name);
        wfName = (TextView) findViewById(R.id.fName);
        navDrawer_participants = (TextView) findViewById(R.id.navDrawer_participants);
        skContent = (ScrollView) findViewById(R.id.sk_content);
        joinGroupContent = (LinearLayout) findViewById(R.id.joinGroup_content);
        txtField = (TextInputLayout) findViewById(R.id.filledTextField);
        taskNavHide = (LinearLayout) findViewById(R.id.taskNavHide);
        announcementsNavHide = (LinearLayout) findViewById(R.id.announcementsNavHide);
        groupNavHide = (LinearLayout) findViewById(R.id.groupNavHide);
        rewardsNavHide = (LinearLayout) findViewById(R.id.rewardsNavHide);
        trees = (CardView) findViewById(R.id.cv_trees);
        recycle = (CardView) findViewById(R.id.cv_recycle);
        waste = (CardView) findViewById(R.id.cv_waste);
        nav_home = (TextView) findViewById(R.id.navDrawer_home);
        nav_home.setTextColor(Color.WHITE);
        nav_home.setBackgroundColor(getResources().getColor(R.color.lightGray));

        // Google Sign In Options
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id1))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity()); - NOT A COMMENT !!!
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        // get current user
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // user logged in, get info
        String personEmail = firebaseUser.getEmail();
        String personName = firebaseUser.getDisplayName();
        user_email.setText(personEmail);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uid = currentFirebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // CHECK WHAT USER TYPE IS AUTHENTICATED
        DB = new DBHelper(this);
        Cursor res = DB.getUserTypeData();
        if (res.moveToFirst()) {
            switch (res.getString(0))
            {
                case "Leader":
                    skContent.setVisibility(View.VISIBLE);
                    totalCoins.setVisibility(View.GONE);
                    totalCoinsBtn.setVisibility(View.GONE);
                    joinGroupContent.setVisibility(View.GONE);
                    Global.uid = currentFirebaseUser.getUid();
                    Global.user_type = "SK";
                    // Retrieve Firstname and Lastname of SK
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
                                                        Glide.with(HomeActivity.this).load(user_image).into(profileImage);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });

                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("fname").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String fName = dataSnapshot.getValue(String.class);
                                                        firstname.setText(fName);
                                                        wfName.setText(fName);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });

                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("lname").addValueEventListener(

                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String lName = dataSnapshot.getValue(String.class);
                                                        lastname.setText(lName);


                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });


                                        databaseReference.child("Users").child("SK").child(uid).child("User Profile").child(k).child("group_name").addValueEventListener(

                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String gName = dataSnapshot.getValue(String.class);
                                                        groupName.setText(gName);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });

                    // NOTIFIED WHEN NEW PARTICIPANT JOINED
         /*           databaseReference.child("Users").child("SK").child(uid).child("Joined Participant").addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int count = 0;
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String request = childSnapshot.getKey();

                                        if (!request.equals(""))
                                        {
                                            Toast.makeText(getApplicationContext(), "request > " + request, Toast.LENGTH_SHORT).show();
                                            count += 1;

                                        }
                                        notification.setText(String.valueOf(count));

                                    }


                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });
*/

                    break;

                case "Participant":
                    navDrawer_participants.setText("Group");
                    Global.user_type = "";
                    qr.setVisibility(View.GONE);
                    // UID FOR PATH
                    DB = new DBHelper(this);
                    Cursor res_uid = DB.getUidData();
                    if (res_uid.moveToFirst()) {
                        Global.uid = res_uid.getString(0);
                    }
                    // RETRIEVE DP
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
                                                        Glide.with(HomeActivity.this).load(user_image).into(profileImage);
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
                                                        wfName.setText(fName);

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


                                        databaseReference.child("Users").child("Participant").child(uid).child("group_name").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String gName = dataSnapshot.getValue(String.class);
                                                        groupName.setText(gName);

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) { }
                                                });                                                      }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });

                    // DISPLAY TOTAL COINS OF PARTICIPANT
                    databaseReference.child("Users").child("Participant").child(uid).child("Total Coins").addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String total_coins = dataSnapshot.getValue(String.class);

                            /*        if (!totalCoins.getText().toString().equals(total_coins))
                                    {
                                        showReceivedCoins();
                                    }*/
                                    totalCoins.setText(total_coins);
                                    if (totalCoins.getText().toString().equals(""))
                                    {
                                        totalCoins.setText("0");
                                        databaseReference.child("Users").child("Participant").child(uid).child("Total Coins")
                                                .setValue("0");
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) { }
                            });


                    // GET CURRENT UID
                    databaseReference.child("Users").child("Participant").child(uid).child("Total Coins").addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    total_coins = dataSnapshot.getValue(String.class);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });



                    // GET UID OF SK FOR ITERATION
                    if (res_uid.moveToFirst())
                    {
                        // GET EMAIL FROM PARTICIPANT AND SK, THEN IF MATCH UPDATE THE JOINED PARTICIPANT COINS
                        databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            String userPrivateInfo_key = childSnapshot.getKey();
                                            assert userPrivateInfo_key != null;
                                            databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(userPrivateInfo_key).child("email").addValueEventListener(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            email_from_participant = dataSnapshot.getValue(String.class);

                                                            databaseReference.child("Users").child("SK").child(res_uid.getString(0)).child("Joined").addValueEventListener(
                                                                    new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                                String joined_key = childSnapshot.getKey();
                                                                                assert joined_key != null;
                                                                                databaseReference.child("Users").child("SK").child(res_uid.getString(0)).child("Joined").child(joined_key).child("email").addValueEventListener(
                                                                                        new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                email_from_sk = dataSnapshot.getValue(String.class);

                                                                                                assert email_from_sk != null;
                                                                                                if (email_from_sk.equals(email_from_participant))
                                                                                                {
                                                                                                    int tl_coins = Integer.parseInt(total_coins);
                                                                                                    databaseReference.child("Users").child("SK").child(res_uid.getString(0)).child("Joined").child(joined_key).child("coins").setValue(tl_coins);
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(DatabaseError databaseError) {
                                                                                            }
                                                                                        });

                                                                            }
                                                                        }
                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) { }
                                                                    });



                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) { }
                                                    });

                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });


                    }

                    // CHECK IF PARTICIPANT ALREADY HAVE UID OF SK ELSE GET THE SK UID IF THE GENERATED KEY IS MATCHED
                    Handler h = new Handler();
                    DB = new DBHelper(this);
                    if (res_uid.getCount() != 0 )
                    {
                        joinGroupContent.setVisibility(View.GONE);
                        skContent.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        taskNavHide.setVisibility(View.GONE);
                        announcementsNavHide.setVisibility(View.GONE);
                        groupNavHide.setVisibility(View.GONE);
                        rewardsNavHide.setVisibility(View.GONE);

                        // CLEAR BUTTON
                        clearBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                code.setText("");
                            }
                        });

                        // JOIN BUTTON FUNCTION
                        joinBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // SHOW PROGRESS DIALOG
                                progressDialog.show();

                                if (TextUtils.isEmpty(code.getText().toString()))
                                {
                                    Toast.makeText(getApplicationContext(), "Please enter group code. ", Toast.LENGTH_SHORT).show();
                                    // DISMISS PROGRESS DIALOG
                                    progressDialog.dismiss();
                                }
                                else
                                {
                                    //  ITERATE TO SK DATA AND IF THE CODE ENTERED BY PARTICIPANT MATCH TO THE LEADER GROUP CODE THEN PARTICIPANT SHOULD  SUCCESSFULLY JOINED
                                    databaseReference.child("Users")
                                            .child("SK")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                        String uid_of_sk = childSnapshot.getKey();
                                                        assert uid_of_sk != null;
                                                        databaseReference.child("Users").child("SK").child(uid_of_sk).child("User Profile")
                                                                .addValueEventListener(new ValueEventListener() {
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                            String profile_key = childSnapshot.getKey();
                                                                            assert profile_key != null;
                                                                            databaseReference.child("Users").child("SK").child(uid_of_sk).child("User Profile").child(profile_key).child("group_code").addValueEventListener(
                                                                                    new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            String generated_code = dataSnapshot.getValue(String.class);

                                                                                            assert generated_code != null;
                                                                                            if (generated_code.equals(code.getText().toString()))
                                                                                            {
                                                                                                generated_code_holder = generated_code;
                                                                                                Global.uid = uid_of_sk; // DAOJOINEDPARTICIPANT, FOR JOINING A GROUP
                                                                                                uid_of_sk_holder = uid_of_sk;

                                                                                                databaseReference.child("Users").child("SK").child(uid_of_sk).child("User Profile").child(profile_key).child("group_name").addValueEventListener(
                                                                                                        new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                String gName = dataSnapshot.getValue(String.class);
                                                                                                                groupName.setText(gName);

                                                                                                                databaseReference.child("Users").child("Participant").child(uid).child("group_name")
                                                                                                                        .setValue(gName);
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onCancelled(DatabaseError databaseError) {
                                                                                                            }
                                                                                                        });

                                                                                                databaseReference.child("Users").child("Participant").child(uid).child("group_code")
                                                                                                        .setValue(code.getText().toString()).addOnSuccessListener(suc ->
                                                                                                        {
                                                                                                            Toast.makeText(getApplicationContext(), "You have Successfully joined this group.", Toast.LENGTH_SHORT).show();

                                                                                                        }).addOnFailureListener(er->

                                                                                                        {
                                                                                                            Toast.makeText(getApplicationContext(), ""+ er.getMessage(),  Toast.LENGTH_SHORT).show();

                                                                                                        });
                                                                                            }
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
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                }
                                            });

                                    h.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (generated_code_holder.equals(code.getText().toString()))
                                            {
                                                // GET PARTICIPANT DATA TO DISPLAY IN PARTICIPANT LIST
                                                databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").addValueEventListener(
                                                        new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                    String privateInfo_key = childSnapshot.getKey();

                                                                    assert privateInfo_key != null;
                                                                    databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(privateInfo_key).child("firstname").addValueEventListener(
                                                                            new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    String fName = dataSnapshot.getValue(String.class);

                                                                                    databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(privateInfo_key).child("lastname").addValueEventListener(
                                                                                            new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                    String lName = dataSnapshot.getValue(String.class);

                                                                                                    databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(privateInfo_key).child("email").addValueEventListener(
                                                                                                            new ValueEventListener() {
                                                                                                                @Override
                                                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                    String email = dataSnapshot.getValue(String.class);

                                                                                                                    databaseReference.child("Users").child("Participant").child(uid).child("User Profile").addValueEventListener(
                                                                                                                            new ValueEventListener() {
                                                                                                                                @Override
                                                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                                                                                        String profile_key = childSnapshot.getKey();
                                                                                                                                        assert profile_key != null;

                                                                                                                                        databaseReference.child("Users").child("Participant").child(uid).child("Total Coins").addValueEventListener(
                                                                                                                                                new ValueEventListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                        total_coins = dataSnapshot.getValue(String.class);
                                                                                                                                                    }

                                                                                                                                                    @Override
                                                                                                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                                                                                                    }
                                                                                                                                                });

                                                                                                                                        databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(profile_key).child("user_image").addValueEventListener(
                                                                                                                                                new ValueEventListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                        String user_image = dataSnapshot.getValue(String.class);
                                                                                                                                                        int tl_cns = Integer.parseInt(total_coins);

                                                                                                                                                        DaoJoinedParticipant dao = new DaoJoinedParticipant();
                                                                                                                                                        MyJoinedParticipant myj =  new MyJoinedParticipant(user_image, fName, lName, email, tl_cns);
                                                                                                                                                        dao.add(myj).addOnSuccessListener(suc ->
                                                                                                                                                        {
                                                                                                                                                            // DISMISS PROGRESS DIALOG
                                                                                                                                                            progressDialog.dismiss();
                                                                                                                                                            DB.insertUidData(uid_of_sk_holder);
                                                                                                                                                            skContent.setVisibility(View.VISIBLE);
                                                                                                                                                            recreate();

                                                                                                                                                        }).addOnFailureListener(er->

                                                                                                                                                        {
                                                                                                                                                            Toast.makeText(getApplicationContext(), "UID couldn't get.", Toast.LENGTH_SHORT).show();
                                                                                                                                                        });


                                                                                                                                                    }

                                                                                                                                                    @Override
                                                                                                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                                                                                                    }
                                                                                                                                                });

                                                                                                                                    }
                                                                                                                                }
                                                                                                                                @Override
                                                                                                                                public void onCancelled(@NonNull DatabaseError error) { }
                                                                                                                            });


                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onCancelled(DatabaseError databaseError) {
                                                                                                                }
                                                                                                            });

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(DatabaseError databaseError) {
                                                                                                }
                                                                                            });

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) { }
                                                        });

                                            }
                                            else
                                            {
                                                code.setTextColor(Color.RED);
                                                h.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        code.setTextColor(Color.BLACK);
                                                    }
                                                },1000);

                                                Toast.makeText(getApplicationContext(), "Code not exist.", Toast.LENGTH_SHORT).show();
                                                // DISMISS PROGRESS DIALOG
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }, 1500);
                                }
                            }
                        });

                    }

                    break;
            }

        }


        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), QRScanner.class));
            }
        });

        trees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FactTrees.class));

            }
        });
        waste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FactWaste.class));

            }
        });
        recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FactRecycle.class));

            }
        });

    }

    void showReceivedCoins() {
        final Dialog dialog = new Dialog(this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        dialog.setTitle(totalCoins.getText().toString());
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.recieve_coins_dialog);
        dialog.show();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    public void ClickMenu(View view)
    {
        //Open drawer
        openDrawer(drawerLayout);
    }


    static void openDrawer(DrawerLayout drawerLayout)
    {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view)
    {
        //Close drawer
        // redirect to User Profile
        closeDrawer(drawerLayout);
        redirectActivity(this, UserProfile.class);

    }

    public static void closeDrawer(DrawerLayout drawerLayout)
    {
        // Close drawer layout
        // Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);

        }
    }

    public void ClickTaskCardView(View view)
    {
        startActivity(new Intent(getApplicationContext(), TaskActivity.class));
    }


    public void ClickAnnouncementCardView(View view)
    {
        startActivity(new Intent(getApplicationContext(), AnnouncementActivity.class));
    }


    public void ClickRewardCardView(View view)
    {
        startActivity(new Intent(getApplicationContext(), RewardActivity.class));
    }


    public void ClickParticipantCardView(View view)
    {
        startActivity(new Intent(getApplicationContext(), MemberActivity.class));
    }

    public void ClickHome(View view)
    {
        // Recreate activity
        recreate();
    }

    public void ClickTask(View view)
    {
        // Redirect activity to Task
        redirectActivity (this, TaskActivity.class);
    }

    public void ClickAnnouncements(View view)
    {
        // Redirect activity to Announcements
        redirectActivity (this, AnnouncementActivity.class);

    }
    public void ClickParticipants(View view)
    {
        // Redirect activity to about us
        redirectActivity(this, MemberActivity.class);

    }

    public void ClickRewards(View view)
    {
        // Redirect activity to dashboard
        redirectActivity(this, RewardActivity.class);

    }
    public void ClickSettings(View view)
    {
        // Redirect activity to dashboard
        redirectActivity(this, SettingActivity.class);

    }
    public void ClickAboutUs(View view)
    {
        // Redirect activity to dashboard
        redirectActivity(this, AboutUs.class);

    }

    public void ClickLogout(View view)
    {
        // Close app
        logout(this);
    }

    public void logout(Activity Activity)
    {

        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity);
        // Set title
        builder.setTitle("Logout");
        // Set message
        builder.setMessage("Are you sure you want to logout ?");
        // Positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Toast.makeText(HomeActivity.this, "Redirect to login . .", Toast.LENGTH_SHORT).show();
                signOut();

            }
        });
        // Negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dismiss dialog
                dialogInterface.dismiss();
            }
        });
        // Show dialog
        builder.show();

    }

    private void signOut() {
        auth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        startActivity(new Intent(getApplicationContext(), UserLogin.class));
                        finish();
                    }
                });
    }

    public static void redirectActivity(Activity activity, Class aClass)
    {
        // Initialize intent
        Intent intent =  new Intent(activity, aClass);
        // Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }


}