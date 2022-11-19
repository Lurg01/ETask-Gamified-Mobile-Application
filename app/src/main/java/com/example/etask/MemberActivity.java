package com.example.etask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemberActivity extends AppCompatActivity {

    // INITIALIZE VARIABLE
    FirebaseAuth auth;
    DrawerLayout drawerLayout;
    DBHelper DB;
    Button leaveGroup;
    LinearLayout  groupContent, leaderboardContent, taskNavHide, announcementsNavHide, groupNavHide,
            rewardsNavHide,  groupNav, leaderboardNav;
    public static class Global { public static String uid, key, user_type, uid_of_sk_from_memberActivity;}
    SwipeRefreshLayout swipeRefreshLayoutForParticipantList, swipeRefreshLayoutForLeaderboard;
    RecyclerView recyclerViewForParticipantList, recyclerViewForLeaderboard;
    DaoJoinedParticipant dao;
    FirebaseRecyclerAdapter adapterForParticipantList, adapterForLeaderboard;
    FloatingActionButton inviteFab;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    Handler h = new Handler();
    ImageButton qr, totalCoinsBtn, groupIcon, leaderboardIcon;
    CircleImageView profileImage;
    TextView firstname, lastname, navDrawer_participants, totalCoins, groupName, peopleTxt,
            leaderboardTxt, code, user_email, coins;
    boolean isLoadingParticipantList =false;
    boolean isLoadingLeaderboard =false;

    List<MyJoinedParticipant> rankList = new ArrayList<MyJoinedParticipant>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_member);

        // init firebase auth
        auth = FirebaseAuth.getInstance();
        //Assign variable
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        qr = (ImageButton) findViewById(R.id.qr);
        coins = (TextView) findViewById(R.id.coins);
        totalCoins = (TextView) findViewById(R.id.total_coins);
        totalCoinsBtn = (ImageButton) findViewById(R.id.total_coins_btn);
        leaderboardIcon = (ImageButton) findViewById(R.id.leaderboard_icon);
        leaderboardContent = (LinearLayout) findViewById(R.id.leaderboard_content);
        groupIcon = (ImageButton) findViewById(R.id.group_icon);
        groupNav = (LinearLayout) findViewById(R.id.group_nav);
        leaderboardNav = (LinearLayout) findViewById(R.id.leaderboard_nav);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        firstname = (TextView) findViewById(R.id.fName_txt);
        lastname = (TextView) findViewById(R.id.lName_txt);
        groupName = (TextView) findViewById(R.id.group_name);
        peopleTxt = (TextView) findViewById(R.id.people_txt);
        leaderboardTxt = (TextView) findViewById(R.id.leaderboard_txt);
        leaveGroup = (Button) findViewById(R.id.leave_group);
        taskNavHide = (LinearLayout) findViewById(R.id.taskNavHide);
        announcementsNavHide = (LinearLayout) findViewById(R.id.announcementsNavHide);
        groupNavHide = (LinearLayout) findViewById(R.id.groupNavHide);
        rewardsNavHide = (LinearLayout) findViewById(R.id.rewardsNavHide);
        groupContent = (LinearLayout) findViewById(R.id.group_content);
        user_email = findViewById(R.id.email_txt);
        code = (TextView) findViewById(R.id.code);
        inviteFab = (FloatingActionButton) findViewById(R.id.btn_invite);
        navDrawer_participants = (TextView) findViewById(R.id.navDrawer_participants);
        navDrawer_participants.setTextColor(Color.WHITE);
        navDrawer_participants.setBackgroundColor(getResources().getColor(R.color.lightGray));

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

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), QRScanner.class));
            }
        });

        // Get Current User Login
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uid = currentFirebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        DB = new DBHelper(this);
        Cursor res = DB.getUserTypeData();
        if (res.moveToFirst()) {
            switch (res.getString(0))
            {
                case "Leader":
                    qr.setVisibility(View.VISIBLE);
                    totalCoins.setVisibility(View.GONE);
                    totalCoinsBtn.setVisibility(View.GONE);
                    leaveGroup.setVisibility(View.INVISIBLE);
                    Global.user_type = "SK";
                    Global.uid_of_sk_from_memberActivity = uid;
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
                                                        Glide.with(MemberActivity.this).load(user_image).into(profileImage);

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

                    break;

                case "Participant":
                    qr.setVisibility(View.GONE);
                    inviteFab.setVisibility(View.GONE);
                    navDrawer_participants.setText("Group");

                    // CHECK IF PARTICIPANT ALREADY HAVE UID OF SK ELSE GET THE SK UID IF THE GENERATED KEY IS MATCHED
                    DB = new DBHelper(this);
                    Cursor res_uid = DB.getUidData();
                    if (res_uid.getCount() == 0 )
                    {
                        taskNavHide.setVisibility(View.GONE);
                        announcementsNavHide.setVisibility(View.GONE);
                        groupNavHide.setVisibility(View.GONE);
                        rewardsNavHide.setVisibility(View.GONE);
                    }
                    Global.user_type = "";

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
                                                        Glide.with(MemberActivity.this).load(user_image).into(profileImage);
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

                                        databaseReference.child("Users").child("Participant").child(uid).child("group_name").addValueEventListener(
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

                    // DISPLAY TOTAL COINS OF PARTICIPANT
                    databaseReference.child("Users").child("Participant").child(uid).child("Total Coins").addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String total_coins = dataSnapshot.getValue(String.class);

                         /*           if (totalCoins.getText().toString().equals(total_coins))
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



                    Collections.sort(rankList, MyJoinedParticipant.CoinsLToHComparator);



/*

                    if (res_uid.moveToFirst())
                    {
                        Global.uid_of_sk_from_memberActivity = res_uid.getString(0);

                        // SORT PARTICIPANT BY LOWEST/BOTTOM AND HIGHEST/TOP OF TOTAL COINS ACCUMULATE
                        databaseReference.child("Users")
                                .child("SK")
                                .child(res_uid.getString(0))
                                .child("Joined")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            String k = childSnapshot.getKey();
                                            assert k != null;
                                            databaseReference.child("Users").child("SK").child(res_uid.getString(0)).child("Joined").child(k).child("coins").addValueEventListener(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String total_coins = dataSnapshot.getValue(String.class);
                                                            assert total_coins != null;
                                                            int count = 0;

                                                            ArrayList<Integer> rank = new ArrayList<>();
                                                            int total_coins_num = Integer.parseInt(total_coins);




                                                     */
/*   if (total_coins_num > c) {

                                                            c = total_coins_num;

                                                           *//*
*/
/* if (index == 1)
                                                            {*//*
*/
/*
//                                                                        index-=1;

                                                            String coins_txt = String.valueOf(c);
                                                            coins.setText(coins_txt);

                                        *//*
*/
/*                    databaseReference.child("Users")
                                                                    .child("Participant")
                                                                    .child(uid).child("Total Coins")
                                                                    .setValue(coins_txt);
*//*
*/
/*

//                                                                    }
                                                            index++;


                                                        }

        *//*



                                                            rank.add(total_coins_num);

//                                                            Collections.sort(rank);
                                                            for (Integer coins : rank) {

                                                                while (count != 1) {

                                                                    String toStr = String.valueOf(coins);

//                                                                    Toast.makeText(MemberActivity.this, "" + , Toast.LENGTH_SHORT).show();


                                                                    count++;
                                                                }

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
*/

                    break;
            }

        }

        // INVITE FAB FOR ADDING A PARTICIPANT TO GROUP
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        code.setText(generateString());
        inviteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // REDIRECT TO LOGIN ACTIVITY
                // INITIALIZE ALERT DIALOG
                builder.setIcon(R.drawable.groupx);
                builder.setTitle("Group key: [ " + code.getText().toString() + " ]");
                builder.setMessage("Click Copy to ClipBoard to get this generated key and send it as message for someone who you want to become your Participant in this Group.");
                // Negative no button
                builder.setPositiveButton("Copy to ClipBoard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // DISMISS DIALOG
                        dialogInterface.dismiss();

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("code", code.getText().toString());
                        clipboard.setPrimaryClip(clip);

                        databaseReference.child("Users")
                                .child("SK")
                                .child(uid)
                                .child("User Profile")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            String k = childSnapshot.getKey();
//                                            Global.key = k;
                                            assert k != null;
                                            databaseReference.child("Users").child("SK").child(uid)
                                                    .child("User Profile").child(k).child("group_code")
                                                    .setValue(code.getText().toString()).addOnSuccessListener(suc ->
                                                    {
                                                        Toast.makeText(MemberActivity.this, "Code copied.", Toast.LENGTH_SHORT).show();

                                                    }).addOnFailureListener(er->

                                                    {
                                                        Toast.makeText(MemberActivity.this, ""+ er.getMessage(),  Toast.LENGTH_SHORT).show();

                                                    });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                    }
                });

                // Show dialog
                builder.show();

            }
        });

        // PARTICIPANT > LEAVE GROUP BUTTON
        Cursor res_uid = DB.getUidData();
        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // INITIALIZE ALERT DIALOG
                builder.setIcon(R.drawable.groupx);
                builder.setTitle("Leave Group");
                builder.setMessage("Are you sure, you want to leave this Group?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // DISMISS DIALOG
                        dialogInterface.dismiss();
                        if (res_uid.moveToFirst()) {
                            DB.deleteUidData(res_uid.getString(0));
                            groupContent.setVisibility(View.GONE);

//                                    if (myj.getEmail().equals("00096bueranolurg@gmail.com"))
//                                    {
                            /*DaoJoinedParticipant dao = new DaoJoinedParticipant();
                            dao.remove(myj.getKey()).addOnSuccessListener(suc ->
                            {*/
                                Toast.makeText(getApplicationContext(), "You've left the group.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//

                           /* }).addOnFailureListener(er ->
                            {
                                Toast.makeText(MemberActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
//                                    }
*/
                        }


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                // SHOW DIALOG
                builder.show();

            }
        });

        groupIcon.setColorFilter(getResources().getColor(R.color.lightGreen));
        peopleTxt.setTextColor(getResources().getColor(R.color.lightGreen));
        leaderboardIcon.setColorFilter(getResources().getColor(R.color.defaultGray));
        leaderboardTxt.setTextColor(getResources().getColor(R.color.defaultGray));

        groupIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupIcon.setColorFilter(getResources().getColor(R.color.lightGreen));
                peopleTxt.setTextColor(getResources().getColor(R.color.lightGreen));
                leaderboardIcon.setColorFilter(getResources().getColor(R.color.defaultGray));
                leaderboardTxt.setTextColor(getResources().getColor(R.color.defaultGray));
                leaderboardContent.setVisibility(View.GONE);
                groupContent.setVisibility(View.VISIBLE);

            }
        });

        leaderboardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboardIcon.setColorFilter(getResources().getColor(R.color.lightGreen));
                leaderboardTxt.setTextColor(getResources().getColor(R.color.lightGreen));
                groupIcon.setColorFilter(getResources().getColor(R.color.defaultGray));
                peopleTxt.setTextColor(getResources().getColor(R.color.defaultGray));
                groupContent.setVisibility(View.GONE);
                leaderboardContent.setVisibility(View.VISIBLE);

            }
        });


        showDataOfParticipantList();
        showDataOfLeaderboard();

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

    // SHOW DATA OF PARTICIPANT LIST
    private void showDataOfParticipantList()
    {

        // ASSIGN VARIABLE
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        swipeRefreshLayoutForParticipantList = findViewById(R.id.swip_participant_list);
        recyclerViewForParticipantList = findViewById(R.id.rv_participant_list);
        recyclerViewForParticipantList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewForParticipantList.setLayoutManager(layoutManager);
        dao = new DaoJoinedParticipant();
        loadDataOfParticipantList();

        recyclerViewForParticipantList.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
                {
                    if(!isLoadingParticipantList)
                    {
                        isLoadingParticipantList=true;
                        loadDataOfParticipantList();

                    }
                }
            }
        });

        recyclerViewForParticipantList.setAdapter(adapterForParticipantList);
    }


    // LOAD DATA FOR PARTICIPANT LIST
    private void loadDataOfParticipantList() {
        DB = new DBHelper(this);
        Cursor res = DB.getUserTypeData();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        FirebaseRecyclerOptions<MyJoinedParticipant> option =
                new FirebaseRecyclerOptions.Builder<MyJoinedParticipant>()
                        .setQuery(dao.get(), new SnapshotParser<MyJoinedParticipant>() {
                            @NonNull
                            @Override
                            public MyJoinedParticipant parseSnapshot(@NonNull DataSnapshot snapshot) {
                                MyJoinedParticipant myj = snapshot.getValue(MyJoinedParticipant.class);
                                assert myj != null;
                                myj.setKey(snapshot.getKey());

                                return myj;
                            }
                        }).build();

        adapterForParticipantList = new FirebaseRecyclerAdapter(option) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

                ParticipantVh vh = (ParticipantVh) holder;
                MyJoinedParticipant myj = (MyJoinedParticipant) model;
                Glide.with(MemberActivity.this).load(myj.getUser_image()).into(vh.participantImage);
                vh.txt_fName.setText(myj.getFirstname());
                vh.txt_lName.setText(myj.getLastname());
                vh.txt_email.setText(myj.getEmail());

                if (res.moveToFirst()) {
                    switch (res.getString(0)) {
                        case "Participant":
                            vh.txtOption.setVisibility(View.GONE);
                            break;
                        case "Leader":
                            vh.txtOption.setOnClickListener(v ->
                            {
                                PopupMenu popupMenu = new PopupMenu(MemberActivity.this, vh.txtOption);
                                popupMenu.inflate(R.menu.option_menu);
                                popupMenu.setOnMenuItemClickListener(item ->
                                {
                                    switch (item.getItemId()) {

                                        case R.id.menu_edit:
                                            startActivity(new Intent(getApplicationContext(), AddTaskActivity.class));
                                            break;

                                        case R.id.menu_remove:
                                            DaoTask dao = new DaoTask();
                                            dao.remove(myj.getKey()).addOnSuccessListener(suc ->
                                            {
                                                Toast.makeText(getApplicationContext(), "Task deleted.", Toast.LENGTH_SHORT).show();

                                            }).addOnFailureListener(er ->
                                            {
                                                Toast.makeText(MemberActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                            break;
                                    }
                                    return false;
                                });
                                popupMenu.show();

                            });
                            break;
                    }
                }



            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MemberActivity.this).inflate(R.layout.participant_list, parent, false);
                return new ParticipantVh(view);
            }

            @Override
            public void onDataChanged() {
                //super.onDataChanged();
                isLoadingParticipantList =true;
                swipeRefreshLayoutForParticipantList.setRefreshing(true);
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadingParticipantList = false;
                        swipeRefreshLayoutForParticipantList.setRefreshing(false);
                    }
                },1000);

            }

        };
        isLoadingParticipantList = false;
        swipeRefreshLayoutForParticipantList.setRefreshing(false);

    }

    // SHOW DATA OF LEADER BOARD
    private void showDataOfLeaderboard()
    {
        // ASSIGN VARIABLE
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        swipeRefreshLayoutForLeaderboard = findViewById(R.id.swip_participant_leaderboard);
        recyclerViewForLeaderboard = findViewById(R.id.rv_participant_leaderboard);
        recyclerViewForLeaderboard.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewForLeaderboard.setLayoutManager(layoutManager);
        dao = new DaoJoinedParticipant();
        loadLeaderboardData();
        recyclerViewForLeaderboard.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
                {
                    if(!isLoadingLeaderboard)
                    {
                        isLoadingLeaderboard=true;
                        loadLeaderboardData();
                    }
                }
            }
        });

        recyclerViewForLeaderboard.setAdapter(adapterForLeaderboard);
    }

    // LOAD DATA FOR LEADER BOARD
    private void loadLeaderboardData() {
        DB = new DBHelper(this);
        Cursor res = DB.getUserTypeData();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        FirebaseRecyclerOptions<MyJoinedParticipant> option =
                new FirebaseRecyclerOptions.Builder<MyJoinedParticipant>()
                        .setQuery(dao.get(), new SnapshotParser<MyJoinedParticipant>() {
                            @NonNull
                            @Override
                            public MyJoinedParticipant parseSnapshot(@NonNull DataSnapshot snapshot) {
                                MyJoinedParticipant myj = snapshot.getValue(MyJoinedParticipant.class);
                                assert myj != null;
                      /*          rankList.addAll(Arrays.asList(new MyJoinedParticipant[] {myj}));
                                Collections.sort(rankList, MyJoinedParticipant.CoinsLToHComparator);*/
                                myj.setKey(snapshot.getKey());
                                return myj;
                            }
                        }).build();

        adapterForLeaderboard = new FirebaseRecyclerAdapter(option) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

                ParticipantVh vh = (ParticipantVh) holder;
                MyJoinedParticipant myj = (MyJoinedParticipant) model;

                Glide.with(MemberActivity.this).load(myj.getUser_image()).into(vh.participantImage);
                vh.txt_fName.setText(myj.getFirstname());
                vh.txt_lName.setText(myj.getLastname());
                vh.totalCoins.setText(String.valueOf(myj.getCoins()));
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MemberActivity.this).inflate(R.layout.participant_leaderboard, parent, false);
                return new ParticipantVh(view);
            }

            @Override
            public void onDataChanged() {
                //super.onDataChanged();
                isLoadingLeaderboard =true;
                swipeRefreshLayoutForLeaderboard.setRefreshing(true);
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoadingLeaderboard = false;
                        swipeRefreshLayoutForLeaderboard.setRefreshing(false);
                    }
                },1000);

            }

        };
        isLoadingLeaderboard = false;
        swipeRefreshLayoutForLeaderboard.setRefreshing(false);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        adapterForParticipantList.startListening();
        adapterForLeaderboard.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterForParticipantList.stopListening();
        adapterForLeaderboard.startListening();
    }


    public void ClickMenu(View view)
    {
        //Open drawer
        HomeActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view)
    {
        // Close drawer
        HomeActivity.closeDrawer(drawerLayout);
        // redirect to User Profile
        HomeActivity.redirectActivity(this, UserProfile.class);
    }

    public void ClickParticipants(View view)
    {
        // Recreate activity
        recreate();
    }
    public void ClickHome(View view) {
        // Redirect activity to home
        HomeActivity.redirectActivity(this, HomeActivity.class);
    }

    public void ClickTask(View view) {
        // Redirect activity to Task
        HomeActivity.redirectActivity(this, TaskActivity.class);
    }

    public void ClickAnnouncements(View view)
    {
        // Redirect activity to Announcements
        HomeActivity.redirectActivity(this, AnnouncementActivity.class);

    }
    public void ClickRewards(View view)
    {
        // Redirect activity to Rewards
        HomeActivity.redirectActivity(this, RewardActivity.class);
    }
    public void ClickSettings(View view)
    {
        // Redirect activity to dashboard
        HomeActivity.redirectActivity(this, SettingActivity.class);

    }
    public void ClickAboutUs(View view)
    {
        // Redirect activity to dashboard
        HomeActivity.redirectActivity(this, AboutUs.class);
    }

    public void ClickLogout(View view)
    {
        // Redirect to login activity

        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set title
        builder.setTitle("Logout");
        // Set message
        builder.setMessage("Are you sure you want to logout ?");
        // Positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Toast.makeText(MemberActivity.this, "Redirect to login . .", Toast.LENGTH_SHORT).show();
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


    public String generateString() {
        String uuid = UUID.randomUUID().toString().replace("-","").substring(0,8);
        return uuid;
    }


    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        HomeActivity.closeDrawer(drawerLayout);
    }
}