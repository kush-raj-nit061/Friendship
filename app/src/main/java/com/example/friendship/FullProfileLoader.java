package com.example.friendship;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullProfileLoader extends AppCompatActivity {

    ImageView back,profile;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_profile_loader);

        back = findViewById(R.id.back);
        profile = findViewById(R.id.profile);

        Intent i = getIntent();
        String url =i.getStringExtra("purl");
        Glide.with(getApplicationContext()).load(url).into(profile);
    }
}