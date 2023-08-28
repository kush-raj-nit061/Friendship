package com.example.friendship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class Testing2 extends AppCompatActivity {

    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;
    LottieAnimationView img2,imgFire;
    ImageView circleImage,fullImage;
    TextView eventName,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing2);
        imgFire = findViewById(R.id.skyshot);
        circleImage = findViewById(R.id.circleImage);
        fullImage = findViewById(R.id.imageView);
        eventName = findViewById(R.id.eventName);
        userName=  findViewById(R.id.username);
        arrowBtn = findViewById(R.id.arrowBtn);
        cardView = findViewById(R.id.cardView);
        img2 = findViewById(R.id.imageView2);
        img2.setAnimationFromUrl("https://firebasestorage.googleapis.com/v0/b/tesla-members-record.appspot.com/o/animation_llv0av4k.json?alt=media&token=9bb46354-e1f6-495c-8d1c-37a26feebf8d");
        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFire.playAnimation();

            }
        });
    }
}