package com.example.friendship.Collaboration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CollabDetails2 extends AppCompatActivity {

    LottieAnimationView frog1,frog2,frog3;

    EditText description;

    ImageView tvNext,tvPrevious;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_details2);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        tvPrevious = findViewById(R.id.tvprevious);
        description = findViewById(R.id.etDescription);

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

                String desc = description.getText().toString();
                Map<String,Object> map = new HashMap<>();
                map.put("description",desc);

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("preCollab");
                try {
                    db.child(fAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(CollabDetails2.this, CollabDetails3.class);
                            startActivity(i);
                            finish();

                        }
                    });

                }catch (Exception ignored){}



            }
        });

        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CollabDetails2.this, CollabDetails1.class);
                startActivity(i);
                finish();
            }
        });


    }
}