package com.example.etask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;


public class QRScanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String qr_text = "", uid_of_participant = "", reward_key = "";
    ImageButton exit;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_qrscanner);

        exit = (ImageButton) findViewById(R.id.exit);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qr_text =  result.getText();
                        String[] text = qr_text.split(" ");
                        uid_of_participant = text[0];
                        reward_key = text[1];
                        Toast.makeText(QRScanner.this, ""+text[0] + " "+text[1], Toast.LENGTH_SHORT).show();
                        rewardClaimed();

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();

                databaseReference.child("Users")
                        .child("Participant")
                        .child(uid_of_participant)
                        .child("My Bag")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    String key = childSnapshot.getKey();
                                    assert key != null;
                                    if (key.equals(reward_key))
                                    {
                                        databaseReference.child("Users")
                                                .child("Participant")
                                                .child(uid_of_participant)
                                                .child("My Bag").child(key).removeValue().addOnSuccessListener(suc ->
                                                {
                                                    final Dialog dialog = new Dialog(QRScanner.this);
                                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    dialog.setCancelable(true);
                                                    dialog.setContentView(R.layout.reward_claimed_dialog);
                                                    dialog.show();
                                                    recreate();
                                                });

                                    }

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
        requestForCamera();

    }

    private void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                mCodeScanner.startPreview();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getApplicationContext(), "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }

        }).check();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();

    }

    void rewardClaimed()
    {

    }


}