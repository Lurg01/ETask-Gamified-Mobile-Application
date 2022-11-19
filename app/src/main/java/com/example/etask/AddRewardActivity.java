package com.example.etask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.io.IOException;
import java.util.UUID;


public class AddRewardActivity extends AppCompatActivity {

    ImageButton back;
    MaterialButton btnSave;
    private ImageView selectedView;
    TextView catTxt;
    Animation scaleUp, scaleDown;
    private  ImageButton food,bag,clothes,medicine,others;
    Calendar myCalendar = Calendar.getInstance();
    EditText  name, desc, quantity, coins, timePicker, datePicker;
    HashMap<String, Object> hashMap = new HashMap<> ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_add_reward);


        name = (EditText) findViewById(R.id.editTxt_rewardName);
        desc = (EditText) findViewById(R.id.editTxt_rewardDesc);
        quantity = (EditText) findViewById(R.id.editTxt_rewardQuantity);
        coins = (EditText) findViewById(R.id.editTxt_rewardCoins);

        catTxt = (TextView) findViewById(R.id.cat_txt);
        back = (ImageButton) findViewById(R.id.imageBtn_back);
        btnSave = (MaterialButton) findViewById(R.id.save_btn);
        selectedView = (ImageView) findViewById(R.id.selected_img);

        food = (ImageButton) findViewById(R.id.food);
        bag = (ImageButton) findViewById(R.id.school);
        clothes = (ImageButton) findViewById(R.id.clothes);
        medicine = (ImageButton) findViewById(R.id.medicine);
        others = (ImageButton) findViewById(R.id.other);

        timePicker = (EditText) findViewById(R.id.time_picker);
        datePicker = (EditText) findViewById(R.id.date_picker);

        scaleUp = (Animation) AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = (Animation) AnimationUtils.loadAnimation(this,R.anim.scale_down);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        food.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                selectedView.setImageResource(R.drawable.food);
                selectedView.startAnimation(scaleUp);
                catTxt.setText("Food");
                food.setVisibility(View.GONE);
                clothes.setVisibility(View.VISIBLE);
                bag.setVisibility(View.VISIBLE);
                medicine.setVisibility(View.VISIBLE);
                others.setVisibility(View.VISIBLE);

                return true;
            }
        });
        bag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                selectedView.setImageResource(R.drawable.backpack);
                selectedView.startAnimation(scaleUp);
                catTxt.setText("School Supplies");
                bag.setVisibility(View.GONE);
                clothes.setVisibility(View.VISIBLE);
                food.setVisibility(View.VISIBLE);
                medicine.setVisibility(View.VISIBLE);
                others.setVisibility(View.VISIBLE);

                return true;
            }
        });
        clothes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                selectedView.setImageResource(R.drawable.clothes);
                selectedView.startAnimation(scaleUp);
                catTxt.setText("Clothes");
                clothes.setVisibility(View.GONE);
                food.setVisibility(View.VISIBLE);
                bag.setVisibility(View.VISIBLE);
                medicine.setVisibility(View.VISIBLE);
                others.setVisibility(View.VISIBLE);

                return true;
            }
        });
        others.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                selectedView.setImageResource(R.drawable.gift);
                selectedView.startAnimation(scaleUp);
                catTxt.setText("Mystery Object");
                others.setVisibility(View.GONE);
                clothes.setVisibility(View.VISIBLE);
                bag.setVisibility(View.VISIBLE);
                medicine.setVisibility(View.VISIBLE);
                food.setVisibility(View.VISIBLE);

                return true;
            }
        });
        medicine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                selectedView.setImageResource(R.drawable.medicine);
                selectedView.startAnimation(scaleUp);
                catTxt.setText("Medicine");
                medicine.setVisibility(View.GONE);
                clothes.setVisibility(View.VISIBLE);
                bag.setVisibility(View.VISIBLE);
                food.setVisibility(View.VISIBLE);
                others.setVisibility(View.VISIBLE);

                return true;
            }
        });

        //Start the animations preoidically by calling 'shineStart' method with ScheduledExecutorService
        ScheduledExecutorService executorService =
                Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }, 1, 1, TimeUnit.SECONDS);

        MyReward myR_edit = (MyReward) getIntent().getSerializableExtra("Edit");
        if(myR_edit != null)
        {

            Glide.with(AddRewardActivity.this).load(myR_edit.getImage()).into(selectedView);
            name.setText(myR_edit.getName());
            desc.setText(myR_edit.getDescription());
            quantity.setText(myR_edit.getQuantity());
            coins.setText(myR_edit.getCoins());
            datePicker.setText(myR_edit.getDate());
            timePicker.setText(myR_edit.getTime());
            btnSave.setText(R.string.save);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Images")
                    .addValueEventListener(new ValueEventListener () {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                String name = childSnapshot.getKey();
                                assert name != null;
                                databaseReference.child("Images").child(name).addValueEventListener(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String imageSelected = dataSnapshot.getValue(String.class);
                                                if (myR_edit.getImage().equals (imageSelected)){ catTxt.setText (name);}

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
        else
        { btnSave.setText(R.string.add);}

        Handler h = new Handler();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String my_name, my_desc, my_quantity, my_worth, my_date, my_time;
                my_name = name.getText().toString();
                my_desc = desc.getText().toString();
                my_quantity = quantity.getText().toString();
                int quantity_int = Integer.parseInt(my_quantity);
                my_worth = coins.getText().toString();
                int num = Integer.parseInt(my_worth);
                my_date = datePicker.getText().toString();
                my_time = timePicker.getText().toString();

                if (!TextUtils.isEmpty(my_name) && !TextUtils.isEmpty(my_desc) && !TextUtils.isEmpty(String.valueOf(quantity_int)) && !TextUtils.isEmpty(String.valueOf(num))
                        && !TextUtils.isEmpty (catTxt.getText ().toString ()) && !TextUtils.isEmpty (my_date) && !TextUtils.isEmpty (my_time))
                {


                    DaoReward dao = new DaoReward();
                    MyReward myR = new MyReward(catTxt.getText().toString() , name.getText().toString(), desc.getText().toString(),
                            quantity.getText().toString(), coins.getText().toString(), datePicker.getText().toString(), timePicker.getText().toString());
                    if (myR_edit == null) {
                        dao.add(myR).addOnSuccessListener(suc ->
                        {

                            Toast.makeText(AddRewardActivity.this, "Product Added.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddRewardActivity.this, RewardActivity.class));


                        }).addOnFailureListener(er ->

                        {
                            Toast.makeText(AddRewardActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                        });

                    } else {

                        hashMap.put("name", name.getText().toString());
                        hashMap.put("description", desc.getText().toString());
                        hashMap.put("quantity", quantity.getText().toString());
                        hashMap.put("coins", coins.getText().toString());
                        hashMap.put("image", String.valueOf(R.drawable.clothes) );
                        if (name.getText().toString().equals(myR_edit.getName()) && desc.getText().toString().equals(myR_edit.getDescription()) && quantity.getText().toString().equals(myR_edit.getQuantity())
                                && coins.getText().toString().equals(myR_edit.getCoins()) && String.valueOf(R.drawable.clothes).equals(myR_edit.getImage())) {
                            Toast.makeText(AddRewardActivity.this, "Data not changed.", Toast.LENGTH_SHORT).show();
                        } else {
                            dao.update(myR_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                            {
                                Toast.makeText(AddRewardActivity.this, "Product Updated.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddRewardActivity.this, RewardActivity.class));

                            }).addOnFailureListener(er ->
                            {

                                Toast.makeText(AddRewardActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }

                if (TextUtils.isEmpty(my_name))
                {
                    name.setHintTextColor(Color.RED);
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            name.setHintTextColor(getResources().getColor(R.color.lightGray));
                        }
                    },1000);
                }
                if (TextUtils.isEmpty(my_desc))
                {
                    desc.setHintTextColor(Color.RED);
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            desc.setHintTextColor(getResources().getColor(R.color.lightGray));
                        }
                    },1000);
                }
                if (TextUtils.isEmpty(my_quantity))
                {
                    quantity.setHintTextColor(Color.RED);
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            quantity.setHintTextColor(getResources().getColor(R.color.lightGray));
                        }
                    },1000);
                }
                if (TextUtils.isEmpty(my_worth))
                {
                    coins.setHintTextColor(Color.RED);
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coins.setHintTextColor(getResources().getColor(R.color.lightGray));
                        }
                    },1000);
                }
                if (TextUtils.isEmpty(my_date))
                {
                    datePicker.setHintTextColor(Color.RED);
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            datePicker.setHintTextColor(getResources().getColor(R.color.lightGray));
                        }
                    },1000);
                }
                if (TextUtils.isEmpty(my_time))
                {
                    timePicker.setHintTextColor(Color.RED);
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            timePicker.setHintTextColor(getResources().getColor(R.color.lightGray));
                        }
                    },1000);
                }
            }

        });

    }
    public void ClickTime(View view) {selectTime();}
    public void ClickDate(View view) {selectDate();}

    private void selectTime(){
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(AddRewardActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                currentTime.set(Calendar.HOUR_OF_DAY,hour);
                currentTime.set(Calendar.MINUTE,minute);

                String myFormat = "HH:mm";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                timePicker.setText(dateFormat.format(currentTime.getTime()));

            }
        },hour,minute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void selectDate(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                datePicker.setText(updateDate());

            }
        };

   /*     datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {*/
                new DatePickerDialog(AddRewardActivity.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

      /*      }
        });*/
    }

    private String updateDate(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return  dateFormat.format(myCalendar.getTime());
    }
}