package com.test.friendship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.friendship.Collaboration.CollabDetails1;
import com.test.friendship.Model.Collaboration;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

public class CollaborationActivity extends AppCompatActivity {
    ImageView add;

    private RecyclerView recCollab;
    private CollaborationAdapter collabAdapter;
    TextView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        add = findViewById(R.id.add);
        recCollab= findViewById(R.id.recCollab);
        back = findViewById(R.id.back);
        GridLayoutManager manager = new GridLayoutManager(this,2);


        recCollab.setHasFixedSize(true);
        recCollab.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecorationCollab = new DividerItemDecoration(recCollab.getContext(), DividerItemDecoration.VERTICAL);
        recCollab.addItemDecoration(dividerItemDecorationCollab);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        DatabaseReference reference = database.getReference().child("Collab");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat datePatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        recCollab.setVisibility(View.GONE);



        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                    try {
                        Query query = reference.orderByChild("date").limitToLast(count);

                        FirebaseRecyclerOptions<Collaboration> options =
                                new FirebaseRecyclerOptions.Builder<Collaboration>()
                                        .setQuery(query, Collaboration.class)
                                        .build();

                        collabAdapter=new CollaborationAdapter(options);
                        recCollab.setAdapter(collabAdapter);
                        recCollab.setVisibility(View.VISIBLE);
                        collabAdapter.startListening();

                    }catch (Exception ignored){}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception ignored){

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CollaborationActivity.this, CollabDetails1.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}