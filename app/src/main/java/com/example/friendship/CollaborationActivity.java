package com.example.friendship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.friendship.Collaboration.CollabDetails1;

public class CollaborationActivity extends AppCompatActivity {
    ImageView add;
    RecyclerView collaborator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        add = findViewById(R.id.add);
        collaborator = findViewById(R.id.Collaborator);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CollaborationActivity.this, CollabDetails1.class);
                startActivity(i);
                finish();
            }
        });
    }
}