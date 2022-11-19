package com.example.etask;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class UserCategory extends AppCompatActivity {

    DBHelper DB;
    RadioGroup radioGroup;
    RadioButton sk, participant;
    Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull (getSupportActionBar ()).hide ();
        setContentView(R.layout.activity_user_category);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        sk = (RadioButton) findViewById(R.id.sk);
        participant = (RadioButton) findViewById(R.id.participant);
        proceed = (Button) findViewById(R.id.proceed);
        DB = new DBHelper(this);
        String for_sk = sk.getText().toString();
        String for_participant = participant.getText().toString();

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int rG = radioGroup.getCheckedRadioButtonId();
                switch(rG)
                {
                    case R.id.sk:

                        Boolean check_insert_data_sk = DB.insertUserTypeData(for_sk);
                        if (check_insert_data_sk.equals(true))
                        {
                            Toast.makeText(UserCategory.this,"You will sign up as " + for_sk, Toast.LENGTH_SHORT).show();
                        }

                        startActivity(new Intent(UserCategory.this, UserSignup.class));
                        finish();
                        break;

                    case R.id.participant:

                        Boolean check_insert_data_participant = DB.insertUserTypeData(for_participant);
                        if (check_insert_data_participant.equals(true))
                        {
                            Toast.makeText(UserCategory.this,"You will sign up as " + for_participant, Toast.LENGTH_SHORT).show();
                        }

                        startActivity(new Intent(UserCategory.this, UserSignup.class));
                        finish();
                        break;

                    default:
                        Toast.makeText(UserCategory.this,"Please select what type of user you are !", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor res = DB.getUserTypeData();
        if (res.getCount() != 0)
        {
            startActivity(new Intent(getApplicationContext(), UserLogin.class));
        }
    }

}