package com.example.etask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class UserSignup extends AppCompatActivity {

    // INITIALIZE
    TextInputEditText firstname, lastname, email, pass, confPass;
    Button sign_up, goto_login;
    RadioGroup radioGroup;
    RadioButton sk, participant;
    String txtFirstName, txtLastName, txtEmail, txtPass, txtConfPass;
    FirebaseAuth auth;
    DBHelper DB;
    int click_count = 0;
    ProgressDialog progressDialog;
    ActionBar actionBar;

    public static class Global {
        public static String user, uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_user_signup);


        // CONFIGURE ACTION BAR
        actionBar = getSupportActionBar();
        actionBar.setTitle("SignUp");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // INIT FIREBASE AUTH
        auth = FirebaseAuth.getInstance();

        // CONFIGURE PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating you account . . .");
        progressDialog.setCanceledOnTouchOutside(false);

        firstname = (TextInputEditText) findViewById(R.id.editTxt_fName);
        lastname = (TextInputEditText) findViewById(R.id.editTxt_lName);
        email = (TextInputEditText) findViewById(R.id.editTxt_email);
        pass = (TextInputEditText) findViewById(R.id.editTxt_pass);
        confPass = (TextInputEditText) findViewById(R.id.editTxt_confPass);
        sign_up = findViewById(R.id.signUp_btn);

        goto_login = findViewById(R.id.login_btn);

        sk = (RadioButton) findViewById(R.id.sk);
        participant = (RadioButton) findViewById(R.id.participant);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);


