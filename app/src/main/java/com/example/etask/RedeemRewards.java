package com.example.etask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

// FOR GENERATING QR

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class RedeemRewards extends AppCompatActivity {


    // Initialize variable
    DBHelper DB;
    Handler h = new Handler();
    LinearLayout skContent, participantContent;
    TextView rewardName, rewardDescription, rewardWorth,  taskDate, taskTime,
            totalCoins,deductionOfCoins, rewardQuantity;
    MaterialButton redeemBtn, bagBtn;
    ImageButton drawerBtn, totalCoinsBtn, qr, backBtn;
    public static class Global { public static String uid_of_sk, uid_of_participant, key;}

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    DaoRedeemHistory dao;
    FirebaseRecyclerAdapter adapter;
    boolean isLoading=false;
    String key =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull (getSupportActionBar ()).hide ();
        setContentView(R.layout.activity_redeem_rewards);

        drawerBtn = (ImageButton) findViewById(R.id.drawer_btn);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        qr = (ImageButton) findViewById(R.id.qr);
        totalCoins = (TextView) findViewById(R.id.total_coins);
        totalCoinsBtn = (ImageButton) findViewById(R.id.total_coins_btn);
        deductionOfCoins = (TextView) findViewById(R.id.deduction_of_coins);
        skContent = (LinearLayout) findViewById(R.id.sk_content_redeemRewards);
        participantContent = (LinearLayout) findViewById(R.id.participant_content_redeemRewards);
        rewardName = (TextView) findViewById(R.id.reward_name);
        rewardDescription = (TextView) findViewById(R.id.reward_description);
        rewardQuantity = (TextView) findViewById(R.id.reward_quantity);
        rewardWorth = (TextView) findViewById(R.id.reward_worth);
        taskDate = (TextView) findViewById(R.id.task_date);
        taskTime = (TextView) findViewById(R.id.task_time);
        redeemBtn = (MaterialButton) findViewById(R.id.redeem_btn);
        bagBtn = (MaterialButton) findViewById(R.id.bag_btn);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QRScanner.class));
            }
        });

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uid = currentFirebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        MyReward view_rewards = (MyReward)getIntent().getSerializableExtra("Edit");
        if(view_rewards != null)
        {
            rewardName.setText(view_rewards.getName());
            rewardDescription.setText(view_rewards.getDescription());
            rewardQuantity.setText(view_rewards.getQuantity());
            rewardWorth.setText(view_rewards.getCoins());
            taskDate.setText(view_rewards.getDate());
            taskTime.setText(view_rewards.getTime());
            Global.key = view_rewards.getKey();

        }

        DB = new DBHelper(this);
        Cursor res = DB.getUserTypeData();

        if (res.moveToFirst()) {
            switch (res.getString(0)) {
                case "Leader":
                    participantContent.setVisibility(View.GONE);
                    totalCoins.setVisibility(View.GONE);
                    totalCoinsBtn.setVisibility(View.GONE);
                    drawerBtn.setVisibility(View.GONE);
                    Global.uid_of_sk = uid;

                    break;
                case "Participant":
                    qr.setVisibility(View.GONE);
                    skContent.setVisibility(View.GONE);
                    drawerBtn.setVisibility(View.GONE);
                    Cursor uid_of_sk = DB.getUidData();
                    if (uid_of_sk.moveToFirst())
                    {
                        Global.uid_of_sk = uid_of_sk.getString(0);
                    }
                    Global.uid_of_participant = uid;
                    // DISPLAY TOTAL COINS OF PARTICIPANT
                    databaseReference.child("Users").child("Participant").child(uid).child("Total Coins").addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String total_coins = dataSnapshot.getValue(String.class);
                                  /*  if (!totalCoins.getText().toString().equals(total_coins))
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
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // PARTICIPANT USES > REDEEM REWARD FUNCTION
        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor res_uid = DB.getUidData();
                int total_coins =Integer.parseInt(totalCoins.getText().toString());
                int worth = Integer.parseInt(rewardWorth.getText().toString());
                if (total_coins < worth)
                {
                    showInsufficientCoins();
                }
                else
                {
                    // GET THE CURRENT COINS OF PARTICIPANT AND DEDUCT THE WORTH OF COINS ONCE THE PARTICIPANT REDEEM
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Users").child("Participant").child(uid).child("Total Coins").addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String coins = dataSnapshot.getValue(String.class);
                                    assert coins != null;
                                    int current_coins = Integer.parseInt(coins);
                                    current_coins -= worth;
                                    deductionOfCoins.setText(String.valueOf(current_coins));

                                    assert view_rewards != null;
                                    int quantity_int = Integer.parseInt(view_rewards.getQuantity());
                                    quantity_int -= 1;

                                    if (res_uid.moveToFirst()) {
                                        databaseReference.child("Users")
                                                .child("SK")
                                                .child(res_uid.getString(0))
                                                .child("Reward Data")
                                                .child(view_rewards.getKey())
                                                .child("quantity")
                                                .setValue(String.valueOf(quantity_int));

                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                            });
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            databaseReference.child("Users")
                                    .child("Participant")
                                    .child(uid)
                                    .child("Total Coins")
                                    .setValue(deductionOfCoins.getText().toString());

                        }

                    },1000);

                    DaoAddToBag dao = new DaoAddToBag();
                    MyAddToBag myBag =  new MyAddToBag(view_rewards.getImage(), view_rewards.getName(), view_rewards.getCoins());
                    dao.add(myBag).addOnSuccessListener(suc ->
                    {
                        Toast.makeText(getApplicationContext(), "Reward Added to Bag.", Toast.LENGTH_SHORT).show();
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

                                                            databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("fname").addValueEventListener(
                                                                    new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            String fName = dataSnapshot.getValue(String.class);

                                                                            databaseReference.child("Users").child("Participant").child(uid).child("User Profile").child(k).child("lname").addValueEventListener(
                                                                                    new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            String lName = dataSnapshot.getValue(String.class);

                                                                                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                                                                            LocalDateTime now = LocalDateTime.now();

                                                                                            DaoRedeemHistory dao = new DaoRedeemHistory();
                                                                                            MyRedeemHistory myRdm = new MyRedeemHistory(user_image, fName, lName, dtf.format(now));
                                                                                            dao.add(myRdm);
                                                                                            startActivity(new Intent(RedeemRewards.this, RewardActivity.class));
                                                                                        }
                                                                                        @Override
                                                                                        public void onCancelled(DatabaseError databaseError) { }
                                                                                    });


                                                                        }
                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) { }
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
                    }).addOnFailureListener(er->

                    {
                        Toast.makeText(getApplicationContext(), ""+ er.getMessage(),  Toast.LENGTH_SHORT).show();

                    });
                }

            }
        });

        // SHOW SUBMITTED TASK DATA FOR SK ADMIN
        showData();
        // PARTICIPANT USES
        bagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), MyBag.class));

            }

        });

    }


    private void showData()
    {
        // ASSIGN VARIABLE
        swipeRefreshLayout = findViewById(R.id.swip_redeem_history);
        recyclerView = findViewById(R.id.rv_redeem_history);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        dao = new DaoRedeemHistory();
        loadData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
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

        FirebaseRecyclerOptions<MyRedeemHistory> option =
                new FirebaseRecyclerOptions.Builder<MyRedeemHistory>()
                        .setQuery(dao.get(), new SnapshotParser<MyRedeemHistory>() {
                            @NonNull
                            @Override
                            public MyRedeemHistory parseSnapshot(@NonNull DataSnapshot snapshot) {
                                MyRedeemHistory myRdm = snapshot.getValue(MyRedeemHistory.class);
                                assert myRdm != null;
                                myRdm.setKey(snapshot.getKey());
                                return myRdm;
                            }
                        }).build();

        adapter = new FirebaseRecyclerAdapter(option) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

                ParticipantVh vh = (ParticipantVh) holder;
                MyRedeemHistory myRdm = (MyRedeemHistory) model;
                Glide.with(RedeemRewards.this).load(myRdm.getUser_image()).into(vh.participantImage);
                vh.txt_fName.setText(myRdm.getFirstname());
                vh.txt_lName.setText(myRdm.getLastname());
                vh.txtDatetime.setText(myRdm.getDatetime());
                vh.txt_email.setVisibility(View.GONE);
                vh.txtOption.setVisibility(View.GONE);

            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(RedeemRewards.this).inflate(R.layout.participant_list, parent, false);
                return new ParticipantVh(view);
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

    void showReceivedCoins() {
        final Dialog dialog = new Dialog(this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.recieve_coins_dialog);
        dialog.show();
    }

    void showInsufficientCoins() {
        final Dialog dialog = new Dialog(this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.insufficient_coins);
        dialog.show();
    }


}