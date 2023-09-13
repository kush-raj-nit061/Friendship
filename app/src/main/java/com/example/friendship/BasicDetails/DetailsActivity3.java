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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DetailsActivity3 extends AppCompatActivity {

    Spinner spinner ;
    String foodSelected;
    LottieAnimationView frog1,frog2,frog3,frog4;
    ImageView tvSkip;
    ImageView tvNext,tvPrevious;
    Chip chip1,chip2,chip3,chip4,chip5,chip6,chip7,chip8,chip9,chip10,chip11;
    Chip chip1p,chip2p,chip3p,chip4p,chip5p,chip6p,chip7p,chip8p,chip9p;
    String strBookLike,strPlaceTravel;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("unregistered");



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details3);
        tvPrevious = findViewById(R.id.tvPrevious);
        strBookLike="";
        strPlaceTravel="";
        chip1 = findViewById(R.id.chip1);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);
        chip7 = findViewById(R.id.chip7);
        chip8 = findViewById(R.id.chip8);
        chip9 = findViewById(R.id.chip9);
        chip10 = findViewById(R.id.chip10);
        chip11 = findViewById(R.id.chip11);


        chip1p = findViewById(R.id.chip1p);
        chip2p = findViewById(R.id.chip2p);
        chip3p = findViewById(R.id.chip3p);
        chip4p = findViewById(R.id.chip4p);
        chip5p = findViewById(R.id.chip5p);
        chip6p = findViewById(R.id.chip6p);
        chip7p = findViewById(R.id.chip7p);
        chip8p = findViewById(R.id.chip8p);
        chip9p = findViewById(R.id.chip9p);


        ArrayList<Chip> arrayChip = new ArrayList<>();
        arrayChip.add(chip1);
        arrayChip.add(chip2);
        arrayChip.add(chip3);
        arrayChip.add(chip4);
        arrayChip.add(chip5);
        arrayChip.add(chip6);
        arrayChip.add(chip7);
        arrayChip.add(chip8);
        arrayChip.add(chip9);
        arrayChip.add(chip10);
        arrayChip.add(chip11);

        ArrayList<Chip> arrayChipp = new ArrayList<>();
        arrayChipp.add(chip1p);
        arrayChipp.add(chip2p);
        arrayChipp.add(chip3p);
        arrayChipp.add(chip4p);
        arrayChipp.add(chip5p);
        arrayChipp.add(chip6p);
        arrayChipp.add(chip7p);
        arrayChipp.add(chip8p);
        arrayChipp.add(chip9p);





        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        frog4 = findViewById(R.id.frog4);
        tvSkip = findViewById(R.id.tvSkip);
        tvNext = findViewById(R.id.tvNext);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodSelected =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Vegetarian");
        arrayList.add("NonVegetarian");
        arrayList.add("Flexiterian");
        arrayList.add("Vegan");
        arrayList.add("Pollotarians");
        arrayList.add("Pescetarians");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
            public void run() {


                frog2.setVisibility(View.VISIBLE);
                frog2.playAnimation();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        frog3.playAnimation();
                    }
                }, 3000);

            }
        }, 3000);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0;i<arrayChipp.size();i++){
                    Chip chip = arrayChipp.get(i);
                    if(chip.isChecked()){
                        if(strPlaceTravel == null || strPlaceTravel.isEmpty()){
                            strPlaceTravel=chip.getText().toString()+"";
                        }else {
                            strPlaceTravel+=","+chip.getText().toString();
                        }
                    }

                }


                for (int i = 0;i<arrayChip.size();i++){
                    Chip chip = arrayChip.get(i);
                    if(chip.isChecked()){
                        if(strBookLike == null || strBookLike.isEmpty()){
                            strBookLike=chip.getText().toString()+"";
                        }else {
                            strBookLike+=","+chip.getText().toString();
                        }
                    }

                }



                Toast.makeText(getApplicationContext(),strBookLike,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),strPlaceTravel,Toast.LENGTH_SHORT).show();


                Map<String,Object> users = new HashMap<>();
                users.put("travellike",strPlaceTravel);
                users.put("books",strBookLike);
                users.put("foods",foodSelected);

                strBookLike = "";
                strPlaceTravel ="";



                myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent i = new Intent(DetailsActivity3.this,DetailsActivity4.class);
                startActivity(i);
                finish();
            }
        });


        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity3.this,DetailsActivity5.class);
                startActivity(i);
                finish();
            }
        });

        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity3.this,DetailsActivity2.class);
                startActivity(i);
                finish();
            }
        });


    }
}