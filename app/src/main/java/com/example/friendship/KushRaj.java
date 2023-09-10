package com.example.friendship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class KushRaj extends AppCompatActivity {

    RelativeLayout expandableView;
    Button arrowBtn;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kush_raj);

//        expandableView = findViewById(R.id.expandableView);
//        arrowBtn = findViewById(R.id.arrowBtn);
//        cardView = findViewById(R.id.cardView);

//        arrowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (expandableView.getVisibility()==View.GONE){
//                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
//                    expandableView.setVisibility(View.VISIBLE);
//                    arrowBtn.setBackgroundResource(R.drawable.baseline_arrow_back_24);
//                } else {
//                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
//                    expandableView.setVisibility(View.GONE);
//                    arrowBtn.setBackgroundResource(R.drawable.arrowdown);
//                }
//            }
//        });
    }
}