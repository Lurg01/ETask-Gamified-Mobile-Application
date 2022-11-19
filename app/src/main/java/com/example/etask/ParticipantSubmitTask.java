package com.example.etask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

import java.util.Objects;


// FOR LOCATION
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
/*import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ParticipantSubmitTask extends AppCompatActivity {


    // INITIALIZE VARIABLE
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    DaoSubmitTask dao;
    FirebaseRecyclerAdapter adapter;
    Handler h = new Handler();
    boolean isLoading=false;
    String key =null;
    DBHelper DB;
    LinearLayout participantContent, participantResponses, descriptionContent;
    TextInputLayout txtField;
    TextInputEditText statement;
    TextView taskTitle, taskDescription, taskCoins, taskDate,
            taskTime, participantTask, sumOfCoins, starTxtView, doneTxtView, taskDescriptionSubmitted;
    private static final int PICK_IMAGE = 100;
    ImageButton back, getLocation;
    Button submitBtn;
    View line;
    ImageView startImageView, doneImageView;
    public static class Global { public static String key, uid, uid_of_sk;}
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUriStart, imageUriDone;
    ProgressDialog progressDialog;
    ActionBar actionBar;

    // FOR LOCATION
    private static  final int REQUEST_LOCATION=1;
    TextView showLocationTxt;
    LocationManager locationManager;
    String latitude,longitude, my_location = "", description = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull (getSupportActionBar ()).hide ();
        setContentView(R.layout.activity_participant_submit_task);

        DB = new DBHelper(this);
        back = (ImageButton) findViewById(R.id.back_btn);
        getLocation = (ImageButton) findViewById(R.id.location_imgBtn);
        taskTitle = (TextView) findViewById(R.id.task_title);
        taskDescription = (TextView) findViewById(R.id.task_description);
        taskCoins = (TextView) findViewById(R.id.task_coins);
        taskDate = (TextView) findViewById(R.id.task_date);
        taskTime = (TextView) findViewById(R.id.task_time);
        participantTask = (TextView) findViewById(R.id.participant_task);
        sumOfCoins = (TextView) findViewById(R.id.sum_of_coins);
        taskDescriptionSubmitted = (TextView) findViewById(R.id.taskDescription_submitted);
        participantContent = (LinearLayout) findViewById(R.id.participant_content);
        participantResponses = (LinearLayout) findViewById(R.id.participant_responses);
        descriptionContent = (LinearLayout) findViewById(R.id.description_content);
        line = (View) findViewById(R.id.line);
        startImageView = (ImageView) findViewById(R.id.startTask_imageView);
        doneImageView = (ImageView) findViewById(R.id.doneTask_imageView);
        starTxtView = (TextView) findViewById(R.id.startTask_txtView);
        doneTxtView = (TextView) findViewById(R.id.doneTask_txtView);
        txtField = (TextInputLayout) findViewById(R.id.filledTextField);
        statement = (TextInputEditText) findViewById(R.id.task_statement);
        submitBtn = (Button) findViewById(R.id.submit_btn);

        // FOR LOCATION
        // ADD PERMISSION
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        showLocationTxt= (TextView) findViewById(R.id.show_location);

        // CONFIGURE ACTION BAR
        actionBar = getSupportActionBar();
        actionBar.setTitle("Submit Task");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // CONFIGURE PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Submitting your task. . .");
        progressDialog.setCanceledOnTouchOutside(false);

        //  GET CURRENT USER UID
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Global.uid = currentFirebaseUser.getUid();

        MyTask myt_edit = (MyTask)getIntent().getSerializableExtra("Edit");
        if(myt_edit != null)
        {
            taskTitle.setText(myt_edit.getTitle());
            taskDescription.setText(myt_edit.getDescription());
            taskCoins.setText(myt_edit.getCoins());
            taskDate.setText(myt_edit.getDate());
            taskTime.setText(myt_edit.getTime());

            // CAN BE MOVE TO TOP IN CASE OF DIS FUNCTION
            Cursor resUid = DB.getUidData();
            if (resUid.moveToFirst()) {
                Global.uid_of_sk = resUid.getString(0);

            }
            Global.key = myt_edit.getKey();

        }

        // IDENTIFY USER TYPE
        Cursor res = DB.getUserTypeData();
        if (res.moveToFirst()) {
            switch (res.getString(0)) {
                case "Leader":
                    participantTask.setText("Responses Feed");
                    participantContent.setVisibility(View.GONE);
                    break;

                case "Participant":
                    participantTask.setText("Task Submission");

                    // CHECK IF PARTICIPANT ALREADY SUBMITTED A TASK
                    Cursor resUid = DB.getUidData();
                    String uid = currentFirebaseUser.getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                    if (resUid.moveToFirst()) {
                        databaseReference.child("Users")
                                .child("SK")
                                .child(resUid.getString(0))
                                .child("Task Data")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            String k0 = childSnapshot.getKey();
                                            assert k0 != null;
                                            databaseReference.child("Users")
                                                    .child("SK")
                                                    .child(resUid.getString(0))
                                                    .child("Task Data")
                                                    .child(k0)
                                                    .child("Uploaded Task")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                String k1 = childSnapshot.getKey();
                                                                assert k1 != null;
                                                                databaseReference.child("Users")
                                                                        .child("SK")
                                                                        .child(resUid.getString(0))
                                                                        .child("Task Data")
                                                                        .child(k0)
                                                                        .child("Uploaded Task")
                                                                        .child(k1)
                                                                        .child("uid")
                                                                        .addValueEventListener(
                                                                        new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                String check_uid = dataSnapshot.getValue(String.class);
                                                                                assert check_uid != null;
                                                                                if (check_uid.equals(uid))
                                                                                {
                                                                                    databaseReference.child("Users")
                                                                                            .child("SK")
                                                                                            .child(resUid.getString(0))
                                                                                            .child("Task Data")
                                                                                            .child(k0)
                                                                                            .child("Uploaded Task")
                                                                                            .child(k1)
                                                                                            .child("my_location")
                                                                                            .addValueEventListener(
                                                                                                    new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                            my_location = dataSnapshot.getValue(String.class);

                                                                                                        }
                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                                                                                    });

                                                                                    databaseReference.child("Users")
                                                                                            .child("SK")
                                                                                            .child(resUid.getString(0))
                                                                                            .child("Task Data")
                                                                                            .child(k0)
                                                                                            .child("Uploaded Task")
                                                                                            .child(k1)
                                                                                            .child("statement")
                                                                                            .addValueEventListener(
                                                                                                    new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                            description = dataSnapshot.getValue(String.class);

                                                                                                            // FOR DISPLAYING DATA AND OTHER RESOURCES
                                                                                                            startImageView.setImageResource(R.drawable.ic_baseline_image_24);
                                                                                                            doneImageView.setImageResource(R.drawable.ic_baseline_image_24);
                                                                                                            descriptionContent.setVisibility(View.VISIBLE);
                                                                                                            taskDescriptionSubmitted.setVisibility(View.VISIBLE);
                                                                                                            taskDescriptionSubmitted.setText(description);
                                                                                                            txtField.setVisibility(View.GONE);
                                                                                                            startImageView.setEnabled(false);
                                                                                                            doneImageView.setEnabled(false);
                                                                                                            getLocation.setVisibility(View.INVISIBLE);
                                                                                                            showLocationTxt.setText(my_location);
                                                                                                            submitBtn.setEnabled(false);
                                                                                                            submitBtn.setText("UnSubmit");
                                                                                                            participantResponses.setVisibility(View.GONE);
                                                                                                            submitBtn.setTextColor(getResources().getColor(R.color.white));
                                                                                                            submitBtn.setBackgroundColor(getResources().getColor(R.color.lightGray));

                                                                                                        }
                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                                                                                    });

                                                                                }
                                                                            }
                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                                                                        });

                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) { }
                                                    });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });

                    }
                    break;
            }
        }

        showData();  // SHOW SUBMITTED TASK DATA FOR LEADER

        // BACK BUTTON
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TaskActivity.class));
            }
        });

        //  FOR SELECTING IMAGE ON STAR TASK
        startImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntentStart = new Intent();
                galleryIntentStart.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntentStart.setType("image/*");
                startActivityForResult(galleryIntentStart , 2);
            }
        });
        //  FOR SELECTING IMAGE ON DONE TASK
        doneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntentDone = new Intent();
                galleryIntentDone.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntentDone.setType("image/*");
                startActivityForResult(galleryIntentDone , 3);
            }
        });

        // GET LOCATION FUNCTION
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // CHECK GPS IS ENABLE OR NOT

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    // WRITE FUNCTION TO ENABLE GPS

                    OnGPS();
                }
                else
                {
                    //GPS IS ALREADY ON THEN
                    getLocation();
                }

            }
        });

        // SUBMIT BUTTON FUNCTION
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (imageUriStart != null && imageUriDone != null && !Objects.requireNonNull(statement.getText()).toString().equals(""))
            {
                // Initialize alert dialog
                builder.setIcon(R.drawable.groupx);
                builder.setTitle("Submit");
                builder.setMessage("Your about to submit a task now?");
                // Negative no button
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // DISMISS DIALOG
                        dialogInterface.dismiss();
                        uploadToFirebase(imageUriStart, imageUriDone);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                // SHOW DIALOG
                builder.show();

            }
            else if (imageUriStart != null && imageUriDone != null &&  statement.getText().toString().equals(""))
            {
                Toast.makeText(getApplicationContext(), "Add Description.", Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(getApplicationContext(), "Select an Image.", Toast.LENGTH_SHORT).show();
            }



            }
        });


    }

    // GET CURRENT LOCATION
    private void getLocation() {

        //CHECK PERMISSIONS AGAIN
        if (ActivityCompat.checkSelfPermission(ParticipantSubmitTask.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ParticipantSubmitTask.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                showLocationTxt.setText(latitude+", "+longitude);
                getLocation.setVisibility(View.INVISIBLE);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                showLocationTxt.setText(latitude+", "+longitude);
                getLocation.setVisibility(View.INVISIBLE);

            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                showLocationTxt.setText(latitude+", "+longitude);
                getLocation.setVisibility(View.INVISIBLE);

            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

        }

    }
    // TURN ON GPS
    private void OnGPS() {

        final androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final androidx.appcompat.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    // LEADER USES
    private void showData()
    {

        // ASSIGN VARIABLE
        swipeRefreshLayout = findViewById(R.id.swip_participant_task);
        recyclerView = findViewById(R.id.rv_participant_task);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        dao = new DaoSubmitTask();
        loadData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
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
    // LEADER USES
    private void loadData() {
        FirebaseRecyclerOptions<MySubmitTask> option =
                new FirebaseRecyclerOptions.Builder<MySubmitTask>()
                        .setQuery(dao.get(), new SnapshotParser<MySubmitTask>() {
                            @NonNull
                            @Override
                            public MySubmitTask parseSnapshot(@NonNull DataSnapshot snapshot) {
                                MySubmitTask mySub = snapshot.getValue(MySubmitTask.class);
                                assert mySub != null;
                                mySub.setKey(snapshot.getKey());
                                return mySub;
                            }
                        }).build();

        adapter = new FirebaseRecyclerAdapter(option) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

                ParticipantSubmissionVh vh = (ParticipantSubmissionVh) holder;
                MySubmitTask mySub = (MySubmitTask) model;

                vh.txt_full_name.setText(mySub.getFull_name());
                Glide.with(ParticipantSubmitTask.this).load(mySub.getImageUrl_start()).into(vh.txt_imageUrlStart);
                Glide.with(ParticipantSubmitTask.this).load(mySub.getImageUrl_done()).into(vh.txt_imageUrlDone);
                SpannableString content = new SpannableString("Location >");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                vh.txtLocation.setText(content);
                vh.txtLocation.setOnClickListener(v ->
                {
                    vh.txtLocation.setTextColor(getColor(R.color.lightGray));
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vh.txtLocation.setTextColor(getColor(R.color.grayscale));
                            startActivity(new Intent(ParticipantSubmitTask.this, MapActivity.class).putExtra("Val", mySub.getMy_location()));

                            Toast.makeText(getApplicationContext(), ""+ mySub.getMy_location(), Toast.LENGTH_SHORT).show();
                        }
                    },1000);


                });

                vh.txt_statement.setText(mySub.getStatement());

                vh.approve_task.setOnClickListener(v ->
                {

                    // GET THE CURRENT COINS OF PARTICIPANT AND ADD THE EARNING COINS FOR TOTAL COINS
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Users").child("Participant").child(mySub.getUid()).child("Total Coins").addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String coins = dataSnapshot.getValue(String.class);
                                    assert coins != null;
                                    int current_coins = Integer.parseInt(coins);
                                    int earned_coins = Integer.parseInt(taskCoins.getText().toString());
                                    current_coins += earned_coins;
                                    sumOfCoins.setText(String.valueOf(current_coins));

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                            });
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            databaseReference.child("Users").child("Participant").child(mySub.getUid()).child("Total Coins")
                                    .setValue(sumOfCoins.getText().toString());
                        }
                    },1000);

                });


            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(ParticipantSubmitTask.this).inflate(R.layout.participant_submission, parent, false);
                return new ParticipantSubmissionVh(view);
            }

            @Override
            public void onDataChanged() {
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

    // PARTICIPANT USERS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){

            imageUriStart = data.getData();
            startImageView.setImageURI(imageUriStart);

        }
        else if (requestCode == 3 && resultCode == RESULT_OK && data != null)
        {
            imageUriDone = data.getData();
            doneImageView.setImageURI(imageUriDone);
        }
    }

    // PARTICIPANT USERS
    private void uploadToFirebase(Uri uriStart, Uri uriDone){

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String uid = currentFirebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // RETRIEVE FIRSTNAME AND LASTNAME OF PARTICIPANT
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

                                            databaseReference.child("Users").child("Participant").child(uid).child("User Private Info").child(k).child("lastname").addValueEventListener(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String lName = dataSnapshot.getValue(String.class);
                                                            String full_name = String.format("%s %s" , fName, lName);

                                                            final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uriStart));
                                                            fileRef.putFile(uriStart).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uriStart) {

                                                                            final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uriDone));
                                                                            fileRef.putFile(uriDone).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                        @Override
                                                                                        public void onSuccess(Uri uriDone) {

                                                                                            DaoSubmitTask dao = new DaoSubmitTask();
                                                                                            MySubmitTask mySub = new MySubmitTask(uid, full_name, uriStart.toString(), uriDone.toString(),
                                                                                                    showLocationTxt.getText().toString(), Objects.requireNonNull(statement.getText()).toString());
                                                                                            dao.add(mySub).addOnSuccessListener(suc ->
                                                                                            {
                                                                                                // DISMISS PROGRESS DIALOG
                                                                                                progressDialog.dismiss();
                                                                                                showCompleteDialog();

                                                                                            });

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
                                                                                    showErrorDialog();
                                                                                }
                                                                            });


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
                                                                    showErrorDialog();
                                                                }
                                                            });

                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) { }
                                                    });

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

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    void showErrorDialog() {
        final Dialog dialog = new Dialog(this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.errordialog);
        dialog.show();
    }

    void showCompleteDialog() {
        final Dialog dialog = new Dialog(this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.tasksdialog);
        dialog.show();
    }

}
