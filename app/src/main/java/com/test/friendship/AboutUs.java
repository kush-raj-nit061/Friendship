package com.test.friendship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.test.friendship.R;
import com.test.friendship.Model.Developer;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AboutUs extends AppCompatActivity {

    RecyclerView recFeatured1;
    DeveloperAdapter userAdapter;
    LinearLayoutManager manager;
//    ImageView backs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        recFeatured1  = findViewById(R.id.recAbout);
//        backs = findViewById(R.id.backs);
        manager = new LinearLayoutManager(AboutUs.this,LinearLayoutManager.HORIZONTAL,false);
        recFeatured1.setLayoutManager(manager);
//        recFeatured1.setHasFixedSize(true);
        recFeatured1.setItemAnimator(null);


        FirebaseRecyclerOptions<Developer> options =
                new FirebaseRecyclerOptions.Builder<Developer>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Developers"), Developer.class)
                        .build();
        userAdapter=new DeveloperAdapter(options);
        recFeatured1.setAdapter(userAdapter);
        userAdapter.startListening();

//        backs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }
}