package com.test.friendship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.test.friendship.R;



import com.test.friendship.Model.Help;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HelpAndSupport extends AppCompatActivity {
    HelpAdapter userAdapter;
    LinearLayoutManager manager;
    RecyclerView rechelp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);

        rechelp = findViewById(R.id.recHelp);

        rechelp.setLayoutManager(new LinearLayoutManager(this));
//        rechelp.setHasFixedSize(true);
        rechelp.setItemAnimator(null);

        FirebaseRecyclerOptions<Help> options =
                new FirebaseRecyclerOptions.Builder<Help>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Help"), Help.class)
                        .build();
        userAdapter=new HelpAdapter(options);
        rechelp.setAdapter(userAdapter);
        userAdapter.startListening();

    }
}