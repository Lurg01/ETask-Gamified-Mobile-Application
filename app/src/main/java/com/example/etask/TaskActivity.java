package com.example.etask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class TaskActivity extends AppCompatActivity {

    // Initialize variable
    FirebaseAuth auth;
    DBHelper DB;
    DrawerLayout drawerLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    DaoTask dao;
    FirebaseRecyclerAdapter adapter;
    FloatingActionButton fab;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    Handler h = new Handler();
    ImageButton qr,  totalCoinsBtn;
    CircleImageView profileImage;
    TextView user_email, firstname, lastname, nav_task,  totalCoins, emptyTask, groupName, navDrawer_participants;
    boolean isLoading=false;
    public static class Global { public static String uid_of_sk;}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull (getSupportActionBar ()).hide ();
        setContentView(R.layout.activity_task);

        // init firebase auth
        auth = FirebaseAuth.getInstance();

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        qr = (ImageButton) findViewById(R.id.qr);
        totalCoins = (TextView) findViewById(R.id.total_coins);
        totalCoinsBtn = (ImageButton) findViewById(R.id.total_coins_btn);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        firstname = (TextView) findViewById(R.id.fName_txt);
        lastname = (TextView) findViewById(R.id.lName_txt);
        groupName = (TextView) findViewById(R.id.group_name);
        user_email = (TextView) findViewById(R.id.email_txt);
        navDrawer_participants = (TextView) findViewById(R.id.navDrawer_participants);
        nav_task = (TextView) findViewById(R.id.navDrawer_task);
        nav_task.setTextColor(Color.WHITE);
        nav_task.setBackgroundColor(getResources().getColor(R.color.lightGray));
        emptyTask = (TextView) findViewById(R.id.empty_task);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id1))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity()); - NOT A COMMENT !!!
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);




        if (acct != null)
        {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            user_email.setText(personEmail);
        }
        else {
            // get current user
            FirebaseUser firebaseUser = auth.getCurrentUser();
            // user logged in, get info
            String personEmail = firebaseUser.getEmail();
            String personName = firebaseUser.getDisplayName();
            user_email.setText(personEmail);
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
                                                            Glide.with(TaskActivity.this).load(user_image).into(profileImage);

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
                        navDrawer_participants.setText("Group");
                        qr.setVisibility(View.GONE);
                        fab.setVisibility(View.GONE);
                        emptyTask.setText("Admin have not posted any task yet..");

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
                                                            Glide.with(TaskActivity.this).load(user_image).into(profileImage);
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

                            /*            if (totalCoins.getText().toString().equals(total_coins))
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



                        break;
                }
            }
        }

        // can have a switch for use type
        DB = new DBHelper(this);
        Cursor res_uid = DB.getUidData();

        if (res_uid.moveToFirst()) {
            Global.uid_of_sk = res_uid.getString(0);
        }

        // LOAD TASK DATA
        showData();

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QRScanner.class));

            }
        });

        // FOR SK ADMIN ONLY
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddTask();
            }
        });

    }


    private void showData()
    {

        //Assign variable
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TaskActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        dao = new DaoTask();
        loadData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                if (totalItem == 0) { emptyTask.setVisibility(View.VISIBLE); }
                else { emptyTask.setVisibility(View.INVISIBLE);}
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem < lastVisible+3)
                {
                    if(!isLoading)
                    {
                        isLoading=true;
                        loadData();
                    }
                }
            }
        });

        recyclerView.setAdapter(adapter);

    }

    private void loadData() {
        DB = new DBHelper(this);
        Cursor res = DB.getUserTypeData();
        FirebaseRecyclerOptions<MyTask> option =
                new FirebaseRecyclerOptions.Builder<MyTask>()
                        .setQuery(dao.get(), new SnapshotParser<MyTask>() {
                            @NonNull
                            @Override
                            public MyTask parseSnapshot(@NonNull DataSnapshot snapshot) {
                                MyTask myt = snapshot.getValue(MyTask.class);
                                assert myt != null;
                                myt.setKey(snapshot.getKey());
                                return myt;
                            }
                        }).build();

        adapter = new FirebaseRecyclerAdapter(option) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

                TaskVh vh = (TaskVh) holder;
                MyTask myt = (MyTask) model;
                vh.txt_title.setText(myt.getTitle());
                vh.txt_time.setText(myt.getTime());
                vh.txt_date.setText(myt.getDate());
                vh.txt_coins.setText(myt.getCoins());
                vh.view_task.setOnClickListener(view ->  {
                    startActivity(new Intent(getApplicationContext(), ParticipantSubmitTask.class).putExtra("Edit", myt));
                });
                if (res.moveToFirst()) {
                    switch (res.getString(0)) {
                        case "Participant":
                            vh.txt_option.setVisibility(View.GONE);
                            fab.setVisibility(View.GONE);
                            break;
                        case "Leader":
                            vh.txt_option.setVisibility(View.VISIBLE);
                            vh.txt_option.setOnClickListener(v ->
                            {
                                PopupMenu popupMenu = new PopupMenu(TaskActivity.this, vh.txt_option);
                                popupMenu.inflate(R.menu.option_menu);
                                popupMenu.setOnMenuItemClickListener(item ->
                                {
                                    switch (item.getItemId()) {

                                        case R.id.menu_edit:
                                            startActivity(new Intent(getApplicationContext(), AddTaskActivity.class).putExtra("Edit", myt));
                                            break;

                                        case R.id.menu_remove:
                                            DaoTask dao = new DaoTask();
                                            dao.remove(myt.getKey()).addOnSuccessListener(suc ->
                                            {
                                                Toast.makeText(getApplicationContext(), "Task deleted.", Toast.LENGTH_SHORT).show();
//                                                notifyItemRemoved(position);
//                                                list.remove(position);

                                            }).addOnFailureListener(er ->
                                            {
                                                Toast.makeText(TaskActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
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
                View view = LayoutInflater.from(TaskActivity.this).inflate(R.layout.layout_item, parent, false);
                return new TaskVh(view);
            }

            @Override
            public void onDataChanged() {
                //super.onDataChanged();
                isLoading =true;
                swipeRefreshLayout.setRefreshing(true);
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);

            }
        };
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);


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
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void openAddTask(){
        startActivity(new Intent(TaskActivity.this, AddTaskActivity.class));

    }

    public void ClickMenu(View view)
    {
        //Open drawer
        HomeActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view)
    {
        // Close drawer
        // redirect to User Profile
        HomeActivity.closeDrawer(drawerLayout);
        HomeActivity.redirectActivity(this, UserProfile.class);

    }
    public void ClickTask(View view)
    {
        // Recreate activity
        recreate();

    }
    public void ClickHome(View view) {
        // Redirect activity to Announcements
        HomeActivity.redirectActivity(this, HomeActivity.class);
    }

    public void ClickAnnouncements(View view) {
        // Redirect activity to Announcements
        HomeActivity.redirectActivity(this, AnnouncementActivity.class);
    }


    public void ClickParticipants(View view)
    {
        // Redirect activity to about us
        HomeActivity.redirectActivity(this, MemberActivity.class);
    }
    public void ClickRewards(View view)
    {
        // Redirect activity to about us
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
                Toast.makeText(TaskActivity.this, "Redirect to login . .", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        HomeActivity.closeDrawer(drawerLayout);

    }

}