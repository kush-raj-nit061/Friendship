package com.example.friendship.BasicDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.R;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity4 extends AppCompatActivity {

    LottieAnimationView tvNext;

    LottieAnimationView frog1,frog2,frog3,frog4;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details4);
        tvNext = findViewById(R.id.tvNext);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        frog4 = findViewById(R.id.frog4);

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
                Intent i = new Intent(DetailsActivity4.this,DetailsActivity5.class);
                startActivity(i);
            }
        });


    }
}