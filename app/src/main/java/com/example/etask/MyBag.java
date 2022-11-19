package com.example.etask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Objects;

public class MyBag extends AppCompatActivity {
    // FOR BAG
    DBHelper DB;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    DaoAddToBag dao;
    FirebaseRecyclerAdapter adapter;
    Handler h = new Handler();

    boolean isLoading=false;
    String key =null;
    // FOR GENERATING QR
    ImageView qrGenerator;
    LinearLayout myBag, showQr;
    String reward_key = "";
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    String uid = currentFirebaseUser.getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull (getSupportActionBar ()).hide ();
        setContentView(R.layout.activity_my_bag);

        qrGenerator = (ImageView) findViewById(R.id.qr_generator);
        myBag = (LinearLayout) findViewById(R.id.my_bag);
        showQr = (LinearLayout) findViewById(R.id.show_qr);

        showData();
    }

    private void showData()
    {
        //ASSIGN VARIABLE
        swipeRefreshLayout = findViewById(R.id.swip_bag);
        recyclerView = findViewById(R.id.rv_bag);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyBag.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        dao = new DaoAddToBag();
        loadData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
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
        FirebaseRecyclerOptions<MyAddToBag> option =
                new FirebaseRecyclerOptions.Builder<MyAddToBag>()
                        .setQuery(dao.get(), new SnapshotParser<MyAddToBag>() {
                            @NonNull
                            @Override
                            public MyAddToBag parseSnapshot(@NonNull DataSnapshot snapshot) {
                                MyAddToBag myAdd = snapshot.getValue(MyAddToBag.class);
                                assert myAdd != null;
                                myAdd.setKey(snapshot.getKey());
                                return myAdd;
                            }
                        }).build();

        adapter = new FirebaseRecyclerAdapter(option) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

                RewardVh vh = (RewardVh) holder;
                MyAddToBag myAdd = (MyAddToBag) model;
                vh.txt_name.setText(myAdd.getName());
                vh.stockContent.setVisibility(View.GONE);
                vh.txtAvailableStock.setVisibility(View.GONE);
                vh.txt_option.setVisibility(View.GONE);
                vh.txt_coins.setText(myAdd.getCoins());
                reward_key = myAdd.getKey();
                vh.view_rewards.setOnClickListener(view ->  {
                    myBag.setVisibility(View.GONE);
                    showQr.setVisibility(View.VISIBLE);
                    showGeneratedQr();

                });
                switch (myAdd.getImage())
                {
                    case "Food":
                        vh.txt_imageUrl.setImageResource(R.drawable.food);
                        break;
                    case "Clothes":
                        vh.txt_imageUrl.setImageResource(R.drawable.clothes);
                        break;
                    case "Medicine":
                        vh.txt_imageUrl.setImageResource(R.drawable.medicine);
                        break;
                    case "Mystery Object":
                        vh.txt_imageUrl.setImageResource(R.drawable.gift);
                        break;
                    case "School Supplies":
                        vh.txt_imageUrl.setImageResource(R.drawable.backpack);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "No Image", Toast.LENGTH_SHORT).show();
                        break;
                }

               /*
                if (res.moveToFirst()) {
                    switch (res.getString(0)) {
                        case "Participant":
                            vh.txt_option.setVisibility(View.GONE);
                            break;
                        case "SK":
                            vh.txt_option.setOnClickListener(v ->
                            {
                                PopupMenu popupMenu = new PopupMenu(MyBag.this, vh.txt_option);
                                popupMenu.inflate(R.menu.option_menu);
                                popupMenu.setOnMenuItemClickListener(item ->
                                {
                                    switch (item.getItemId()) {

                                        case R.id.menu_edit:
                                            Intent i = new Intent(MyBag.this, AddRewardActivity.class);
                                            i.putExtra("Edit", myR);
                                            startActivity(i); // WARNING !!!
                                            break;

                                        case R.id.menu_remove:
                                            //DaoRewards dao = new DaoRewards();
                                            dao.remove(myR.getKey()).addOnSuccessListener(suc ->
                                            {
                                                Toast.makeText(getApplicationContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                                                //notifyItemRemoved(position);
                                                //list.remove(position);
                                            }).addOnFailureListener(er ->
                                            {
                                                Toast.makeText(getApplicationContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
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
*/

            }
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(MyBag.this).inflate(R.layout.layout_item_reward, parent,false);
                return new RewardVh(view);
            }

            @Override
            public void onDataChanged()
            {
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
                itemRemove();;

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


    void showGeneratedQr() {

        MultiFormatWriter writer = new MultiFormatWriter();
        try
        {
            BitMatrix matrix = writer.encode(uid + " " + reward_key, BarcodeFormat.QR_CODE,700,700);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            //set data image to imageview
            qrGenerator.setImageBitmap(bitmap);

        } catch (WriterException e)
        {
            e.printStackTrace();
        }

    }
    void itemRemove()
    {
        databaseReference.child("Users")
                .child("Participant")
                .child(uid)
                .child("My Bag").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                myBag.setVisibility(View.VISIBLE);
                showQr.setVisibility(View.GONE);
                rewardClaimed();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void rewardClaimed() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.reward_claimed_dialog);
        dialog.show();
    }


}