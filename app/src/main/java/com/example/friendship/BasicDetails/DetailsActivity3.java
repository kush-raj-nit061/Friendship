package com.example.friendship.BasicDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.MainActivity;
import com.example.friendship.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity3 extends AppCompatActivity {

    Spinner spinner ;
    String foodSelected;
    LottieAnimationView frog1,frog2,frog3,frog4;
    TextView tvSkip;
    LottieAnimationView tvNext;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details3);
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
                Intent i = new Intent(DetailsActivity3.this,DetailsActivity4.class);
                startActivity(i);
            }
        });


        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity3.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}