/*

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Handler h = new Handler();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Users")
                        .child("SK")
                        .child("Users Info")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    String k = childSnapshot.getKey();

                                    //Toast.makeText(NavHomeActivity.this, "" + k, Toast.LENGTH_SHORT).show();
                                    databaseReference.child("Users").child("SK").child("Users Info").child(k).child("username").addValueEventListener(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    // Get the address object
                                                    String uName = dataSnapshot.getValue(String.class);
                                                    // get the string here, using address.getCity() or whatever you called the getter

                                                    LinkedHashMap<String, List<String>> A = new LinkedHashMap<String,List<String>>();
                                                    List<String> list = new ArrayList<String>();
                                                    list.add(uName);

                                                    //A.put("a", list);
                                                    for (String n: list)
                                                    {
                                                        Toast.makeText(SignUp.this, ""+n, Toast.LENGTH_SHORT).show();

                                                        if (charSequence.toString().equals(n)) {
                                                            String notf = "Username Existing, try a unique one 😐";
                                                            notify.setText(notf);
                                                            notify.setTextColor(Color.RED);
                                                            notify.setBackgroundResource(R.color.browser_actions_bg_grey);

                                                        }

                                                        else if (!charSequence.toString().equals(n))  {
                                                            String notf = "Your Username is Unique 😃";
                                                            notify.setText(notf);
                                                            notify.setTextColor(Color.GREEN);

                                                        }

                                                    }


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

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

 */



        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();


            }
        });

        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserSignup.this, UserLogin.class));
                finish();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // goto previous activity when back button of action bar clicked
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler h = new Handler();
        DB = new DBHelper(this);

        // get current user
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // check if user is already logged in
        Cursor res = DB.getUserTypeData();
        if (firebaseUser != null)
        {

        // check if usertype exist
            if (res.moveToFirst()) {
                switch (res.getString(0))
                {
                    case "Leader":
                    case "Participant":

                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserSignup.this, "Please finish setting up your account for necessary details.", Toast.LENGTH_SHORT).show();

                            }
                        },1000);

                        startActivity(new Intent(UserSignup.this, SetUpActivity.class));
                        finish();
                        break;

                }
            }


        }

        else
        {
            if (res.moveToFirst()) {
                switch (res.getString(0))
                {
                    case "Leader":
                        sk.setChecked(true);
                        break;
                    case "Participant":
                        participant.setChecked(true);
                        break;
                }
            }

        }

    }
    private void validateData()
    {

        Handler h = new Handler();
        txtFirstName = firstname.getText().toString();
        txtLastName= lastname.getText().toString();
        txtEmail = email.getText().toString();
        txtPass = pass.getText().toString();
        txtConfPass = confPass.getText().toString();


        // validate data
        if(TextUtils.isEmpty(txtFirstName) || TextUtils.isEmpty(txtLastName) || TextUtils.isEmpty(txtEmail) ||
                TextUtils.isEmpty(txtPass) || TextUtils.isEmpty(txtConfPass)){
            Toast.makeText(UserSignup.this, "Please complete the required credentials !", Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(txtConfPass))
        {

            Toast.makeText(UserSignup.this, "Confirm password !", Toast.LENGTH_SHORT).show();

        }
        else if(txtPass.length() < 6)
        {
            Toast.makeText(UserSignup.this,"Your password is too short !", Toast.LENGTH_SHORT).show();
            pass.setTextColor(Color.RED);

            if(click_count  < 2 )
            {
                click_count += 1;
            }
            else
            {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(UserSignup.this);
                dlgAlert.setMessage("Your password is too short !");
                dlgAlert.setTitle("Error Message...");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pass.setTextColor(Color.BLACK);
                }
            },1000);

        }else if(!txtPass.equals(txtConfPass))
        {

            Toast.makeText(UserSignup.this,"Your password did not match !", Toast.LENGTH_SHORT).show();
            confPass.setTextColor(Color.RED);

            if(click_count  < 2 )
            {
                click_count += 1;
            }
            else
            {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(UserSignup.this);
                dlgAlert.setMessage("Your password did not match ");
                dlgAlert.setTitle("Error Message...");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    confPass.setTextColor(Color.BLACK);
                }
            },1000);

        }
        else
        {

            signUp(txtEmail,txtPass);

        }

    }

    private void signUp(String userEmail, String userPass) {

        // SHOW PROGRESS DIALOG
        progressDialog.show();
        final Handler h = new Handler();

        DB = new DBHelper(this);


        auth.createUserWithEmailAndPassword(userEmail, userPass).addOnSuccessListener(UserSignup.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // sigup success
                progressDialog.dismiss();
                // get user info
                FirebaseUser firebaseUser = auth.getCurrentUser();
                Cursor res = DB.getUserTypeData();
                int rG = radioGroup.getCheckedRadioButtonId();
                switch (rG) {

                    case R.id.sk:

                        Global.user = "SK";
                        if (res.moveToFirst()) {
                            if (res.getString(0).equals("Participant")) {

                                DB.deleteUserTypeData(res.getString(0));
                                DB.insertUserTypeData(sk.getText().toString());
                                checkUserType();

                            }
                        }

                        checkUserType();
                        break;

                    case R.id.participant:

                        Global.user = "Participant";
                        if (res.moveToFirst()) {
                            if (res.getString(0).equals("Leader")) {

                                DB.deleteUserTypeData(res.getString(0));
                                DB.insertUserTypeData(participant.getText().toString());
                                checkUserType();
                            }
                        }
                        checkUserType();
                        break;

                    default:
                        Toast.makeText(UserSignup.this, "Please select user type !", Toast.LENGTH_SHORT).show();
                        break;
                }

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                assert currentFirebaseUser != null;
                Global.uid = currentFirebaseUser.getUid();

                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        DaoUsername dao = new DaoUsername();
                        GetUsername getU =  new GetUsername(txtFirstName, txtLastName, txtEmail, txtPass); // Convert input to int  >   Integer.parseInt(coins.getText().toString())
                        dao.add(getU).addOnSuccessListener(suc ->
                        {

                            assert firebaseUser != null;
                            String email = firebaseUser.getEmail();

                            Toast.makeText(UserSignup.this, "You're Successfully Sign up.\n" + email, Toast.LENGTH_SHORT).show();
                        });

                    }
                },1000);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // SIGNUP FAILED
                progressDialog.dismiss();
                Toast.makeText(UserSignup.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkUserType() {

        DB = new DBHelper(this);
        Cursor res_usertype = DB.getUserTypeData();
        if (res_usertype.moveToFirst()) {
            switch (res_usertype.getString(0))
            {
                case "Leader":
                    startActivity(new Intent(UserSignup.this, SetUpActivity.class));
                    finish();
                    break;

                case "Participant":

                    // CHECK IF NEW PARTICIPANT SIGN UP ACCOUNT, ALREADY HAVE UID OF SK, IF TRUE DELETE ELSE DIRECT TO HOME
                    Cursor res_uid = DB.getUidData();
                    if (res_uid.getCount() != 0 )
                    {
                        if (res_uid.moveToFirst()) {
                            DB.deleteUidData(res_uid.getString(0));
                            recreate();
                        }

                    }
                    else {

                        startActivity(new Intent(UserSignup.this, SetUpActivity.class));
                        finish();
                    }
                    break;
            }

        }

    }

}