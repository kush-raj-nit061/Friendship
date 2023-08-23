package com.example.friendship.BasicDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity1 extends AppCompatActivity {

    LottieAnimationView frog1,frog2,frog3;
    LottieAnimationView tvNext;
    Spinner spinner ;
    String yearSelected;
    EditText etHobbies,etShortDetails;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    String purl;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("students");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details1);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        tvNext = findViewById(R.id.tvNext);
        spinner = findViewById(R.id.spinner);
        etHobbies =findViewById(R.id.etHobbies);
        etShortDetails = findViewById(R.id.etShortDetails);

        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
            public void run() {


                frog2.setVisibility(View.VISIBLE);

                handler.postDelayed(new Runnable() {
                    public void run() {

                        frog2.setVisibility(View.VISIBLE);
                    }
                }, 3000);

            }
        }, 3000);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearSelected =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> users = new HashMap<>();
                users.put("shortBio",etShortDetails.getText().toString());

                users.put("hobbies",etHobbies.getText().toString());

                users.put("year",yearSelected);

                myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    }
                });




                Intent i = new Intent(DetailsActivity1.this,DetailsActivity2.class);
                startActivity(i);
            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1st Year");
        arrayList.add("2nd Year");
        arrayList.add("3rd Year");
        arrayList.add("4th Year");
        arrayList.add("M.Tech");
        arrayList.add("P.H.D");
        arrayList.add("Dual Degree");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);






    }
}