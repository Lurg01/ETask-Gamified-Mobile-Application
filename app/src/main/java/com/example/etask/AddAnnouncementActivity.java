package com.example.etask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;


public class AddAnnouncementActivity extends AppCompatActivity {

    ImageButton back;
    Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_add_announcement);


        final EditText title = findViewById(R.id.editTxt_title1);
        final EditText desc = findViewById(R.id.editTxt_desc1);

        back = findViewById(R.id.imageBtn_back);
        createBtn = findViewById(R.id.create_btn1);

        String from = getIntent().getExtras().getString("val");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
               /* Intent i = new Intent(AddAns.this, AnnouncementsActivity.class);
                startActivity(i);
                finish();*/
            }
        });

        DaoAnnouncement dao = new DaoAnnouncement();
        MyAnnouncement myAns_edit = (MyAnnouncement) getIntent().getSerializableExtra("EDIT");
        if(myAns_edit != null)
        {
            createBtn.setText("UPDATE");
            title.setText(myAns_edit.getTitle());   //  WARNING !!!!!!!
            desc.setText(myAns_edit.getDescription()); //  WARNING !!!!!!!


            //startActivity(new Intent(AddTask.this, RvActivity.class));
            //finish();
        }
        if (myAns_edit == null)
        {
            createBtn.setText("SUBMIT");

        }

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Handler h = new Handler();
                String mytitle = title.getText().toString();
                String mydesc = desc.getText().toString();


                if( TextUtils.isEmpty(mytitle)) { title.setError("Title is required !"); }
                else if(TextUtils.isEmpty(mydesc)) { desc.setError("Description field Empty  !");}
                else
                {

                    MyAnnouncement myAns =  new MyAnnouncement(title.getText().toString(), desc.getText().toString()); // Convert input to int  >   Integer.parseInt(coins.getText().toString())
                    if(myAns_edit == null)
                    {
                        dao.add(myAns).addOnSuccessListener(suc ->
                        {

                            if (from.equals("home"))
                            {
                                Toast.makeText(AddAnnouncementActivity.this, "Announcement posted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddAnnouncementActivity.this, HomeActivity.class));
                            }
                            else if (from.equals("announcement"))
                            {
                                Toast.makeText(AddAnnouncementActivity.this, "Announcement posted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddAnnouncementActivity.this, AnnouncementActivity.class));
                            }



                        }).addOnFailureListener(er ->
                        {
                            Toast.makeText(AddAnnouncementActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                    }
                    else
                    {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("title", title.getText().toString());
                        hashMap.put("description", desc.getText().toString());

                        dao.update(myAns_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                        {
                            Toast.makeText(AddAnnouncementActivity.this, "Announcement Updated", Toast.LENGTH_SHORT).show();
                          /*  startActivity(new Intent(AddAns.this, AnnouncementsActivity.class));
                            finish();*/
                        }).addOnFailureListener(er ->
                        {
                            Toast.makeText(AddAnnouncementActivity.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }

                }
                desc.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (desc.hasFocus()) {
                            view.getParent().requestDisallowInterceptTouchEvent(true);
                            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                                case MotionEvent.ACTION_SCROLL:
                                    view.getParent().requestDisallowInterceptTouchEvent(false);
                                    return true;
                            }
                        }
                        return false;
                    }
                });


/*
                Intent i = new Intent(AddTask.this, NavHome.class);
                str = title.getText().toString();
                str1 = desc.getText().toString();
                str2 = num.getText().toString();
                i.putExtra("Val", str);
                i.putExtra("Val1", str1);
                i.putExtra("Val2", str2);
                startActivity(i);
                finish();

 */

            }


        });



    }
}