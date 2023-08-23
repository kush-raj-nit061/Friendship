package com.example.friendship.BasicDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity4 extends AppCompatActivity {

    LottieAnimationView tvNext,tvPrevious;
    String strQualityLike,strQualityDislike;
    EditText etQualityLike,etQualityDislike;
    Chip chip1,chip2,chip3,chip4,chip5,chip6,chip7,chip8,chip9,chip10,chip11;
    Chip chip1p,chip2p,chip3p,chip4p,chip5p,chip6p,chip7p,chip8p,chip9p,chip10p,chip11p;

    LottieAnimationView frog1,frog2,frog3,frog4;
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
        setContentView(R.layout.activity_details4);
        tvPrevious = findViewById(R.id.tvPrevious);
        tvNext = findViewById(R.id.tvNext);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        frog4 = findViewById(R.id.frog4);
        etQualityDislike = findViewById(R.id.etQualityDislike);
        etQualityLike = findViewById(R.id.etQualityLike);

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


        chip1p = findViewById(R.id.chip1d);
        chip2p = findViewById(R.id.chip2d);
        chip3p = findViewById(R.id.chip3d);
        chip4p = findViewById(R.id.chip4d);
        chip5p = findViewById(R.id.chip5d);
        chip6p = findViewById(R.id.chip6d);

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




        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
            public void run() {


                frog2.setVisibility(View.VISIBLE);
                frog2.playAnimation();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        frog3.playAnimation();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                frog4.playAnimation();
                            }
                        },3000);
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
                        if(strQualityDislike == null || strQualityDislike.isEmpty()){
                            strQualityDislike=chip.getText().toString()+"";
                        }else {
                            strQualityDislike+=","+chip.getText().toString();
                        }
                    }

                }


                for (int i = 0;i<arrayChip.size();i++){
                    Chip chip = arrayChip.get(i);
                    if(chip.isChecked()){
                        if(strQualityLike == null || strQualityLike.isEmpty()){
                            strQualityLike=chip.getText().toString()+"";
                        }else {
                            strQualityLike+=","+chip.getText().toString();
                        }
                    }

                }


                Map<String,Object> users = new HashMap<>();
                users.put("qualitylike",strQualityLike+" "+etQualityLike.getText().toString());
                users.put("qualitydislike",strQualityDislike+" "+etQualityDislike.getText().toString());

                strQualityLike ="";
                strQualityDislike="";




                myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    }
                });


                Intent i = new Intent(DetailsActivity4.this,DetailsActivity5.class);
                startActivity(i);
            }
        });

        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity4.this,DetailsActivity3.class);
                startActivity(i);
            }
        });


    }
}