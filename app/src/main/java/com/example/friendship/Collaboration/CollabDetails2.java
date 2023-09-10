package com.example.friendship.Collaboration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.R;

public class CollabDetails2 extends AppCompatActivity {

    LottieAnimationView frog1,frog2,frog3;

    ImageView tvNext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_details2);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);

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

        tvNext = findViewById(R.id.tvNext);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CollabDetails2.this, CollabDetails3.class);
                startActivity(i);
            }
        });
    }
}