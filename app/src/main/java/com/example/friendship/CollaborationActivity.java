package com.example.friendship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.friendship.Collaboration.CollabDetails1;
import com.example.friendship.Model.Collaborator;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CollaborationActivity extends AppCompatActivity {
    ImageView add;
    RecyclerView collaborator;

    CollaboratorAdapter adapter;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Collab").child(fAuth.getCurrentUser().getUid()).child("members");

    LinearLayoutManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        add = findViewById(R.id.add);
        collaborator = findViewById(R.id.Collaborator);
        manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        collaborator.setLayoutManager(manager);
        collaborator.setItemAnimator(null);

        try {

            FirebaseRecyclerOptions<Collaborator> options =
                    new FirebaseRecyclerOptions.Builder<Collaborator>()
                            .setQuery(db, Collaborator.class)
                            .build();
            adapter=new CollaboratorAdapter(options);
            collaborator.setAdapter(adapter);

            adapter.startListening();

        }catch (Exception e){

            Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show();
        }






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