package com.example.etask;

import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;



public class AddTaskActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();
    ImageButton back;
    EditText title, desc, coins;
    Button createBtn;
    EditText timePicker, datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_add_task);

        title = (EditText) findViewById(R.id.editTxt_title);
        desc = (EditText) findViewById(R.id.editTxt_desc);
        coins = (EditText) findViewById(R.id.editTxt_points);

        back = (ImageButton) findViewById(R.id.imageBtn_back);
        createBtn = (Button) findViewById(R.id.create_btn);

        datePicker = (EditText) findViewById(R.id.date_picker);
        timePicker = (EditText) findViewById(R.id.time_picker);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        MyTask myt_edit = (MyTask)getIntent().getSerializableExtra("Edit");
        if(myt_edit != null)
        {
            createBtn.setText(R.string.update);
            title.setText(myt_edit.getTitle());
            desc.setText(myt_edit.getDescription());
            coins.setText(myt_edit.getCoins());
            datePicker.setText(myt_edit.getDate());
            timePicker.setText(myt_edit.getTime());
        }
        else {createBtn.setText(R.string.post);}

        Handler h = new Handler();
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String my_title = title.getText().toString();
                String my_desc = desc.getText().toString();
                String my_coins = coins.getText().toString();
                String my_date = datePicker.getText().toString();
                String my_time = timePicker.getText().toString();

                if (!TextUtils.isEmpty(my_title) && !TextUtils.isEmpty(my_time) && !TextUtils.isEmpty(my_date)
                        && !TextUtils.isEmpty(my_desc) && !TextUtils.isEmpty(my_coins))
                {
                    DaoTask dao = new DaoTask();
                    MyTask myt =  new MyTask(title.getText().toString(), desc.getText().toString(), coins.getText().toString(), datePicker.getText().toString(), timePicker.getText().toString()); // Convert input to int  >   Integer.parseInt(coins.getText().toString())
                    if(myt_edit == null)
                    {
                        dao.add(myt).addOnSuccessListener(suc ->
                        {

                            Toast.makeText(AddTaskActivity.this, "Task Added.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), TaskActivity.class));


                        }).addOnFailureListener(er->

                        {
                            Toast.makeText(getApplicationContext(), ""+ er.getMessage(),  Toast.LENGTH_SHORT).show();

                        });

                    }
                    else
                    {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("title", title.getText().toString());
                        hashMap.put("time", timePicker.getText().toString());
                        hashMap.put("date", datePicker.getText().toString());
                        hashMap.put("description", desc.getText().toString());
                        hashMap.put("coins", coins.getText().toString());

                        dao.update(myt_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                        {

                            Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), TaskActivity.class));
                            finish();


                        }).addOnFailureListener(er ->
                        {

                            Toast.makeText(getApplicationContext(), ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }

                }

                 if (TextUtils.isEmpty(my_title))
                {
                    title.setHintTextColor(Color.RED);
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            title.setHintTextColor(getResources().getColor(R.color.lightGray));
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
                 if (TextUtils.isEmpty(my_coins))
                {
                    coins.setHintTextColor(Color.RED);
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coins.setHintTextColor(getResources().getColor(R.color.lightGray));
                        }
                    },1000);
                }
            }

        });

    }
    public void ClickTime(View view)
    {
        selectTime();
    }
    public void ClickDate(View view)
    {
        selectDate();
    }

    private void selectTime(){
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

/*        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {*/
                new DatePickerDialog(AddTaskActivity.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
/*
            }
        });*/
    }

    private String updateDate(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return  dateFormat.format(myCalendar.getTime());
    }

}