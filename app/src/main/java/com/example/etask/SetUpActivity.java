package com.example.etask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpActivity extends AppCompatActivity {

    // INIT
    DBHelper DB;
    CircleImageView profile_image;
    LinearLayout skContent, participantContent;
    EditText fname, lname, group_name, barangay, municipal, town, address, phone, hobby;
    Button finishBtn;
    ProgressDialog progressDialog;
    ActionBar actionBar;



    public static class Global {
        public static String user_type, uid;
    }
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull (getSupportActionBar ()).hide ();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE);
        setContentView(R.layout.activity_set_up);

        DB = new DBHelper(this);

        skContent = (LinearLayout) findViewById(R.id.sk_setup_content);
        participantContent = (LinearLayout) findViewById(R.id.participant_setup_content);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        fname = (EditText) findViewById(R.id.f_ame);           lname = (EditText) findViewById(R.id.l_name);
        group_name = (EditText) findViewById(R.id.group_name);  barangay = (EditText) findViewById(R.id.brgy);
        municipal = (EditText) findViewById(R.id.municipal);    town = (EditText) findViewById(R.id.town);
        address = (EditText) findViewById(R.id.address);        phone = (EditText) findViewById(R.id.phone_number);
        hobby = (EditText) findViewById(R.id.hobby);
        finishBtn = (Button) findViewById(R.id.finish_btn);

        // CONFIGURE ACTION BAR
        actionBar = getSupportActionBar();
        actionBar.setTitle("Setup Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        // CONFIGURE PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Setting up your account . . .");
        progressDialog.setCanceledOnTouchOutside(false);


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntentStart = new Intent();
                galleryIntentStart.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntentStart.setType("image/*");
                startActivityForResult(galleryIntentStart , 2);
            }
        });


        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uid = currentFirebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        DB = new DBHelper(this);
        Cursor res = DB.getUserTypeData();
        if (res.moveToFirst()) {
            switch (res.getString(0)) {
                case "Leader":
                    Global.uid = currentFirebaseUser.getUid();
                    Global.user_type = "SK";
                    databaseReference.child("Users")
                            .child("SK")
                            .child(uid)
                            .child("User Private Info")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        String k = childSnapshot.getKey();

                                        databaseReference.child("Users").child("SK").child(uid).child("User Private Info").child(k).child("firstname").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String fName = dataSnapshot.getValue(String.class);
                                                        fname.setText(fName);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("SK").child(uid).child("User Private Info").child(k).child("lastname").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String lName = dataSnapshot.getValue(String.class);
                                                        lname.setText(lName);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                    break;

                case "Participant":
                    Global.user_type = "";
                    skContent.setVisibility(View.GONE);
                    participantContent.setVisibility(View.VISIBLE);
                    Global.uid = currentFirebaseUser.getUid();
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
                                                        fname.setText(fName);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });

                                        databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(k).child("lastname").addValueEventListener(
                                                new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String lName = dataSnapshot.getValue(String.class);
                                                        lname.setText(lName);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                    break;
            }
        }

        MySetUp mys_edit = (MySetUp) getIntent().getSerializableExtra("Edit");
        if(mys_edit != null)
        {
//            createBtn.setText(R.string.update);
            fname.setText(mys_edit.getFname());             lname.setText(mys_edit.getLname()); //  WARNING !!!!!!!
            group_name.setText(mys_edit.getGroup_name());   barangay.setText(mys_edit.getBarangay());
            municipal.setText(mys_edit.getMunicipal());     town.setText(mys_edit.getTown());

        }
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (imageUri != null)
                {
                    uploadToFirebase(imageUri);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Select an Image.", Toast.LENGTH_SHORT).show();
                }

       /*
                startActivity(new Intent(SetUpActivity.this, UserLogin.class));
                finish();*/

                String check_fname, check_lname, check_group_name, check_barangay, check_municipal, check_town,
                        check_kagd1, check_kagd2, check_kagd3, check_kagd4, check_kagd5, check_kagd6, check_kagd7, check_kagd8;

                check_fname = fname.getText().toString();              check_lname = lname.getText().toString();
                check_group_name = group_name.getText().toString();    check_barangay = barangay.getText().toString();
                check_town = town.getText().toString();                check_municipal = municipal.getText().toString();

            /*    if (TextUtils.isEmpty(check_fname)) { fname.setError(check_fname + " is empty !");}
                if (TextUtils.isEmpty(check_lname)) { lname.setError(check_lname + " is empty !");}
                if (TextUtils.isEmpty(check_group_name)) { group_name.setError(check_group_name + " is empty !");}
                if (TextUtils.isEmpty(check_barangay)) { barangay.setError(check_barangay + " is empty !");}
                if (TextUtils.isEmpty(check_municipal)) { municipal.setError(check_municipal + " is empty !");}
                if (TextUtils.isEmpty(check_town))  { town.setError(check_town + " is empty !");}
                if (TextUtils.isEmpty(check_kagd1)) { kagd1.setError(check_kagd1 + " is empty !");}
                if (TextUtils.isEmpty(check_kagd2)) { kagd2.setError(check_kagd2 + " is empty !");}
                if (TextUtils.isEmpty(check_kagd3)) { kagd3.setError(check_kagd3 + " is empty !");}
                if ( TextUtils.isEmpty(check_kagd4)) { kagd4.setError(check_kagd4 + " is empty !");}
                if (TextUtils.isEmpty(check_kagd5)) { kagd5.setError(check_kagd5 + " is empty !");}
                if (TextUtils.isEmpty(check_kagd6)) { kagd6.setError(check_kagd6 + " is empty !");}
                if (TextUtils.isEmpty(check_kagd7)) { kagd7.setError(check_kagd7 + " is empty !");}
                if (TextUtils.isEmpty(check_kagd8)) { kagd8.setError(check_kagd8 + " is empty !");}

                if( TextUtils.isEmpty(check_fname) &&  TextUtils.isEmpty(check_lname) && TextUtils.isEmpty(check_group_name) && TextUtils.isEmpty(check_barangay) ||
                        TextUtils.isEmpty(check_municipal) &&  TextUtils.isEmpty(check_town) &&  TextUtils.isEmpty(check_kagd1) && TextUtils.isEmpty(check_kagd2) ||
                        TextUtils.isEmpty(check_kagd3) &&  TextUtils.isEmpty(check_kagd4) || TextUtils.isEmpty(check_kagd5) && TextUtils.isEmpty(check_kagd6) ||
                        TextUtils.isEmpty(check_kagd7) &&  TextUtils.isEmpty(check_kagd8)) { Toast.makeText(SetUpActivity.this, "Please fill the necessary details !", Toast.LENGTH_SHORT).show(); }
                else
                {*/



                     // Convert input to int  >   Integer.parseInt(coins.getText().toString())
                 /*   if(mys_edit == null)
                    {
                        dao.add(mys).addOnSuccessListener(suc ->
                        {
                            Toast.makeText(SetUpActivity.this, "Setup Complete for " + check_fname + check_lname + ", your good to go ..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetUpActivity.this, HomeActivity.class));
                            finish();

                        }).addOnFailureListener(er->

                        {
                            Toast.makeText(SetUpActivity.this, ""+ er.getMessage(),  Toast.LENGTH_SHORT).show();

                        });

                    }
                    else
                    {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("firstname", fname.getText().toString());
                        hashMap.put("lastname", lname.getText().toString());
                        hashMap.put("group name", group_name.getText().toString());
                        hashMap.put("barangay", barangay.getText().toString());
                        hashMap.put("municipality",  municipal.getText().toString());
                        hashMap.put("town", town.getText().toString());

                        dao.update(mys_edit.getKey(), hashMap).addOnSuccessListener(suc ->
                        {

                            Toast.makeText(SetUpActivity.this, "Profile Updated.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetUpActivity.this, HomeActivity.class));
                            finish();

                        }).addOnFailureListener(er ->
                        {
                            Toast.makeText(SetUpActivity.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }*/
