package com.example.friendship.BasicDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity2 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageView tvCal;
    EditText etBirthPlace;
    TextView etCal;
    LottieAnimationView frog1,frog2,frog3;
    ImageView tvNext,tvPrevious;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    String purl;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("unregistered");



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);
        tvPrevious = findViewById(R.id.tvprevious);
        tvCal = findViewById(R.id.calender);
        etBirthPlace = findViewById(R.id.etBirthPlace);
        etCal = findViewById(R.id.etCal);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        tvNext = findViewById(R.id.tvNext);

        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
            public void run() {


                frog2.setVisibility(View.VISIBLE);
                frog2.playAnimation();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        frog2.setVisibility(View.VISIBLE);
                    }
                }, 3000);

            }
        }, 3000);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCal.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Select Your Birthday from calender",Toast.LENGTH_SHORT).show();
                    return;
                }if(etBirthPlace.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Your Birth Place",Toast.LENGTH_SHORT).show();
                    return;
                }


                Map<String,Object> users = new HashMap<>();
                users.put("birthday",etCal.getText().toString());
                users.put("location",etBirthPlace.getText().toString());



                myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent i = new Intent(DetailsActivity2.this,DetailsActivity3.class);
                startActivity(i);
            }
        });
        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity2.this,DetailsActivity1.class);
                startActivity(i);
            }
        });

        tvCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int date) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DATE,date);
        String birthDateString = DateFormat.getDateInstance().format(c.getTime());
        etCal.setText(birthDateString);


    }
}