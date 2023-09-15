package com.test.friendship;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.test.friendship.R;


import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

public class FullProfileLoader extends AppCompatActivity {

    ImageView backs;
    ZoomageView profile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile_loader);
        backs = findViewById(R.id.backs);
        profile = findViewById(R.id.profile);

        Intent i = getIntent();
        String url =i.getStringExtra("purl");
        Glide.with(getApplicationContext()).load(url).into(profile);

        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}