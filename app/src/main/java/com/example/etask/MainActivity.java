package com.example.etask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import java.util.Objects;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        load();
    }

    private void load() {

        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(MainActivity.this, UserCategory.class));
                finish();

            }
        }, 10);

    }
}