//                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            profile_image.setImageURI(imageUri);

        }

    }

    private void uploadToFirebase(Uri uri){
        DB = new DBHelper(this);
        // UPLOAD USER SETUP INFO
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        Cursor res = DB.getUserTypeData();
                        if (res.moveToFirst()) {
                            switch (res.getString(0)) {
                                case "Leader":


                                    DaoSetUp dao = new DaoSetUp();
                                    MySetUp mys = new MySetUp(uri.toString(), fname.getText().toString(), lname.getText().toString(),
                                            group_name.getText().toString(), "", barangay.getText().toString(), town.getText().toString(),
                                            municipal.getText().toString());

                                    dao.add(mys).addOnSuccessListener(suc ->
                                    {
                                        // DISMISS PROGRESS DIALOG
                                        progressDialog.dismiss();
                                        Toast.makeText(SetUpActivity.this, "Setup Complete for " + fname.getText().toString() + " " + lname.getText().toString() + ", your good to go ..", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();

                                    }).addOnFailureListener(er ->

                                    {
                                        // DISMISS PROGRESS DIALOG
                                        progressDialog.dismiss();
                                        Toast.makeText(SetUpActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                                    });

                                    break;
                                case "Participant":


                                    DaoSetUp dao1 = new DaoSetUp();
                                    MyParticipantSetup myPS = new MyParticipantSetup(uri.toString(), fname.getText().toString(), lname.getText().toString(),
                                            address.getText().toString(), phone.getText().toString(),hobby.getText().toString());

                                    dao1.add(myPS).addOnSuccessListener(suc ->
                                    {
                                        // DISMISS PROGRESS DIALOG
                                        progressDialog.dismiss();
                                        Toast.makeText(SetUpActivity.this, "Setup Complete for " + fname.getText().toString() + " " + lname.getText().toString() + ", your good to go ..", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();

                                    }).addOnFailureListener(er ->

                                    {
                                        // DISMISS PROGRESS DIALOG
                                        progressDialog.dismiss();
                                        Toast.makeText(SetUpActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                                    });
                                    break;
                            }
                        }
                    }
                });
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                // SHOW PROGRESS DIALOG
                progressDialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // DISMISS PROGRESS DIALOG
                progressDialog.dismiss();

            }
        });

    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}