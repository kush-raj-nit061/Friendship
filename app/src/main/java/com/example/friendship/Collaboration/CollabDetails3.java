package com.example.friendship.Collaboration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.CollaborationActivity;
import com.example.friendship.R;
import com.example.friendship.SeeCollaborationDetail;

public class CollabDetails3 extends AppCompatActivity {

    ImageView tvNext;
    LottieAnimationView frog1,frog2,frog3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_details3);

        tvNext = findViewById(R.id.tvNext);

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

                        frog3.playAnimation();
                    }
                }, 3000);

            }
        }, 3000);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CollabDetails3.this, SeeCollaborationDetail.class);
                startActivity(i);
            }
        });
    }
}