package com.example.etask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.Objects;

public class FactTrees extends AppCompatActivity {
    ImageButton back;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_fact_trees);

        cardView = (CardView) findViewById(R.id.cv11);
        back = (ImageButton) findViewById(R.id.back_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.woodlandtree.com/terrans-tips/posts/fun-facts-about-trees#:~:text=Trees%20renew%20our%20air%20supply,of%20carbon%20dioxide%20each%20year."));
                startActivity(browserIntent);
            }
        });
    }
}