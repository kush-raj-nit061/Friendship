package com.test.friendship.BasicDetails;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.test.friendship.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity2 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageView tvCal;
    EditText etBirthPlace;
    TextView etCal;
    ImageView tvSkip;
    TextView skipp;
    LottieAnimationView frog1,frog2,frog3;
    ImageView tvNext;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("unregistered");
    DatabaseReference Ref = database.getReference("students");



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);
        tvCal = findViewById(R.id.calender);
        etBirthPlace = findViewById(R.id.etBirthPlace);
        etCal = findViewById(R.id.etCal);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        tvNext = findViewById(R.id.tvNext);
        tvSkip = findViewById(R.id.tvSkip);
        skipp = findViewById(R.id.skipp);

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

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity2.this,DetailsActivity5.class);
                startActivity(i);
                finish();
            }
        });
        skipp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity2.this,DetailsActivity5.class);
                startActivity(i);
                finish();
            }
        });
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

                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fStore.collection("users").document(fAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                String detailsGiven = document.getString("detailsGiven");
                                if(detailsGiven.equals("1")){
                                    Ref.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent i = new Intent(DetailsActivity2.this,DetailsActivity3.class);
                                    startActivity(i);
                                    finish();

                                }else {
                                    myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent i = new Intent(DetailsActivity2.this,DetailsActivity3.class);
                                    startActivity(i);
                                    finish();

                                }
                            }
                        }

                    }
                });



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